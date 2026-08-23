package com.example.compose.producttour.core.state

import com.example.compose.producttour.core.model.TourStep

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
