# Repository Guidelines

## Project Structure & Module Organization

This is a Gradle multi-module Java project for an order-processing microservices system. Modules are declared in `settings.gradle`: `order-service`, `inventory-service`, `payment-service`, and `notification-service`.

Each service follows the standard Spring layout: production code belongs in `src/main/java`, configuration in `src/main/resources/application.yml`, and tests in `src/test/java`. Shared infrastructure lives at the repository root, including `docker-compose.yaml` and database initialization scripts under `docker/postgres/init`.

## Build, Test, and Development Commands

- `./gradlew build` compiles all modules, runs tests, and creates build outputs.
- `./gradlew test` runs the full test suite with JUnit Platform.
- `./gradlew :order-service:test` runs tests for one service module.
- `./gradlew :order-service:bootRun` starts a single Spring Boot service locally.
- `docker compose up -d` starts local Postgres and RabbitMQ dependencies.
- `docker compose down` stops local infrastructure.

Use the Gradle wrapper (`./gradlew`) rather than a system Gradle install so builds use the project-supported version.

## Coding Style & Naming Conventions

Use Java 21, configured in the root `build.gradle` toolchain. Follow conventional Java formatting with 4-space indentation, braces on the same line, and clear package names under `demo.<service>`.

Name domain types with nouns such as `Order`, `OrderItem`, and `OrderStatus`. Use descriptive method names that express business behavior, for example `confirmPayment()` or `completeOrder()`. Keep service-specific code inside its module; avoid cross-module coupling unless a shared library is intentionally introduced.

## Testing Guidelines

Tests use JUnit 5 via `useJUnitPlatform()`. Place tests beside the owning module under `src/test/java`, mirroring the production package. Prefer focused domain tests for business rules and Spring integration tests only when framework behavior or wiring is part of the risk.

Test class names should end with `Test`. Existing tests use Portuguese `@DisplayName` descriptions; keep display names readable and behavior-focused. Run `./gradlew test` before opening a pull request.

## Commit & Pull Request Guidelines

Recent history uses Conventional Commit-style messages, especially `feat: ...`. Keep commit subjects short, imperative, and scoped to the change, for example `feat: add order cancellation rule` or `fix: validate empty order items`.

Pull requests should include a brief summary, affected modules, test results, and any infrastructure changes. Link related issues when available. Include screenshots only for user-visible HTTP/API documentation or UI changes.

## Security & Configuration Tips

Do not commit real credentials. Local defaults in `docker-compose.yaml` and `application.yml` are for development only. Keep environment-specific secrets outside the repository and document required variables when adding new configuration.
