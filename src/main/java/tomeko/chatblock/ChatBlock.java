package tomeko.chatblock;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import tomeko.chatblock.chat.*;
import tomeko.chatblock.config.*;

@Mod(modid = ChatBlock.MODID, name = ChatBlock.NAME, version = ChatBlock.VERSION)
public class ChatBlock {
	public static final String MODID = "@ID@";
	public static final String NAME = "@NAME@";
	public static final String VERSION = "@VER@";

	@Mod.Instance(MODID)
	public static ChatBlock INSTANCE;
	public static ChatBlockConfig config;

	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event) {
		config = new ChatBlockConfig();

		Chat.register();
	}
}