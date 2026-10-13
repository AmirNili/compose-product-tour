package io.github.amirnili.producttour.ui.target

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import io.github.amirnili.producttour.core.model.Bounds
import io.github.amirnili.producttour.core.target.TargetRegistration
import io.github.amirnili.producttour.ui.LocalTargetRegistry

/**
 * Marks a composable as a target for a tour step.
 *
 * Registration is removed automatically when this modifier leaves composition.
 */
public fun Modifier.tourTarget(id: String): Modifier = composed {
    val registry = LocalTargetRegistry.current
    SideEffect {
        registry.register(TargetRegistration(id = id, bounds = null, isLaidOut = false))
    }
    DisposableEffect(registry, id) {
        onDispose { registry.unregister(id) }
    }
    onGloballyPositioned { coordinates ->
        val rect = coordinates.boundsInRoot()
        registry.register(
            TargetRegistration(
                id = id,
                bounds = Bounds(rect.left, rect.top, rect.right, rect.bottom),
                isLaidOut = coordinates.isAttached,
            ),
        )
    }
}
