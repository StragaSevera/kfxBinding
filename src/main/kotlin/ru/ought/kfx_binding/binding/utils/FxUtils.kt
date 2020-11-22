package ru.ought.kfx_binding.binding.utils

import javafx.scene.Node
import javafx.scene.Parent
import java.util.*

internal fun Parent.getChildrenDeep(): Sequence<Node> = sequence {
    tailrec suspend fun SequenceScope<Node>.getAllChildren1(nodes: MutableList<Node>) {
        if(nodes.isEmpty()) return
        val node = nodes.first()
        nodes.removeAt(0)
        yield(node)
        if (node is Parent) {
            nodes.addAll(node.childrenUnmodifiable)
        }
        getAllChildren1(nodes)
    }
    getAllChildren1(LinkedList<Node>().apply { add(this@getChildrenDeep) })
}