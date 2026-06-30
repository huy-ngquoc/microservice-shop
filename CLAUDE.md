# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

<!-- code-review-graph MCP tools -->
## MCP Tools: code-review-graph

**IMPORTANT: This project has a knowledge graph. ALWAYS use the
code-review-graph MCP tools BEFORE using Grep/Glob/Read to explore
the codebase.** The graph is faster, cheaper (fewer tokens), and gives
you structural context (callers, dependents, test coverage) that file
scanning cannot.

### When to use graph tools FIRST

- **Exploring code**: `semantic_search_nodes` or `query_graph` instead of Grep
- **Understanding impact**: `get_impact_radius` instead of manually tracing imports
- **Code review**: `detect_changes` + `get_review_context` instead of reading entire files
- **Finding relationships**: `query_graph` with callers_of/callees_of/imports_of/tests_for
- **Architecture questions**: `get_architecture_overview` + `list_communities`

Fall back to Grep/Glob/Read **only** when the graph doesn't cover what you need.

### Key Tools

| Tool | Use when |
| ------ | ---------- |
| `detect_changes` | Reviewing code changes — gives risk-scored analysis |
| `get_review_context` | Need source snippets for review — token-efficient |
| `get_impact_radius` | Understanding blast radius of a change |
| `get_affected_flows` | Finding which execution paths are impacted |
| `query_graph` | Tracing callers, callees, imports, tests, dependencies |
| `semantic_search_nodes` | Finding functions/classes by name or keyword |
| `get_architecture_overview` | Understanding high-level codebase structure |
| `refactor_tool` | Planning renames, finding dead code |

### Workflow

1. The graph auto-updates on file changes (via hooks).
2. Use `detect_changes` for code review.
3. Use `get_affected_flows` to understand impact.
4. Use `query_graph` pattern="tests_for" to check coverage.

## Repository layout

Monorepo. The Maven **reactor lives at `backend/services/`** — parent pom
`backend/services/pom.xml` (`vn.edu.uit.msshop:msshop-parent`), **not** the repo root. Run Maven
from `backend/services/`.

- `docker-compose.yml` + `.env` (repo root) — the full local stack (services + infra).
- `backend/services/` — the reactor: 13 service modules, 6 shared library modules, plus
  `config-server` and `discovery-server`. The authoritative module list is the `<modules>` block
  in `backend/services/pom.xml`.
- `backend/mongo/` — builds the replica-set–enabled MongoDB image used by Compose.
- `backend/stress-test/` — JMeter/k6 load-test harness and reports. Not application code; ignore
  it for service work.

## Build / test / run

Run from `backend/services/`. On Windows use the `mvnw.cmd` wrapper (wrappers also exist inside
each service folder).

| Goal | Command (from `backend/services/`) |
| --- | --- |
| Build whole reactor | `./mvnw.cmd clean install` |
| Build one service | `./mvnw.cmd -pl product-service clean install` |
| Build a nested-module service | `./mvnw.cmd -pl account-service/account clean install` |
| Build a service + its changed deps | `./mvnw.cmd -pl product-service -am clean install` |
| All tests for a service | `./mvnw.cmd -pl product-service test` |
| Single test class | `./mvnw.cmd -pl product-service test -Dtest=CloudinaryPropertiesTest` |
| Single test method | `./mvnw.cmd -pl product-service test -Dtest=CloudinaryPropertiesTest#methodName` |
| Format code only | `./mvnw.cmd formatter:format` |

Notes:

- Most domain services live in a **nested folder** (`<service>/<service>`, e.g.
  `account-service/account`, `order-service/order`). `product-service`, `config-server`,
  `discovery-server`, and the `shared-*` modules are single-level. Use the exact `<module>` path
  from `backend/services/pom.xml` with `-pl`.
- **Formatting runs automatically on every build** — `net.revelc.code.formatter` is bound to the
  default lifecycle (config: `backend/services/java-formatter.xml`). Use `formatter:format` to
  format without a full build.
- Stack: **Java 25**, **Spring Boot 4.0.7**, Spring Cloud 2025.1.0, Lombok, JUnit Jupiter. Tests
  run under `-Duser.timezone=UTC`; Mockito is loaded as a `-javaagent`.

## Quality gates & nullness (important, easy to miss)

The parent pom runs **Error Prone + NullAway at `ERROR`**, but the check is scoped:

```
-Xep:NullAway:ERROR
-XepOpt:NullAway:AnnotatedPackages=vn.edu.uit.msshop
-XepExcludedPaths:.*vn.uit.edu.*
```

