package tomeko.chatblock.mixin;

//? if = 1.8.9 {
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tomeko.chatblock.chat.Chat;

@Mixin(GuiScreen.class)
public class GuiScreenMixin {
    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    private void blockSending(String message, boolean addToChat, CallbackInfo ci) {
        if (!Chat.allowSending(message)) {
            ci.cancel();
        }
    }
}
//?}