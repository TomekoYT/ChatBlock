package tomeko.chatblock.chat

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents
import net.minecraft.network.chat.Component
import tomeko.chatblock.config.ChatBlockConfig

object Chat {
    val config get() = ChatBlockConfig.CONFIG.instance()

    fun register() {
        ClientReceiveMessageEvents.ALLOW_GAME.register(::blockReceivingCustomMessages)
        ClientSendMessageEvents.ALLOW_CHAT.register(::blockSendingCustomMessages)
        ClientSendMessageEvents.ALLOW_COMMAND.register(::blockSendingCustomMessages)
    }

    private fun blockReceivingCustomMessages(messageText: Component?, fromActionBar: Boolean): Boolean {
        if (fromActionBar || messageText == null) {
            return true
        }

        var message = messageText.string

        if (config.blockReceivingIgnoreFormatting) {
            message = removeFormatting(message)
        }

        for (messageToHide in config.messagesToBlockReceiving) {
            if (messageToHide.isEmpty()) continue

            val matches = if (config.blockReceivingCaseSensitive) {
                message.contains(messageToHide)
            } else {
                message.contains(messageToHide, ignoreCase = true)
            }

            if (matches) return false
        }

        return true
    }

    private fun blockSendingCustomMessages(message: String): Boolean {
        for (messageToBlock in config.messagesToBlockSending) {
            if (messageToBlock.isEmpty()) continue

            val matches = if (config.blockSendingCaseSensitive) {
                message.contains(messageToBlock)
            } else {
                message.contains(messageToBlock, ignoreCase = true)
            }

            if (matches) return false
        }

        return true
    }

    private fun removeFormatting(string: String): String {
        return string.replace(Regex("§."), "")
    }
}