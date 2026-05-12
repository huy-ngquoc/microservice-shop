# Product Service Stress Test Runner
# Usage: .\run-tests.ps1 -Test <sequential|light|medium|heavy> [-JMeterHome <path>]

param(
    [Parameter(Mandatory=$true)]
    [ValidateSet("sequential", "light", "medium", "heavy")]
    [string]$Test,
    [string]$JMeterHome
)

$SCRIPT_DIR = $PSScriptRoot
$RESULTS_DIR = "$SCRIPT_DIR\results"
$REPORT_DIR = "$SCRIPT_DIR\report"
$CONFIG_DIR = "$SCRIPT_DIR\config"

if ($JMeterHome) {
    $JMETER_BIN = Join-Path $JMeterHome "bin\jmeter.bat"
    if (-not (Test-Path -Path $JMETER_BIN -PathType Leaf)) {
        throw "JMeter not found at '$JMETER_BIN'. Provide a valid -JMeterHome path."
    }
}
elseif ($env:JMETER_HOME) {
    $JMETER_BIN = Join-Path $env:JMETER_HOME "bin\jmeter.bat"
    if (-not (Test-Path -Path $JMETER_BIN -PathType Leaf)) {
        throw "JMeter not found at '$JMETER_BIN' from JMETER_HOME. Set JMETER_HOME correctly or use -JMeterHome."
    }
}
else {
    $jmeterCommand = Get-Command "jmeter.bat" -ErrorAction SilentlyContinue
    if (-not $jmeterCommand) {
        $jmeterCommand = Get-Command "jmeter" -ErrorAction SilentlyContinue
    }
    if (-not $jmeterCommand) {
        throw "JMeter executable not found. Use -JMeterHome, set JMETER_HOME, or add jmeter to PATH."
    }
    $JMETER_BIN = $jmeterCommand.Source
}

if (-not (Test-Path -Path $CONFIG_DIR -PathType Container)) {
    throw "Config directory not found: $CONFIG_DIR"
}

function Resolve-ProfileConfig {
    param([Parameter(Mandatory=$true)][string]$FileName)
    $path = Join-Path $CONFIG_DIR $FileName
    if (-not (Test-Path -Path $path -PathType Leaf)) {
        throw "Profile config not found: $path"
    }
    return $path
}

# Create directories if not exist
New-Item -ItemType Directory -Force -Path $RESULTS_DIR | Out-Null
New-Item -ItemType Directory -Force -Path $REPORT_DIR | Out-Null

$timestamp = Get-Date -Format "yyyyMMdd-HHmmss"

switch ($Test) {
    "sequential" {
        Write-Host "Running CRUD Sequential Test (verify logic)..." -ForegroundColor Cyan
        Write-Host "Config: 3 users, 5 loops, ~180 requests total" -ForegroundColor Gray
        $profileConfig = Resolve-ProfileConfig "crud-sequential.properties"

        & $JMETER_BIN -n `
            -t "$SCRIPT_DIR\scripts\product-service\crud-sequential.jmx" `
            -q $profileConfig `
            -l "$RESULTS_DIR\crud-sequential-$timestamp.jtl" `
            -j "$RESULTS_DIR\crud-sequential-$timestamp.log"
    }
    "light" {
        Write-Host "Running CRUD Stress Test - LIGHT (~200-300 req/s)..." -ForegroundColor Green
        Write-Host "Config: 25 users, 2 min duration" -ForegroundColor Gray
        $profileConfig = Resolve-ProfileConfig "crud-stress-light.properties"

        & $JMETER_BIN -n `
            -t "$SCRIPT_DIR\scripts\product-service\crud-stress.jmx" `
            -q $profileConfig `
            -l "$RESULTS_DIR\crud-stress-light-$timestamp.jtl" `
            -j "$RESULTS_DIR\crud-stress-light-$timestamp.log" `
            -e -o "$REPORT_DIR\crud-stress-light-$timestamp"
    }
    "medium" {
        Write-Host "Running CRUD Stress Test - MEDIUM (~400-500 req/s)..." -ForegroundColor Yellow
        Write-Host "Config: 45 users, 3 min duration" -ForegroundColor Gray
        Write-Host "WARNING: Run 'light' first to warm up system!" -ForegroundColor Yellow
        $profileConfig = Resolve-ProfileConfig "crud-stress-medium.properties"

        & $JMETER_BIN -n `
            -t "$SCRIPT_DIR\scripts\product-service\crud-stress.jmx" `
            -q $profileConfig `
            -l "$RESULTS_DIR\crud-stress-medium-$timestamp.jtl" `
            -j "$RESULTS_DIR\crud-stress-medium-$timestamp.log" `
            -e -o "$REPORT_DIR\crud-stress-medium-$timestamp"
    }
    "heavy" {
        Write-Host "Running CRUD Stress Test - HEAVY (~500+ req/s)..." -ForegroundColor Red
        Write-Host "Config: 80 users, 5 min duration" -ForegroundColor Gray
        Write-Host "WARNING: High resource usage! Monitor CPU/memory!" -ForegroundColor Red
        $profileConfig = Resolve-ProfileConfig "crud-stress-heavy.properties"

        & $JMETER_BIN -n `
            -t "$SCRIPT_DIR\scripts\product-service\crud-stress.jmx" `
            -q $profileConfig `
            -l "$RESULTS_DIR\crud-stress-heavy-$timestamp.jtl" `
            -j "$RESULTS_DIR\crud-stress-heavy-$timestamp.log" `
            -e -o "$REPORT_DIR\crud-stress-heavy-$timestamp"
    }
}

Write-Host "`nTest completed!" -ForegroundColor Green
Write-Host "Results: $RESULTS_DIR" -ForegroundColor Cyan
Write-Host "Report:  $REPORT_DIR" -ForegroundColor Cyan
