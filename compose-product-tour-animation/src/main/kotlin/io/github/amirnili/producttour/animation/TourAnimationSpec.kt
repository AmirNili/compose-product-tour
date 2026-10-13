package io.github.amirnili.producttour.animation

/**
 * Lightweight animation configuration shared by future UI implementations.
 */
public data class TourAnimationSpec(
    val enabled: Boolean = true,
    val durationMillis: Int = 250,
    val delayMillis: Int = 0,
)
