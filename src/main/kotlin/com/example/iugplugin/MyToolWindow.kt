package com.example.iugplugin

import com.example.iugplugin.data.listNode
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JPanel

class MyToolWindow() : JPanel() {
    init {
        this.apply {
            layout = BorderLayout()
            preferredSize = Dimension(500, 600)
        }

        val graphPanel = GraphPanel2()
        val comboBox = JComboBox<String>()

        for (node in listNode.listNode) {
            comboBox.addItem(node.id)
        }

        val button = JButton("Add")

        button.addActionListener {
            val selectedLabel = comboBox.selectedItem as? String
            val targetNode = listNode.listNode.find { it.id == selectedLabel }

            // Tạo liên kết từ node mới đến node đã chọn
            if (targetNode != null) {
                graphPanel.addNode("${(listNode.listNode.lastOrNull()?.id?.toInt() ?: 0) + 1}", targetNode)
            } else {
                graphPanel.addNode("${(listNode.listNode.lastOrNull()?.id?.toInt() ?: 0) + 1}", null)
            }
            val newNode = listNode.listNode.last()
            comboBox.addItem(newNode.id)
        }

        val topPanel = JPanel(FlowLayout())
        topPanel.add(comboBox)
        topPanel.add(button)

        add(topPanel, BorderLayout.NORTH)
        add(graphPanel, BorderLayout.CENTER)
//        add(GraphPanel2(), BorderLayout.CENTER)
    }
}