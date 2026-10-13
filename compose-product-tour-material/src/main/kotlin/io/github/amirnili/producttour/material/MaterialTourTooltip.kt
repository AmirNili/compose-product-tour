package io.github.amirnili.producttour.material

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amirnili.producttour.core.model.TourStep
import io.github.amirnili.producttour.ui.TooltipActions
import io.github.amirnili.producttour.ui.TourLabels

/**
 * Material 3 tooltip content for a tour step.
 *
 * Callers can override colors, shape, typography and the complete content slot.
 */
@Composable
public fun MaterialTourTooltip(
    step: TourStep,
    actions: TooltipActions,
    labels: TourLabels = TourLabels(),
    progress: String? = null,
    modifier: Modifier = Modifier,
    colors: ColorScheme = MaterialTheme.colorScheme,
    shape: androidx.compose.ui.graphics.Shape = MaterialTheme.shapes.medium,
    typography: Typography = MaterialTheme.typography,
    content: (@Composable (
        step: TourStep,
        actions: TooltipActions,
        labels: TourLabels,
        progress: String?,
    ) -> Unit)? = null,
    style: MaterialTourTooltipStyle = MaterialTourTooltipStyle(),
) {
    if (content != null) {
        content(step, actions, labels, progress)
        return
    }
    val effectiveShape = style.shape ?: shape
    val effectiveTypography = style.typography ?: typography
    val effectiveContainerColor = style.containerColor ?: colors.surface
    val effectiveContentColor = style.contentColor ?: colors.onSurface
    val textButtonContentColor = style.textButtonContentColor ?: colors.primary
    val primaryButtonContainerColor = style.primaryButtonContainerColor ?: colors.primary
    val primaryButtonContentColor = style.primaryButtonContentColor ?: colors.onPrimary

    Surface(
        modifier = modifier,
        shape = effectiveShape,
        color = effectiveContainerColor,
        contentColor = effectiveContentColor,
        tonalElevation = style.tonalElevation,
    ) {
        Column(
            modifier = Modifier.padding(style.contentPadding),
            verticalArrangement = Arrangement.spacedBy(style.contentSpacing),
        ) {
            Text(text = step.title, style = effectiveTypography.titleMedium)
            Text(text = step.description, style = effectiveTypography.bodyMedium)
            progress?.let { Text(text = it, style = effectiveTypography.labelMedium) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(style.actionSpacing),
            ) {
                Button(
                    onClick = actions.skip,
                    colors = ButtonDefaults.textButtonColors(contentColor = textButtonContentColor),
                ) {
                    Text(labels.skip, style = effectiveTypography.labelLarge)
                }
                if (actions.canGoPrevious) {
                    Button(
                        onClick = actions.previous,
                        colors = ButtonDefaults.textButtonColors(contentColor = textButtonContentColor),
                    ) {
                        Text(labels.previous, style = effectiveTypography.labelLarge)
                    }
                }
                Button(
                    onClick = if (progress?.let {
                            it.substringBefore('/').toIntOrNull() ==
                                it.substringAfter('/').toIntOrNull()
                        } == true) {
                        actions.finish
                    } else {
                        actions.next
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryButtonContainerColor,
                        contentColor = primaryButtonContentColor,
                    ),
                ) {
                    Text(
                        text = if (progress?.let {
                                it.substringBefore('/').toIntOrNull() ==
                                    it.substringAfter('/').toIntOrNull()
                            } == true) {
                            labels.finish
                        } else {
                            labels.next
                        },
                        style = effectiveTypography.labelLarge,
                    )
                }
            }
        }
    }
}
