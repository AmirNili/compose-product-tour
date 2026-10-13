package io.github.amirnili.producttour.core.target

import io.github.amirnili.producttour.core.model.Bounds

/**
 * Immutable registration snapshot supplied by a target-aware UI adapter.
 */
public data class TargetRegistration(
    val id: String,
    val bounds: Bounds?,
    val isLaidOut: Boolean,
)

/**
 * Contract for registering and removing targets.
 *
 * A target may be registered before it has bounds; consumers must handle `isLaidOut == false`.
 */
public interface TargetRegistry {
    /** Registers or replaces a target snapshot. */
    public fun register(target: TargetRegistration)

    /** Removes a target when its owner leaves composition. */
    public fun unregister(id: String)

    /** Returns the latest snapshot, or null if the target is not registered. */
    public fun find(id: String): TargetRegistration?

    /** Returns the latest registrations in stable insertion order. */
    public fun all(): List<TargetRegistration>
}
