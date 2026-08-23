# Getting started

## 1. Define steps

هر step یک `id` یکتا دارد. ID باید پایدار باشد و با target متناظر یکی باشد.

```kotlin
val steps = listOf(
    TourStep(
        id = "search",
        titleResId = R.string.tour_search_title,
        descriptionResId = R.string.tour_search_description,
        spotlightShape = SpotlightShape.RoundedRectangle,
    ),
)
```

## 2. Host the tour

```kotlin
val tourState = rememberProductTourState(
    callbacks = TourCallbacks(
        onStepChanged = { step, index -> analytics.track("tour_step", step.id) },
        onFinished = { /* persist completion in the app */ },
        onSkipped = { /* record a skip */ },
    ),
)

ProductTour(state = tourState, steps = steps) {
    SearchButton(Modifier.tourTarget("search"))
}
```

`start()` را از event کاربر یا lifecycle مناسب اپلیکیشن صدا بزنید. اگر target هنوز layout نشده
باشد، overlay تا دریافت bounds معتبر نمایش داده نمی‌شود.

## 3. Build and publish locally

```text
./gradlew clean build
./gradlew publishToMavenLocal
```

After `finish()` or `skip()`, the next `start()` call begins at the first step again.

برای استفاده از Material 3، ماژول `compose-product-tour-material` را نیز اضافه کنید.
