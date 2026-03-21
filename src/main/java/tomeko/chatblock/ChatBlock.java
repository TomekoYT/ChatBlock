package tomeko.chatblock;

import net.fabricmc.api.ClientModInitializer;
import tomeko.chatblock.chat.*;
import tomeko.chatblock.config.*;

public class ChatBlock implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Chat.register();

		ChatBlockConfig.register();
	}
}