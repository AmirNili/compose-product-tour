package com.example.compose.producttour.core.position

import com.example.compose.producttour.core.model.Bounds
import com.example.compose.producttour.core.model.LayoutDirection
import com.example.compose.producttour.core.model.TooltipPlacement

/**
 * Input to a pure tooltip positioning calculation.
 */
public data class TooltipPositionInput(
    val target: Bounds,
    val tooltipWidth: Float,
    val tooltipHeight: Float,
    val containerWidth: Float,
    val containerHeight: Float,
    val preferredPlacement: TooltipPlacement = TooltipPlacement.Auto,
    val layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    val margin: Float = 8f,
)

/**
 * Result of positioning a tooltip in root coordinates.
 */
public data class TooltipPosition(
    val left: Float,
    val top: Float,
    val placement: TooltipPlacement,
)

/**
 * Pure positioning contract. It deliberately has no Compose dependency.
 */
public fun interface TooltipPositioner {
    public fun position(input: TooltipPositionInput): TooltipPosition
}
