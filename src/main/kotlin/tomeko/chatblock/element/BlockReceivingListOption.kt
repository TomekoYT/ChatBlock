package tomeko.chatblock.element

//? if = 1.8.9 {
import cc.polyfrost.oneconfig.utils.InputHandler

object BlockReceivingListOption : AbstractListOption<WrappedBlock>() {
    override fun createWrapped() =
        WrappedBlock(String()) {
            willBeRemoved = it
        }

    override fun drawItem(item: WrappedBlock, vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        item.draw(vg, x, y, inputHandler)
    }

    override fun keyTypedItem(item: WrappedBlock, key: Char, keyCode: Int) =
        item.keyTyped(key, keyCode)

    override fun hasFocusItem(item: WrappedBlock) =
        item.hasFocus()
}
//?}