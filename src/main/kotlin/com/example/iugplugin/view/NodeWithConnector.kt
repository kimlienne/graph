package com.example.demo2.view

import java.awt.*
import javax.swing.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

/**
 * A complete visual representation of a node, including:
 * - the main node content (WorkflowNodeView)
 * - the trailing dashed line with a + button
 * - hover action buttons (edit, etc.)
 *
 * This is the component that gets added to the canvas.
 */
class NodeWithConnector : JPanel() {

    private val node = WorkflowNodeView()
    private val connector = DashedLineView()
    private val buttons = ButtonPanel()

    init {
        layout = null
        isOpaque = false
        preferredSize = Dimension(350, 140)

        // Set layout bounds
        node.setBounds(0, 0, 280, 110)
        connector.setBounds(280, 0, 70, 110)
        buttons.setBounds(0, 110, 280, 30)
        buttons.isVisible = false

        // Add main parts
        add(node)
        add(connector)
        add(buttons)

        // === Add "+" button on the connector ===
        val plusButton = PlusButtonView {
            AddNodeDialog(this, listOf(this@NodeWithConnector)) { id, name, desc, prev, next ->
                println("Add node: $id, $name, $desc, prev=$prev, next=$next")
            }.isVisible = true
        }
        plusButton.setBounds(310, 44, 24, 24)
        add(plusButton)

        // === Show/hide hover buttons logic ===
        addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent?) {
                buttons.isVisible = true
            }

            override fun mouseExited(e: MouseEvent?) {
                val p = MouseInfo.getPointerInfo().location
                SwingUtilities.convertPointFromScreen(p, this@NodeWithConnector)
                if (!node.bounds.contains(p) && !buttons.bounds.contains(p)) {
                    buttons.isVisible = false
                }
            }
        })

        // === Open edit dialog on click ===
        buttons.editButton.addActionListener {
            EditWorkflowNodeDialog(this, node).isVisible = true
        }
    }

    /**
     * Assign content to this node.
     */
    fun setContent(id: String, name: String, desc: String) {
        node.idText = id
        node.nameText = name
        node.descText = desc
        node.repaint()
    }

    /**
     * Get ID string of the current node.
     */
    fun getNodeId(): String = node.idText
}
