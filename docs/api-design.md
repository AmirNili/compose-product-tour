# API design and technical contracts

## Public API

The initial stable surface is intentionally small:

- `TourStep`, `SpotlightShape`, `Bounds`, `TooltipPlacement`, `LayoutDirection`
- `ProductTourState`
- `rememberProductTourState`, `ProductTour`
- `Modifier.tourTarget`
- `MaterialTourTooltip`
- `TooltipPositioner` and its input/output value types
- `TourEvent` and `TourEventListener`
- `TourCallbacks`, `ProductTourUiConfig`, `TooltipActions` and `TourLabels`

Implementations remain private or internal wherever possible.

`TourStep.titleResId` and `descriptionResId` optionally accept application string resource IDs.
The Compose adapter resolves them using the host application's current `Resources` locale;
direct `title` and `description` remain supported.

## Contracts

### Target registration

Targets have a stable string ID and are registered through `TargetRegistry`. Registration is
replace-by-ID and disposal calls `unregister`. A missing or not-yet-laid-out target is not an
error; consumers should wait for a usable bounds snapshot.

### Bounds calculation

Compose adapters convert `LayoutCoordinates.boundsInRoot()` to the platform-neutral `Bounds`
value. Core never receives `LayoutCoordinates` or density objects.

### Spotlight shape

`SpotlightShape` is a sealed interface. Adding a new shape is source-compatible for consumers
that use an `else` branch and does not require changing target registration.

### Tooltip positioning

`TooltipPositioner` is a pure function over `TooltipPositionInput`. The implementation must
account for container bounds, margins, preferred placement and `LayoutDirection`, and return a
clamped `TooltipPosition`. This contract is directly unit-testable on the JVM.

### Animation

`TourAnimationSpec` and `TourAnimationConfig` reserve named slots for overlay, spotlight and
tooltip transitions. Compose playback uses standard animation APIs and can be disabled through
`TourAnimationSpec.enabled`.

### Events and callbacks

`TourEventListener` observes start, step change, skip and finish events. The state interface does
not require a listener, avoiding callback obligations for simple consumers.

`ProductTourUiConfig.overlayContentDescription` and `tooltipContentDescription` let applications
provide localized accessibility text. Default navigation controls expose Button roles and the
overlay exposes a dismiss action mapped to `skip()`.

### RTL/LTR

Core uses its own `LayoutDirection` enum. Compose adapters map
`androidx.compose.ui.unit.LayoutDirection` to it. `Start` and `End` are logical placements;
positioning must resolve them using direction.

### Not-yet-laid-out targets

`TargetRegistration(bounds = null, isLaidOut = false)` is the expected pre-layout state. UI must
not attempt to draw a spotlight or tooltip until bounds are available.

### Cleanup

`Modifier.tourTarget` owns a `DisposableEffect` keyed by registry and target ID. Leaving
composition removes the registration, preventing stale bounds and leaks.
