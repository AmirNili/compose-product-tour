package io.github.amirnili.producttour.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.amirnili.producttour.core.model.TourStep
import io.github.amirnili.producttour.ui.target.tourTarget
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertFalse

@RunWith(AndroidJUnit4::class)
class ProductTourComposeTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rendersTooltipForRegisteredTarget() {
        lateinit var state: io.github.amirnili.producttour.core.state.ProductTourState
        composeRule.setContent {
            state = rememberProductTourState()
            ProductTour(
                state = state,
                steps = listOf(
                    TourStep(
                        id = "target",
                        title = "Target tooltip",
                        description = "Tooltip is visible.",
                    ),
                ),
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .tourTarget("target"),
                )
            }
        }

        composeRule.waitForIdle()
        composeRule.runOnIdle { state.start() }
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Target tooltip").assertIsDisplayed()
    }

    @Test
    fun nextChangesTooltipStep() {
        lateinit var state: io.github.amirnili.producttour.core.state.ProductTourState
        composeRule.setContent {
            state = rememberProductTourState()
            ProductTour(
                state = state,
                steps = listOf(
                    TourStep("one", "First", "First body"),
                    TourStep("two", "Second", "Second body"),
                ),
            ) {
                Box(Modifier.size(80.dp).tourTarget("one"))
                Box(Modifier.size(80.dp).tourTarget("two"))
            }
        }
        composeRule.waitForIdle()
        composeRule.runOnIdle { state.start() }
        composeRule.onNodeWithText("Next").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Second").assertIsDisplayed()
    }

    @Test
    fun skipAndFinishHideTooltip() {
        lateinit var state: io.github.amirnili.producttour.core.state.ProductTourState
        composeRule.setContent {
            state = rememberProductTourState()
            ProductTour(
                state = state,
                steps = listOf(TourStep("target", "Title", "Body")),
            ) {
                Box(Modifier.size(80.dp).tourTarget("target"))
            }
        }
        composeRule.waitForIdle()
        composeRule.runOnIdle { state.start() }
        composeRule.onNodeWithText("Skip").performClick()
        composeRule.waitForIdle()
        composeRule.runOnIdle { assertFalse(state.isRunning) }

        composeRule.runOnIdle { state.start() }
        composeRule.onNodeWithText("Finish").performClick()
        composeRule.waitForIdle()
        composeRule.runOnIdle { assertFalse(state.isRunning) }
    }

    @Test
    fun defaultNavigationExposesClickableSemantics() {
        lateinit var state: io.github.amirnili.producttour.core.state.ProductTourState
        composeRule.setContent {
            state = rememberProductTourState()
            ProductTour(
                state = state,
                steps = listOf(TourStep("target", "Title", "Body")),
            ) {
                Box(Modifier.size(80.dp).tourTarget("target"))
            }
        }
        composeRule.waitForIdle()
        composeRule.runOnIdle { state.start() }
        composeRule.onNodeWithText("Skip").assertHasClickAction()
        composeRule.onNodeWithText("Finish").assertHasClickAction()
    }
}
