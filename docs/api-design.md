# API design and technical contracts

The public API is intentionally small:

- `TourStep`, `SpotlightShape`, `Bounds`, `TooltipPlacement`, and
  `LayoutDirection`.
- `ProductTourState`, `rememberProductTourState`, and `ProductTour`.
- `Modifier.tourTarget`.
- `MaterialTourTooltip`.
- `TooltipPositioner` and its input/output value types.
- `ProductTourUiConfig`, `ProductTourOverlayStyle`,
  `ProductTourTooltipStyle`, and `MaterialTourTooltipStyle`.

Step text can be supplied directly or through application string resources:

```kotlin
TourStep(
    id = "search",
    titleResId = R.string.tour_search_title,
    descriptionResId = R.string.tour_search_description,
)
```

The UI module resolves resource IDs using the host application's current
`Resources` configuration. Core remains independent of Android resources.

Visual configuration is separated from behavior:

- Overlay and spotlight appearance belongs to `ProductTourOverlayStyle`.
- Tooltip appearance belongs to `ProductTourTooltipStyle`.
- Material 3 overrides belong to `MaterialTourTooltipStyle`.
- Placement, labels, progress, animation, and accessibility belong to
  `ProductTourUiConfig`.

Deprecated visual fields remain as a migration fallback for existing callers.
