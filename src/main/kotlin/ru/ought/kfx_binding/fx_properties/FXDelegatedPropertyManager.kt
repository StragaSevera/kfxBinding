package ru.ought.kfx_binding.fx_properties

import javafx.beans.property.Property
import kotlin.reflect.KProperty

internal object FXDelegatedPropertyManager {
    private val fxProperties: MutableMap<Any, MutableMap<String, FXPropertyInfo<*>>> = mutableMapOf()
    fun registerProperty(controller: Any, propertyName: String, fxPropertyInfo: FXPropertyInfo<*>) {
        val controllerFxProperties = fxProperties.getOrPut(controller) { mutableMapOf() }
        controllerFxProperties[propertyName] = fxPropertyInfo
    }

    fun getPropertiesFor(controller: Any): Map<String, FXPropertyInfo<*>> = fxProperties[controller]?.toMap() ?: mapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T> getFxProperty(controller: Any, kProperty: KProperty<T>) =
        (getPropertiesFor(controller)[kProperty.name]?.fxProperty as? Property<T>)
            ?: error("There is no stored property with name ${kProperty.name}")

}

public fun <T> getFxProperty(controller: Any, kProperty: KProperty<T>): Property<T> =
    FXDelegatedPropertyManager.getFxProperty(controller, kProperty)