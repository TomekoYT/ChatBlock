package tomeko.chatblock.chat

//? if = 1.8.9 {
/*import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
*///?} else {
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
//?}
import net.minecraft.client.Minecraft
import tomeko.chatblock.config.ChatBlockConfig
import tomeko.chatblock.utils.Debug
import kotlin.math.*

object Chat {
    //? if = 1.8.9 {
    /*fun register() {
        MinecraftForge.EVENT_BUS.register(Chat)
    }

    @SubscribeEvent
    fun onChatReceive(event: ClientChatReceivedEvent) {
        if (event.type.toInt() == 2 || event.message == null) {
            return
        }

        if (!allowReceiving(event.message.unformattedText)) event.setCanceled(true)
    }
    *///?} else {
    fun register() {
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
    //?}

    private fun allowReceiving(msg: String): Boolean {
        if (msg.isEmpty()) return true

        val message = if (ChatBlockConfig.blockReceivingIgnoreFormatting) msg.replace(Regex("§."), "") else msg

        for (messageToBlock in ChatBlockConfig.messagesToBlockReceiving) {
            if (messageToBlock.isEmpty()) continue

            val matches = try {
                Regex(
                    messageToBlock,
                    if (ChatBlockConfig.blockReceivingCaseSensitive)
                        emptySet()
                    else
                        setOf(RegexOption.IGNORE_CASE)
                ).containsMatchIn(message)
            } catch (_: Exception) {
                Debug.print("Invalid regex: $messageToBlock")
                false
            }

            if (matches) {
                if (ChatBlockConfig.blockReceivingInfoMessage) {
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

        for (wordToBlock in ChatBlockConfig.wordsToBlockSending) {
            if (wordToBlock.isEmpty()) continue

            val wordsInMessage = message.split(" ")

            for (word in wordsInMessage) {
                val similar = 100 * similarity(word, wordToBlock)
                if (similar >= ChatBlockConfig.blockSendingSimilarity) {
                    if (ChatBlockConfig.blockSendingInfoMessage) {
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
        if (a.isEmpty() && b.isEmpty()) return 1f

        val (s1, s2) = if (a.length <= b.length) a to b else b to a
        var prev = IntArray(s1.length + 1) { it }
        var curr = IntArray(s1.length + 1)

        for (i in 1..s2.length) {
            curr[0] = i
            for (j in 1..s1.length) {
                val cost = if (s2[i - 1].lowercaseChar() == s1[j - 1].lowercaseChar()) 0 else 1
                curr[j] = minOf(
                    prev[j] + 1,
                    curr[j - 1] + 1,
                    prev[j - 1] + cost
                )
            }

            val tmp = prev
            prev = curr
            curr = tmp
        }

        return 1f - prev[s1.length].toFloat() / maxOf(a.length, b.length).toFloat()
    }

    private fun sendClientMessage(message: String) {
        //? if = 1.8.9 {
        /*Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.RED}${message}"))
        *///?} else if >= 26.2 {
        /*Minecraft.getInstance().gui.hud.chat.addClientSystemMessage(
            Component.literal(message).withStyle { it.withColor(ChatFormatting.RED) })
        *///?} else {
        Minecraft.getInstance().gui.chat.addClientSystemMessage(
            Component.literal(message).withStyle { it.withColor(ChatFormatting.RED) })
        //?}
    }
}