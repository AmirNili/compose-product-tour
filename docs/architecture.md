# Architecture

The library is designed around small public contracts, state hoisting, and strict dependency
direction. Core behavior must be testable without Android UI or Material.

```text
sample -> material -> ui -> core
                  animation -> core
```

`core` has no Compose dependency. `ui` owns Compose integration and target lifecycle. `material`
is an optional adapter, so applications can provide their own tooltip visuals without pulling
Material 3 into core.

## Module responsibilities

### `compose-product-tour-core`

Owns `TourStep`, bounds, shapes, layout direction, `ProductTourState`, target registration,
events, and the pure `TooltipPositioner` contract. The default positioner is deterministic and
unit-testable on the JVM.

### `compose-product-tour-ui`

Owns composables and Compose lifecycle integration. `ProductTour` provides a target scope;
`Modifier.tourTarget` reports root bounds and unregisters itself from the registry when leaving
composition. The container renders overlay, spotlight and adaptive tooltip layers.

### `compose-product-tour-animation`

Contains stable configuration objects for overlay, spotlight, and tooltip animation slots. It
does not dictate a specific animation engine or expose mutable global state.

### `compose-product-tour-material`

Provides a Material 3 tooltip content adapter. It depends on UI and core, never the reverse.

### `sample`

An executable consumer of the public API used for manual validation.

## Lifecycle and state rules

- State is created with `rememberProductTourState` and hoisted by the caller.
- Targets are keyed by caller-provided IDs and replaced on re-registration.
- A target may exist before layout; `TargetRegistration.isLaidOut` and nullable bounds express this.
- Composition disposal always unregisters the target.
- No singleton or global mutable state is used.
- Event callbacks and baseline accessibility semantics are implemented; scrolling and persistence
  remain extension points for later phases.