- **Only `product-service` is rooted at `vn.edu.uit.msshop`**, so it is the **only service whose
  null-safety is actually enforced**. Every other service uses the swapped root `vn.uit.edu.*`
  (e.g. `vn.uit.edu.msshop.account`, `vn.uit.edu.payment`) and is **excluded** from NullAway.
  When adding nullness annotations or relying on null-checking, know which root you're in.
- Convention: place `@Nullable` **before the return type** — `public @Nullable Type m()` — not on
  the access-modifier line. (Fields keep `@Nullable` on their own line.)

## Local run topology

Bring the stack up with `docker compose up` from the repo root.

- Services depend on **config-server (`:8888`)** and **discovery-server / Eureka (`:8761`)** being
  healthy before they start.
- Centralized config lives in `backend/services/config-server/src/main/resources/config` (mounted
  into the config-server). Secrets/URLs come from the root `.env` (loaded via `dotenv-java`).
- `shared-base` is a **build-only** image: it pre-installs the parent pom + shared modules into
  `~/.m2` so each service Dockerfile can build against them (`additional_contexts: service:shared-base`).
- Infra (see `docker-compose.yml`): PostgreSQL ×3 (account / inventory / payment, plus a shared
  one), MongoDB (replica-set `mongo` + per-service Mongo instances), Redis ×4, **Kafka in KRaft
  mode** (no Zookeeper), **Keycloak** (`:8080`, OIDC), **Zipkin** (`:9411`, tracing), MailDev, and
  **Cloudinary** as an external image store.

## Architecture — `product-service` is the reference implementation

The project targets a **hybrid hexagonal (ports & adapters) + onion + clean architecture, with DDD
and CQRS**. `product-service` follows this most faithfully; treat it as the canonical pattern. It
is **feature-first**: each bounded context (`brand`, `category`, `product`, `variant`) is a full
hexagonal slice. Root package: `vn.edu.uit.msshop.product`. Cross-cutting wiring lives in
`bootstrap/config` (Mongo, Redis cache, Cloudinary, OpenAPI) and `config` (Kafka).

### Layer / package map (per bounded context)

- **`adapter/in/`** — driving adapters:
  - `web/` — REST controllers (`*Controller`), `request/`, `response/`, `mapper/` (`*WebMapper`).
  - `event/` — Kafka consumers (`*EventListener`) and their `payload/` records.
  - `scheduler/` — ShedLock-guarded reconciliation jobs (`*Job`).
- **`adapter/out/`** — driven adapters:
  - `persistence/` — `*Document`, `*MongoRepository`, `*PersistenceAdapter`, `*PersistenceMapper`,
    `*IndexInitializer`.
  - `event/` — `*EventPublicationAdapter` (Spring), `*KafkaPublisherAdapter`,
    `*IntegrationEventBridge`, `config/*KafkaConfig`, `*KafkaProducerListener`.
  - `image/` (`*ImageStorageAdapter`, Cloudinary), `validation/` (`*ValidationAdapter`),
    `reconciliation/` (`*ReconciliationAdapter`), `sync/` (`*FeignClient` + `*FeignAdapter`).
- **`application/`**:
  - `port/in/{command,query}/…` — driving ports (use cases).
  - `port/out/{persistence,event,image,validation,reconciliation,sync}/…` — driven ports.
  - `service/{command,query}/…` — use-case implementations.
  - `dto/{command,query,view,integration}/…`, `mapper/`, `exception/`.
- **`domain/`**:
  - `model/` — aggregate roots (`Variant`, `Product`, `Brand`, `Category`).
  - `model/valueobject/` — value objects; `model/creation/` — `New*` creation models.
  - `event/` — domain events; `sync/` — cross-aggregate snapshots.

### Naming conventions (match these when adding code)

- Driving port ↔ impl: `{Action}{Scope}UseCase` ↔ `{Action}{Scope}Service`
  (e.g. `VariantInfoUpdateByIdUseCase` / `VariantInfoUpdateByIdService`).
- Driven port ↔ adapter: `{Entity}{Op}{Scope}Port` ↔ `{Entity}…Adapter`
  (e.g. `VariantUpdatePort` / `VariantCommandPersistenceAdapter`).
- Inbound Kafka listeners are **entity-first**: `{Entity}{Source}EventListener`
  (e.g. `VariantProductEventListener`), not foreign-entity-first.
- DTOs: `*Command` (write), `*Query` (read params), `*View` (read model / response projection).
- Value objects are entity-prefixed: `VariantId`, `VariantPrice`, `VariantVersion`,
  `VariantDeletionTime`.

