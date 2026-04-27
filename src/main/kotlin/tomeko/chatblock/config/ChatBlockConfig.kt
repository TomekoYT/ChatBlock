package tomeko.chatblock.config

//? if = 1.8.9 {
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
import tomeko.chatblock.element.BlockReceivingListOption
import tomeko.chatblock.element.BlockSendingListOption
import tomeko.chatblock.element.WrappedBlock
import java.lang.reflect.Field
//?} else {
/*import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.controller.*
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler
import dev.isxander.yacl3.config.v2.api.SerialEntry
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder
import dev.isxander.yacl3.platform.YACLPlatform
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
*///?}
import tomeko.chatblock.utils.Constants

//? if = 1.8.9 {
object ChatBlockConfig : Config(Mod(Constants.MOD_NAME, ModType.UTIL_QOL), "${Constants.MOD_ID}.json") {
//?} else {
/*class ChatBlockConfig {
    *///?}

    //? if = 1.8.9 {
    @JvmField
    @Switch(name = "Case sensitive", size = OptionSize.SINGLE)
    //?} else {
    /*@SerialEntry
    *///?}
    var blockReceivingCaseSensitive: Boolean = false

    //? if = 1.8.9 {
    @JvmField
    @Switch(name = "Ignore formatting", size = OptionSize.SINGLE)
    //?} else {
    /*@SerialEntry
    *///?}
    var blockReceivingIgnoreFormatting: Boolean = true

    //? if = 1.8.9 {
    @JvmField
    @Switch(name = "Send message informing about a block", size = OptionSize.SINGLE)
    //?} else {
    /*@SerialEntry
    *///?}
    var blockReceivingInfoMessage: Boolean = false

    //? if = 1.8.9 {
    @Info(
        text = "Block receiving following chat messages:",
        type = InfoType.INFO,
        size = 2
    )
    private var hideInfo = Runnable { }
    //?}

    //? if = 1.8.9 {
    @JvmField
    @CustomOption(id = "blockReceiving")
    var messagesToBlockReceiving: Array<String> = emptyArray()
    //?} else {
    /*@SerialEntry
    var messagesToBlockReceiving: MutableList<String> = mutableListOf()
    *///?}

    //? if = 1.8.9 {
    @JvmField
    @Switch(name = "Case sensitive", size = OptionSize.SINGLE)
    //?} else {
    /*@SerialEntry
    *///?}
    var blockSendingCaseSensitive: Boolean = false

    //? if = 1.8.9 {
    @JvmField
    @Switch(name = "Send message informing about a block", size = OptionSize.SINGLE)
    //?} else {
    /*@SerialEntry
    *///?}
    var blockSendingInfoMessage: Boolean = true

    //? if = 1.8.9 {
    @Info(
        text = "Block sending following chat messages:",
        type = InfoType.INFO,
        size = 2
    )
    private var blockInfo = Runnable { }
    //?}

    //? if = 1.8.9 {
    @JvmField
    @CustomOption(id = "blockSending")
    var messagesToBlockSending: Array<String> = emptyArray()
    //?} else {
    /*@SerialEntry
    var messagesToBlockSending: MutableList<String> = mutableListOf()
    *///?}

    //? if = 1.8.9 {
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
    //?} else {
    /*companion object {
        val CONFIG: ConfigClassHandler<ChatBlockConfig> =
            ConfigClassHandler.createBuilder(ChatBlockConfig::class.java)
                .serializer { config ->
                    GsonConfigSerializerBuilder.create(config)
                        .setPath(YACLPlatform.getConfigDir().resolve("${Constants.MOD_ID}.json"))
                        .build()
                }
                .build()

        fun configScreen(parent: Screen): Screen {
            return YetAnotherConfigLib.create(CONFIG) { defaults, config, builder ->
                builder
                    .title(Component.literal(Constants.MOD_NAME))

                    .category(
                        ConfigCategory.createBuilder()
                            .name(Component.literal("Chat Block Config"))

                            .group(
                                OptionGroup.createBuilder()
                                    .name(Component.literal("Block Receiving Custom Chat Messages"))
                                    .option(
                                        Option.createBuilder<Boolean>()
                                            .name(Component.literal("Case sensitive"))
                                            .binding(
                                                defaults.blockReceivingCaseSensitive,
                                                { config.blockReceivingCaseSensitive },
                                                { config.blockReceivingCaseSensitive = it }
                                            )
                                            .controller(TickBoxControllerBuilder::create)
                                            .build()
                                    )
                                    .option(
                                        Option.createBuilder<Boolean>()
                                            .name(Component.literal("Ignore formatting"))
                                            .binding(
                                                defaults.blockReceivingIgnoreFormatting,
                                                { config.blockReceivingIgnoreFormatting },
                                                { config.blockReceivingIgnoreFormatting = it }
                                            )
                                            .controller(TickBoxControllerBuilder::create)
                                            .build()
                                    )
                                    .option(
                                        Option.createBuilder<Boolean>()
                                            .name(Component.literal("Send info message"))
                                            .description(OptionDescription.of(Component.literal("Send message informing about a block")))
                                            .binding(
                                                defaults.blockReceivingInfoMessage,
                                                { config.blockReceivingInfoMessage },
                                                { config.blockReceivingInfoMessage = it }
                                            )
                                            .controller(TickBoxControllerBuilder::create)
                                            .build()
                                    )
                                    .build()
                            )

                            .group(
                                ListOption.createBuilder<String>()
                                    .name(Component.literal("Block receiving following chat messages:"))
                                    .binding(
                                        defaults.messagesToBlockReceiving,
                                        { config.messagesToBlockReceiving },
                                        { config.messagesToBlockReceiving = it.toMutableList() }
                                    )
                                    .controller(StringControllerBuilder::create)
                                    .initial("")
                                    .build()
                            )

                            .group(
                                OptionGroup.createBuilder()
                                    .name(Component.literal("Block Sending Custom Chat Messages"))
                                    .option(
                                        Option.createBuilder<Boolean>()
                                            .name(Component.literal("Case sensitive"))
                                            .binding(
                                                defaults.blockSendingCaseSensitive,
                                                { config.blockSendingCaseSensitive },
                                                { config.blockSendingCaseSensitive = it }
                                            )
                                            .controller(TickBoxControllerBuilder::create)
                                            .build()
                                    )
                                    .option(
                                        Option.createBuilder<Boolean>()
                                            .name(Component.literal("Send info message"))
                                            .description(OptionDescription.of(Component.literal("Send message informing about a block")))
                                            .binding(
                                                defaults.blockSendingInfoMessage,
                                                { config.blockSendingInfoMessage },
                                                { config.blockSendingInfoMessage = it }
                                            )
                                            .controller(TickBoxControllerBuilder::create)
                                            .build()
                                    )
                                    .build()
                            )

                            .group(
                                ListOption.createBuilder<String>()
                                    .name(Component.literal("Block sending following chat messages:"))
                                    .binding(
                                        defaults.messagesToBlockSending,
                                        { config.messagesToBlockSending },
                                        { config.messagesToBlockSending = it.toMutableList() }
                                    )
                                    .controller(StringControllerBuilder::create)
                                    .initial("")
                                    .build()
                            )

                            .build()
                    )
            }.generateScreen(parent)
        }

        fun register() {
            CONFIG.load()
        }
    }
    *///?}
}