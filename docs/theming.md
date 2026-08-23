# Theming

Visual configuration is split into three focused styles:

1. `ProductTourOverlayStyle` controls the scrim and spotlight border.
2. `ProductTourTooltipStyle` controls the dependency-free tooltip.
3. `MaterialTourTooltipStyle` controls Material 3-specific overrides.

Behavior such as placement, animation, progress, labels, and accessibility
remains in `ProductTourUiConfig`.

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
        actionContentColor = Color(0xFFD6BBF7),
    ),
)
```

Material 3 consumers can override only the tokens they need:

```kotlin
MaterialTourTooltip(
    step = step,
    actions = actions,
    style = MaterialTourTooltipStyle(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        primaryButtonContainerColor = MaterialTheme.colorScheme.primary,
        primaryButtonContentColor = MaterialTheme.colorScheme.onPrimary,
    ),
)
```

The legacy visual fields on `ProductTourUiConfig` remain deprecated as a
migration fallback. New code should use `overlayStyle`.
