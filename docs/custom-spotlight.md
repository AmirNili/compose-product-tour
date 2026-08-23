# Custom spotlight

در نسخه `0.1.0` سه شکل پایه ارائه می‌شود:

- `SpotlightShape.RoundedRectangle`
- `SpotlightShape.Circle`

برای گوشه سفارشی از `SpotlightShape.Rounded(cornerRadius)` استفاده کنید. renderer در ماژول UI
قرار دارد و core فقط مدل shape و bounds را می‌شناسد؛ بنابراین می‌توان در نسخه بعد renderer
تزریق‌پذیر برای path، glow یا چند spotlight اضافه کرد بدون تغییر قرارداد state.

`TourStep.padding` فاصله اطراف target را بر حسب dp مشخص می‌کند و border اختیاری در
`ProductTourUiConfig` تنظیم می‌شود.
