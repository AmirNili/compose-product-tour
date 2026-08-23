package com.example.compose.producttour.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import com.example.compose.producttour.animation.TourAnimationConfig
import com.example.compose.producttour.core.model.Bounds
import com.example.compose.producttour.core.model.LayoutDirection
import com.example.compose.producttour.core.model.SpotlightShape
import com.example.compose.producttour.core.model.TooltipPlacement
import com.example.compose.producttour.core.model.TourStep
import com.example.compose.producttour.core.position.TooltipPositionInput
import com.example.compose.producttour.core.position.defaultTooltipPositioner
import com.example.compose.producttour.core.state.ProductTourState
import com.example.compose.producttour.core.state.TourCallbacks
import com.example.compose.producttour.core.state.createProductTourState
import com.example.compose.producttour.core.target.TargetRegistry
import com.example.compose.producttour.core.target.TargetRegistration
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.foundation.Canvas
import androidx.compose.ui.unit.LayoutDirection as ComposeLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics

/**
 * Text labels used by the default tooltip. Applications can localize or replace them.
 */
public data class TourLabels(
    val next: String = "Next",
    val previous: String = "Previous",
    val skip: String = "Skip",
    val finish: String = "Finish",
)

/**
 * Actions exposed to custom tooltip content.
 */
public data class TooltipActions(
    val next: () -> Unit,
    val previous: () -> Unit,
    val skip: () -> Unit,
    val finish: () -> Unit,
)

/**
 * UI configuration for overlay, spotlight and tooltip behavior.
 */
public data class ProductTourUiConfig(
    val overlayColor: Color = Color.Black,
    val overlayAlpha: Float = 0.64f,
    val spotlightBorderColor: Color = Color.Transparent,
    val spotlightBorderWidth: Float = 0f,
    val showProgress: Boolean = true,
    val preferredPlacement: TooltipPlacement = TooltipPlacement.Auto,
    val animation: TourAnimationConfig = TourAnimationConfig(),
    val labels: TourLabels = TourLabels(),
    val overlayContentDescription: String? = null,
    val tooltipContentDescription: String? = null,
)

private interface ReactiveProductTourState {
    val revision: Int
}

private class RememberedProductTourState(
    initialStep: Int,
    callbacks: () -> TourCallbacks,
) : ProductTourState, ReactiveProductTourState {
    private var revisionState by mutableIntStateOf(0)
    private val delegate = createProductTourState(
        initialStep = initialStep,
        callbacks = TourCallbacks(
            onStepChanged = { step, index ->
                callbacks().onStepChanged(step, index)
                revisionState++
            },
            onFinished = {
                callbacks().onFinished()
                revisionState++
            },
            onSkipped = {
                callbacks().onSkipped()
                revisionState++
            },
        ),
    )

    override val revision: Int
        get() = revisionState
    override val currentStep: Int
        get() = delegate.currentStep
    override val isRunning: Boolean
        get() = delegate.isRunning
    override val currentStepId: String?
        get() = delegate.currentStepId

    override fun updateSteps(steps: List<TourStep>) {
        delegate.updateSteps(steps)
        revisionState++
    }

    override fun start() = delegate.start()
    override fun next() = delegate.next()
    override fun previous() = delegate.previous()
    override fun skip() = delegate.skip()
    override fun finish() = delegate.finish()
}

private class InMemoryTargetRegistry : TargetRegistry {
    private val registrations = mutableStateMapOf<String, TargetRegistration>()

    override fun register(target: TargetRegistration) {
        registrations[target.id] = target
    }

    override fun unregister(id: String) {
        registrations.remove(id)
    }

    override fun find(id: String): TargetRegistration? = registrations[id]

    override fun all(): List<TargetRegistration> = registrations.values.toList()
}

internal val LocalTargetRegistry = compositionLocalOf<TargetRegistry> {
    error("tourTarget must be used inside ProductTour")
}

/**
 * Remembers a state holder that can be passed to [ProductTour].
 */
@Composable
public fun rememberProductTourState(
    initialStep: Int = 0,
    callbacks: TourCallbacks = TourCallbacks(),
): ProductTourState {
    val latestCallbacks = rememberUpdatedState(callbacks)
    return remember(initialStep) {
        RememberedProductTourState(initialStep) { latestCallbacks.value }
    }
}

