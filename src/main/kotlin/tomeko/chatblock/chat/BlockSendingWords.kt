package tomeko.chatblock.chat

//? if = 1.8.9 {
/*import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
*///?} else {
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
//?}
import tomeko.chatblock.config.ChatBlockConfig
import kotlin.math.round

object BlockSendingWords {
    //? if >= 1.21.11 {
    fun register() {
        ClientSendMessageEvents.ALLOW_CHAT.register(::allowSending)
        ClientSendMessageEvents.ALLOW_COMMAND.register(::allowSending)
    }
    //?}

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
                        val info = "Blocked sending message: $message, matched: $word with $wordToBlock (${
                            round(
                                10 * similar
                            ) / 10
                        }% similarity)"

                        //? if = 1.8.9 {
                        /*Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("${EnumChatFormatting.RED}${info}"))
                        *///?} else if = 1.21.11 {
                        /*Minecraft.getInstance().gui.chat.addMessage(
                            Component.literal(info).withStyle { it.withColor(ChatFormatting.RED) })
                        *///?} else if >= 26.2 {
                        /*Minecraft.getInstance().gui.hud.chat.addClientSystemMessage(
                            Component.literal(info).withStyle { it.withColor(ChatFormatting.RED) })
                        *///?} else {
                        Minecraft.getInstance().gui.chat.addClientSystemMessage(
                            Component.literal(info).withStyle { it.withColor(ChatFormatting.RED) })
                        //?}
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
}