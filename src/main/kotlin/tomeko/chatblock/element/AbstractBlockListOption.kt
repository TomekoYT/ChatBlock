package tomeko.chatblock.element

import cc.polyfrost.oneconfig.config.elements.BasicOption
import cc.polyfrost.oneconfig.gui.elements.BasicButton
import cc.polyfrost.oneconfig.gui.elements.IFocusable
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import tomeko.chatblock.ChatBlock

@Suppress("UnstableAPIUsage")
abstract class AbstractBlockListOption<T> :
    BasicOption(null, null, "", "", "General", "", 2), IFocusable {

    protected val addButton = BasicButton(
        32, 32,
        ChatBlock.PLUS_ICON,
        BasicButton.ALIGNMENT_CENTER,
        ColorPalette.PRIMARY
    )

    val items: MutableList<T> = ArrayList()
    var willBeRemoved: T? = null

    init {
        addButton.setClickAction {
            items.add(createWrapped()) // ✅ safe now
        }
    }

    protected abstract fun createWrapped(): T

    override fun getHeight() = items.size * 48 + 32

    override fun draw(vg: Long, x: Int, y: Int, inputHandler: InputHandler) {
        var nextY = y

        for (item in items) {
            drawItem(item, vg, x.toFloat(), nextY.toFloat(), inputHandler)
            nextY += 48
        }

        addButton.draw(vg, x.toFloat(), nextY.toFloat(), inputHandler)

        checkWillBeRemoved()
    }

    private fun checkWillBeRemoved() {
        val item = willBeRemoved ?: return
        items.remove(item)
        willBeRemoved = null
    }

    override fun keyTyped(key: Char, keyCode: Int) {
        items.any { keyTypedItem(it, key, keyCode) }
    }

    override fun hasFocus() = items.any { hasFocusItem(it) }

    protected abstract fun drawItem(item: T, vg: Long, x: Float, y: Float, inputHandler: InputHandler)
    protected abstract fun keyTypedItem(item: T, key: Char, keyCode: Int): Boolean
    protected abstract fun hasFocusItem(item: T): Boolean
}