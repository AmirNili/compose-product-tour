package com.example.compose.producttour.ui

import androidx.compose.ui.graphics.Color

/**
 * Visual tokens for the scrim and spotlight.
 *
 * Placement, animation, labels and accessibility remain in [ProductTourUiConfig].
 */
public data class ProductTourOverlayStyle(
    val color: Color = Color.Black,
    val alpha: Float = 0.64f,
    val spotlightBorderColor: Color = Color.Transparent,
    val spotlightBorderWidth: Float = 0f,
)