### CQRS & DDD invariants

- **Command and query are separated at every layer**: `port/in/command` vs `port/in/query`,
  `service/command` vs `service/query`, `dto/command` vs `dto/query`, and
  `port/out/persistence/.../command` vs `.../query`. Read models are `*View` records.
- **Aggregates are immutable**: mutating methods return a *new* instance (e.g.
  `Variant.updateInfo(...)`). Concurrency uses a `*Version` value object (optimistic locking);
  deletion is **soft** via a `*DeletionTime` value object. Value objects validate their own
  invariants in the constructor.

### Eventing & inter-service communication

- **Two event layers**: in-process **Spring `ApplicationEvent`** domain events
  (`*EventPublicationAdapter`) for intra-service reactions, and **Kafka integration events**
  (`*IntegrationEvent` published by `*KafkaPublisherAdapter`) for cross-service messaging.
  `*IntegrationEventBridge` translates internal Spring events into outbound Kafka integration events.
- **Synchronous reads** between services use **OpenFeign** (`adapter/out/sync/*FeignClient`).
- **Eventual consistency** is repaired by **ShedLock** scheduled jobs in `adapter/in/scheduler`.

### Worked end-to-end trace (variant info update — all real classes)

```
VariantController              adapter/in/web
  → VariantInfoUpdateByIdUseCase        application/port/in/command/lifecycle
    → VariantInfoUpdateByIdService      application/service/command/lifecycle
      → Variant.updateInfo(...)         domain/model            (returns new aggregate)
      → VariantUpdatePort               application/port/out/persistence/variant/command
        → VariantCommandPersistenceAdapter   adapter/out/persistence/variant
          → VariantMongoRepository / VariantDocument
      → VariantEventPublicationPort     (Spring event)  → VariantEventPublicationAdapter
      → VariantIntegrationEventPublicationPort (Kafka)  → VariantKafkaPublisherAdapter
```

## Other services (factual; verify before assuming)

`product-service` is the only service that fully implements the pattern above. The other 12
services share the broad `domain` → `application` (port/service) → `adapter` (in/out) intent but,
due to time pressure, **may flatten or omit layers and do not all implement CQRS or a domain
layer**. For example, `recommendation-service` has no `domain` package and uses a single
`Get*UseCase` / `Get*Service` pair with no command/query split.

**Do not assume another service mirrors `product-service` — inspect it first (graph tools).** Also
note the package-root split (see *Quality gates*): `product-service` = `vn.edu.uit.msshop`, all
others = `vn.uit.edu.*`.

Datastore / messaging quick map (from `docker-compose.yml` + module deps):

| Service | Store(s) | Kafka |
| --- | --- | --- |
| product | MongoDB, Redis, Cloudinary | yes |
| account | PostgreSQL (+ Mongo event store), Keycloak | yes |
| auth | Keycloak only | yes |
| cart | MongoDB, Redis | yes |
| image | Cloudinary only | yes |
| inventory | PostgreSQL, Redis | yes |
| notification | MongoDB | yes |
| order | MongoDB, Redis | yes |
| payment / fake-payment | PostgreSQL (+ Redis), PayOS | yes |
| rating | MongoDB, Cloudinary | yes |
| recommendation | none (stateless; ML/remote calls) | no |

## Shared library modules (`backend/services/shared-*`)

Depended on by services, not deployed independently. Declared in the parent
`dependencyManagement`; build them first (the reactor / `-am` handles this).

- **`shared-kernel`** — cross-cutting domain & application exceptions (`DomainException`,
  `BusinessRuleException`, `NotFoundException`, `OptimisticLockException`), pagination DTOs,
  `UUIDs`, and PATCH/`Change` helpers.
- **`shared-webmvc`** — `GlobalExceptionHandler`, `ApiErrorResponse` / `ValidationError`, and PATCH
  request mapping (`PatchRequest` / `ChangeRequest`).
- **`shared-spring-data`** — Spring Data pagination helpers (`PageRequests`).
- **`shared-spring-cache`** — Redis cache support with a circuit breaker
  (`CircuitBreakingCacheManager`, `RedisCacheConfigSupport`).
- **`shared-observability`** — Micrometer tracing auto-config, incl. Mongo instrumentation
  (`TracingAutoConfiguration`, `MongoTracingAutoConfiguration`).
- **`shared-cloudinary`** — Cloudinary image adapter & URL resolution
  (`CloudinaryImageUrlResolver`, `CloudinaryPublicIds`, `CloudinaryFolders`).
