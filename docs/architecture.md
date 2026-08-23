# Architecture

The library uses small public contracts, state hoisting, and one-way
dependency flow:

```text
sample -> material -> ui -> core
                  animation -> core
```

`core` contains framework-independent models, state management, target
contracts, and tooltip positioning. It has no dependency on Compose UI or
Material.

`ui` owns Compose integration, target lifecycle tracking, overlay rendering,
spotlight rendering, and the default dependency-free tooltip.

`animation` contains reusable animation configuration. It does not own tour
state.

`material` is an optional Material 3 adapter. Applications can use `core` and
`ui` without adding Material 3.

All mutable state is scoped to a state holder or a composition-local registry.
There is no global mutable singleton.
