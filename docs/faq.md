# FAQ

### What happens when a target is not registered?

The tour state remains valid, but the overlay and tooltip are not rendered
until the target has valid layout coordinates.

### Is Material 3 required?

No. Core and UI do not depend on Material 3. The Material module is optional.

### Does the library scroll to a target automatically?

Not yet. The target registration and positioning contracts leave room for a
future scroll coordinator.

### How do I localize step text?

Use `titleResId` and `descriptionResId` on `TourStep`. The UI resolves them
against the host application's current locale configuration.

### How do I style different product themes?

Use `ProductTourOverlayStyle`, `ProductTourTooltipStyle`, and
`MaterialTourTooltipStyle`. See [Theming](theming.md).
