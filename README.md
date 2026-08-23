# Compose Product Tour

یک کتابخانه ماژولار و قابل تست برای Product Tour، Coach Mark، Tooltip و Spotlight در Android
Jetpack Compose.

> وضعیت: نسخه اولیه `0.1.0` آماده build و انتشار Maven است.

## قابلیت‌ها

- state hoisting و کنترل کامل `start`, `next`, `previous`, `skip`, `finish`
- ثبت lifecycle-aware هدف‌ها با `Modifier.tourTarget`
- spotlightهای مستطیلی، گوشه‌گرد و دایره‌ای
- جایگذاری tooltip با fallback و محدودسازی داخل window
- پشتیبانی RTL/LTR و انیمیشن قابل غیرفعال‌سازی
- tooltip پیش‌فرض مستقل از Material و adapter اختیاری Material 3
- callbackهای مرحله، پایان و ردکردن
- semantics برای محتوای tooltip، دکمه‌ها و action ردکردن

## دمو

اپ نمونه در ماژول `sample` سناریوی چندمرحله‌ای واقعی را نمایش می‌دهد. برای انتشار رسانه‌ای،
فایل پیشنهادی `docs/media/product-tour-demo.gif` است؛ در صورت نبود فایل، این مسیر را با GIF
گرفته‌شده از Sample جایگزین کنید.

## نیازمندی‌ها

- Android API 24+
- Kotlin 2.2+
- Jetpack Compose 1.9+
- برای adapter Material: Material 3

## نصب

ماژول‌های مورد نیاز را اضافه کنید (مختصات نسخه در `gradle.properties` متمرکز است):

```kotlin
implementation("com.example.compose.producttour:compose-product-tour-core:0.1.0")
implementation("com.example.compose.producttour:compose-product-tour-ui:0.1.0")
implementation("com.example.compose.producttour:compose-product-tour-material:0.1.0")
```

اگر Material 3 نمی‌خواهید، فقط core و ui را استفاده کنید.

## شروع سریع

```kotlin
val steps = listOf(
    TourStep("search", "جست‌وجو", "برای پیدا کردن محتوا از اینجا شروع کنید."),
    TourStep("save", "ذخیره", "آیتم‌های مهم را ذخیره کنید."),
)
val state = rememberProductTourState()

ProductTour(state = state, steps = steps) {
    SearchButton(Modifier.tourTarget("search"))
    SaveButton(Modifier.tourTarget("save"))
}

LaunchedEffect(Unit) { state.start() }
```

برای tooltip Material 3، `MaterialTourTooltip` را در slot سفارشی `tooltipContent` قرار دهید.

## سفارشی‌سازی

- `ProductTourUiConfig` رنگ overlay، border، placement، progress، animation و labelها را کنترل می‌کند.
- برای tooltip کاملاً سفارشی، slot `tooltipContent` را پیاده‌سازی کنید.
- برای spotlight جدید، `SpotlightShape` و renderer را در لایه UI توسعه دهید؛ core به Compose وابسته نیست.
- قرارداد pure `TooltipPositioner` برای الگوریتم جایگذاری قابل unit test است.

راهنمای کامل در [getting-started](docs/getting-started.md)، [custom-tooltip](docs/custom-tooltip.md)
و [custom-spotlight](docs/custom-spotlight.md) موجود است.

## معماری و ماژول‌ها

- `compose-product-tour-core`: مدل، state، target contract و positioning مستقل از UI
- `compose-product-tour-ui`: Composable، overlay، spotlight و target lifecycle
- `compose-product-tour-animation`: تنظیمات animation
- `compose-product-tour-material`: adapter اختیاری Material 3
- `sample`: مصرف‌کننده واقعی API

جزئیات در [docs/architecture.md](docs/architecture.md) است.

## محدودیت‌های فعلی

Scroll خودکار، persistence و اجرای runtime تست‌های instrumentation به device/emulator متصل نیاز
دارند و در این نسخه abstraction یا پوشش اولیه دارند. موارد شناخته‌شده در
[known-issues](docs/known-issues.md) ثبت شده‌اند.

## Roadmap

نسخه بعدی روی scroll orchestration، API پایدارتر برای custom spotlight renderer، Dokka انتشار‌یافته،
و تست accessibility با TalkBack تمرکز خواهد کرد.

## مشارکت

راهنمای توسعه در [CONTRIBUTING.md](CONTRIBUTING.md) و قوانین رفتاری در
[CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) است. برای گزارش آسیب‌پذیری به
[SECURITY.md](SECURITY.md) مراجعه کنید.

## انتشار

برای ساخت AARهای release:

```text
./gradlew :compose-product-tour-core:bundleReleaseAar
./gradlew :compose-product-tour-ui:bundleReleaseAar
./gradlew :compose-product-tour-animation:bundleReleaseAar
./gradlew :compose-product-tour-material:bundleReleaseAar
```

برای ساخت sources JAR و انتشار محلی:

```text
./gradlew publishToMavenLocal
```

workflow تگ‌های `v*` به GitHub Packages منتشر می‌کند. مقادیر `SIGNING_KEY`،
`SIGNING_PASSWORD` و `GITHUB_TOKEN` فقط از GitHub Secrets خوانده می‌شوند.

## License

Apache License 2.0؛ متن کامل در [LICENSE](LICENSE).
