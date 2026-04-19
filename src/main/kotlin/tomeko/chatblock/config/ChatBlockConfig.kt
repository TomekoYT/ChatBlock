package tomeko.chatblock.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.CustomOption
import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.core.ConfigUtils
import cc.polyfrost.oneconfig.config.data.InfoType
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.config.data.OptionSize
import cc.polyfrost.oneconfig.config.elements.BasicOption
import cc.polyfrost.oneconfig.config.elements.OptionPage
import tomeko.chatblock.ChatBlock
import tomeko.chatblock.element.BlockReceivingListOption
import tomeko.chatblock.element.BlockSendingListOption
import tomeko.chatblock.element.WrappedBlock
import java.lang.reflect.Field

object ChatBlockConfig : Config(Mod(ChatBlock.NAME, ModType.UTIL_QOL), "${ChatBlock.MODID}.json") {
    @JvmField
    @Switch(name = "Block Receiving Case Sensitive", size = OptionSize.SINGLE)
    var blockReceivingCaseSensitive: Boolean = false

    @JvmField
    @Switch(name = "Ignore Formatting", size = OptionSize.SINGLE)
    var blockReceivingIgnoreFormatting: Boolean = true

    @Info(
        text = "Block receiving following chat messages:",
        type = InfoType.INFO,
        size = 2
    )
    private var hideInfo = Runnable { }

    @JvmField
    @CustomOption(id = "blockReceiving")
    var messagesToBlockReceiving: Array<String> = emptyArray()


    @JvmField
    @Switch(name = "Block Sending Case Sensitive", size = OptionSize.SINGLE)
    var blockSendingCaseSensitive: Boolean = false

    @Info(
        text = "Block sending following chat messages:",
        type = InfoType.INFO,
        size = 2
    )
    private var blockInfo = Runnable { }

    @JvmField
    @CustomOption(id = "blockSending")
    var messagesToBlockSending: Array<String> = emptyArray()

    override fun getCustomOption(
        field: Field,
        annotation: CustomOption,
        page: OptionPage,
        mod: Mod,
        migrate: Boolean
    ): BasicOption {
        when (annotation.id) {
            "blockReceiving" -> {
                val option = BlockReceivingListOption
                ConfigUtils.getSubCategory(page, "General", "").options.add(option)
                return option

            }

            else -> {
                val option = BlockSendingListOption
                ConfigUtils.getSubCategory(page, "General", "").options.add(option)
                return option
            }
        }
    }

    override fun load() {
        super.load()

        BlockReceivingListOption.apply {
            items.clear()
            items.addAll(messagesToBlockReceiving.map { message ->
                WrappedBlock(message) {
                    willBeRemoved = it
                }
            })
        }

        BlockSendingListOption.apply {
            items.clear()
            items.addAll(messagesToBlockSending.map { message ->
                WrappedBlock(message) {
                    willBeRemoved = it
                }
            })
        }
    }

    override fun save() {
        messagesToBlockReceiving = BlockReceivingListOption.items
            .map { it.message }
            .toTypedArray()

        messagesToBlockSending = BlockSendingListOption.items
            .map { it.message }
            .toTypedArray()

        super.save()
    }
}