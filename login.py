import requests
import json

# Cấu hình các thông số
LOGIN_URL = "http://localhost:8082/account/login"  # Thay bằng URL của bạn
INPUT_FILE = "user.txt"
OUTPUT_FILE = "tokens.txt"

def get_tokens():
    tokens = []
    
    try:
        with open(INPUT_FILE, 'r') as f:
            lines = f.readlines()[:100]
            
        print(f"--- Đang bắt đầu xử lý {len(lines)} tài khoản ---")
        
        for line in lines:
            # Tách username và password từ dòng (loại bỏ khoảng trắng/xuống dòng)
            parts = line.strip().split(',')
            if len(parts) != 2:
                continue
                
            username, password = parts
            payload = {
                "username": username,
                "password": password
            }
            
            try:
                response = requests.post(
                    LOGIN_URL, 
                    json=payload, 
                    headers={"Content-Type": "application/json"}
                )
                
                if response.status_code == 200:
                    data = response.json()
                    # Lấy access_token từ JSON trả về
                    token = data.get("access_token")
                    if token:
                        tokens.append(token)
                        print(f"[SUCCESS] Đã lấy token cho: {username}")
                    else:
                        print(f"[WARNING] Không tìm thấy trường access_token cho: {username}")
                else:
                    print(f"[ERROR] Login thất bại cho {username}: Status {response.status_code}")
                    
            except Exception as e:
                print(f"[CRITICAL] Lỗi kết nối khi login {username}: {e}")

        # Ghi toàn bộ token vào file txt
        with open(OUTPUT_FILE, 'w') as f_out:
            for t in tokens:
                f_out.write(f"{t}\n")
        
        print(f"--- Hoàn tất! Đã lưu {len(tokens)} token vào {OUTPUT_FILE} ---")

    except FileNotFoundError:
        print(f"Lỗi: Không tìm thấy file {INPUT_FILE}")

if __name__ == "__main__":
    get_tokens()