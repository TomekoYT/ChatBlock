package tomeko.chatblock;

import net.fabricmc.api.ClientModInitializer;
import tomeko.chatblock.config.ChatBlockConfig;

public class ChatBlock implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ChatBlockConfig.register();
	}
}