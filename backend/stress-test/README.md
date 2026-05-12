# Stress Test Suite

Load and stress testing for microservice-shop using Apache JMeter.

## Structure

```
stress-test/
├── scripts/                 # JMeter test plans (.jmx)
│   └── product-service/
│       ├── get-products.jmx      # Simple read stress test
│       ├── crud-sequential.jmx   # Sequential CRUD flow verification
│       └── crud-stress.jmx       # Parallel CRUD stress test
├── config/                  # Environment configs
│   └── local.properties
├── data/                    # Test data (CSV, JSON payloads)
├── report/                  # Generated HTML reports (gitignored)
└── results/                 # Raw JTL results (gitignored)
```

## Prerequisites

- Apache JMeter 5.6+
- Java 11+
- Running services (product-service, inventory-service, etc.)

## Running Tests

### GUI Mode (for development)
```bash
jmeter -t scripts/product-service/get-products.jmx
```

### CLI Mode (for CI/CD)
```bash
jmeter -n -t scripts/product-service/get-products.jmx \
       -l results/products.jtl \
       -e -o report/products
```

## Test Scenarios

| Script | Description | Default Config |
|--------|-------------|----------------|
| `get-products.jmx` | GET /products with random pagination | 100 users, 5 min |
| `crud-sequential.jmx` | Sequential CRUD: Create → Read → Update → Delete | 5 users, 10 loops |
| `crud-stress.jmx` | Parallel CRUD with 4 thread groups | 67 total users, 3 min |

### CRUD Sequential Test

Tests the full CRUD lifecycle in sequence:
1. **Setup**: Creates test Brand & Category
2. **CRUD Loop**: For each iteration:
   - Create Product with Variant
   - Query Product List, Product by ID, Variant by ID, Variant List
   - Update Product, Update Variant
   - Soft Delete Variant → Restore
   - Soft Delete Product → Query Deleted → Hard Delete
3. **Teardown**: Cleans up test Brand & Category

```bash
# Run with defaults (5 users, 10 loops)
jmeter -n -t scripts/product-service/crud-sequential.jmx \
       -l results/crud-sequential.jtl

# Customize users and loops
jmeter -n -t scripts/product-service/crud-sequential.jmx \
       -Jusers=10 -Jloops=20 \
       -l results/crud-sequential.jtl
```

### CRUD Stress Test

Parallel stress test simulating realistic mixed workload:

| Thread Group | Users | Description | Start Delay |
|--------------|-------|-------------|-------------|
| Writers | 10 | Continuously create products | 0s |
| Readers | 50 | Random read operations (list, get by ID) | 5s |
| Updaters | 5 | Fetch → Update products/variants | 10s |
| Deleters | 2 | Soft delete → Hard delete | 30s |

```bash
# Run with defaults (3 min, 67 total users)
jmeter -n -t scripts/product-service/crud-stress.jmx \
       -l results/crud-stress.jtl \
       -e -o report/crud-stress

# Customize parameters
jmeter -n -t scripts/product-service/crud-stress.jmx \
       -Jduration=300 \
       -Jwriters=20 -Jreaders=100 -Jupdaters=10 -Jdeleters=5 \
       -JcreateRate=600 \
       -l results/crud-stress.jtl
```

**Parameters:**
- `duration`: Test duration in seconds (default: 180)
- `writers`: Number of create threads (default: 10)
- `readers`: Number of read threads (default: 50)
- `updaters`: Number of update threads (default: 5)
- `deleters`: Number of delete threads (default: 2)
- `createRate`: Products created per minute across all writers (default: 300)

## Example Baseline Metrics (local, illustrative)

These numbers are from one local run and are **not guaranteed targets**.  
Actual throughput/latency/error rate will vary by machine specs, Docker resource limits, and backing service state.

| Metric | Value |
|--------|-------|
| Throughput | ~5,000-7,000 req/s (with Redis cache) |
| Avg Response | 15-30ms |
| Error Rate | <0.1% |

## Quick Start (PowerShell)

`run-tests.ps1` locates JMeter from PATH, `JMETER_HOME`, or the `-JMeterHome` parameter
(use the JMeter *home* folder that contains the `bin` directory).

```powershell
cd backend\stress-test

# Allow running the unsigned local script for this PowerShell session only
# (no permanent change to the machine's execution policy)
Set-ExecutionPolicy -ExecutionPolicy Unrestricted -Scope Process

# If JMeter is not on PATH, pass the home folder or set JMETER_HOME for this session
.\run-tests.ps1 -Test sequential -JMeterHome "C:\apache-jmeter-5.6.3"
$env:JMETER_HOME = "C:\apache-jmeter-5.6.3"

# Step 1: Verify CRUD logic works
.\run-tests.ps1 -Test sequential

# Step 2: Light stress test (warm up)
.\run-tests.ps1 -Test light

# Step 3: Medium stress test (target 80% capacity)
.\run-tests.ps1 -Test medium

# Step 4: Heavy stress test (find limits)
.\run-tests.ps1 -Test heavy
```

## Test Profiles

| Profile | Users | Duration | Target | Use Case |
|---------|-------|----------|--------|----------|
| `sequential` | 3 | ~2 min | N/A | Verify CRUD logic |
| `light` | 25 | 2 min | 200-300 req/s | Warm up, baseline |
| `medium` | 45 | 3 min | 400-500 req/s | 80% capacity test |
| `heavy` | 80 | 5 min | 500+ req/s | Find breaking point |

## Performance Requirements

| Metric | Target |
|--------|--------|
| Throughput | >= 500 req/s |
| Response Time (p95) | < 500ms |
| Error Rate | < 1% at 80% load |
| Self-recovery | Service auto-recovers after failure |

## Circuit Breaker Test

Illustrative example from a local run (environment-dependent):
- Redis UP: ~5,000 req/s, 15ms avg
- Redis DOWN: ~500 req/s, 200ms avg (fallback to DB)
- 0% errors during failover
