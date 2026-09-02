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

Two files sit next to `server.py`, ~137 MiB in total:

| File | Size |
| --- | --- |
| `gender_effb3.pth` | 41 MiB |
| `best_body_shape_resnet50_new.h5` | 96 MiB |

They are tracked in **Git LFS**, so on a machine with `git-lfs` installed a
plain `git clone` already brings them down and there is nothing else to do.
Check that the real files arrived, not the pointers:

```bash
git lfs ls-files
```

If either file is a few hundred bytes of text starting with
`version https://git-lfs.github.com/spec/v1`, the clone ran without LFS and
the pointer files were checked out instead. Fix it with:

```bash
git lfs install
git lfs pull
```

> GitHub's free tier gives 1 GB of LFS storage and 1 GB of bandwidth per
> month. At ~137 MiB per clone that is roughly seven full clones a month
> before LFS is throttled, CI runs included.

The weights are opaque: there is no training code or dataset in this
repo, so they cannot be regenerated here. What *is* recorded, in
`server.py`, is everything needed to use them — the `efficientnet_b3`
architecture, the class label order, and the preprocessing each model
expects.

## Running locally (uv)

Requires Python 3.10 (`.python-version`). `torch` and `torchvision` are
pinned to the CPU index in `pyproject.toml`, so `uv sync` does not pull
down the multi-GB CUDA build.

```bash
uv sync
uv run python server.py
```

Environment variables:

| Variable | Default | Meaning |
| --- | --- | --- |
| `MODEL_DIR` | `.` | Directory holding the weights |
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

- The two weight files are **mounted** from the working tree rather than
  baked into the image. LFS normally puts them there at clone time; if
  only the pointer files are present the container exits immediately with
  a "weight not found" message, so run `git lfs pull` first.
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

| File | Used by | TensorFlow | torch / torchvision |
| --- | --- | --- | --- |
| `pyproject.toml` + `uv.lock` | local dev (`uv sync`) | `tensorflow` | plain version, CPU wheels come from `[tool.uv.sources]` |
| `requirements.docker.txt` | the Docker image | `tensorflow-cpu` | explicit `+cpu` local version |

Keep their pins in sync when upgrading. Do **not** add `+cpu` to the
`pyproject.toml` pins: the PyTorch CPU index publishes that local version
only for Linux and Windows, so pinning it there breaks `uv sync` on macOS.
