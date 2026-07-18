package tomeko.chatblock.chat

//? if = 1.8.9 {
/*import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
*///?} else {
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
//?}
import tomeko.chatblock.config.ChatBlockConfig
import tomeko.chatblock.utils.Debug

object BlockReceivingMessages {
    fun register() {
        //? if = 1.8.9 {
        /*MinecraftForge.EVENT_BUS.register(this)
        *///?} else {
        ClientReceiveMessageEvents.ALLOW_GAME.register(::onChatReceive)
        //?}
    }

    //? if = 1.8.9 {
    /*@SubscribeEvent
    *///?}
    fun onChatReceive(
        //? if = 1.8.9 {
        /*event: ClientChatReceivedEvent
        *///?} else {
        message: Component?, fromActionBar: Boolean
        //?}
    )
    //? if >= 1.21.11 {
            : Boolean
    //?}
    {
        //? if = 1.8.9 {
        /*if (event.type.toInt() == 2 || event.message == null) {
            return
        }

        if (!allowReceiving(event.message.unformattedText)) event.setCanceled(true)
        *///?} else {
        return fromActionBar || message == null || allowReceiving(message.string)
        //?}
    }

    private fun allowReceiving(msg: String): Boolean {
        if (msg.isEmpty()) return true

        val message = msg.replace(Regex("§."), "")

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

        return true
    }
}