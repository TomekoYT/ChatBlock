package tomeko.chatblock.chat

//? if = 1.8.9 {
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//?} else {
/*import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents
import net.minecraft.network.chat.Component
*///?}
import tomeko.chatblock.config.ChatBlockConfig

object Chat {
    //? if = 1.8.9 {
    val config get() = ChatBlockConfig
    //?} else {
    /*val config get() = ChatBlockConfig.CONFIG.instance()
    *///?}

    //? if = 1.8.9 {
    fun register() {
        MinecraftForge.EVENT_BUS.register(Chat)
    }

    @JvmStatic
    fun allowSending(message: String): Boolean {
        return !shouldBlock(message, config.messagesToBlockSending, config.blockSendingCaseSensitive)
    }

    @SubscribeEvent
    fun onChatReceive(event: ClientChatReceivedEvent) {
        if (event.type.toInt() == 2 || event.message == null) {
            return
        }

        var message = event.message.getUnformattedText()
        if (!allowReceiving(message)) event.setCanceled(true)
    }
    //?} else {
    /*fun register() {
        ClientReceiveMessageEvents.ALLOW_GAME.register(::onChatReceive)
        ClientSendMessageEvents.ALLOW_CHAT.register(::allowSending)
        ClientSendMessageEvents.ALLOW_COMMAND.register(::allowSending)
    }

    fun allowSending(message: String): Boolean {
        return !shouldBlock(message, config.messagesToBlockSending, config.blockSendingCaseSensitive)
    }

    private fun onChatReceive(messageText: Component?, fromActionBar: Boolean): Boolean {
        if (fromActionBar || messageText == null) {
            return true
        }

        var message = messageText.string
        return allowReceiving(message)
    }
    *///?}

    private fun allowReceiving(message: String): Boolean {
        var msg = message
        if (config.blockReceivingIgnoreFormatting) {
            msg = msg.replace(Regex("§."), "")
        }

        return !shouldBlock(msg, config.messagesToBlockReceiving, config.blockReceivingCaseSensitive)
    }

    private fun shouldBlock(
        message: String,
        //? if = 1.8.9 {
        messagesToBlock: Array<String>,
        //?} else {
        /*messagesToBlock: MutableList<String>,
        *///?}
        caseSensitive: Boolean
    ): Boolean {
        if (message.isEmpty()) return false

        for (messageToBlock in messagesToBlock) {
            if (messageToBlock.isEmpty()) continue

            val matches = if (caseSensitive) {
                message.contains(messageToBlock)
            } else {
                message.contains(messageToBlock, ignoreCase = true)
            }

            if (matches) return true
        }

        return false
    }
}