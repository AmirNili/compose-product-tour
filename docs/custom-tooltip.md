# Custom tooltip

برای کنترل کامل ظاهر tooltip از slot `tooltipContent` استفاده کنید. این slot فقط داده و action
می‌گیرد و state داخلی را expose نمی‌کند:

```kotlin
ProductTour(
    state = state,
    steps = steps,
    config = ProductTourUiConfig(labels = TourLabels(next = "Continue")),
    tooltipContent = { step, actions, labels, progress ->
        MyTooltip(
            title = step.title,
            body = step.description,
            progress = progress,
            onNext = actions.next,
            onPrevious = actions.previous,
            onSkip = actions.skip,
            nextLabel = labels.next,
        )
    },
) {
    content()
}
```

کنترل‌های قابل دسترس باید semantics مناسب و role `Button` داشته باشند. متن‌های قابل مشاهده و
labelها را از resource یا localization اپلیکیشن تأمین کنید.
