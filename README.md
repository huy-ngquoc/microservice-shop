# MSShop — Microservices E‑Commerce Platform

A backend e‑commerce platform built as a fleet of Spring Boot microservices, following a hybrid
**Hexagonal + Onion + Clean Architecture** with **Domain‑Driven Design** and **CQRS**. Services talk
asynchronously over **Kafka** and synchronously via **OpenFeign**, on top of centralized
configuration, service discovery, and distributed tracing.

> 📐 `product-service` is the **reference implementation** of the architecture described below. The
> other services share the same intent but, under time pressure, may flatten or omit layers —
> inspect each one before assuming it follows the full pattern.

---

## Tech stack

| Area | Technology |
| --- | --- |
| Language / framework | Java 25, Spring Boot 4.0.7, Spring Cloud 2025.1.0 |
| Persistence | MongoDB (replica set), PostgreSQL, Redis |
| Messaging | Apache Kafka (KRaft mode) |
| AuthN / AuthZ | Keycloak (OIDC) |
| Config & discovery | Spring Cloud Config + Netflix Eureka |
| Observability | Micrometer tracing → Zipkin |
| Media storage | Cloudinary |
| Payments | PayOS |
| Build & run | Maven (multi‑module reactor), Docker Compose |
| Code quality | Error Prone + NullAway, formatter auto‑applied on build |

## Architecture

The project targets a hybrid **Hexagonal (ports & adapters) + Onion + Clean Architecture, with DDD
and CQRS**. `product-service` follows it most faithfully and is **feature‑first**: each bounded
context (`brand`, `category`, `product`, `variant`) is a full hexagonal slice. Root package:
`vn.edu.uit.msshop.product`. Cross‑cutting wiring lives in `bootstrap/config` (Mongo, Redis cache,
Cloudinary, OpenAPI) and `config` (Kafka).

### Layer / package map (per bounded context)

```
adapter/in/                       driving adapters
  web/         REST controllers (*Controller), request/, response/, mapper/ (*WebMapper)
  event/       Kafka consumers (*EventListener) and their payload/ records
  scheduler/   ShedLock‑guarded reconciliation jobs (*Job)

application/
  port/in/{command,query}/...      driving ports (use cases)
  port/out/{persistence,event,image,validation,reconciliation,sync}/...   driven ports
  service/{command,query}/...      use‑case implementations
  dto/{command,query,view,integration}/...,  mapper/,  exception/

domain/
  model/                aggregate roots (Variant, Product, Brand, Category)
  model/valueobject/    value objects;   model/creation/  → New* creation models
  event/                domain events;   sync/  → cross‑aggregate snapshots

adapter/out/                      driven adapters
  persistence/  *Document, *MongoRepository, *PersistenceAdapter, *PersistenceMapper, *IndexInitializer
  event/        *EventPublicationAdapter (Spring), *KafkaPublisherAdapter, *IntegrationEventBridge, *KafkaConfig
  image/        *ImageStorageAdapter (Cloudinary)
  validation/   *ValidationAdapter
  reconciliation/  *ReconciliationAdapter
  sync/         *FeignClient + *FeignAdapter (synchronous inter‑service reads)
```

### Naming conventions

Two principles run through every type name:

- **Entity‑first** — a class leads with **its own module's entity**, never a foreign one. The same
  Product↔Variant relationship is named from each side's point of view: the `variant` module has
  `VariantProductEventListener`, while the `product` module has `ProductVariantEventListener` (and
  `ProductToVariantCreationSyncAdapter` for directional sync). Don't lead with the foreign entity.
- **Class names are nouns** — every type is a noun phrase whose *head* is its stereotype
  (`UseCase`, `Service`, `Port`, `Adapter`, `Mapper`, `Event`, `Job`, `Document`, value object),
  with the operation as a qualifier: `VariantInfoUpdateByIdService`, not a verb like
  `UpdateVariantInfo`.

Concrete patterns:

- Driving port ↔ impl: `{Entity}{Operation}{Scope}UseCase` ↔ `…Service`
  (e.g. `VariantInfoUpdateByIdUseCase` / `VariantInfoUpdateByIdService`).
