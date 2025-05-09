package com.example.demo2

import com.example.demo2.view.CanvasPanel
import com.example.demo2.view.NodeWithConnector
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JScrollPane

class IUGEditorPanel : JPanel() {

    private val canvas = CanvasPanel()

    init {
        layout = BorderLayout()

        // Add canvas panel to fill the editor area
        val scrollPane = JScrollPane(canvas)
        scrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        scrollPane.border = null
        add(scrollPane, BorderLayout.CENTER)


        // Use absolute positioning for manual node placement
        canvas.layout = null

        //  Add Node with dashed line and + button 
        val nodeWithConnector = NodeWithConnector()

        // Set position and size of the entire node group (includes connector)
        nodeWithConnector.setBounds(100, 200, 350, 140)

        // Add node group to canvas
        canvas.add(nodeWithConnector)


    }
}
