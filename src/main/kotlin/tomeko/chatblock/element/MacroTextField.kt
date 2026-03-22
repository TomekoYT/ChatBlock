package tomeko.chatblock.element

import cc.polyfrost.oneconfig.gui.elements.text.TextInputField
import cc.polyfrost.oneconfig.utils.InputHandler
import tomeko.chatblock.assets.SVGs
import tomeko.chatblock.config.Macro

@Suppress("UnstableAPIUsage")
class MacroTextField(
    private val macro: Macro
) : TextInputField(608, 32, "", false, false, SVGs.TEXT_INPUT) {

    override fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        input = macro.text
        super.draw(vg, x, y, inputHandler)
    }

    fun isKeyTyped(key: Char, keyCode: Int): Boolean {
        if (!isToggled) return false
        keyTyped(key, keyCode)
        macro.text = input
        return true
    }
}