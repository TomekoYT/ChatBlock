package tomeko.chatblock.chat;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.text.Text;
import tomeko.chatblock.config.ChatBlockConfig;

public class Chat {
    public static void register() {
        ClientSendMessageEvents.ALLOW_CHAT.register(Chat::BlockCustomMessages);
        ClientSendMessageEvents.ALLOW_COMMAND.register(Chat::BlockCustomMessages);
        ClientReceiveMessageEvents.ALLOW_GAME.register(Chat::HideCustomMessages);
    }

    private static boolean BlockCustomMessages(String message) {
        for (String messageToBlock : ChatBlockConfig.messagesToBlock) {
            if (messageToBlock.isEmpty()) continue;

            if (
                    (ChatBlockConfig.blockCaseSensitive && message.contains(messageToBlock))
                    || (!ChatBlockConfig.blockCaseSensitive && message.toLowerCase().contains(messageToBlock.toLowerCase()))
            ) return false;
        }

        return true;
    }

    private static boolean HideCustomMessages(Text messageText, boolean fromActionBar) {
        if (fromActionBar || messageText == null) {
            return true;
        }

        String message = messageText.getString();

        if (ChatBlockConfig.hideIgnoreFormatting) message = removeFormatting(message);

        for (String messageToHide : ChatBlockConfig.messagesToHide) {
            if (messageToHide.isEmpty()) continue;

            if (
                    (ChatBlockConfig.hideCaseSensitive && message.contains(messageToHide))
                            || (!ChatBlockConfig.hideCaseSensitive && message.toLowerCase().contains(messageToHide.toLowerCase()))
            ) return false;
        }

        return true;
    }

    private static String removeFormatting(String string) {
        return string.replaceAll("§.", "");
    }
}
