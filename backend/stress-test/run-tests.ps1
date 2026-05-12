# Product Service Stress Test Runner
# Usage: .\run-tests.ps1 -Test <sequential|light|medium|heavy>

param(
    [Parameter(Mandatory=$true)]
    [ValidateSet("sequential", "light", "medium", "heavy")]
    [string]$Test
)

$JMETER_HOME = "C:\Users\Alley\Desktop\apache-jmeter-5.6.3"
$JMETER_BIN = "$JMETER_HOME\bin\jmeter.bat"
$SCRIPT_DIR = $PSScriptRoot
$RESULTS_DIR = "$SCRIPT_DIR\results"
$REPORT_DIR = "$SCRIPT_DIR\report"

# Create directories if not exist
New-Item -ItemType Directory -Force -Path $RESULTS_DIR | Out-Null
New-Item -ItemType Directory -Force -Path $REPORT_DIR | Out-Null

$timestamp = Get-Date -Format "yyyyMMdd-HHmmss"

switch ($Test) {
    "sequential" {
        Write-Host "Running CRUD Sequential Test (verify logic)..." -ForegroundColor Cyan
        Write-Host "Config: 3 users, 5 loops, ~180 requests total" -ForegroundColor Gray

        & $JMETER_BIN -n `
            -t "$SCRIPT_DIR\scripts\product-service\crud-sequential.jmx" `
            -Jusers=3 -Jloops=5 `
            -l "$RESULTS_DIR\crud-sequential-$timestamp.jtl" `
            -j "$RESULTS_DIR\crud-sequential-$timestamp.log"
    }
    "light" {
        Write-Host "Running CRUD Stress Test - LIGHT (~200-300 req/s)..." -ForegroundColor Green
        Write-Host "Config: 25 users, 2 min duration" -ForegroundColor Gray

        & $JMETER_BIN -n `
            -t "$SCRIPT_DIR\scripts\product-service\crud-stress.jmx" `
            -Jduration=120 `
            -Jwriters=5 -Jreaders=15 -Jupdaters=3 -Jdeleters=2 `
            -JcreateRate=120 `
            -l "$RESULTS_DIR\crud-stress-light-$timestamp.jtl" `
            -j "$RESULTS_DIR\crud-stress-light-$timestamp.log" `
            -e -o "$REPORT_DIR\crud-stress-light-$timestamp"
    }
    "medium" {
        Write-Host "Running CRUD Stress Test - MEDIUM (~400-500 req/s)..." -ForegroundColor Yellow
        Write-Host "Config: 45 users, 3 min duration" -ForegroundColor Gray
        Write-Host "WARNING: Run 'light' first to warm up system!" -ForegroundColor Yellow

        & $JMETER_BIN -n `
            -t "$SCRIPT_DIR\scripts\product-service\crud-stress.jmx" `
            -Jduration=180 `
            -Jwriters=8 -Jreaders=30 -Jupdaters=5 -Jdeleters=2 `
            -JcreateRate=240 `
            -l "$RESULTS_DIR\crud-stress-medium-$timestamp.jtl" `
            -j "$RESULTS_DIR\crud-stress-medium-$timestamp.log" `
            -e -o "$REPORT_DIR\crud-stress-medium-$timestamp"
    }
    "heavy" {
        Write-Host "Running CRUD Stress Test - HEAVY (~500+ req/s)..." -ForegroundColor Red
        Write-Host "Config: 80 users, 5 min duration" -ForegroundColor Gray
        Write-Host "WARNING: High resource usage! Monitor CPU/memory!" -ForegroundColor Red

        & $JMETER_BIN -n `
            -t "$SCRIPT_DIR\scripts\product-service\crud-stress.jmx" `
            -Jduration=300 `
            -Jwriters=15 -Jreaders=50 -Jupdaters=10 -Jdeleters=5 `
            -JcreateRate=420 `
            -l "$RESULTS_DIR\crud-stress-heavy-$timestamp.jtl" `
            -j "$RESULTS_DIR\crud-stress-heavy-$timestamp.log" `
            -e -o "$REPORT_DIR\crud-stress-heavy-$timestamp"
    }
}

Write-Host "`nTest completed!" -ForegroundColor Green
Write-Host "Results: $RESULTS_DIR" -ForegroundColor Cyan
Write-Host "Report:  $REPORT_DIR" -ForegroundColor Cyan
