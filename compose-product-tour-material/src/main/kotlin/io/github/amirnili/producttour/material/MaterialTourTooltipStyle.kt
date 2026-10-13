package io.github.amirnili.producttour.material

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Optional Material 3 overrides for [MaterialTourTooltip].
 *
 * Nullable color, shape and typography values inherit from the active
 * [androidx.compose.material3.MaterialTheme] when they are not supplied.
 */
public data class MaterialTourTooltipStyle(
    val containerColor: Color? = null,
    val contentColor: Color? = null,
    val shape: Shape? = null,
    val typography: Typography? = null,
    val tonalElevation: Dp = 4.dp,
    val contentPadding: Dp = 16.dp,
    val contentSpacing: Dp = 8.dp,
    val actionSpacing: Dp = 8.dp,
    val textButtonContentColor: Color? = null,
    val primaryButtonContainerColor: Color? = null,
    val primaryButtonContentColor: Color? = null,
)
