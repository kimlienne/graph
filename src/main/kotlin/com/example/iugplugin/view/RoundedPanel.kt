package com.example.demo2.view

import java.awt.*
import javax.swing.JPanel

// A panel with rounded corners and custom fill color
class RoundedPanel(
        private val radius: Int = 16,
        private val fillColor: Color = Color(36, 36, 36)
) : JPanel() {

    init {
        isOpaque = false
    }

    override fun paintComponent(g: Graphics) {
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = fillColor
        g2.fillRoundRect(0, 0, width, height, radius, radius)
    }
}