/**
 * Hosts content and renders the active overlay, spotlight and tooltip.
 *
 * Targets should be marked with [com.example.compose.producttour.ui.target.tourTarget].
 * Custom tooltip content receives the current step and navigation actions.
 */
@Composable
public fun ProductTour(
    state: ProductTourState,
    steps: List<TourStep>,
    modifier: Modifier = Modifier,
    config: ProductTourUiConfig = ProductTourUiConfig(),
    tooltipContent: @Composable (
        step: TourStep,
        actions: TooltipActions,
        labels: TourLabels,
        progress: String?,
    ) -> Unit = { step, actions, labels, progress ->
        DefaultTourTooltip(step, actions, labels, progress)
    },
    content: @Composable () -> Unit,
) {
    val registry = remember { InMemoryTargetRegistry() }
    val rootOffset = remember { mutableStateOf(Offset.Zero) }
    val rootSize = remember { mutableStateOf(IntSize.Zero) }
    val revision = (state as? ReactiveProductTourState)?.revision ?: 0
    LaunchedEffect(state, steps) {
        state.updateSteps(steps)
    }

    CompositionLocalProvider(LocalTargetRegistry provides registry) {
        key(revision) {
            Box(
                modifier = modifier
                    .onGloballyPositioned { coordinates ->
                        rootOffset.value = coordinates.localToRoot(Offset.Zero)
                        rootSize.value = coordinates.size
                    },
            ) {
                content()
                val step = steps.getOrNull(state.currentStep)
                val registration = step?.id?.let(registry::find)
                val bounds = registration?.bounds?.let { target ->
                    Bounds(
                        left = target.left - rootOffset.value.x,
                        top = target.top - rootOffset.value.y,
                        right = target.right - rootOffset.value.x,
                        bottom = target.bottom - rootOffset.value.y,
                    )
                }
                if (state.isRunning && step != null && bounds != null && registration.isLaidOut) {
                    TourOverlay(
                        step = step,
                        state = state,
                        target = bounds,
                        containerSize = rootSize.value,
                        stepCount = steps.size,
                        config = config,
                        tooltipContent = tooltipContent,
                    )
                }
            }
        }
    }
}

@Composable
private fun DefaultTourTooltip(
    step: TourStep,
    actions: TooltipActions,
    labels: TourLabels,
    progress: String?,
) {
    Column(
        modifier = Modifier
            .widthIn(max = 320.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
            .border(1.dp, Color(0x22000000), RoundedCornerShape(16.dp)),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        BasicText(step.title)
        BasicText(step.description)
        if (progress != null) BasicText(progress)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicText(
                labels.skip,
                modifier = Modifier
                    .clickable(onClick = actions.skip)
                    .semantics { role = Role.Button },
            )
            BasicText(
                labels.previous,
                modifier = Modifier
                    .clickable(onClick = actions.previous)
                    .semantics { role = Role.Button },
            )
            val isLast = progress?.let {
                it.substringBefore('/').toIntOrNull() == it.substringAfter('/').toIntOrNull()
            } == true
            BasicText(
                if (isLast) labels.finish else labels.next,
                modifier = Modifier
                    .clickable(
                        onClick = if (isLast) {
                            actions.finish
                        } else {
                            actions.next
                        },
                    )
                    .semantics { role = Role.Button },
            )
        }
    }
}

