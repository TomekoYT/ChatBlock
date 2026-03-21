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
    public static List<String> customChatMessagesToHide = new ArrayList<>();

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.literal(Constants.MOD_NAME))

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Chat Block Config"))

                        .group(ListOption.<String>createBuilder()
                                .name(Text.literal("Hide Custom Chat Messages"))
                                .binding(defaults.customChatMessagesToHide, () -> config.customChatMessagesToHide, newVal -> config.customChatMessagesToHide = newVal)
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
