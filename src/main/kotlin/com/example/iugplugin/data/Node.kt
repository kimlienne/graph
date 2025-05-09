package com.example.iugplugin.data

class Node (
    val id: String,
    var x: Int = 0,
    var y: Int = 0,
    val children: MutableList<Node> = mutableListOf(),
    var level: Int = 0,
    var point: Int = 0,
    var parent: Node? = null
)

class Edge (
    val from: Node,
    val to: Node
)

object listNode {
    val listNode = mutableListOf<Node>()
}

object listEdge {
    val listEdge = mutableListOf<Edge>()
}