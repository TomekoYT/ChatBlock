package tomeko.chatblock.config

import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.controller.*
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler
import dev.isxander.yacl3.config.v2.api.SerialEntry
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder
import dev.isxander.yacl3.platform.YACLPlatform
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import tomeko.chatblock.utils.Constants

class ChatBlockConfig {
    @SerialEntry
    var blockReceivingCaseSensitive: Boolean = false

    @SerialEntry
    var blockReceivingIgnoreFormatting: Boolean = true

    @SerialEntry
    var messagesToBlockReceiving: MutableList<String> = mutableListOf()

    @SerialEntry
    var blockSendingCaseSensitive: Boolean = false

    @SerialEntry
    var messagesToBlockSending: MutableList<String> = mutableListOf()

    companion object {
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
                                            .name(Component.literal("Case-Sensitive"))
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
                                            .name(Component.literal("Ignore Formatting"))
                                            .binding(
                                                defaults.blockReceivingIgnoreFormatting,
                                                { config.blockReceivingIgnoreFormatting },
                                                { config.blockReceivingIgnoreFormatting = it }
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
                                            .name(Component.literal("Case-Sensitive"))
                                            .binding(
                                                defaults.blockSendingCaseSensitive,
                                                { config.blockSendingCaseSensitive },
                                                { config.blockSendingCaseSensitive = it }
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
}