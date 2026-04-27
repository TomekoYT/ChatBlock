package tomeko.chatblock

//? if = 1.8.9 {
import cc.polyfrost.oneconfig.events.EventManager
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import tomeko.chatblock.gui.CloseInactiveConfigScreen
//?} else {
/*import net.fabricmc.api.ClientModInitializer
*///?}
import tomeko.chatblock.chat.Chat
import tomeko.chatblock.config.ChatBlockConfig
import tomeko.chatblock.utils.Constants

//? if = 1.8.9 {
@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter")
object ChatBlock {
    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        ChatBlockConfig.initialize()
        EventManager.INSTANCE.register(this)
        Chat.register()
        CloseInactiveConfigScreen.register()
    }
}
//?} else {
/*class ChatBlock : ClientModInitializer {
    override fun onInitializeClient() {
        Chat.register()
        ChatBlockConfig.register()
    }
}
*///?}