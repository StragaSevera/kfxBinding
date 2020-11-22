package ru.ought.kfx_binding.fx_properties

import kotlin.reflect.KProperty

public class FXDelegatedPropertyProvider<T>(private val fxPropertyInfo: FXPropertyInfo<T>) {
    public operator fun provideDelegate(thisRef: Any, property: KProperty<*>): FXDelegatedProperty<T> {
        FXDelegatedPropertyManager.registerProperty(thisRef, property.name, fxPropertyInfo)
        return FXDelegatedProperty(fxPropertyInfo)
    }
}

public class FXDelegatedProperty<T>(private val fxPropertyInfo: FXPropertyInfo<T>) {
    public operator fun getValue(thisRef: Any, property: KProperty<*>): T {
        return fxPropertyInfo.fxProperty.value
    }

    public operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        fxPropertyInfo.fxProperty.value = value
    }
}