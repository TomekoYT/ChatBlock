package tomeko.chatblock.chat;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.network.chat.Component;
import tomeko.chatblock.config.ChatBlockConfig;

public class Chat {
    public static void register() {
        ClientReceiveMessageEvents.ALLOW_GAME.register(Chat::BlockReceivingCustomMessages);
        ClientSendMessageEvents.ALLOW_CHAT.register(Chat::BlockSendingCustomMessages);
        ClientSendMessageEvents.ALLOW_COMMAND.register(Chat::BlockSendingCustomMessages);
    }

    private static boolean BlockReceivingCustomMessages(Component messageText, boolean fromActionBar) {
        if (fromActionBar || messageText == null) {
            return true;
        }

        String message = messageText.getString();

        if (ChatBlockConfig.blockReceivingIgnoreFormatting) message = removeFormatting(message);

        for (String messageToHide : ChatBlockConfig.messagesToBlockReceiving) {
            if (messageToHide.isEmpty()) continue;

            if (
                    (ChatBlockConfig.blockReceivingCaseSensitive && message.contains(messageToHide))
                            || (!ChatBlockConfig.blockReceivingCaseSensitive && message.toLowerCase().contains(messageToHide.toLowerCase()))
            ) return false;
        }

        return true;
    }

    private static boolean BlockSendingCustomMessages(String message) {
        for (String messageToBlock : ChatBlockConfig.messagesToBlockSending) {
            if (messageToBlock.isEmpty()) continue;

            if (
                    (ChatBlockConfig.blockSendingCaseSensitive && message.contains(messageToBlock))
                    || (!ChatBlockConfig.blockSendingCaseSensitive && message.toLowerCase().contains(messageToBlock.toLowerCase()))
            ) return false;
        }

        return true;
    }

    private static String removeFormatting(String string) {
        return string.replaceAll("§.", "");
    }
}
