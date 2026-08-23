# Release notes: v0.1.0

The first release provides a modular Compose Product Tour foundation with:

- Core state, step, target, callback, and positioning contracts.
- Compose overlay, spotlight, tooltip, and target lifecycle integration.
- Configurable animations and RTL/LTR-aware tooltip placement.
- Material 3 tooltip support with theme overrides.
- Resource-backed localized step content.
- Unit tests, Compose test sources, Maven publication, and CI workflows.

Validation:

```bash
./gradlew clean build
./gradlew test
./gradlew lint
./gradlew publishToMavenLocal
```
