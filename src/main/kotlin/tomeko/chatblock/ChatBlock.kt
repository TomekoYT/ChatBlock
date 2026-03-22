package tomeko.chatblock

import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.renderer.asset.SVG
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import tomeko.chatblock.chat.Chat
import tomeko.chatblock.config.ChatBlockConfig

@Mod(modid = ChatBlock.MODID, name = ChatBlock.NAME, version = ChatBlock.VERSION, modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter")
object ChatBlock {
    const val MODID: String = "@ID@"
    const val NAME: String = "@NAME@"
    const val VERSION: String = "@VER@"

    val PLUS_ICON = SVG("/assets/chatblock/icons/plus.svg")
    val MINUS_ICON = SVG("/assets/chatblock/icons/minus.svg")

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        ChatBlockConfig.initialize()
        EventManager.INSTANCE.register(this)
        Chat.register()
    }
}