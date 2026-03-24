package tomeko.chatblock.element

import cc.polyfrost.oneconfig.config.elements.BasicOption
import cc.polyfrost.oneconfig.gui.elements.BasicButton
import cc.polyfrost.oneconfig.gui.elements.IFocusable
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import tomeko.chatblock.ChatBlock
import tomeko.chatblock.config.Macro

@Suppress("UnstableAPIUsage")
object BlockReceivingListOption : BasicOption(null, null, "", "", "General", "", 2), IFocusable {
    private val addButton = BasicButton(32, 32, ChatBlock.PLUS_ICON, BasicButton.ALIGNMENT_CENTER, ColorPalette.PRIMARY)
    var wrappedBlockReceivings: MutableList<WrappedBlockReceiving> = ArrayList()
    var willBeRemoved: WrappedBlockReceiving? = null

    init {
        addButton.setClickAction {
            wrappedBlockReceivings.add(WrappedBlockReceiving(Macro()))
        }
    }

    override fun getHeight() = wrappedBlockReceivings.size * 48 + 32

    override fun draw(vg: Long, x: Int, y: Int, inputHandler: InputHandler) {
        var nextY = y

        for (option in wrappedBlockReceivings) {
            option.draw(vg, x.toFloat(), nextY.toFloat(), inputHandler)
            nextY += 48
        }

        addButton.draw(vg, x.toFloat(), nextY.toFloat(), inputHandler)

        checkWillBeRemoved()
    }

    private fun checkWillBeRemoved() {
        val macro = willBeRemoved ?: return
        wrappedBlockReceivings.remove(macro)
        willBeRemoved = null
    }

    override fun keyTyped(key: Char, keyCode: Int) {
        wrappedBlockReceivings.any { it.keyTyped(key, keyCode) }
    }

    override fun hasFocus() = wrappedBlockReceivings.any { it.hasFocus() }
}