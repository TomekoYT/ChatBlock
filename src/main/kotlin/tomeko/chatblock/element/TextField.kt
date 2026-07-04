package tomeko.chatblock.element

//? if = 1.8.9 {
/*import cc.polyfrost.oneconfig.gui.elements.text.TextInputField
import cc.polyfrost.oneconfig.utils.InputHandler

class TextField(
    private val getMessage: () -> String,
    private val setMessage: (String) -> Unit,
    private val onKeyTyped: ((Char, Int) -> Boolean)? = null
) : TextInputField(608, 32, "", false, false, null) {

    override fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        input = getMessage()
        super.draw(vg, x, y, inputHandler)
    }

    fun isKeyTyped(key: Char, keyCode: Int): Boolean {
        if (!isToggled) return false

        if (onKeyTyped?.invoke(key, keyCode) == true) {
            return true
        }

        keyTyped(key, keyCode)
        setMessage(input)
        return true
    }

    fun focus() {
        toggled = true
        caretPos = input.length
        prevCaret = caretPos
        start = 0f
        end = 0f
        selectedText = null
    }

    fun unfocus() {
        toggled = false
        start = 0f
        end = 0f
        selectedText = null
    }
}
*///?}