package com.example.compose.producttour.animation

/**
 * Named animation slots keep animation choices extensible without exposing implementation details.
 */
public data class TourAnimationConfig(
    val overlay: TourAnimationSpec = TourAnimationSpec(),
    val spotlight: TourAnimationSpec = TourAnimationSpec(),
    val tooltip: TourAnimationSpec = TourAnimationSpec(),
)
