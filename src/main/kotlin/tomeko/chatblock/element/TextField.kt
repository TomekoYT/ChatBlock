package tomeko.chatblock.element

import cc.polyfrost.oneconfig.gui.elements.text.TextInputField
import cc.polyfrost.oneconfig.internal.assets.SVGs
import cc.polyfrost.oneconfig.utils.InputHandler

class TextField(
    private val getMessage: () -> String,
    private val setMessage: (String) -> Unit
) : TextInputField(608, 32, "", false, false, SVGs.TEXT_INPUT) {

    override fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        input = getMessage()
        super.draw(vg, x, y, inputHandler)
    }

    fun isKeyTyped(key: Char, keyCode: Int): Boolean {
        if (!isToggled) return false
        keyTyped(key, keyCode)
        setMessage(input)
        return true
    }
}