package io.github.amirnili.producttour.core.state

import io.github.amirnili.producttour.core.model.TourStep

/**
 * Optional lifecycle callbacks for a tour.
 *
 * Defaults are no-ops so simple callers do not need to provide callbacks.
 */
public data class TourCallbacks(
    val onStepChanged: (step: TourStep, index: Int) -> Unit = { _, _ -> },
    val onFinished: () -> Unit = {},
    val onSkipped: () -> Unit = {},
)
