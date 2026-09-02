# syntax=docker/dockerfile:1
FROM python:3.10-slim

WORKDIR /app

# No apt packages are installed on purpose: the resolved dependency set has no
# OpenCV, so nothing here needs libGL or glib. Pillow, torch and tensorflow-cpu
# ship the shared libraries they need inside their wheels.
COPY requirements.docker.txt .

# The cache mount keeps the ~300MB of wheels across rebuilds, so editing this
# file does not mean downloading tensorflow again.
#
# tensorflow-cpu is a single 274MB wheel. pip's default 15s socket timeout gives
# up on it over a slow or stalling link, so the timeout and retry count are
# raised well past the defaults.
RUN --mount=type=cache,target=/root/.cache/pip \
    pip install --timeout 120 --retries 10 -r requirements.docker.txt

COPY server.py .

# Weights are NOT baked into the image -- they are mounted next to server.py
# at run time (see docker-compose.yml). MODEL_DIR is set explicitly rather than
# relying on the "." default so the server does not depend on its own cwd.
ENV MODEL_DIR=/app \
    PORT=9090 \
    PYTHONUNBUFFERED=1

EXPOSE 9090

CMD ["python", "server.py"]
