package tomeko.chatblock

import net.fabricmc.api.ClientModInitializer
import tomeko.chatblock.chat.Chat
import tomeko.chatblock.config.ChatBlockConfig

class ChatBlock : ClientModInitializer {
    override fun onInitializeClient() {
        Chat.register()
        ChatBlockConfig.register()
    }
}