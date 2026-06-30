# product-service

The **product catalog** service of MSShop: it owns brands, categories, products, and their
variants, plus the images and the read projections used by the storefront. It is also the
**reference implementation** of the project's hybrid **Hexagonal + Onion + Clean + DDD + CQRS**
architecture — the cleanest example to copy from.

> For the architecture conventions shared across the fleet (layer/package map, naming rules,
> CQRS & DDD invariants, the two‑layer eventing model), see the
> [root README](../../../README.md). This document covers what is **specific to product-service**.

---

## Bounded contexts

Feature‑first: each context is a full hexagonal slice under `vn.edu.uit.msshop.product`.

| Context | Owns | Notable value objects |
| --- | --- | --- |
| `brand` | Brands + brand logos | `BrandId`, `BrandName`, `BrandLogoKey`, `BrandVersion` |
| `category` | Categories + category images | `CategoryId`, `CategoryName`, `CategoryImageKey` |
| `product` | Products, pricing range, rating & sold/stock aggregates | `ProductId`, `ProductPriceRange`, `ProductRatingTotal` |
| `variant` | Variants (the sellable SKU), sold/stock counts | `VariantId`, `VariantPrice`, `VariantVersion`, `VariantDeletionTime` |

Aggregates are immutable (mutations return a new instance), use a `*Version` for optimistic
locking, and are soft‑deleted via a `*DeletionTime`.

## Tech & datastores

| Concern | Choice |
| --- | --- |
| Runtime | Java 25, Spring Boot 4.0.7, **port 8070** |
| Primary store | MongoDB (database `product`) |
| Cache | Redis, via `shared-spring-cache` (`CircuitBreakingCacheManager`), per‑entity TTLs |
| Images | Cloudinary (`shared-cloudinary`); multipart upload limit 5 MB |
| Messaging | Apache Kafka |
| Platform | Spring Cloud Config + Eureka, Micrometer → Zipkin |

> Null‑safety note: product-service is rooted at `vn.edu.uit.msshop`, so it is the **only** service
> where Error Prone + NullAway run at `ERROR`. Keep `@Nullable` before the return type.

## HTTP API

Controllers are grouped by bounded context and by role (driving adapters in `adapter/in/web`):

| Role suffix | Purpose | Examples |
| --- | --- | --- |
| `*LifecycleController` | Create / update / soft‑delete / restore | `BrandLifecycleController`, `CategoryLifecycleController` |
| `*LookupController` / `*QueryController` | Reads / listings / projections | `BrandLookupController`, `ProductQueryController` |
| `*ImageController` / `*LogoController` | Media upload & removal | `VariantImageController`, `BrandLogoController` |
| `ProductOptionController`, `ProductVariantController` | Product options & variant composition | — |
| `*InternalController` | Endpoints intended for service‑to‑service calls | `ProductInternalController`, `VariantInternalController` |

- **Auth**: Bearer **JWT** (`OpenApiConfig` registers the `bearerAuth` security scheme).
- **Interactive docs**: springdoc OpenAPI — Swagger UI at `/swagger-ui.html`, OpenAPI JSON at
  `/v3/api-docs` (API title *"Microservice shop - Product service"*, version `v1`).

For exact paths and payloads, use Swagger UI rather than this README (it stays in sync with the code).

## Event-driven architecture

product-service uses **two event layers** (see the root README for the general pattern):

1. **In‑process** — Spring `ApplicationEvent` domain events for intra‑service reactions
   (published by `*EventPublicationAdapter`, e.g. keeping the product read model in step with its
   variants).
2. **Cross‑service** — **Kafka** integration events for choreography between services.

### Publishes — Kafka topic `variant-topic` (3 partitions, key = aggregate id)

| Event | When |
| --- | --- |
| `VariantUpdatedIntegrationEvent` | a variant's info changes |
| `VariantSoftDeletedIntegrationEvent` | a variant is soft‑deleted |

Published by `VariantKafkaPublisherAdapter`; `VariantIntegrationEventBridge` translates the internal
Spring events into these outbound Kafka events.

### Consumes — group `product-service-group`

| Topic | Listener | Reaction |
| --- | --- | --- |
| `rating-product` | `ProductRatingEventListener` | apply `RatingCreated/Updated/Deleted` → product rating aggregates |
| `order-variant` | `VariantOrderEventListener` | apply variant **sold‑count** changes |
| `inventory-variant` | `VariantInventoryEventListener` | apply variant **stock‑count** changes |

### Synchronous reconciliation (OpenFeign) + scheduled repair (ShedLock)

Because events can be missed, ShedLock‑guarded jobs in `adapter/in/scheduler` periodically pull the
source of truth over **OpenFeign** and reconcile:

| Feign client | Target service | Endpoint |
| --- | --- | --- |
| `ProductRatingFeignClient` | `rating-service` | `GET /rating/public/updated-rating-info` |
| `VariantSoldCountFeignClient` | `order-service` | `GET /order/public/sold_counts` |
| `VariantStockCountFeignClient` | `inventory-service` | `POST /inventory/public/updated_inventory` |

## Configuration

Bootstrap config is minimal ([application.yml](src/main/resources/application.yml)); the real config
is served centrally by config-server
([product-service.yml](../config-server/src/main/resources/configurations/product-service.yml)).

Key environment variables (resolved inside each container — see the root `.env.example`):

| Variable | Used for |
| --- | --- |
| `PRODUCT_DB_HOST`, `PRODUCT_DB_PORT` | MongoDB host/port (db `product`) |
| `REDIS_PRODUCT_HOST`, `REDIS_PORT` | Redis cache |
| `KAFKA_URL` | Kafka bootstrap servers |
| `CLOUDINARY_*` | Cloudinary credentials |
| `CONFIG_SERVER_URL`, `DISCOVERY_SERVER_URL`, `ZIPKIN_URL` | platform wiring |

Also configured centrally: per‑entity cache TTLs + cache circuit breaker, OpenFeign timeouts for
`order-service` / `rating-service`, tracing sampling probability `0.1`, and a single‑thread
scheduler pool for the reconciliation jobs.

## Build, run, test

product-service is a **single‑level** Maven module, so use `-pl product-service` (no nested path).
Run from `backend/services/` with the Maven Wrapper (`./mvnw`; on Windows use `mvnw.cmd`):

```sh
# Build (with changed shared deps)
./mvnw -pl product-service -am clean install

# All tests / a single test
./mvnw -pl product-service test
./mvnw -pl product-service test -Dtest=CloudinaryPropertiesTest#methodName

# Format only (also runs automatically on every build)
./mvnw formatter:format
```

Run the whole stack (product-service listens on **8070**; depends on config-server, discovery-server,
and MongoDB being healthy) from the repo root:

```sh
docker compose up -d product-service
```
