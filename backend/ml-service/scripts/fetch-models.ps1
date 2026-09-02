#Requires -Version 5.1
<#
.SYNOPSIS
    Download the ml-service model weights into backend/ml-service/models.

.DESCRIPTION
    Reads models/manifest.json, skips files that are already present with a matching
    checksum, fetches the rest and verifies their SHA256.

    Where the weights come from, in order of precedence:

      1. ML_MODELS_SRC       - a local folder to copy from (no network)
      2. ML_MODELS_BASE_URL  - a base URL; each file is fetched from <base>/<filename>
      3. the "url" field of each entry in models/manifest.json

    The manifest deliberately holds no host-specific default, so this repo stays portable
    across git hosting platforms: mirror the weights wherever you like (a release asset, a
    package registry, an object store, a plain web server) and point ML_MODELS_BASE_URL at it.

        $env:ML_MODELS_SRC = "d:\path\to\weights"
        .\scripts\fetch-models.ps1

        $env:ML_MODELS_BASE_URL = "https://example.com/msshop/weights"
        .\scripts\fetch-models.ps1
#>
[CmdletBinding()]
param(
    [string] $Source = $env:ML_MODELS_SRC,
    [string] $BaseUrl = $env:ML_MODELS_BASE_URL
)

$ErrorActionPreference = 'Stop'

$serviceDir = Split-Path -Parent $PSScriptRoot
$modelDir = Join-Path $serviceDir 'models'
$manifestPath = Join-Path $modelDir 'manifest.json'

if (-not (Test-Path $manifestPath)) {
    throw "Manifest not found: $manifestPath"
}

$manifest = Get-Content -Raw -Encoding UTF8 $manifestPath | ConvertFrom-Json

function Test-Checksum {
    param([string] $Path, [string] $Expected)

    if (-not (Test-Path $Path)) { return $false }
    $actual = (Get-FileHash -Algorithm SHA256 -Path $Path).Hash.ToLowerInvariant()
    return $actual -eq $Expected.ToLowerInvariant()
}

$failed = @()

foreach ($model in $manifest.models) {
    $target = Join-Path $modelDir $model.file

    if (Test-Checksum -Path $target -Expected $model.sha256) {
        Write-Host "[ok]   $($model.file) - already present, checksum matches."
        continue
    }

    if (Test-Path $target) {
        Write-Warning "$($model.file) exists but its checksum does not match - refetching."
    }

    if ($Source) {
        $src = Join-Path $Source $model.file
        if (-not (Test-Path $src)) {
            $failed += "$($model.file): not found under ML_MODELS_SRC ($src)"
            continue
        }
        Write-Host "[copy] $($model.file) from $src"
        Copy-Item -Path $src -Destination $target -Force
    }
    else {
        $url = if ($BaseUrl) { "$($BaseUrl.TrimEnd('/'))/$($model.file)" } else { $model.url }
        if (-not $url) {
            $failed += "$($model.file): no download source. Set ML_MODELS_SRC or ML_MODELS_BASE_URL, or fill in this entry's url in models/manifest.json."
            continue
        }
        Write-Host "[get]  $($model.file) from $url"
        $previous = $ProgressPreference
        $ProgressPreference = 'SilentlyContinue'   # Invoke-WebRequest is far faster without the progress bar
        try {
            Invoke-WebRequest -Uri $url -OutFile $target -UseBasicParsing
        }
        catch {
            $failed += "$($model.file): download failed from $url - $($_.Exception.Message)"
            if (Test-Path $target) { Remove-Item $target -Force }
            continue
        }
        finally {
            $ProgressPreference = $previous
        }
    }

    if (-not (Test-Checksum -Path $target -Expected $model.sha256)) {
        $failed += "$($model.file): SHA256 mismatch after fetching."
    }
    else {
        Write-Host "[ok]   $($model.file) - checksum matches."
    }
}

if ($failed.Count -gt 0) {
    Write-Host ''
    foreach ($message in $failed) { Write-Host "[fail] $message" -ForegroundColor Red }
    exit 1
}

Write-Host ''
Write-Host "Done. Weights are in $modelDir"
