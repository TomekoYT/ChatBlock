package tomeko.chatblock.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.CustomOption;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.OptionSize;
import tomeko.chatblock.ChatBlock;

import java.util.ArrayList;
import java.util.List;

public class ChatBlockConfig extends Config {
    @Switch(
            name = "Block Sending Case Sensitive",
            size = OptionSize.SINGLE
    )
    public static boolean blockCaseSensitive = false;

    @CustomOption
    public static List<String> messagesToBlock = new ArrayList<>();

    @Switch(
            name = "Block Receiving Case Sensitive",
            size = OptionSize.SINGLE
    )
    public static boolean hideCaseSensitive = false;

    @Switch(
            name = "Ignore Formatting",
            size = OptionSize.SINGLE
    )
    public static boolean hideIgnoreFormatting = true;

    @CustomOption
    public static List<String> messagesToHide = new ArrayList<>();

    public ChatBlockConfig() {
        super(new Mod(ChatBlock.NAME, ModType.UTIL_QOL), ChatBlock.MODID + ".json");
        initialize();
    }
}
