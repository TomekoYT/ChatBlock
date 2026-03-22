package tomeko.chatblock

import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.events.event.KeyInputEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import cc.polyfrost.oneconfig.renderer.asset.SVG
import cc.polyfrost.oneconfig.utils.dsl.mc
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import tomeko.chatblock.chat.Chat
import tomeko.chatblock.config.ChatBlockConfig
import tomeko.chatblock.element.MacroListOption
import tomeko.chatblock.element.WrappedMacro

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

    @Subscribe
    fun onKeyInput(event: KeyInputEvent) {
        for (wrapped in MacroListOption.wrappedMacros) {
            wrapped.onKeyInput()
        }
    }

    private fun WrappedMacro.onKeyInput() {
        if (!macro.enabled) return
        if (!firstPressed()) return
        with(macro.text) {
            if (ClientCommandHandler.instance.executeCommand(mc.thePlayer, this) == 0) {
                mc.thePlayer.sendChatMessage(this)
            }
        }
    }
}