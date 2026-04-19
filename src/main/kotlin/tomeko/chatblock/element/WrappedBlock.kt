package tomeko.chatblock.element

import cc.polyfrost.oneconfig.gui.elements.BasicButton
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import tomeko.chatblock.ChatBlock

class WrappedBlock(
    var message: String,
    private val onRemove: (WrappedBlock) -> Unit
) {
    private val removeButton = BasicButton(
        32, 32,
        ChatBlock.MINUS_ICON,
        BasicButton.ALIGNMENT_CENTER,
        ColorPalette.PRIMARY_DESTRUCTIVE
    )

    private val textField = TextField(
        getMessage = { message },
        setMessage = { message = it }
    )

    init {
        removeButton.setClickAction {
            onRemove(this)
        }
    }

    fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        removeButton.draw(vg, x, y, inputHandler)
        textField.draw(vg, x + 40, y, inputHandler)
    }

    fun keyTyped(key: Char, keyCode: Int) =
        textField.isKeyTyped(key, keyCode)

    fun hasFocus() =
        textField.isToggled
}