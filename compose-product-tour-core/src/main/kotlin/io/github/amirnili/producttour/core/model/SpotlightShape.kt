package io.github.amirnili.producttour.core.model

/**
 * Describes the shape used to highlight a target.
 */
public sealed interface SpotlightShape {
    /** A rounded rectangle around the target. */
    public data object RoundedRectangle : SpotlightShape

    /** A circle that encloses the target bounds. */
    public data object Circle : SpotlightShape

    /** A rectangle with a caller-provided corner radius in dp. */
    public data class Rounded(val cornerRadius: Float) : SpotlightShape
}