- Driven port ↔ adapter: `{Entity}{Operation}{Scope}Port` ↔ `{Entity}…Adapter`
  (e.g. `VariantUpdatePort` / `VariantCommandPersistenceAdapter`).
- Inbound Kafka listeners: `{Entity}{Source}EventListener`
  (e.g. `VariantProductEventListener`, `ProductVariantEventListener`).
- DTOs: `*Command` (write), `*Query` (read params), `*View` (read model / response projection).
- Value objects are entity‑prefixed nouns: `VariantId`, `VariantPrice`, `VariantVersion`,
  `VariantDeletionTime`.

### CQRS & DDD invariants

- **Command and query are separated at every layer**: `port/in/command` vs `port/in/query`,
  `service/command` vs `service/query`, `dto/command` vs `dto/query`, and
  `port/out/persistence/.../command` vs `.../query`. Read models are `*View` records.
- **Aggregates are immutable**: mutating methods return a *new* instance (e.g.
  `Variant.updateInfo(...)`). Concurrency uses a `*Version` value object (optimistic locking);
  deletion is **soft** via a `*DeletionTime` value object; value objects validate their own
  invariants in the constructor.

### Eventing & inter‑service communication

- **Two event layers**: in‑process Spring `ApplicationEvent` domain events
  (`*EventPublicationAdapter`) for intra‑service reactions, and **Kafka** integration events
  (`*IntegrationEvent` published by `*KafkaPublisherAdapter`) for cross‑service choreography.
  `*IntegrationEventBridge` translates internal Spring events into outbound Kafka events.
- **Synchronous reads** between services use **OpenFeign** (`adapter/out/sync/*FeignClient`).
- **Eventual consistency** is repaired by **ShedLock**‑guarded scheduled jobs (`adapter/in/scheduler`).

### Worked example — variant info update (all real classes)

```
VariantController                          adapter/in/web
  → VariantInfoUpdateByIdUseCase           application/port/in/command/lifecycle
    → VariantInfoUpdateByIdService         application/service/command/lifecycle
      → Variant.updateInfo(...)            domain/model           (returns a new aggregate)
      → VariantUpdatePort                  application/port/out/persistence/variant/command
        → VariantCommandPersistenceAdapter adapter/out/persistence/variant
          → VariantMongoRepository / VariantDocument
      → VariantEventPublicationPort        (Spring event) → VariantEventPublicationAdapter
      → VariantIntegrationEventPublicationPort (Kafka)    → VariantKafkaPublisherAdapter
```

## Code quality & nullness

The parent POM runs **Error Prone + NullAway at `ERROR`**, but scoped:

```
-Xep:NullAway:ERROR
-XepOpt:NullAway:AnnotatedPackages=vn.edu.uit.msshop
-XepExcludedPaths:.*vn.uit.edu.*
```

- **Only `product-service` is rooted at `vn.edu.uit.msshop`**, so it is the only service whose
  null‑safety is actually enforced. The other services use the swapped root `vn.uit.edu.*`
  (e.g. `vn.uit.edu.msshop.account`, `vn.uit.edu.payment`) and are excluded from NullAway.
- Convention: place `@Nullable` **before the return type** — `public @Nullable Type m()` — not on
  the access‑modifier line.
- **Formatting runs automatically on every build** (`net.revelc.code.formatter`, config
  `backend/services/java-formatter.xml`). Run it standalone with `./mvnw formatter:format`.

## Services

| Service | Port | Data store(s) | Kafka |
| --- | --- | --- | --- |
| product-service | 8070 \* | MongoDB, Redis, Cloudinary | ✅ |
| account-service | 8071 | PostgreSQL (+ Mongo events), Keycloak | ✅ |
| auth-service | 8082 | Keycloak | ✅ |
| cart-service | 8089 | MongoDB, Redis | ✅ |
| inventory-service | 8090 | PostgreSQL, Redis | ✅ |
| notification-service | 8092 | MongoDB | ✅ |
| order-service | dynamic \* | MongoDB, Redis | ✅ |
| payment-service | 8074 | PostgreSQL, Redis, PayOS | ✅ |
| fake-payment-service | 8075 | PostgreSQL, PayOS | ✅ |
| rating-service | 8072 | MongoDB, Cloudinary | ✅ |
| recommendation-service | 8099 | — (stateless; ML / remote calls) | — |

