package com.example.compose.producttour.core.position

import com.example.compose.producttour.core.model.TooltipPlacement
import com.example.compose.producttour.core.model.LayoutDirection
import kotlin.math.max
import kotlin.math.min

/**
 * Temporary deterministic positioning implementation used by tests and future UI adapters.
 *
 * It is internal on purpose: the public seam is [TooltipPositioner], which can evolve without
 * making a particular fallback policy part of the stable API.
 */
internal object DefaultTooltipPositioner : TooltipPositioner {
    override fun position(input: TooltipPositionInput): TooltipPosition {
        val candidates = when (input.preferredPlacement) {
            TooltipPlacement.Above -> listOf(
                TooltipPlacement.Above,
                TooltipPlacement.Below,
                TooltipPlacement.End,
                TooltipPlacement.Start,
            )
            TooltipPlacement.Below -> listOf(
                TooltipPlacement.Below,
                TooltipPlacement.Above,
                TooltipPlacement.End,
                TooltipPlacement.Start,
            )
            TooltipPlacement.Start -> listOf(
                TooltipPlacement.Start,
                TooltipPlacement.End,
                TooltipPlacement.Below,
                TooltipPlacement.Above,
            )
            TooltipPlacement.End -> listOf(
                TooltipPlacement.End,
                TooltipPlacement.Start,
                TooltipPlacement.Below,
                TooltipPlacement.Above,
            )
            TooltipPlacement.Auto -> listOf(
                TooltipPlacement.Below,
                TooltipPlacement.Above,
                TooltipPlacement.End,
                TooltipPlacement.Start,
            )
        }
        val preferred = candidates.firstOrNull { placement ->
            placementFits(input, placement)
        } ?: candidates.first()

        val rawLeft = when (preferred) {
            TooltipPlacement.Start ->
                if (input.layoutDirection == LayoutDirection.Ltr) {
                    input.target.left - input.margin - input.tooltipWidth
                } else {
                    input.target.right + input.margin
                }
            TooltipPlacement.End ->
                if (input.layoutDirection == LayoutDirection.Ltr) {
                    input.target.right + input.margin
                } else {
                    input.target.left - input.margin - input.tooltipWidth
                }
            else -> input.target.centerX - input.tooltipWidth / 2f
        }
        val rawTop = when (preferred) {
            TooltipPlacement.Above -> input.target.top - input.margin - input.tooltipHeight
            TooltipPlacement.Below -> input.target.bottom + input.margin
            else -> input.target.centerY - input.tooltipHeight / 2f
        }

        val maxLeft = max(0f, input.containerWidth - input.tooltipWidth)
        val maxTop = max(0f, input.containerHeight - input.tooltipHeight)
        return TooltipPosition(
            left = min(max(0f, rawLeft), maxLeft),
            top = min(max(0f, rawTop), maxTop),
            placement = preferred,
        )
    }

    private fun placementFits(input: TooltipPositionInput, placement: TooltipPlacement): Boolean {
        val horizontal = when (placement) {
            TooltipPlacement.Start -> if (input.layoutDirection == LayoutDirection.Ltr) {
                input.target.left - input.margin - input.tooltipWidth >= 0f
            } else {
                input.target.right + input.margin + input.tooltipWidth <= input.containerWidth
            }
            TooltipPlacement.End -> if (input.layoutDirection == LayoutDirection.Ltr) {
                input.target.right + input.margin + input.tooltipWidth <= input.containerWidth
            } else {
                input.target.left - input.margin - input.tooltipWidth >= 0f
            }
            else -> false
        }
        val vertical = when (placement) {
            TooltipPlacement.Above ->
                input.target.top - input.margin - input.tooltipHeight >= 0f
            TooltipPlacement.Below ->
                input.target.bottom + input.margin + input.tooltipHeight <= input.containerHeight
            else -> false
        }
        return when (placement) {
            TooltipPlacement.Above, TooltipPlacement.Below -> vertical
            TooltipPlacement.Start, TooltipPlacement.End -> horizontal
            TooltipPlacement.Auto -> false
        }
    }
}

/**
 * Returns the default deterministic tooltip positioner.
 *
 * The implementation is kept internal so the fallback policy can evolve behind this contract.
 */
public fun defaultTooltipPositioner(): TooltipPositioner = DefaultTooltipPositioner
