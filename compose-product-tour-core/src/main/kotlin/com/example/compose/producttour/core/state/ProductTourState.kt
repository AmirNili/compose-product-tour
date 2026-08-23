package com.example.compose.producttour.core.state

/**
 * Minimal state contract for controlling a product tour.
 *
 * Implementations own their state; callers should hoist one instance and pass it to UI.
 */
public interface ProductTourState {
    /** Zero-based index of the current step. */
    public val currentStep: Int

    /** Whether the tour is currently visible and accepting navigation. */
    public val isRunning: Boolean

    /** ID of the current step, if a step is available. */
    public val currentStepId: String?

    /**
     * Replaces the ordered step list.
     *
     * Implementations must reject duplicate IDs and clamp an out-of-range current index.
     */
    public fun updateSteps(steps: List<com.example.compose.producttour.core.model.TourStep>)

    /** Starts the tour; after a previous finish or skip, the next run starts at step zero. */
    public fun start()

    /** Advances to the next step, or finishes when already at the last step. */
    public fun next()

    /** Moves to the previous step when possible. */
    public fun previous()

    /** Stops the tour without marking it completed. */
    public fun skip()

    /** Marks the tour as completed and stops it. */
    public fun finish()
}
