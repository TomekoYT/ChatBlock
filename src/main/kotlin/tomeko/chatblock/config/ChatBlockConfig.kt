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
import tomeko.chatblock.element.MacroListOption
import tomeko.chatblock.element.WrappedMacro
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
    @CustomOption
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
    @CustomOption
    var messagesToHide: Array<Macro> = emptyArray()

    var blockOption: MacroListOption = MacroListOption()
    var hideOption: MacroListOption = MacroListOption()

    override fun getCustomOption(
        field: Field,
        annotation: CustomOption,
        page: OptionPage,
        mod: Mod,
        migrate: Boolean
    ): BasicOption {

        val option = when (field.name) {
            "messagesToBlock" -> blockOption
            "messagesToHide" -> hideOption
            else -> MacroListOption()
        }

        option.wrappedMacros = when (field.name) {
            "messagesToBlock" -> messagesToBlock.mapTo(mutableListOf()) { WrappedMacro(it, option) }
            "messagesToHide" -> messagesToHide.mapTo(mutableListOf()) { WrappedMacro(it, option) }
            else -> mutableListOf()
        }

        ConfigUtils.getSubCategory(page, "General", "").options.add(option)

        return option
    }

    override fun load() {
        super.load()

        blockOption.wrappedMacros = messagesToBlock.mapTo(mutableListOf()) { WrappedMacro(it, blockOption) }
        hideOption.wrappedMacros = messagesToHide.mapTo(mutableListOf()) { WrappedMacro(it, hideOption) }
    }

    override fun save() {
        messagesToBlock = blockOption.wrappedMacros.map { it.macro }.toTypedArray()
        messagesToHide = hideOption.wrappedMacros.map { it.macro }.toTypedArray()

        super.save()
    }
}