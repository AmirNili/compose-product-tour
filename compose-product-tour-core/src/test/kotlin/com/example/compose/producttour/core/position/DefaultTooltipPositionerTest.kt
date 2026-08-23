package com.example.compose.producttour.core.position

import com.example.compose.producttour.core.model.Bounds
import com.example.compose.producttour.core.model.LayoutDirection
import com.example.compose.producttour.core.model.TooltipPlacement
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultTooltipPositionerTest {
    @Test
    fun autoPlacementPrefersSpaceBelowAndClampsToContainer() {
        val result = DefaultTooltipPositioner.position(
            TooltipPositionInput(
                target = Bounds(left = 80f, top = 80f, right = 120f, bottom = 120f),
                tooltipWidth = 200f,
                tooltipHeight = 40f,
                containerWidth = 240f,
                containerHeight = 240f,
                preferredPlacement = TooltipPlacement.Auto,
            ),
        )

        assertEquals(TooltipPlacement.Below, result.placement)
        assertEquals(0f, result.left, 0.001f)
        assertEquals(128f, result.top, 0.001f)
    }

    @Test
    fun startPlacementResolvesAgainstRtlDirection() {
        val result = DefaultTooltipPositioner.position(
            TooltipPositionInput(
                target = Bounds(100f, 100f, 140f, 140f),
                tooltipWidth = 40f,
                tooltipHeight = 20f,
                containerWidth = 300f,
                containerHeight = 300f,
                preferredPlacement = TooltipPlacement.Start,
                layoutDirection = LayoutDirection.Rtl,
            ),
        )

        assertEquals(148f, result.left, 0.001f)
    }

    @Test
    fun unavailablePreferredPlacementFallsBackToBelow() {
        val result = DefaultTooltipPositioner.position(
            TooltipPositionInput(
                target = Bounds(100f, 10f, 140f, 40f),
                tooltipWidth = 80f,
                tooltipHeight = 30f,
                containerWidth = 300f,
                containerHeight = 200f,
                preferredPlacement = TooltipPlacement.Above,
            ),
        )

        assertEquals(TooltipPlacement.Below, result.placement)
        assertEquals(48f, result.top, 0.001f)
    }
}
