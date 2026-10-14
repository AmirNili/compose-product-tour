# Compose Product Tour

[![JitPack](https://jitpack.io/v/AmirNili/compose-product-tour.svg)](https://jitpack.io/#AmirNili/compose-product-tour)
[![License: Apache-2.0](https://img.shields.io/badge/License-Apache--2.0-blue.svg)](LICENSE)
![minSdk 24](https://img.shields.io/badge/minSdk-24-green)
![Jetpack Compose 1.9.5](https://img.shields.io/badge/Jetpack%20Compose-1.9.5-blue)

A modular, customizable product tour and coach marks library for Android apps built with Jetpack Compose.

**Package prefix:** `io.github.amirnili.producttour` (since `v0.2.0`).

## Demo

<p align="center">
  <img src="demo/product-tour.gif" alt="Compose Product Tour highlighting five targets with spotlight overlays and tooltips" width="320">
</p>

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
    implementation("com.github.AmirNili.compose-product-tour:compose-product-tour-core:v0.2.0")
    implementation("com.github.AmirNili.compose-product-tour:compose-product-tour-ui:v0.2.0")
    // Optional Material 3 tooltip adapter:
    implementation("com.github.AmirNili.compose-product-tour:compose-product-tour-material:v0.2.0")
}
```

The UI module depends on core and animation. The Material module depends on core and UI. Add the animation module directly only if you use its API independently.

## Quick start

Create a tour state, define a step, and mark a composable with the same target ID:

```kotlin
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amirnili.producttour.core.model.SpotlightShape
import io.github.amirnili.producttour.core.model.TourStep
import io.github.amirnili.producttour.ui.ProductTour
import io.github.amirnili.producttour.ui.rememberProductTourState
import io.github.amirnili.producttour.ui.target.tourTarget

@Composable
fun SearchScreen() {
    val tourState = rememberProductTourState()
    val steps = listOf(
        TourStep(
            id = "search",
            title = "Search",
            description = "Tap here to search your workspace.",
            spotlightShape = SpotlightShape.RoundedRectangle,
        ),
    )

    ProductTour(state = tourState, steps = steps) {
        BasicText(
            text = "Search",
            modifier = Modifier
                .tourTarget("search")
                .clickable(onClick = {})
                .padding(16.dp),
        )
    }

    LaunchedEffect(Unit) {
        tourState.start()
    }
}
```

For localized tour text, use `titleResId` and `descriptionResId` with string resources from your app.

See [Getting Started](docs/getting-started.md) for lifecycle details.

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

The sample consumes published artifacts by default. To build it against the local library modules while developing:

```bash
./gradlew -PsampleUsePublishedProductTour=false :sample:assembleDebug
```

## License

Distributed under the Apache License 2.0. See [LICENSE](LICENSE).
