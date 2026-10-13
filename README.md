# Compose Product Tour

[![JitPack](https://jitpack.io/v/AmirNili/compose-product-tour-source.svg)](https://jitpack.io/#AmirNili/compose-product-tour-source)
[![License: Apache-2.0](https://img.shields.io/badge/License-Apache--2.0-blue.svg)](LICENSE)
![minSdk 24](https://img.shields.io/badge/minSdk-24-green)
![Jetpack Compose 1.9.5](https://img.shields.io/badge/Jetpack%20Compose-1.9.5-blue)

A modular, customizable product tour and coach marks library for Android apps built with Jetpack Compose.

## Demo

<!-- Add a product tour GIF or screenshot here. -->

**Preview:** _GIF/demo coming soon._

## Features

- Hoisted state with `start`, `next`, `previous`, `skip`, and `finish` actions.
- Lifecycle-aware target registration with `Modifier.tourTarget("id")`.
- Rectangle, rounded rectangle, and circular spotlights.
- Adaptive tooltip placement with RTL/LTR support.
- Custom overlay, spotlight, tooltip, typography, colors, shapes, and animations.
- Optional Material 3 tooltip adapter.
- Resource-backed text and accessibility semantics.
- No global mutable state; the core module has no Material dependency.

## Requirements

- Android API 24 or newer.
- Java 17 to run Gradle. Library bytecode targets Java 11.
- Jetpack Compose 1.9.5 (the version used to build this release).

## Installation

Add JitPack to the repositories in your root `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Add the modules you need to your app's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.AmirNili.compose-product-tour-source:compose-product-tour-core:v0.1.1")
    implementation("com.github.AmirNili.compose-product-tour-source:compose-product-tour-ui:v0.1.1")
    // Optional Material 3 tooltip adapter:
    implementation("com.github.AmirNili.compose-product-tour-source:compose-product-tour-material:v0.1.1")
}
```

The UI module depends on core and animation. The Material module depends on core and UI. Add the animation module directly only if you use its API independently.

## Quick start

Create localized string resources for the step title and description, then define a step with the same ID as its target:

```kotlin
@Composable
fun SearchScreen() {
    val steps = listOf(
        TourStep(
            id = "search",
            titleResId = R.string.tour_search_title,
            descriptionResId = R.string.tour_search_description,
            spotlightShape = SpotlightShape.RoundedRectangle,
        ),
    )
    val tourState = rememberProductTourState()

    ProductTour(state = tourState, steps = steps) {
        SearchButton(Modifier.tourTarget("search"))
    }

    LaunchedEffect(Unit) {
        tourState.start()
    }
}
```

See [Getting Started](docs/getting-started.md) for imports, resources, and lifecycle details.

## Modules

| Module | Purpose |
| --- | --- |
| `compose-product-tour-core` | Models, state, target contracts, and positioning |
| `compose-product-tour-ui` | Compose container, overlay, spotlight, tooltip, and target integration |
| `compose-product-tour-animation` | Animation configuration |
| `compose-product-tour-material` | Optional Material 3 tooltip |
| `sample` | Runnable demonstration app |

## Documentation and project policies

| Guide | Description |
| --- | --- |
| [Getting Started](docs/getting-started.md) | Define steps, register targets, and control the tour |
| [Theming & Styles](docs/theming.md) | Configure overlays, tooltips, and visual styles |
| [Custom Spotlight](docs/custom-spotlight.md) | Customize spotlight rendering |
| [Custom Tooltip](docs/custom-tooltip.md) | Replace or style tooltip content |
| [API Design & Specifications](docs/api-design.md) | Public API contracts and design notes |
| [FAQ](docs/faq.md) | Common questions |
| [Known Issues](docs/known-issues.md) | Current limitations and workarounds |
| [Contributing](CONTRIBUTING.md) | Contribution workflow and validation |
| [Code of Conduct](CODE_OF_CONDUCT.md) | Community expectations |
| [Security](SECURITY.md) | Private vulnerability reporting guidance |

## Build and verify

```bash
./gradlew clean build
./gradlew test
./gradlew lint
```

## License

Distributed under the Apache License 2.0. See [LICENSE](LICENSE).
