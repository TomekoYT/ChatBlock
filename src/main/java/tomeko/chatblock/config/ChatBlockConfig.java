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
    public static boolean blockCaseSensitive = false;
    @SerialEntry
    public static List<String> messagesToBlock = new ArrayList<>();

    @SerialEntry
    public static boolean hideCaseSensitive = false;
    @SerialEntry
    public static boolean hideIgnoreFormatting = true;
    @SerialEntry
    public static List<String> messagesToHide = new ArrayList<>();

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.literal(Constants.MOD_NAME))

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Chat Block Config"))

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Block Sending Custom Chat Messages"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Case-Sensitive"))
                                        .binding(defaults.blockCaseSensitive, () -> config.blockCaseSensitive, newVal -> config.blockCaseSensitive = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .group(ListOption.<String>createBuilder()
                                .name(Text.literal("Block sending following chat messages:"))
                                .binding(defaults.messagesToBlock, () -> config.messagesToBlock, newVal -> config.messagesToBlock = newVal)
                                .controller(StringControllerBuilder::create)
                                .initial("")
                                .build())

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Block Receiving Custom Chat Messages"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Case-Sensitive"))
                                        .binding(defaults.hideCaseSensitive, () -> config.hideCaseSensitive, newVal -> config.hideCaseSensitive = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Ignore Formatting"))
                                        .binding(defaults.hideIgnoreFormatting, () -> config.hideIgnoreFormatting, newVal -> config.hideIgnoreFormatting = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .group(ListOption.<String>createBuilder()
                                .name(Text.literal("Block receiving following chat messages:"))
                                .binding(defaults.messagesToHide, () -> config.messagesToHide, newVal -> config.messagesToHide = newVal)
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
