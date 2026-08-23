# FAQ

### اگر step target نداشته باشد چه می‌شود؟

تور متوقف یا crash نمی‌کند؛ تا زمانی که target با bounds معتبر ثبت شود tooltip و spotlight
نمایش داده نمی‌شوند.

### آیا Material 3 اجباری است؟

خیر. core و ui بدون Material کار می‌کنند و adapter Material در ماژول جداگانه است.

### آیا scroll خودکار وجود دارد؟

در `0.1.0` orchestration کامل scroll ارائه نشده است. اپلیکیشن می‌تواند پیش از `next()` با
`BringIntoViewRequester` یا scroll state خود target را آماده کند.

### آیا state سراسری است؟

خیر. state با `rememberProductTourState` ساخته و توسط caller hoist می‌شود.
