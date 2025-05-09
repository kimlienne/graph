package com.example.demo2.view

import java.awt.*
import javax.swing.JPanel

class DashedLineView : JPanel() {

    init {
        preferredSize = Dimension(50, 110)
        isOpaque = false
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        val centerY = height / 2
        val startX = 0
        val endX = width - 20

        // Draw dashed line
        g2.color = Color(146, 119, 255)
        g2.stroke = BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, floatArrayOf(6f, 6f), 0f)
        g2.drawLine(startX, centerY, endX, centerY)

    }
}
