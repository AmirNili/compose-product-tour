package com.example.compose.producttour.material

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
import com.example.compose.producttour.core.model.TourStep
import com.example.compose.producttour.ui.TooltipActions
import com.example.compose.producttour.ui.TourLabels

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
) {
    if (content != null) {
        content(step, actions, labels, progress)
        return
    }
    Surface(
        modifier = modifier,
        shape = shape,
        color = colors.surface,
        contentColor = colors.onSurface,
        tonalElevation = 4.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = step.title, style = typography.titleMedium)
            Text(text = step.description, style = typography.bodyMedium)
            progress?.let { Text(text = it, style = typography.labelMedium) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = actions.skip,
                    colors = ButtonDefaults.textButtonColors(contentColor = colors.primary),
                ) {
                    Text(labels.skip, style = typography.labelLarge)
                }
                Button(
                    onClick = actions.previous,
                    colors = ButtonDefaults.textButtonColors(contentColor = colors.primary),
                ) {
                    Text(labels.previous, style = typography.labelLarge)
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
                        style = typography.labelLarge,
                    )
                }
            }
        }
    }
}
