package tomeko.chatblock.mixins;

//? if 1.8.9 {
/*import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tomeko.chatblock.event.ClientSendMessageEvents;

@Mixin(GuiScreen.class)
public abstract class GuiScreenMixin {
    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    private void hymod$onSendChatMessage(String message, boolean addToChat, CallbackInfo ci) {
        if (!ClientSendMessageEvents.ALLOW.invoker().allowSendChatMessage(message)) {
            ci.cancel();
            return;
        }

        message = ClientSendMessageEvents.MODIFY.invoker().modifySendChatMessage(message);
        if (message == null) {
            ci.cancel();
            return;
        }

        ClientSendMessageEvents.CHAT.invoker().onSendChatMessage(message);
    }
}
*///?}