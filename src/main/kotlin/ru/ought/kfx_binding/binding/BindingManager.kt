package ru.ought.kfx_binding.binding

import javafx.beans.binding.Bindings
import javafx.beans.property.Property
import javafx.scene.Node
import javafx.scene.Parent
import ru.ought.kfx_binding.binding.annotations.AfterBinding
import ru.ought.kfx_binding.binding.utils.getChildrenDeep
import ru.ought.kfx_binding.fx_properties.DoubleBindingDirection
import ru.ought.kfx_binding.fx_properties.FXDelegatedPropertyManager
import ru.ought.kfx_binding.fx_properties.FXPropertyInfo
import kotlin.reflect.full.functions

@Suppress("UNCHECKED_CAST")
internal object BindingManager {
    fun performBinding(root: Parent, controller: Any?) {
        requireNotNull(controller)

        val children = root.getChildrenDeep().toList()
        val controllerPropertiesInfo = FXDelegatedPropertyManager.getPropertiesFor(controller)

        for ((propertyName, info) in controllerPropertiesInfo) {
            val controllerFxProperty = info.fxProperty as Property<Any>
            val nodeFxProperty = getNodeFxProperty(info, propertyName, children)
            when (info.direction) {
                DoubleBindingDirection.ViewToController -> Bindings.bindBidirectional(
                    nodeFxProperty,
                    controllerFxProperty
                )
                DoubleBindingDirection.ControllerToView -> Bindings.bindBidirectional(
                    controllerFxProperty,
                    nodeFxProperty
                )
            }
        }
    }

    fun callAfterBinding(controller: Any?) {
        requireNotNull(controller)

        val afterBindingMethods =
            controller::class.functions.filter { method -> method.annotations.any { it is AfterBinding } }
        check(afterBindingMethods.size <= 1) { "There can be only one AfterBinding method" }

        afterBindingMethods.firstOrNull()?.call(controller)
    }

    private fun getNodeFxProperty(
        info: FXPropertyInfo<*>,
        controllerPropertyName: String,
        children: List<Node>
    ): Property<Any> {
        var node: Node? = null
        var nodePropertyName: String? = null
        var nodeId: String? = null

        val bindingVariants = info.parse(controllerPropertyName)
        for (binding in bindingVariants) {
            val currentNode = children.find { it.id == binding.targetId }
            if (currentNode != null) {
                node = currentNode
                nodePropertyName = binding.targetPropertyName
                nodeId = binding.targetId
            }
        }

        requireNotNull(node) { "Cannot find a node that matches $controllerPropertyName" }

        return (node::class.functions
            .find { it.name == nodePropertyName }
            ?.call(node)
            ?: error("Cannot find property $nodePropertyName in node with id $nodeId!")) as Property<Any>
    }
}