@Composable
private fun TourOverlay(
    step: TourStep,
    state: ProductTourState,
    target: Bounds,
    containerSize: IntSize,
    stepCount: Int,
    config: ProductTourUiConfig,
    tooltipContent: @Composable (
        step: TourStep,
        actions: TooltipActions,
        labels: TourLabels,
        progress: String?,
    ) -> Unit,
) {
    val density = LocalDensity.current
    val paddingPx = with(density) { step.padding.dp.toPx() }
    val padded = Bounds(
        target.left - paddingPx,
        target.top - paddingPx,
        target.right + paddingPx,
        target.bottom + paddingPx,
    )
    val duration = if (config.animation.spotlight.enabled) {
        config.animation.spotlight.durationMillis
    } else {
        0
    }
    val animatedLeft by animateFloatAsState(padded.left, tween(duration), label = "spotlight-left")
    val animatedTop by animateFloatAsState(padded.top, tween(duration), label = "spotlight-top")
    val animatedRight by animateFloatAsState(padded.right, tween(duration), label = "spotlight-right")
    val animatedBottom by animateFloatAsState(padded.bottom, tween(duration), label = "spotlight-bottom")
    val overlayAlpha by animateFloatAsState(
        if (state.isRunning) config.overlayAlpha else 0f,
        tween(if (config.animation.overlay.enabled) config.animation.overlay.durationMillis else 0),
        label = "overlay-alpha",
    )
    val rect = Bounds(animatedLeft, animatedTop, animatedRight, animatedBottom)

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen },
        ) {
            drawRect(color = config.overlayColor.copy(alpha = overlayAlpha))
            val path = spotlightPath(rect, step.spotlightShape)
            drawPath(path, color = Color.Transparent, blendMode = BlendMode.Clear)
            if (config.spotlightBorderWidth > 0f && config.spotlightBorderColor.alpha > 0f) {
                drawPath(
                    path = path,
                    color = config.spotlightBorderColor,
                    style = Stroke(config.spotlightBorderWidth),
                )
            }
        }
        AnimatedVisibility(
            visible = state.isRunning,
            enter = androidx.compose.animation.fadeIn(
                tween(if (config.animation.tooltip.enabled) config.animation.tooltip.durationMillis else 0),
            ),
            exit = androidx.compose.animation.fadeOut(
                tween(if (config.animation.tooltip.enabled) config.animation.tooltip.durationMillis else 0),
            ),
        ) {
            TooltipHost(
                step = step,
                state = state,
                target = rect,
                containerSize = containerSize,
                stepCount = stepCount,
                config = config,
                tooltipContent = tooltipContent,
            )
        }
    }
}

@Composable
private fun TooltipHost(
    step: TourStep,
    state: ProductTourState,
    target: Bounds,
    containerSize: IntSize,
    stepCount: Int,
    config: ProductTourUiConfig,
    tooltipContent: @Composable (
        step: TourStep,
        actions: TooltipActions,
        labels: TourLabels,
        progress: String?,
    ) -> Unit,
) {
    var tooltipSize by remember { mutableStateOf(IntSize.Zero) }
    val direction = when (LocalLayoutDirection.current) {
        ComposeLayoutDirection.Ltr -> LayoutDirection.Ltr
        ComposeLayoutDirection.Rtl -> LayoutDirection.Rtl
    }
    val position = defaultTooltipPositioner().position(
        TooltipPositionInput(
            target = target,
            tooltipWidth = tooltipSize.width.coerceAtLeast(1).toFloat(),
            tooltipHeight = tooltipSize.height.coerceAtLeast(1).toFloat(),
            containerWidth = containerSize.width.toFloat(),
            containerHeight = containerSize.height.toFloat(),
            preferredPlacement = config.preferredPlacement,
            layoutDirection = direction,
            margin = 12f,
        ),
    )
    val progress = if (config.showProgress && stepCount > 0) {
        "${state.currentStep + 1}/$stepCount"
    } else {
        null
    }
    Box(
        modifier = Modifier
            .offset { IntOffset(position.left.toInt(), position.top.toInt()) }
            .onSizeChanged { tooltipSize = it }
            .sizeIn(maxWidth = containerSize.width.coerceAtLeast(1).dp),
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.semantics {
                contentDescription = config.tooltipContentDescription
                    ?: listOf(step.title, step.description)
                        .filter(String::isNotBlank)
                        .joinToString(". ")
            },
        ) {
            tooltipContent(
                step,
                TooltipActions(state::next, state::previous, state::skip, state::finish),
                config.labels,
                progress,
            )
        }
    }
}

private fun DrawScope.spotlightPath(bounds: Bounds, shape: SpotlightShape): Path {
    val path = Path()
    val rect = Rect(bounds.left, bounds.top, bounds.right, bounds.bottom)
    when (shape) {
        SpotlightShape.Circle -> {
            val radius = minOf(rect.width, rect.height) / 2f
            path.addOval(
                Rect(
                    rect.center.x - radius,
                    rect.center.y - radius,
                    rect.center.x + radius,
                    rect.center.y + radius,
                ),
            )
        }
        SpotlightShape.RoundedRectangle -> path.addRoundRect(
            androidx.compose.ui.geometry.RoundRect(rect, 12f, 12f),
        )
        is SpotlightShape.Rounded -> path.addRoundRect(
            androidx.compose.ui.geometry.RoundRect(
                rect,
                shape.cornerRadius,
                shape.cornerRadius,
            ),
        )
    }
    return path
}
