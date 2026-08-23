# Known issues

- Compose `androidTest` sources require an emulator or connected device to
  execute. CI currently compiles the instrumentation test source.
- Automatic scrolling, persistence, and deep-link orchestration are planned
  for a future release.
- On very small windows, the tooltip is clamped to the available bounds and
  may overlap application content.
- Dynamic step ID changes require the caller to keep matching targets in the
  composition.
