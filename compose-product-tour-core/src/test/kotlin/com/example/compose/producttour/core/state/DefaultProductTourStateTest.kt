package com.example.compose.producttour.core.state

import com.example.compose.producttour.core.model.TourStep
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultProductTourStateTest {
    private val steps = listOf(
        TourStep("one", "One", "First"),
        TourStep("two", "Two", "Second"),
        TourStep("three", "Three", "Third"),
    )

    @Test
    fun startNextPreviousSkipAndFinish() {
        var skipped = 0
        var finished = 0
        val changed = mutableListOf<String>()
        val state = createProductTourState(
            callbacks = TourCallbacks(
                onStepChanged = { step, _ -> changed += step.id },
                onFinished = { finished++ },
                onSkipped = { skipped++ },
            ),
        )
        state.updateSteps(steps)
        state.start()
        assertTrue(state.isRunning)
        assertEquals(listOf("one"), changed)
        state.next()
        assertEquals(1, state.currentStep)
        assertEquals(listOf("one", "two"), changed)
        state.previous()
        assertEquals(0, state.currentStep)
        state.skip()
        assertFalse(state.isRunning)
        assertEquals(1, skipped)
        state.start()
        state.next()
        state.next()
        state.next()
        assertFalse(state.isRunning)
        assertEquals(1, finished)
    }

    @Test
    fun emptyStepsCannotStart() {
        val state = createProductTourState()
        state.updateSteps(emptyList())
        state.start()
        assertFalse(state.isRunning)
        assertEquals(null, state.currentStepId)
    }

    @Test
    fun tourCanBeStartedAgainAfterSkip() {
        val state = createProductTourState()
        state.updateSteps(steps)
        state.start()
        state.skip()
        state.start()
        assertTrue(state.isRunning)
        assertEquals("one", state.currentStepId)
    }

    @Test
    fun tourStartsAtFirstStepAfterFinish() {
        val state = createProductTourState()
        state.updateSteps(steps)
        state.start()
        state.next()
        state.next()
        state.next()
        assertFalse(state.isRunning)
        state.start()
        assertEquals(0, state.currentStep)
        assertEquals("one", state.currentStepId)
    }

    @Test
    fun initialStepIsClampedWhenStepListChanges() {
        val state = createProductTourState(initialStep = 99)
        state.updateSteps(steps)
        assertEquals(2, state.currentStep)
        state.updateSteps(listOf(steps.first()))
        assertEquals(0, state.currentStep)
    }

    @Test
    fun previousAtFirstAndNextAtLastAreSafe() {
        var finished = 0
        val state = createProductTourState(callbacks = TourCallbacks(onFinished = { finished++ }))
        state.updateSteps(steps)
        state.start()
        state.previous()
        assertEquals(0, state.currentStep)
        state.next()
        state.next()
        state.next()
        state.next()
        assertEquals(1, finished)
        assertFalse(state.isRunning)
    }

    @Test(expected = IllegalArgumentException::class)
    fun duplicateStepIdsAreRejected() {
        createProductTourState().updateSteps(
            listOf(
                TourStep("same", "One", "First"),
                TourStep("same", "Two", "Second"),
            ),
        )
    }
}
