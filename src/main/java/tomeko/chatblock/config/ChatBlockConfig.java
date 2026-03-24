package tomeko.chatblock.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import tomeko.chatblock.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ChatBlockConfig {
    public static final ConfigClassHandler<ChatBlockConfig> CONFIG = ConfigClassHandler.createBuilder(ChatBlockConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(YACLPlatform.getConfigDir().resolve(Constants.MOD_ID + ".json"))
                    .build())
            .build();

    @SerialEntry
    public static boolean blockSendingCaseSensitive = false;
    @SerialEntry
    public static List<String> messagesToBlockSending = new ArrayList<>();

    @SerialEntry
    public static boolean blockReceivingCaseSensitive = false;
    @SerialEntry
    public static boolean blockReceivingIgnoreFormatting = true;
    @SerialEntry
    public static List<String> messagesToBlockReceiving = new ArrayList<>();

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.literal(Constants.MOD_NAME))

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Chat Block Config"))

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Block Sending Custom Chat Messages"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Case-Sensitive"))
                                        .binding(defaults.blockSendingCaseSensitive, () -> config.blockSendingCaseSensitive, newVal -> config.blockSendingCaseSensitive = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .group(ListOption.<String>createBuilder()
                                .name(Text.literal("Block sending following chat messages:"))
                                .binding(defaults.messagesToBlockSending, () -> config.messagesToBlockSending, newVal -> config.messagesToBlockSending = newVal)
                                .controller(StringControllerBuilder::create)
                                .initial("")
                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Block Receiving Custom Chat Messages"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Case-Sensitive"))
                                        .binding(defaults.blockReceivingCaseSensitive, () -> config.blockReceivingCaseSensitive, newVal -> config.blockReceivingCaseSensitive = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Ignore Formatting"))
                                        .binding(defaults.blockReceivingIgnoreFormatting, () -> config.blockReceivingIgnoreFormatting, newVal -> config.blockReceivingIgnoreFormatting = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .group(ListOption.<String>createBuilder()
                                .name(Text.literal("Block receiving following chat messages:"))
                                .binding(defaults.messagesToBlockReceiving, () -> config.messagesToBlockReceiving, newVal -> config.messagesToBlockReceiving = newVal)
                                .controller(StringControllerBuilder::create)
                                .initial("")
                                .build())
                        .build())

        )).generateScreen(parent);
    }

    public static void register() {
        ChatBlockConfig.CONFIG.load();
    }
}
