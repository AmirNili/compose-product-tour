package com.example.compose.producttour.core.model

/**
 * A single, stable item in a product tour.
 *
 * Padding is expressed in density-independent pixels and is converted by the UI adapter.
 */
public data class TourStep(
    val id: String,
    val title: String,
    val description: String,
    val spotlightShape: SpotlightShape = SpotlightShape.RoundedRectangle,
    val padding: Float = 8f,
)
