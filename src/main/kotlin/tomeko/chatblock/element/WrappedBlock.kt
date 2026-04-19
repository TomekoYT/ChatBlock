package tomeko.chatblock.element

import cc.polyfrost.oneconfig.gui.elements.BasicButton
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import tomeko.chatblock.ChatBlock
import tomeko.chatblock.config.Macro

@Suppress("UnstableAPIUsage")
class WrappedBlock(
    val macro: Macro,
    private val onRemove: (WrappedBlock) -> Unit
) {
    private val removeButton = BasicButton(
        32, 32,
        ChatBlock.MINUS_ICON,
        BasicButton.ALIGNMENT_CENTER,
        ColorPalette.PRIMARY_DESTRUCTIVE
    )

    private val checkbox = MacroCheckbox(macro)
    private val textField = MacroTextField(macro)

    init {
        removeButton.setClickAction {
            onRemove(this)
        }
    }

    fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        removeButton.draw(vg, x, y, inputHandler)
        checkbox.draw(vg, x + 58, y, inputHandler)
        textField.draw(vg, x + 96, y, inputHandler)
    }

    fun keyTyped(key: Char, keyCode: Int) =
        textField.isKeyTyped(key, keyCode)

    fun hasFocus() =
        textField.isToggled
}