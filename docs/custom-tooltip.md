# Custom tooltip

Use the `tooltipContent` slot for complete control over tooltip visuals. The
slot receives the current step, localized labels, progress text, and navigation
actions.

```kotlin
ProductTour(
    state = state,
    steps = steps,
    config = ProductTourUiConfig(
        labels = TourLabels(next = "Continue"),
    ),
    tooltipContent = { step, actions, labels, progress ->
        MyTooltip(
            title = step.title,
            body = step.description,
            progress = progress,
            nextLabel = labels.next,
            onNext = actions.next,
            onPrevious = actions.previous,
            onSkip = actions.skip,
            onFinish = actions.finish,
        )
    },
) {
    AppContent()
}
```

Custom controls should expose button roles and accessible labels. Use resource
strings for application localization.
