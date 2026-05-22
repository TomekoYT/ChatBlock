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
import kotlin.math.*

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

    @SubscribeEvent
    fun onChatReceive(event: ClientChatReceivedEvent) {
        if (event.type.toInt() == 2 || event.message == null) {
            return
        }

        if (!allowReceiving(event.message.unformattedText)) event.setCanceled(true)
    }
    //?} else {
    /*fun register() {
        ClientReceiveMessageEvents.ALLOW_GAME.register(::onChatReceive)
        ClientSendMessageEvents.ALLOW_CHAT.register(::allowSending)
        ClientSendMessageEvents.ALLOW_COMMAND.register(::allowSending)
    }

    private fun onChatReceive(message: Component?, fromActionBar: Boolean): Boolean {
        if (fromActionBar || message == null) {
            return true
        }

        return allowReceiving(message.string)
    }
    *///?}

    private fun allowReceiving(msg: String): Boolean {
        if (msg.isEmpty()) return true

        val message = if (config.blockReceivingIgnoreFormatting) msg.replace(Regex("§."), "") else msg

        for (messageToBlock in config.messagesToBlockReceiving) {
            if (messageToBlock.isEmpty()) continue

            val matches =
                if (config.blockReceivingCaseSensitive)
                    message.contains(messageToBlock)
                else
                    message.contains(messageToBlock, ignoreCase = true)

            if (matches) {
                if (config.blockReceivingInfoMessage) {
                    sendClientMessage("Blocked receiving message: $message, contains: $messageToBlock")
                }
                return false
            }
        }

        return true
    }

    @JvmStatic
    fun allowSending(message: String): Boolean {
        if (message.isEmpty()) return true

        for (wordToBlock in config.wordsToBlockSending) {
            if (wordToBlock.isEmpty()) continue

            val wordsInMessage = message.split(" ")

            for (word in wordsInMessage) {
                val similar = 100 * similarity(word, wordToBlock)
                if (similar >= config.blockSendingSimilarity) {
                    if (config.blockSendingInfoMessage) {
                        sendClientMessage(
                            "Blocked sending message: $message, matched: $word with $wordToBlock (${
                                round(
                                    10 * similar
                                ) / 10
                            }% similarity)"
                        )
                    }
                    return false
                }
            }
        }

        return true
    }

    private fun similarity(a: String, b: String): Float {
        val dp = Array(a.length + 1) { Array(b.length + 1) { 0 } }
        for (i in 0..a.length) dp[i][0] = i
        for (j in 0..b.length) dp[0][j] = j

        for (i in 1..a.length) {
            for (j in 1..b.length) {
                val cost = if (a[i - 1].lowercaseChar() == b[j - 1].lowercaseChar()) 0 else 1

                dp[i][j] = min(min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost)
            }
        }

        return 1.0f - dp[a.length][b.length].toFloat() / max(a.length, b.length).toFloat()
    }

    private fun sendClientMessage(message: String) {
        //? if = 1.8.9 {
        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.RED}${message}"))
        //?} else if >= 26.1 {
        /*Minecraft.getInstance().gui.chat.addClientSystemMessage(Component.literal(message).withStyle{it.withColor(ChatFormatting.RED)})
        *///?} else {
        /*Minecraft.getInstance().gui.chat.addMessage(Component.literal(message).withStyle{it.withColor(ChatFormatting.RED)})
        *///?}
    }
}