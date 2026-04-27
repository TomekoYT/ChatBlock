package tomeko.chatblock.chat

//? if = 1.8.9 {
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//?} else {
/*import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
*///?}
import net.minecraft.client.Minecraft
import tomeko.chatblock.config.ChatBlockConfig

object Chat {
    private val RECEIVING = "receiving"
    private val SENDING = "sending"

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
        return !shouldBlock(message, config.messagesToBlockSending, config.blockSendingCaseSensitive, config.blockSendingInfoMessage, SENDING)
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
        return !shouldBlock(message, config.messagesToBlockSending, config.blockSendingCaseSensitive, config.blockSendingInfoMessage, SENDING)
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

        return !shouldBlock(msg, config.messagesToBlockReceiving, config.blockReceivingCaseSensitive, config.blockReceivingInfoMessage, RECEIVING)
    }

    private fun shouldBlock(
        message: String,
        //? if = 1.8.9 {
        messagesToBlock: Array<String>,
        //?} else {
        /*messagesToBlock: MutableList<String>,
        *///?}
        caseSensitive: Boolean,
        sendInfoMessage: Boolean,
        type: String
    ): Boolean {
        if (message.isEmpty()) return false

        for (messageToBlock in messagesToBlock) {
            if (messageToBlock.isEmpty()) continue

            val matches = if (caseSensitive) {
                message.contains(messageToBlock)
            } else {
                message.contains(messageToBlock, ignoreCase = true)
            }

            if (matches)
            {
                if (sendInfoMessage) {
                    val infoMessage = "Blocked $type message: $message, contains: $messageToBlock"
                    //? if = 1.8.9 {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.RED}${infoMessage}"))
                    //?} else if >= 26.1 {
                    /*Minecraft.getInstance().gui.chat.addClientSystemMessage(Component.literal(infoMessage).withStyle{it.withColor(ChatFormatting.RED)})
                    *///?} else {
                    /*Minecraft.getInstance().gui.chat.addMessage(Component.literal(infoMessage).withStyle{it.withColor(ChatFormatting.RED)})
                    *///?}
                }
                return true
            }
        }

        return false
    }
}