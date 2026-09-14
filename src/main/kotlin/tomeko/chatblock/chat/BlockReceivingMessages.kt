package tomeko.chatblock.chat

//? if 1.8.9 {
/*import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.IChatComponent as Component
*///?} else {
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
//?}
import tomeko.chatblock.config.ChatBlockConfig
//? if 1.8.9 {
//import tomeko.chatblock.event.ClientReceiveMessageEvents
//?}
import tomeko.chatblock.utils.Debug

object BlockReceivingMessages {
    fun register() {
        ClientReceiveMessageEvents.ALLOW_GAME.register(::allowReceiving)
    }

    private fun allowReceiving(component: Component?, fromActionBar: Boolean): Boolean {
        if (fromActionBar || component == null) return false

        val message =
            //? if 1.8.9 {
            //component.unformattedText.replace(Regex("§."), "")
        //?} else {
        component.string.replace(Regex("§."), "")
        //?}

        for (messageToBlock in ChatBlockConfig.messagesToBlockReceiving) {
            if (messageToBlock.isEmpty()) continue

            val matches = try {
                Regex(
                    messageToBlock,
                    if (ChatBlockConfig.blockReceivingCaseSensitive)
                        emptySet()
                    else
                        setOf(RegexOption.IGNORE_CASE)
                ).matches(message)
            } catch (_: Exception) {
                Debug.log("Invalid regex: $messageToBlock")
                false
            }

            if (matches) {
                if (ChatBlockConfig.blockReceivingInfoMessage) {
                    val info = "Blocked receiving message: $message, regex: $messageToBlock"

                    //? if 1.8.9 {
                    /*Minecraft.getMinecraft().thePlayer.addChatMessage(
                        ChatComponentText(info).setChatStyle(
                            ChatStyle().setColor(
                                EnumChatFormatting.RED
                            )
                        )
                    )
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
        return true
    }
}