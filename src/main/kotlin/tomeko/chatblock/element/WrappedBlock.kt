package tomeko.chatblock.element

//? if = 1.8.9 {
/*import cc.polyfrost.oneconfig.gui.elements.BasicButton
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import tomeko.chatblock.utils.Constants

class WrappedBlock(
    var message: String,
    private val onRemove: (WrappedBlock) -> Unit,
    private val splitOnSpace: Boolean = false
) {
    private val removeButton = BasicButton(
        32, 32,
        Constants.MINUS_ICON,
        BasicButton.ALIGNMENT_CENTER,
        ColorPalette.PRIMARY_DESTRUCTIVE
    )

    private val textField = TextField(
        getMessage = { message },
        setMessage = { message = it },
        onKeyTyped = { key, _ ->
            if (!splitOnSpace || key != ' ') {
                return@TextField false
            }

            if (message.isBlank()) {
                return@TextField true
            }

            unfocus()

            val next = WrappedBlock(
                "",
                onRemove,
                splitOnSpace = true
            )

            BlockSendingListOption.insertAfter(this, next)
            next.focus()

            return@TextField true
        }
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

    fun focus() {
        textField.focus()
    }

    fun unfocus() {
        textField.unfocus()
    }
}
*///?}