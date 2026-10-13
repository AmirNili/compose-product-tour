package io.github.amirnili.producttour.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Visual tokens for the library's dependency-free default tooltip.
 *
 * Applications can create one style per product theme and pass it through
 * [ProductTourUiConfig.tooltipStyle]. Material 3 consumers can use the corresponding
 * [io.github.amirnili.producttour.material.MaterialTourTooltipStyle].
 */
public data class ProductTourTooltipStyle(
    val containerColor: Color = Color.White,
    val contentColor: Color = Color.Black,
    val titleTextStyle: TextStyle = TextStyle(),
    val descriptionTextStyle: TextStyle = TextStyle(),
    val progressTextStyle: TextStyle = TextStyle(),
    val actionTextStyle: TextStyle = TextStyle(),
    val actionContentColor: Color = Color(0xFF1A73E8),
    val shape: Shape = RoundedCornerShape(16.dp),
    val borderColor: Color = Color(0x22000000),
    val borderWidth: Dp = 1.dp,
    val contentPadding: Dp = 16.dp,
    val contentSpacing: Dp = 8.dp,
    val actionSpacing: Dp = 8.dp,
    val maxWidth: Dp = 320.dp,
)
