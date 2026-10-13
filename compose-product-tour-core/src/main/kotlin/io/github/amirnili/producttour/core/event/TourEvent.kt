package io.github.amirnili.producttour.core.event

/**
 * Events emitted by a tour controller.
 */
public sealed interface TourEvent {
    public data object Started : TourEvent
    public data class StepChanged(val stepId: String, val index: Int) : TourEvent
    public data object Skipped : TourEvent
    public data object Finished : TourEvent
}

/**
 * Callback contract for observing tour lifecycle events.
 */
public fun interface TourEventListener {
    public fun onEvent(event: TourEvent)
}
