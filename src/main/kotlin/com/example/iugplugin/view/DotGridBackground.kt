package com.example.demo2.view

import java.awt.Color
import java.awt.Graphics2D

class DotGridBackground(
        private val gridSpacing: Int = 20,
        private val dotSize: Int = 2,
        private val dotColor: Color = Color(60, 60, 60)
) {
    fun draw(g2: Graphics2D, width: Int, height: Int) {
        g2.color = dotColor
        for (x in 0 until width step gridSpacing) {
            for (y in 0 until height step gridSpacing) {
                g2.fillOval(x, y, dotSize, dotSize)
            }
        }
    }
}