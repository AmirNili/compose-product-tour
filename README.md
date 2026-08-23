# Compose Product Tour

A modular, testable Product Tour and Coach Marks library for Android Jetpack Compose.

## Features

- Hoisted tour state with `start`, `next`, `previous`, `skip`, and `finish`.
- Lifecycle-aware targets through `Modifier.tourTarget("id")`.
- Rectangle, rounded rectangle, and circular spotlights.
- Adaptive tooltip placement with RTL/LTR support.
- Configurable overlay, spotlight, tooltip, typography, colors, shapes, and animations.
- Material 3 tooltip adapter.
- Resource-backed step text for application localization.
- Accessibility semantics for tooltip content and navigation actions.
- No global mutable state and no Material dependency in core.

## Requirements

- Android API 24+
- Kotlin and Jetpack Compose versions defined in `gradle/libs.versions.toml`
- Java 17 for Gradle builds

## Installation

```kotlin
dependencies {
    implementation("io.github.amirnili:compose-product-tour-core:0.1.0")
    implementation("io.github.amirnili:compose-product-tour-ui:0.1.0")
    implementation("io.github.amirnili:compose-product-tour-material:0.1.0")
}
```

## Quick start

```kotlin
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
```

## Theming

Use `ProductTourOverlayStyle` for the scrim and spotlight, and
`ProductTourTooltipStyle` for the dependency-free tooltip:

```kotlin
ProductTourUiConfig(
    overlayStyle = ProductTourOverlayStyle(
        color = Color(0xFF101828),
        alpha = 0.72f,
        spotlightBorderColor = Color(0xFF7F56D9),
        spotlightBorderWidth = 2f,
    ),
    tooltipStyle = ProductTourTooltipStyle(
        containerColor = Color(0xFF1D2939),
        contentColor = Color.White,
        actionContentColor = Color(0xFFD6BBFB),
    ),
)
```

For Material 3, pass `MaterialTourTooltipStyle` to `MaterialTourTooltip`.
Applications can replace the complete `tooltipContent` slot for full control.

## Modules

- `compose-product-tour-core`: framework-independent models, state, targets, and positioning.
- `compose-product-tour-ui`: Compose container, overlay, spotlight, tooltip, and target integration.
- `compose-product-tour-animation`: animation configuration.
- `compose-product-tour-material`: optional Material 3 tooltip implementation.
- `sample`: runnable demonstration application.

## Documentation

- [Getting started](docs/getting-started.md)
- [Theming](docs/theming.md)
- [Custom tooltip](docs/custom-tooltip.md)
- [Custom spotlight](docs/custom-spotlight.md)
- [Architecture](docs/architecture.md)
- [FAQ](docs/faq.md)
- [Known issues](docs/known-issues.md)

## Build and test

```bash
./gradlew clean build
./gradlew test
./gradlew lint
```

## License

Apache License 2.0. See [LICENSE](LICENSE).
