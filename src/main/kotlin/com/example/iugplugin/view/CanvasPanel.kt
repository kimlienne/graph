package com.example.demo2.view

import java.awt.*
import javax.swing.JPanel

class CanvasPanel : JPanel() {

    private val gridDrawer = DotGridBackground()

    init {
        layout = null
        background = Color(20, 20, 20)
        preferredSize = Dimension(1600, 900)

    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        gridDrawer.draw(g2, width, height)
    }
}