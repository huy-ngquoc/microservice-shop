#!/usr/bin/env bash
# Download the ml-service model weights into backend/ml-service/models.
#
# Reads models/manifest.json, skips files that are already present with a matching
# checksum, fetches the rest and verifies their SHA256.
#
# Where the weights come from, in order of precedence:
#
#   1. ML_MODELS_SRC       - a local folder to copy from (no network)
#   2. ML_MODELS_BASE_URL  - a base URL; each file is fetched from <base>/<filename>
#   3. the "url" field of each entry in models/manifest.json
#
# The manifest deliberately holds no host-specific default, so this repo stays portable
# across git hosting platforms: mirror the weights wherever you like (a release asset, a
# package registry, an object store, a plain web server) and point ML_MODELS_BASE_URL at it.
#
#     ML_MODELS_SRC=/path/to/weights bash scripts/fetch-models.sh
#     ML_MODELS_BASE_URL=https://example.com/weights bash scripts/fetch-models.sh
set -euo pipefail

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
service_dir="$(dirname "$script_dir")"
model_dir="$service_dir/models"
manifest="$model_dir/manifest.json"
source_dir="${ML_MODELS_SRC:-}"
base_url="${ML_MODELS_BASE_URL:-}"

[ -f "$manifest" ] || { echo "Manifest not found: $manifest" >&2; exit 1; }

# manifest.json is a flat JSON file owned by this repo (see its _comment), so grep/sed
# parsing is enough -- no need to make everyone install jq.
extract_field() {
    grep -o "\"$1\"[[:space:]]*:[[:space:]]*\(\"[^\"]*\"\|null\)" "$manifest" \
        | sed 's/.*:[[:space:]]*//; s/^"//; s/"$//'
}

# `mapfile` is bash 4+, which macOS still does not ship; a read loop works everywhere.
files=(); shas=(); urls=()
while IFS= read -r line; do files+=("$line"); done < <(extract_field file)
while IFS= read -r line; do shas+=("$line"); done < <(extract_field sha256)
while IFS= read -r line; do urls+=("$line"); done < <(extract_field url)

# sha256sum on Linux/Git Bash, shasum on macOS, openssl as a last resort.
sha256_of() {
    if command -v sha256sum > /dev/null 2>&1; then
        sha256sum "$1" | cut -d' ' -f1
    elif command -v shasum > /dev/null 2>&1; then
        shasum -a 256 "$1" | cut -d' ' -f1
    else
        openssl dgst -sha256 "$1" | awk '{print $NF}'
    fi
}

checksum_ok() {
    local path="$1" expected="$2"
    [ -f "$path" ] || return 1
    [ "$(sha256_of "$path")" = "$expected" ]
}

failed=()

for i in "${!files[@]}"; do
    file="${files[$i]}"
    sha="${shas[$i]}"
    url="${urls[$i]}"
    target="$model_dir/$file"

    if checksum_ok "$target" "$sha"; then
        echo "[ok]   $file - already present, checksum matches."
        continue
    fi

    if [ -f "$target" ]; then
        echo "[warn] $file exists but its checksum does not match - refetching." >&2
    fi

    if [ -n "$source_dir" ]; then
        if [ ! -f "$source_dir/$file" ]; then
            failed+=("$file: not found under ML_MODELS_SRC ($source_dir/$file)")
            continue
        fi
        echo "[copy] $file from $source_dir/$file"
        cp "$source_dir/$file" "$target"
    else
        if [ -n "$base_url" ]; then
            url="${base_url%/}/$file"
        fi
        if [ "$url" = "null" ] || [ -z "$url" ]; then
            failed+=("$file: no download source. Set ML_MODELS_SRC or ML_MODELS_BASE_URL, or fill in this entry's url in models/manifest.json.")
            continue
        fi
        echo "[get]  $file from $url"
        if ! curl -fL --progress-bar -o "$target" "$url"; then
            failed+=("$file: download failed from $url")
            rm -f "$target"
            continue
        fi
    fi

    if checksum_ok "$target" "$sha"; then
        echo "[ok]   $file - checksum matches."
    else
        failed+=("$file: SHA256 mismatch after fetching.")
    fi
done

if [ "${#failed[@]}" -gt 0 ]; then
    echo
    for message in "${failed[@]}"; do echo "[fail] $message" >&2; done
    exit 1
fi

echo
echo "Done. Weights are in $model_dir"
