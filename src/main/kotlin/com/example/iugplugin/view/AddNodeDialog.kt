package com.example.demo2.view

import java.awt.*
import javax.swing.*
import javax.swing.border.LineBorder

/**
 * Dialog to add a new node with input fields and dropdowns.
 */
class AddNodeDialog(
        private val parentComponent: Component,
        private val existingNodes: List<NodeWithConnector>,
        private val onAdd: (id: String, name: String, desc: String, prev: String?, next: String?) -> Unit
) : JDialog(
        SwingUtilities.getWindowAncestor(parentComponent),
        "Add Node",
        ModalityType.APPLICATION_MODAL
) {

    // Input fields
    private val idField = JTextField()
    private val nameField = JTextField()
    private val descField = JTextField()

    // Dropdowns with default value "None"
    private val prevCombo = JComboBox<String>()
    private val nextCombo = JComboBox<String>()

    init {
        isUndecorated = true
        layout = BorderLayout()
        background = Color(0, 0, 0, 0) // transparent to support rounded corners

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
        val title = JLabel("ADD NEW NODE")
        title.foreground = Color.WHITE
        title.font = Font("SansSerif", Font.BOLD, 14)
        panel.add(title, gbc)

        // === Text fields ===
        addLabeledField(panel, gbc, "ID Step:", idField, 1)
        addLabeledField(panel, gbc, "Name Step:", nameField, 2)
        addLabeledField(panel, gbc, "Desc Step:", descField, 3)

        // === Fill dropdowns with nodes ===
        fillComboBoxes()

        addLabeledField(panel, gbc, "Previous Node:", prevCombo, 4)
        addLabeledField(panel, gbc, "Next Node:", nextCombo, 5)

        // === Button row ===
        val buttonPanel = JPanel(FlowLayout(FlowLayout.CENTER, 10, 10))
        buttonPanel.isOpaque = false

        val addBtn = RoundedButton("Add")
        val cancelBtn = RoundedButton("Cancel")

        addBtn.addActionListener {
            val id = idField.text.trim()
            val name = nameField.text.trim()
            val desc = descField.text.trim()
            val prev = comboValue(prevCombo)
            val next = comboValue(nextCombo)

            if (id.isNotEmpty() && name.isNotEmpty()) {
                onAdd(id, name, desc, prev, next)
                dispose()
            } else {
                JOptionPane.showMessageDialog(this, "ID and Name are required.", "Validation", JOptionPane.WARNING_MESSAGE)
            }
        }

        cancelBtn.addActionListener { dispose() }

        buttonPanel.add(addBtn)
        buttonPanel.add(cancelBtn)

        gbc.gridy = 6
        gbc.gridwidth = 2
        panel.add(buttonPanel, gbc)

        setContentPane(panel)
        pack()
        setLocationRelativeTo(null)

        shape = java.awt.geom.RoundRectangle2D.Float(0f, 0f, width.toFloat(), height.toFloat(), 20f, 20f)
    }

    /**
     * Add a label + input field pair to layout grid.
     */
    private fun addLabeledField(panel: JPanel, gbc: GridBagConstraints, label: String, field: JComponent, y: Int) {
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
        if (field is JTextField || field is JComboBox<*>) {
            field.background = Color(25, 25, 25)
            field.foreground = Color.WHITE
            field.border = LineBorder(Color(100, 100, 100), 1, true)
        }
        panel.add(field, gbc)
    }

    /**
     * Fill dropdowns with node IDs from existing node list.
     */
    private fun fillComboBoxes() {
        prevCombo.addItem("None")
        nextCombo.addItem("None")
        for (node in existingNodes) {
            val id = node.getNodeId()
            val label = "$id ($id.hashCode())"
            prevCombo.addItem(label)
            nextCombo.addItem(label)
        }
    }

    /**
     * Convert combo box selection to nullable string.
     */
    private fun comboValue(combo: JComboBox<String>): String? {
        val selected = combo.selectedItem as? String
        return if (selected == "None") null else selected?.substringBefore(" (") // extract raw ID
    }
}