\* `product-service` runs with 2 replicas and `order-service` is published on a dynamic host port;
both are reached client‑side through Eureka. `image-service` also exists as a Maven module but is
not part of the default Compose stack.

## Shared library modules (`backend/services/shared-*`)

Depended on by services, not deployed independently:

- **`shared-kernel`** — cross‑cutting domain & application exceptions (`DomainException`,
  `BusinessRuleException`, `NotFoundException`, `OptimisticLockException`), pagination DTOs,
  `UUIDs`, and PATCH/`Change` helpers.
- **`shared-webmvc`** — `GlobalExceptionHandler`, `ApiErrorResponse` / `ValidationError`, and PATCH
  request mapping (`PatchRequest` / `ChangeRequest`).
- **`shared-spring-data`** — Spring Data pagination helpers (`PageRequests`).
- **`shared-spring-cache`** — Redis cache support with a circuit breaker
  (`CircuitBreakingCacheManager`, `RedisCacheConfigSupport`).
- **`shared-observability`** — Micrometer tracing auto‑config incl. Mongo instrumentation
  (`TracingAutoConfiguration`, `MongoTracingAutoConfiguration`).
- **`shared-cloudinary`** — Cloudinary image adapter & URL resolution
  (`CloudinaryImageUrlResolver`, `CloudinaryPublicIds`, `CloudinaryFolders`).

## Repository layout

```
microservice-shop/
├── backend/
│   ├── services/                 # Maven reactor (run mvn from here)
│   │   ├── product-service/      # reference implementation
│   │   ├── <name>-service/...     # the other domain services
│   │   ├── shared-kernel/  shared-webmvc/  shared-spring-data/
│   │   ├── shared-spring-cache/  shared-observability/  shared-cloudinary/
│   │   ├── config-server/  discovery-server/
│   │   └── pom.xml               # parent / reactor POM
│   ├── mongo/                    # replica‑set MongoDB image
│   └── stress-test/              # JMeter / k6 load tests
├── tools/                        # data generators, observability & manual‑test helpers
└── docker-compose.yml            # full local stack (services + infrastructure)
```

## Getting started

### Prerequisites
- Docker + Docker Compose
- JDK 25 (only needed for local Maven builds outside Docker)

### Run the stack
1. Clone the repository.
2. Copy `.env.example` to `.env` and fill in the secrets (Keycloak client secret, Cloudinary,
   PayOS). The non‑secret values are already wired for the Docker Compose network:
   ```sh
   cp .env.example .env
   ```
3. Start everything:
   ```sh
   docker compose up -d
   ```
   > MongoDB replica‑set keyfiles are **generated automatically** at build/startup — no manual step.
4. `config-server` (`:8888`) and `discovery-server` (`:8761`) come up first; the rest register with
   Eureka once those are healthy.

### Management & observability UIs

| Component | URL |
| --- | --- |
| Eureka (service discovery) | http://localhost:8761 |
| Spring Cloud Config | http://localhost:8888 |
| Keycloak | http://localhost:8080 |
| Zipkin (tracing) | http://localhost:9411 |
| pgAdmin | http://localhost:5050 |
| Mongo Express | http://localhost:8081 |
| MailDev | http://localhost:1080 |

## Build & test

Run from `backend/services/` using the bundled Maven Wrapper (`./mvnw`; on Windows use `mvnw.cmd`):

```sh
# Build the whole reactor
./mvnw clean install

# Build one service together with its changed dependencies
./mvnw -pl product-service -am clean install

# Tests for one service
./mvnw -pl product-service test

# A single test class / method
./mvnw -pl product-service test -Dtest=CloudinaryPropertiesTest
./mvnw -pl product-service test -Dtest=CloudinaryPropertiesTest#methodName

# Format the code (also runs automatically on every build)
./mvnw formatter:format
```

> Nested modules need their folder path with `-pl` (e.g. `-pl account-service/account`).
