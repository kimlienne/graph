package com.example.demo2.view

import java.awt.*
import javax.swing.*
import javax.swing.border.LineBorder

/**
 * Popup dialog to edit a WorkflowNodeView (ID, Name, Desc).
 */
class EditWorkflowNodeDialog(
        private val parent: Component,
        private val node: WorkflowNodeView
) : JDialog(
        SwingUtilities.getWindowAncestor(parent),
        "Edit Node",
        ModalityType.APPLICATION_MODAL
) {

    private val idField = JTextField(node.idText)
    private val nameField = JTextField(node.nameText)
    private val descField = JTextField(node.descText)

    init {
        isUndecorated = true
        layout = BorderLayout()
        background = Color(0, 0, 0, 0) // transparent for rounded shape

        val panel = RoundedPanel(20, Color(36, 36, 36))
        panel.layout = GridBagLayout()
        panel.border = LineBorder(Color(60, 60, 60), 1, true)

        val gbc = GridBagConstraints()
        gbc.insets = Insets(5, 10, 5, 10)
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.gridx = 0
        gbc.gridwidth = 2

        // === Title ===
        gbc.gridy = 0
        val title = JLabel("EDIT WORKFLOW NODE")
        title.foreground = Color.WHITE
        title.font = Font("SansSerif", Font.BOLD, 14)
        panel.add(title, gbc)

        // === Input fields ===
        addLabeledField(panel, gbc, "ID Step:", idField, 1)
        addLabeledField(panel, gbc, "Name Step:", nameField, 2)
        addLabeledField(panel, gbc, "Desc Step:", descField, 3)

        // === Buttons ===
        val buttonPanel = JPanel(FlowLayout(FlowLayout.CENTER, 10, 10))
        buttonPanel.isOpaque = false

        val updateBtn = RoundedButton("Update")
        val closeBtn = RoundedButton("Close")

        updateBtn.addActionListener {
            node.idText = idField.text
            node.nameText = nameField.text
            node.descText = descField.text
            node.repaint()
            dispose()
        }

        closeBtn.addActionListener { dispose() }

        buttonPanel.add(updateBtn)
        buttonPanel.add(closeBtn)

        gbc.gridy = 4
        panel.add(buttonPanel, gbc)

        setContentPane(panel)
        pack()

        // Center the dialog
        setLocationRelativeTo(null)
        shape = java.awt.geom.RoundRectangle2D.Float(0f, 0f, width.toFloat(), height.toFloat(), 20f, 20f)
    }

    /**
     * Helper to add label + input field
     */
    private fun addLabeledField(panel: JPanel, gbc: GridBagConstraints, label: String, field: JTextField, y: Int) {
        gbc.gridy = y
        gbc.gridwidth = 1
        gbc.gridx = 0
        val lbl = JLabel(label)
        lbl.foreground = Color.WHITE
        lbl.font = Font("SansSerif", Font.PLAIN, 12)
        panel.add(lbl, gbc)

        gbc.gridx = 1
        gbc.ipady = 10
        field.font = Font("SansSerif", Font.PLAIN, 12)
        field.background = Color(25, 25, 25)
        field.foreground = Color.WHITE
        field.border = LineBorder(Color(100, 100, 100), 1, true)
        panel.add(field, gbc)
    }

    /**
     * Simple rounded background panel
     */
    class RoundedPanel(private val radius: Int, private val fillColor: Color) : JPanel() {
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

    /**
     * Rounded button with custom paint
     */
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
}
