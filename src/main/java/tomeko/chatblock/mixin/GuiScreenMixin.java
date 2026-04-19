package tomeko.chatblock.mixin;

import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tomeko.chatblock.config.ChatBlockConfig;

@Mixin(GuiScreen.class)
public class GuiScreenMixin {
    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    private void blockChatMessage(String message, boolean addToChat, CallbackInfo ci) {
        for (String messageToBlock : ChatBlockConfig.messagesToBlockSending) {
            if (messageToBlock.isEmpty()) continue;

            if (
                    (ChatBlockConfig.blockSendingCaseSensitive && message.contains(messageToBlock))
                            || (!ChatBlockConfig.blockSendingCaseSensitive && message.toLowerCase().contains(messageToBlock.toLowerCase()))
            ) {
                ci.cancel();
                return;
            }
        }
    }
}
