# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

`bazar-space` is a Kotlin / Spring Boot 4 microservice (JDK 21) that manages "spaces": creation, membership (`user_space`), avatars, and join codes/links. It is part of the Bazar platform and talks to sibling services: `bazar-authorization` (gRPC via `bazar-authorization-sdk`, plus Feign), `bazar-persona` (user info, Feign), and `bazar-files` (avatar upload/download, Feign + Kafka events). Auth is a Keycloak JWT resource server. Jira project key is `BZR`; branches are `feature/BZR-<n>` and commits are prefixed `BZR-<n> ...`.

## Commands

```bash
./gradlew build                       # compile + test (also runs openApiGenerate)
./gradlew test                        # all tests; Testcontainers needs Docker running
./gradlew test --tests 'org.bazar.space.api.controller.SpaceControllerTest'
./gradlew test --tests 'org.bazar.space.domain.space.SpaceTest.someMethod'
./gradlew openApiGenerate             # regenerate API interfaces/DTOs from the OpenAPI spec
./gradlew openApiValidate             # validate the OpenAPI spec
./gradlew bootJar                     # CI copies build/libs/bazar-space-*.jar to application.jar for the Dockerfile
```

Resolving dependencies requires GitHub Packages credentials for `org.bazar:bazar-authorization-sdk`: env `GITHUB_TOKEN`/`GITHUB_ACTOR`, or Gradle properties `gpr.token`/`gpr.user`.

Run locally with the `local` profile (mock authz, relaxed security). The app needs env vars from `application.yaml` (`DB_URL`, `DB_USER`, `DB_PASS`, `KC_URL`, `KAFKA_*`, `BAZAR_*_API_URL`, `BAZAR_AUTHORIZATION_HOST/PORT`, `JOIN_LINK_BASE_URL`). Server is on `:8080` under context path `/api`; actuator on `:8090`.

## Architecture

Hexagonal (ports & adapters), **enforced by ArchUnit** in `src/test/kotlin/org/bazar/space/arch/ArchitectureTest.kt` — break a rule and `./gradlew test` fails:

- `domain` — pure model (`Space`, `SpaceMembership`, `UserSpace`, `SpaceJoinRequest`), `DomainException` subclasses, `Permission`/`AuthorizationCheck`, `SpaceDomainEvent`. Depends on nothing.
- `application` — use cases per aggregate (`space`, `userspace`, `joinrequest`, `shared`): `port/in` interfaces + `port/in/impl` implementations, `port/out` interfaces, `command`/`query` objects. May only depend on `domain`, JDK/Kotlin, and Spring `@Service` / `@Transactional` annotations — no other Spring or library imports.
- `adapter.inbound` — REST controllers and Kafka consumer; may use domain, application, infrastructure.
- `adapter.outbound` — persistence, Feign clients, authz, Kafka producer, outbox; may use domain, application, infrastructure.
- `infrastructure` — Spring `@Configuration`/properties; only adapters may reference it.

Note: a few `port/in` interfaces (e.g. `CreateSpaceUseCase`, `DeleteSpaceUseCase`, `GetUsersInSpaceUseCase`) declare package `org.bazar.space.application.port.in` although they live under `application/<aggregate>/port/in/`. Check the actual `package` line when importing.

### API is spec-first

REST interfaces and DTOs (`org.bazar.space.api.*ControllerApi`, `org.bazar.space.model.*`) are **generated** into `build/generated/openapi` from `src/main/resources/openapi/bazar-space-openapi.yaml` before every Kotlin compile. To add or change an endpoint: edit the YAML first, then implement the generated interface method in `adapter/inbound/rest/*Controller.kt`. Never edit generated code.

### Request flow & conventions

- Controllers map DTOs → `Command`/`Query`, call a use case, map the result back (`*DtoMapper.kt`). The current user comes from `CurrentUserProvider` (JWT-backed `SpringCurrentUserProvider`).
- Use cases start with `authorizer.authorizeOrThrow(buildAuthorizationCheck(spaceId, Permission.X))`. Side effects on the external authz service go through `AuthzManager`.
- Errors: throw a `DomainException` subclass; map it to HTTP status/code/message (messages are in Russian) in `adapter/inbound/rest/error/DomainExceptionMapper.kt`. The `when` is exhaustive, so new exceptions must be added there.
- Persistence is **Spring Data JDBC** (not JPA): `*Entity` + `*EntityRepository` + `*Mapper` + `*RepositoryAdapter` implementing the application `*RepositoryPort`.

### Authz switching by profile

`AuthorizerAdapter`/`AuthzManagerAdapter` and the real `SecurityConfig` chains are `@Profile("!local && !test")`. In `local`/`test`, `MockAuthorizer`/`MockAuthzManager` (`@ConditionalOnMissingBean`) allow everything; integration tests additionally replace both with `@MockitoBean`.

### Events: transactional outbox → Kafka

- Outbound: use cases call `EventPublisher.publish(SpaceDomainEvent)` inside a `@Transactional` method (enforced at runtime). `OutboxEventPublisher` writes an `outbox` row. `SpaceOutboxProcessingJob` (every 2s, extends `AbstractOutboxJob`) locks a batch, sends it to topic `space.events`, and marks rows DONE/ERROR. `ResetOutboxLocksJob` releases stale locks. Jobs are toggled with `app.jobs.*.enabled`. Payload schema: `src/main/resources/kafka/space-events.json`.
- Inbound: `BazarFilesEventConsumer` listens on `bazar.files.events`, ignores events whose `domain` ≠ `file-domain` (`bazar-space`), and completes avatar uploads (avatar flow: initiate upload via bazar-files → files service emits event → avatar marked active).

### Database migrations

Liquibase, plain SQL files under `src/main/resources/db/changelog/v<version>/`, named `YYYYMMDD-NN-0-description.sql`, listed in that version's `changelog.yaml`, which is included from `changelog-master.yaml`. A new version folder usually corresponds to bumping `version` in `build.gradle.kts`. When adding tables, also update `src/test/resources/db/scripts/clearTables.sql`.

## Tests

- `BaseIntegrationTest`: `@SpringBootTest` with profile `test`, shared Postgres 16 + Kafka Testcontainers (`SharedAppContext`, started once per JVM), truncates tables after each test via `clearTables.sql`, and mocks `Authorizer`/`AuthzManager`.
- `BaseWebTest` adds MockMvc and a fixed `authenticatedUserId`. Controller tests live in `src/test/kotlin/.../api/controller/`.
- Test data helpers: `utils/*Creator.kt`, `utils/repository/JbdcTestHelper.kt`. `KafkaTestConsumer` for asserting produced events; use Awaitility for async assertions.
- Mockito runs as a Java agent (configured in `build.gradle.kts`); use `mockito-kotlin`.

## CI/CD

`.github/workflows/bazar-space.yml`: on push/PR to `master` or `release/**`, it runs `./gradlew test` and `bootJar`, plus CodeQL. Pushes to `master` publish the Docker image `<version>-SNAPSHOT`. Pushes to `release/**` publish `<version>` and `latest`, and tag `bspace-<version>`. The version is read from `version = "..."` in `build.gradle.kts`.
