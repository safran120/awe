package com.safranpulse.pulsechat.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Outline shapes for the PingChat theme.
 */
@Immutable
object Outlines {
    /**
     * A small rounded corner shape for cards, buttons, etc.
     */
    val Small: Shape = RoundedCornerShape(4.dp)

    /**
     * A medium rounded corner shape.
     */
    val Medium: Shape = RoundedCornerShape(8.dp)

    /**
     * A large rounded corner shape.
     */
    val Large: Shape = RoundedCornerShape(12.dp)

    /**
     * A pill shape for chips and buttons.
     */
    val Pill: Shape = RoundedCornerShape(24.dp)

    /**
     * A cut shape for top-aligned cards, with the top-start corner cut.
     */
    val CutTopStart: Shape = CutCornerShape(topStart = 8.dp)
}