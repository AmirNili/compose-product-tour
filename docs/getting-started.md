# Getting started

## Define steps

Each step must have a unique ID matching a target in the composition.

```kotlin
val steps = listOf(
    TourStep(
        id = "search",
        titleResId = R.string.tour_search_title,
        descriptionResId = R.string.tour_search_description,
        spotlightShape = SpotlightShape.RoundedRectangle,
        padding = 8f,
    ),
)
```

## Register targets

```kotlin
ProductTour(state = state, steps = steps) {
    SearchButton(Modifier.tourTarget("search"))
}
```

Targets register their layout bounds and unregister automatically when they
leave the composition.

## Start and restart

```kotlin
val state = rememberProductTourState()

LaunchedEffect(Unit) {
    state.start()
}
```

After `finish()` or `skip()`, the next `start()` begins at the first step.
`Previous` is hidden while the first step is active.
