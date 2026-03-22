package tomeko.chatblock.mixin;

import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tomeko.chatblock.config.ChatBlockConfig;
import tomeko.chatblock.config.Macro;

@Mixin(GuiScreen.class)
public class GuiScreenMixin {
    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    private void blockChatMessage(String message, boolean addToChat, CallbackInfo ci) {
        for (Macro macro : ChatBlockConfig.messagesToBlock) {
            if (!macro.getEnabled()) continue;

            String messageToBlock = macro.getText();
            if (messageToBlock.isEmpty()) continue;

            if (
                    (ChatBlockConfig.blockCaseSensitive && message.contains(messageToBlock))
                            || (!ChatBlockConfig.blockCaseSensitive && message.toLowerCase().contains(messageToBlock.toLowerCase()))
            ) {
                ci.cancel();
                return;
            }
        }
    }
}
