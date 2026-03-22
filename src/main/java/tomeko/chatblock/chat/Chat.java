package tomeko.chatblock.chat;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import tomeko.chatblock.config.ChatBlockConfig;
import tomeko.chatblock.config.Macro;

public class Chat {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new Chat());
    }

    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event) {
        if (event.type == 2 || event.message == null) {
            return;
        }

        String message = event.message.getUnformattedText();

        if (ChatBlockConfig.hideIgnoreFormatting) {
            message = removeFormatting(message);
        }

        for (Macro macro : ChatBlockConfig.messagesToHide) {
            if (!macro.getEnabled()) continue;

            String messageToHide = macro.getText();
            if (messageToHide.isEmpty()) continue;

            if (
                    (ChatBlockConfig.hideCaseSensitive && message.contains(messageToHide))
                            || (!ChatBlockConfig.hideCaseSensitive && message.toLowerCase().contains(messageToHide.toLowerCase()))
            ) {
                event.setCanceled(true);
                return;
            }
        }
    }

    private String removeFormatting(String string) {
        return string.replaceAll("§.", "");
    }
}