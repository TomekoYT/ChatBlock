package tomeko.chatblock.chat;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.Text;
import tomeko.chatblock.config.ChatBlockConfig;

public class Chat {
    public static void register() {
        ClientReceiveMessageEvents.ALLOW_GAME.register(Chat::HideCustomMessages);
    }

    private static boolean HideCustomMessages(Text message, boolean fromActionBar)
    {
        if (fromActionBar || message == null) {
            return true;
        }

        String unformattedMessage = removeFormatting(message.getString());

        for (String messageToHide : ChatBlockConfig.customChatMessagesToHide) {
            if (messageToHide.isEmpty()) {
                continue;
            }

            if (unformattedMessage.contains(messageToHide)) {
                return false;
            }
        }

        return true;
    }

    private static String removeFormatting(String string) {
        return string.replaceAll("§.", "");
    }
}
