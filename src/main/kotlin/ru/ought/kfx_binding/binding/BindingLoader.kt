package ru.ought.kfx_binding.binding

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import java.io.InputStream

public class BindingLoader(private val data: InputStream) {
    public companion object {
        public fun from(resourceName: String): BindingLoader = BindingLoader(BindingLoader::class.java.getResourceAsStream(resourceName))
    }

    public fun <T : Parent> load(): T {
        val fxmlLoader = FXMLLoader()
        val root = fxmlLoader.load<T>(data)
        val controller = fxmlLoader.getController<Any>()
        BindingManager.performBinding(root, controller)
        BindingManager.callAfterBinding(controller)
        return root
    }
}