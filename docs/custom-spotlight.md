# Custom spotlight

The built-in renderer supports:

- `SpotlightShape.RoundedRectangle`
- `SpotlightShape.Circle`
- `SpotlightShape.Rounded(cornerRadius)`

Use `TourStep.padding` to expand the highlighted bounds. Configure the
spotlight border through `ProductTourOverlayStyle`.

```kotlin
ProductTourUiConfig(
    overlayStyle = ProductTourOverlayStyle(
        spotlightBorderColor = Color(0xFF7F56D9),
        spotlightBorderWidth = 2f,
    ),
)
```

For a completely custom renderer, keep the core `SpotlightShape` and `Bounds`
contracts unchanged and provide a custom UI layer in a future extension
module.
