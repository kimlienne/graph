package com.example.iugplugin

import com.example.iugplugin.data.Edge
import com.example.iugplugin.data.Node
import com.example.iugplugin.data.listEdge
import com.example.iugplugin.data.listNode
import javax.swing.*
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.QuadCurve2D
import kotlin.math.abs
import kotlin.math.pow

class GraphPanel : JPanel() {
    private var selectedNode: Node? = null
    private var selectedEdge: Edge? = null

    init {
        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                val clickedNode = getNodeAt(e.x, e.y)
                if (clickedNode != null) {
                    selectedNode = clickedNode
                    showContextMenu(e.x, e.y)
                } else {
                    val clickedEdge = getEdgeAt(e.x, e.y)
                    if (clickedEdge != null) {
                        selectedEdge = clickedEdge
                        showContextMenuEdge(e.x, e.y)
                    }
                }
            }
        })
    }

    private fun getNodeAt(x: Int, y: Int): Node? {
        return listNode.listNode.find {
            val nodeX = it.x
            val nodeY = it.y
            x in (nodeX - 15)..(nodeX + 15) && y in (nodeY - 15)..(nodeY + 15)
        }
    }

    private fun getEdgeAt(x: Int, y: Int): Edge? {
        return listEdge.listEdge.find { edge ->
            val fromX = edge.from.x
            val fromY = edge.from.y
            val toX = edge.to.x
            val toY = edge.to.y

            val distance = distanceToLineSegment(x, y, fromX, fromY, toX, toY)

            distance <= 15
        }
    }

    private fun distanceToLineSegment(px: Int, py: Int, x1: Int, y1: Int, x2: Int, y2: Int): Double {
        val dx = x2 - x1
        val dy = y2 - y1
        val lengthSquared = dx * dx + dy * dy

        if (lengthSquared.toDouble() == 0.0) {
            return Math.sqrt((px - x1) * (px - x1) + (py - y1) * (py - y1).toDouble())
        }

        val t = ((px - x1) * dx + (py - y1) * dy).toDouble() / lengthSquared
        val tClamped = t.coerceIn(0.0, 1.0)

        val closestX = x1 + tClamped * dx
        val closestY = y1 + tClamped * dy

        return Math.sqrt((px - closestX).pow(2) + (py - closestY).pow(2))
    }

    private fun showContextMenuEdge(x: Int, y: Int) {
        val popupMenu = JPopupMenu()
        val deleteItem = JMenuItem("Delete")
        deleteItem.addActionListener {
            selectedEdge?.let { edge ->
                removeEdge(edge)
                layoutNodes()
                repaint()
            }
        }
        popupMenu.add(deleteItem)
        popupMenu.show(this, x, y)
    }

    private fun removeEdge(edge: Edge) {
        edge.from.children.remove(edge.to)
        if (edge.to.parent == edge.from) {
            edge.to.parent = null
        }
        listEdge.listEdge.remove(edge)
        recalculateTreeLevels()
    }

    private fun showContextMenu(x: Int, y: Int) {
        val popupMenu = JPopupMenu()

        val deleteItem = JMenuItem("Delete Node")
        deleteItem.addActionListener {
            selectedNode?.let { node ->
                removeNode(node)
                layoutNodes()
                repaint()
            }
        }
        popupMenu.add(deleteItem)

        val addConnectionItem = JMenuItem("Add Connection")
        addConnectionItem.addActionListener {
            selectedNode?.let { node ->
                showAddConnectionDialog(node)
            }
        }
        popupMenu.add(addConnectionItem)

        popupMenu.show(this, x, y)
    }

    private fun removeNode(node: Node) {
        node.parent?.children?.remove(node)
        listEdge.listEdge.removeAll { it.from == node || it.to == node }
        listNode.listNode.remove(node)

        recalculateTreeLevels()
    }

    private fun showAddConnectionDialog(fromNode: Node) {
        val nodeIds = listNode.listNode.filter { it != fromNode }.map { it.id }
        val nodeListComboBox = JComboBox(nodeIds.toTypedArray())

        val dialog = JOptionPane.showConfirmDialog(this, nodeListComboBox,
            "Select Node to Connect", JOptionPane.OK_CANCEL_OPTION)

        if (dialog == JOptionPane.OK_OPTION) {
            val selectedNodeId = nodeListComboBox.selectedItem as? String
            val toNode = listNode.listNode.find { it.id == selectedNodeId }

            if (toNode != null) {
                fromNode.children.add(toNode)
                if (toNode.parent != null) {
                    if (toNode.parent!!.level < fromNode.level) {
                        toNode.parent = fromNode
                    }
                } else {
                    toNode.parent = fromNode
                }
                addEdge(fromNode, toNode)
                recalculateTreeLevels()
                layoutNodes()
                repaint()
            }
        }
    }

    fun addNode(id: String, parent: Node?) {
        val node = Node(id)

        if (parent != null) {
            node.level = parent.level + 1

            if (node.parent != null) {
                if (node.parent!!.level < parent.level) {
                    node.parent = parent
                }
            }

            parent.children.add(node)
            addEdge(parent, node)
//            parent.point += 1

            var nodeCheck = parent
            while (nodeCheck != null) {
                nodeCheck.point += 1
                nodeCheck = nodeCheck.parent
            }
        }

        listNode.listNode.add(node)
        layoutNodes()
        repaint()
    }

    fun addEdge(from: Node, to: Node) {
        listEdge.listEdge.add(Edge(from, to))
        repaint()
    }

    fun groupByLevel(): Map<Int, List<Node>> {
        val levelMap = mutableMapOf<Int, MutableList<Node>>()
        val queue = ArrayDeque<Node>()
        queue.add(listNode.listNode[0])

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            levelMap.getOrPut(current.level) { mutableListOf() }.add(current)
            for (child in current.children) {
                queue.add(child)
            }
        }

        return levelMap
            .toSortedMap()
            .mapValues { (_, nodes) ->
                nodes.sortedWith(compareByDescending<Node> { it.point }
                    .thenByDescending { it.parent?.point ?: 0 }) }
    }

    private fun layoutNodes() {
        val grouped = groupByLevel()
        grouped.forEach { (level, nodes) ->
            if (level == 0) {
                nodes.forEach  { node ->
                    node.x = 50
                    node.y = 50
                }
            } else {
                nodes.forEachIndexed  { i, node ->
                    node.x = 50 + level*100
                    if (node.level == 0) {
                        node.y = 50
                    } else {
                        var parentY: Int = 50
                        if (node.parent != null) {
                            parentY = node.parent!!.y
                        }
                        if (parentY < 50) {
                            node.y = parentY - i*50
                        } else {
                            node.y = parentY + i*50
                        }
                    }
                }
            }
        }


        var count = 0
        for (node in listNode.listNode) {
            if (node.level == -1) {
                node.x = 50 + count * 100
                node.y = 800
                count++
            }
        }
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2 = g as Graphics2D

        // Draw edges
        for (edge in listEdge.listEdge) {
            if (edge.from.x == edge.to.x && abs(edge.to.y - edge.from.y) > 50) {
                g2.color = Color.WHITE
                g2.stroke = BasicStroke(2f)
                val point1 = Pair(edge.from.x, edge.from.y)
                val point2 = Pair(edge.from.x + 50, abs(edge.to.y - edge.from.y)/2)
                val point3 = Pair(edge.to.x, edge.to.y)

                val curve = QuadCurve2D.Float()
                curve.setCurve(point1.first.toFloat(), point1.second.toFloat(),
                    point2.first.toFloat(), point2.second.toFloat(),
                    point3.first.toFloat(), point3.second.toFloat())

                g2.draw(curve)
            } else {
                if (edge.from.y == edge.to.y && abs(edge.to.x - edge.from.x) > 100) {
                    g2.color = Color.WHITE
                    g2.stroke = BasicStroke(2f)
                    val point1 = Pair(edge.from.x, edge.from.y)
                    val point2 = Pair(edge.from.x + 50, abs(edge.to.y - edge.from.y)/2)
                    val point3 = Pair(edge.to.x, edge.to.y)

                    val curve = QuadCurve2D.Float()
                    curve.setCurve(point1.first.toFloat(), point1.second.toFloat(),
                        point2.first.toFloat(), point2.second.toFloat(),
                        point3.first.toFloat(), point3.second.toFloat())

                    g2.draw(curve)
                } else {
                    g2.color = Color.BLACK
                    g2.stroke = BasicStroke(2f)
                    g2.drawLine(edge.from.x, edge.from.y, edge.to.x, edge.to.y)
                }
            }

        }

        // Draw nodes
        for (node in listNode.listNode) {
            g2.color = Color.ORANGE
            g2.fillOval(node.x - 15, node.y - 15, 30, 30)
            g2.color = Color.BLACK
            g2.drawOval(node.x - 15, node.y - 15, 30, 30)
            g2.drawString(node.id, node.x - 5, node.y + 5)
        }
    }

    private fun recalculateTreeLevels() {
        val visited = mutableSetOf<Node?>()
        val queue: ArrayDeque<Node?> = ArrayDeque()

        val node = listNode.listNode.firstOrNull()
        if (node != null) {
            node.level = 0
            node.point = 0
        }

        queue.add(node)
        visited.add(node)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            for (child in current!!.children) {
                if (child !in visited && child in listNode.listNode) {
                    child.level = current.level + 1
                    if (child.parent != null) {
                        if (child.parent!!.level < current.level) {
                            child.parent = current
                        }
                    } else {
                        child.parent = current
                    }
                    current.point += 1
                    queue.add(child)
                    visited.add(child)
                }
            }
        }

        for (node1 in listNode.listNode) {
            if (node1 !in visited && node1.level != 0) {
                node1.level = -1
            }
        }

        for (n in listNode.listNode) {
            println(n)
        }

        layoutNodes()
    }

}