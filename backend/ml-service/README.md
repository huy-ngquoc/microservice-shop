# ml-service

HTTP server that infers **gender** and **body shape** from an image,
serving `recommendation-service`. It is not part of the Maven reactor:
this is a standalone Python component that sits alongside `backend/mongo/`
and `backend/stress-test/`.

## Contract

| | |
| --- | --- |
| `POST /` | Body is **raw image bytes** (`application/octet-stream`) |
| `GET /health` | `{"status": "ok"}` — used by the Compose healthcheck |

`POST /` responds with:

```json
{
  "status": "success",
  "prediction": { "gender": "Female", "body_shape": "Hourglass" }
}
```

- `gender` is one of `Female`, `Male` — EfficientNet-B3 (PyTorch).
- `body_shape` is one of `Apple`, `Hourglass`, `Inverted_triangle`, `Pear`,
  `Rectangle` — ResNet50 (Keras).

Errors return HTTP 500 with `{"status": "error", "message": "..."}`.

On the consumer side, `MLServerCaller` calls this server at the address
resolved from `app.ml-service.url` (env var `ML_SERVICE_URL`):
[MLServerCaller.java](../services/recommendation-service/recommendation/src/main/java/vn/uit/edu/msshop/recommendation/adapter/remote/MLServerCaller.java)

## Model weights

The two weight files are **not** committed (~137 MiB in total):

| File | Size |
| --- | --- |
| `models/gender_effb3.pth` | 41 MiB |
| `models/best_body_shape_resnet50_new.h5` | 96 MiB |

They are kept out of git on purpose. Committing them would bloat every
clone permanently, and Git LFS would tie the repo to one hosting platform:
LFS support, storage quotas and bandwidth limits differ from host to host,
and some hosts have none. Keeping the weights outside git means this repo
can be pushed to any git host, self-hosted included, with no extra setup.

Fetch them with the helper script. It reads `models/manifest.json`,
skips files that already match their checksum, and verifies SHA256 after
fetching:

```powershell
# Windows
.\scripts\fetch-models.ps1
```

```bash
# Linux / macOS / Git Bash
bash scripts/fetch-models.sh
```

The script looks for the weights in this order:

| Source | How |
| --- | --- |
| A local folder | `ML_MODELS_SRC=/path/to/weights` — copies, no network |
| A mirror you host | `ML_MODELS_BASE_URL=https://example.com/weights` — fetches `<base>/<filename>` |
| `manifest.json` | The `url` field of each entry, if it is filled in |

```powershell
$env:ML_MODELS_SRC = "d:\path\to\weights"
.\scripts\fetch-models.ps1

$env:ML_MODELS_BASE_URL = "https://example.com/weights"
.\scripts\fetch-models.ps1
```

```bash
ML_MODELS_SRC=/path/to/weights bash scripts/fetch-models.sh
ML_MODELS_BASE_URL=https://example.com/weights bash scripts/fetch-models.sh
```

> `url` in `manifest.json` is `null` by default, so nothing in this repo
> points at a particular host. Mirror the weights wherever suits you —
> a release asset, a package registry, an object store,
> a plain web server — and either hand people `ML_MODELS_BASE_URL` or fill
> `url` in. Whichever route is used, the SHA256 in the manifest is checked,
> so a wrong or tampered mirror is caught.

## Running locally (uv)

Requires Python 3.10 (`.python-version`). `torch` and `torchvision` are
pinned to the CPU index in `pyproject.toml`, so `uv sync` does not pull
down the multi-GB CUDA build.

```bash
uv sync
bash scripts/fetch-models.sh
uv run python server.py
```

Environment variables:

| Variable | Default | Meaning |
| --- | --- | --- |
| `MODEL_DIR` | `models` | Directory holding the weights |
| `PORT` | `9090` | Listening port |

Quick check:

```bash
curl http://localhost:9090/health
curl --data-binary @photo.jpg \
  -H "Content-Type: application/octet-stream" \
  http://localhost:9090/
```

## Running with Docker Compose

The service sits behind the `ml` profile, so a plain `docker compose up`
does **not** start it:

```bash
docker compose --profile ml up -d ml-service
```

Things worth knowing:

- Weights are **mounted** from `backend/ml-service/models` rather than
  baked into the image, so run `fetch-models` first —
  otherwise the container exits immediately with a "weight not found"
  message.
- The image uses `tensorflow-cpu` plus CPU torch wheels and lands at
  roughly 3.3 GB; the first build takes a while, most of it spent
  downloading ~450 MB of wheels. Rebuilds reuse pip's cache mount,
  so only a first build pays that cost.
- Loading both models is slow, which is why the healthcheck uses
  `start_period: 120s`.
- When running under Compose, set `ML_SERVICE_URL=http://ml-service:9090`
  in `.env` so `recommendation-service` reaches it over the internal
  network instead of `host.docker.internal`.
- The Docker image installs from `requirements.docker.txt`,
  not from `pyproject.toml`, because the Linux `tensorflow` wheel bundles
  the CUDA runtime. The two lists pin the same versions and have to be
  bumped together.

## Dependency lists

There are deliberately two of them:

| File | Used by | Difference |
| --- | --- | --- |
| `pyproject.toml` + `uv.lock` | local dev (`uv sync`) | `tensorflow` |
| `requirements.docker.txt` | the Docker image | `tensorflow-cpu` |

Keep their pins in sync when upgrading.
