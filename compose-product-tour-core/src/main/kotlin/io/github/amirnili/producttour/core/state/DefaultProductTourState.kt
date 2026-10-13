package io.github.amirnili.producttour.core.state

import io.github.amirnili.producttour.core.model.TourStep

/**
 * Creates a platform-neutral state holder for a product tour.
 *
 * The returned interface is the stable public API; the implementation remains private.
 */
public fun createProductTourState(
    initialStep: Int = 0,
    callbacks: TourCallbacks = TourCallbacks(),
): ProductTourState = DefaultProductTourState(initialStep, callbacks)

private class DefaultProductTourState(
    initialStep: Int,
    private val callbacks: TourCallbacks,
) : ProductTourState {
    private var steps: List<TourStep> = emptyList()
    private var index: Int = initialStep.coerceAtLeast(0)
    private var running: Boolean = false
    private var restartFromBeginningOnStart: Boolean = false

    override val currentStep: Int
        get() = index
    override val isRunning: Boolean
        get() = running
    override val currentStepId: String?
        get() = steps.getOrNull(index)?.id

    override fun updateSteps(steps: List<TourStep>) {
        require(steps.map(TourStep::id).toSet().size == steps.size) {
            "Tour step IDs must be unique."
        }
        this.steps = steps
        index = index.coerceIn(0, steps.lastIndex.coerceAtLeast(0))
        if (steps.isEmpty()) running = false
    }

    override fun start() {
        if (steps.isNotEmpty()) {
            if (restartFromBeginningOnStart) {
                index = 0
                restartFromBeginningOnStart = false
            }
            running = true
            steps.getOrNull(index)?.let { callbacks.onStepChanged(it, index) }
        }
    }

    override fun next() {
        if (steps.isEmpty()) return
        if (index < steps.lastIndex) {
            index += 1
            callbacks.onStepChanged(steps[index], index)
        } else {
            finish()
        }
    }

    override fun previous() {
        if (steps.isEmpty() || index == 0) return
        index -= 1
        callbacks.onStepChanged(steps[index], index)
    }

    override fun skip() {
        if (!running) return
        running = false
        restartFromBeginningOnStart = true
        callbacks.onSkipped()
    }

    override fun finish() {
        if (!running) return
        running = false
        restartFromBeginningOnStart = true
        callbacks.onFinished()
    }
}
