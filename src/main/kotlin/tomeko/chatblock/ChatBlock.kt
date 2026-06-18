package tomeko.chatblock

//? if = 1.8.9 {
/*import cc.polyfrost.oneconfig.events.EventManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
*///?} else {
import net.fabricmc.api.ClientModInitializer
//?}
import tomeko.chatblock.chat.*
import tomeko.chatblock.commands.*
import tomeko.chatblock.config.*
import tomeko.chatblock.utils.*

//? if = 1.8.9 {
/*@Mod(
    modid = Constants.MOD_ID,
    name = Constants.MOD_NAME,
    version = Constants.MOD_VERSION,
    modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter"
)
*///?}
class ChatBlock
//? if >= 26.1 {
    : ClientModInitializer
//?}
{
    //? if = 1.8.9 {
    /*@Mod.EventHandler
    *///?} else {
    override
    //?}
    fun onInitializeClient(
        //? if = 1.8.9 {
        /*event: FMLInitializationEvent
        *///?}
    ) {
        //? if = 1.8.9 {
        /*EventManager.INSTANCE.register(this)
        *///?}

        Chat.register()

        ChatBlockCommand.register()

        ChatBlockConfig.register()
    }
}