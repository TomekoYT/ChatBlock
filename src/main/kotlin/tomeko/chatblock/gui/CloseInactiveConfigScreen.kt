package tomeko.chatblock.gui

import net.minecraft.client.Minecraft
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

class CloseInactiveConfigScreen {
    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (org.lwjgl.opengl.Display.isActive()) return

        val screen = Minecraft.getMinecraft().currentScreen ?: return

        if (screen::class.java.name.contains("oneconfig")) {
            Minecraft.getMinecraft().displayGuiScreen(null)
        }
    }

    companion object {
        fun register() {
            MinecraftForge.EVENT_BUS.register(CloseInactiveConfigScreen())
        }
    }
}