package tomeko.chatblock.chat

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import tomeko.chatblock.config.ChatBlockConfig

class Chat {
    @SubscribeEvent
    fun onChatReceive(event: ClientChatReceivedEvent) {
        if (event.type.toInt() == 2 || event.message == null) {
            return
        }

        var message = event.message.getUnformattedText()

        if (ChatBlockConfig.blockReceivingIgnoreFormatting) {
            message = removeFormatting(message)
        }

        for (macro in ChatBlockConfig.messagesToBlockReceiving) {
            if (!macro.enabled) continue

            val messageToHide = macro.text
            if (messageToHide.isEmpty()) continue

            if ((ChatBlockConfig.blockReceivingCaseSensitive && message.contains(messageToHide))
                || (!ChatBlockConfig.blockReceivingCaseSensitive && message.lowercase().contains(messageToHide.lowercase()))
            ) {
                event.setCanceled(true)
                return
            }
        }
    }

    private fun removeFormatting(string: String): String {
        return string.replace("§.".toRegex(), "")
    }

    companion object {
        fun register() {
            MinecraftForge.EVENT_BUS.register(Chat())
        }
    }
}