package io.github.amirnili.producttour.core.model

/**
 * A platform-neutral rectangle in root coordinates, measured in pixels.
 */
public data class Bounds(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
) {
    public val width: Float get() = right - left
    public val height: Float get() = bottom - top
    public val centerX: Float get() = (left + right) / 2f
    public val centerY: Float get() = (top + bottom) / 2f
}
