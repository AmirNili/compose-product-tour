# Contributing

## Workflow

1. Open an issue for significant changes and discuss public API impact.
2. Keep changes scoped to the relevant module.
3. Add KDoc for public APIs and unit tests for pure logic.
4. Avoid new dependencies unless they are justified.
5. Do not commit secrets, signing keys, keystores, or credentials.

## Validation

Run the following before opening a pull request:

```bash
./gradlew clean build
./gradlew test
./gradlew lint
```

Use Kotlin official formatting and keep public APIs minimal and stable.
