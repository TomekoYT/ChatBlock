package tomeko.chatblock.element

import cc.polyfrost.oneconfig.config.elements.BasicOption
import cc.polyfrost.oneconfig.gui.elements.BasicButton
import cc.polyfrost.oneconfig.gui.elements.IFocusable
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import tomeko.chatblock.ChatBlock
import tomeko.chatblock.config.Macro

@Suppress("UnstableAPIUsage")
object BlockSendingListOption : BasicOption(null, null, "", "", "General", "", 2), IFocusable {
    private val addButton = BasicButton(32, 32, ChatBlock.PLUS_ICON, BasicButton.ALIGNMENT_CENTER, ColorPalette.PRIMARY)
    var wrappedBlockSendings: MutableList<WrappedBlockSending> = ArrayList()
    var willBeRemoved: WrappedBlockSending? = null

    init {
        addButton.setClickAction {
            wrappedBlockSendings.add(WrappedBlockSending(Macro()))
        }
    }

    override fun getHeight() = wrappedBlockSendings.size * 48 + 32

    override fun draw(vg: Long, x: Int, y: Int, inputHandler: InputHandler) {
        var nextY = y

        for (option in wrappedBlockSendings) {
            option.draw(vg, x.toFloat(), nextY.toFloat(), inputHandler)
            nextY += 48
        }

        addButton.draw(vg, x.toFloat(), nextY.toFloat(), inputHandler)

        checkWillBeRemoved()
    }

    private fun checkWillBeRemoved() {
        val macro = willBeRemoved ?: return
        wrappedBlockSendings.remove(macro)
        willBeRemoved = null
    }

    override fun keyTyped(key: Char, keyCode: Int) {
        wrappedBlockSendings.any { it.keyTyped(key, keyCode) }
    }

    override fun hasFocus() = wrappedBlockSendings.any { it.hasFocus() }
}