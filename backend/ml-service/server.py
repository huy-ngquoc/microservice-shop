import http.server
import json
import io
import os
import torch
import numpy as np
import timm
from PIL import Image
from torchvision import transforms
from tensorflow.keras.models import load_model
from tensorflow.keras.applications.resnet50 import preprocess_input

# ==========================================
# 1. CONFIGURATION & MODEL LOADING
# ==========================================
DEVICE = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# Directory holding the model weights. Weights are not tracked in git -- see scripts/fetch-models.
MODEL_DIR = os.environ.get("MODEL_DIR", "models")
PORT = int(os.environ.get("PORT", "9090"))

GENDER_MODEL_PATH = os.path.join(MODEL_DIR, "gender_effb3.pth")
BODY_SHAPE_MODEL_PATH = os.path.join(MODEL_DIR, "best_body_shape_resnet50_new.h5")

for path in (GENDER_MODEL_PATH, BODY_SHAPE_MODEL_PATH):
    if not os.path.isfile(path):
        raise SystemExit(
            f"Model weight not found: {path}\n"
            "Run scripts/fetch-models.ps1 (Windows) or scripts/fetch-models.sh (Linux/macOS) first."
        )

# --- Gender model (PyTorch) ---
gender_model = timm.create_model("efficientnet_b3", pretrained=False, num_classes=2)
gender_model.load_state_dict(torch.load(GENDER_MODEL_PATH, map_location=DEVICE))
gender_model.to(DEVICE)
gender_model.eval()

gender_transform = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
])
GENDER_CLASSES = ['Female', 'Male']

# --- Body shape model (Keras/H5) ---
# Note: loading an .h5 model may need compile=False when it is only used for prediction.
body_shape_model = load_model(BODY_SHAPE_MODEL_PATH)
BODY_SHAPE_CLASSES = ['apple', 'hourglass', 'inverted_triangle', 'pear', 'rectangle']



# ==========================================
# 2. SERVER LOGIC
# ==========================================
class SimpleAIHandler(http.server.BaseHTTPRequestHandler):

    def do_GET(self):
        # Healthcheck endpoint used by Docker Compose.
        if self.path.rstrip('/') in ('/health', '/health/'):
            self._send_response(200, {"status": "ok"})
        else:
            self._send_response(404, {"status": "error", "message": "Not found"})

    def do_POST(self):
        # Read the body before entering the try below: a request without a usable
        # Content-Length has to be rejected with a response rather than blowing up
        # the handler, which would drop the connection without answering at all.
        raw_length = self.headers['Content-Length']
        if raw_length is None:
            self._send_response(411, {"status": "error", "message": "Content-Length required"})
            return
        try:
            content_length = int(raw_length)
        except ValueError:
            self._send_response(400, {"status": "error", "message": "Malformed Content-Length"})
            return

        post_data = self.rfile.read(content_length)

        try:
            # 1. Turn the received bytes into an image
            image = Image.open(io.BytesIO(post_data)).convert('RGB')

            # 2. Predict gender (PyTorch)
            gender_input = gender_transform(image).unsqueeze(0).to(DEVICE)
            with torch.no_grad():
                gender_out = gender_model(gender_input)
                gender_idx = gender_out.argmax(1).item()
                gender_res = GENDER_CLASSES[gender_idx]


            # 3. Predict body shape (Keras/ResNet50)
            # ResNet50 expects 224x224 input
            body_img = image.resize((224, 224))
            body_array = np.array(body_img)
            body_array = np.expand_dims(body_array, axis=0)
            body_array = preprocess_input(body_array) # ResNet50-specific normalization

            body_out = body_shape_model.predict(body_array, verbose=0)
            body_idx = np.argmax(body_out)
            body_res = BODY_SHAPE_CLASSES[body_idx].capitalize()

            # Age prediction was prototyped with DeepFace but is not enabled.
            # To bring it back, run it on the image here and add the value to
            # the "prediction" object below.

            # 4. Return the result as JSON
            response_data = {
                "status": "success",
                "prediction": {
                    "gender": gender_res,
                    "body_shape": body_res
                }
            }
            self._send_response(200, response_data)

        except Exception as e:
            self._send_response(500, {"status": "error", "message": str(e)})

    def _send_response(self, status_code, data):
        self.send_response(status_code)
        self.send_header('Content-Type', 'application/json')
        self.end_headers()
        self.wfile.write(json.dumps(data).encode('utf-8'))

# ==========================================
# 3. RUN THE SERVER
# ==========================================
if __name__ == '__main__':
    server_address = ('', PORT)
    httpd = http.server.HTTPServer(server_address, SimpleAIHandler)
    print(f"AI server listening on port {PORT}...")
    print("POST an image (raw bytes) to get a prediction.")
    httpd.serve_forever()
