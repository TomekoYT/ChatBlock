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
import tomeko.chatblock.element.WrappedBlockReceiving
import tomeko.chatblock.element.WrappedBlockSending
import java.lang.reflect.Field

object ChatBlockConfig : Config(Mod(ChatBlock.NAME, ModType.UTIL_QOL), "${ChatBlock.MODID}.json") {

    @JvmField
    @Switch(name = "Block Sending Case Sensitive", size = OptionSize.SINGLE)
    var blockCaseSensitive: Boolean = false

    @Info(
        text = "Block sending following chat messages:",
        type = InfoType.INFO,
        size = 2
    )
    private var blockInfo = Runnable { }

    @JvmField
    @CustomOption(id = "block")
    var messagesToBlock: Array<Macro> = emptyArray()

    @JvmField
    @Switch(name = "Block Receiving Case Sensitive", size = OptionSize.SINGLE)
    var hideCaseSensitive: Boolean = false

    @JvmField
    @Switch(name = "Ignore Formatting", size = OptionSize.SINGLE)
    var hideIgnoreFormatting: Boolean = true

    @Info(
        text = "Block receiving following chat messages:",
        type = InfoType.INFO,
        size = 2
    )
    private var hideInfo = Runnable { }

    @JvmField
    @CustomOption(id = "send")
    var messagesToHide: Array<Macro> = emptyArray()

    override fun getCustomOption(
        field: Field,
        annotation: CustomOption,
        page: OptionPage,
        mod: Mod,
        migrate: Boolean
    ): BasicOption {
        when (annotation.id) {
            "block" -> {
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

        BlockReceivingListOption.wrappedBlockReceivings = messagesToBlock.mapTo(mutableListOf()) { macro ->
            WrappedBlockReceiving(macro)
        }

        BlockSendingListOption.wrappedBlockSendings = messagesToHide.mapTo(mutableListOf()) { macro ->
            WrappedBlockSending(macro)
        }
    }

    override fun save() {
        messagesToBlock = BlockReceivingListOption.wrappedBlockReceivings.map { wrapped ->
            wrapped.macro
        }.toTypedArray()

        messagesToHide = BlockSendingListOption.wrappedBlockSendings.map { wrapped ->
            wrapped.macro
        }.toTypedArray()

        super.save()
    }
}