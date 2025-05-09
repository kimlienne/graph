package com.example.demo2.view

import java.awt.*
import javax.swing.JButton
import javax.swing.border.LineBorder

// A custom button with rounded corners and styled appearance
class RoundedButton(text: String) : JButton(text) {

    init {
        isFocusPainted = false
        isContentAreaFilled = false
        foreground = Color.WHITE
        background = Color(60, 60, 60)
        font = Font("SansSerif", Font.PLAIN, 13)
        border = LineBorder(Color(150, 150, 150), 1, true)
        preferredSize = Dimension(90, 30)
        cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
    }

    override fun paintComponent(g: Graphics) {
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = background
        g2.fillRoundRect(0, 0, width, height, 16, 16)
        g2.color = foreground
        val fm = g.fontMetrics
        val textWidth = fm.stringWidth(text)
        val textY = (height + fm.ascent - fm.descent) / 2
        g2.drawString(text, (width - textWidth) / 2, textY)
    }
}
