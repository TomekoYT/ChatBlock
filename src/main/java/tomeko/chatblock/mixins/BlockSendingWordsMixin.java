package tomeko.chatblock.mixins;

//? if = 1.8.9 {
/*import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tomeko.chatblock.chat.BlockSendingWords;

@Mixin(GuiScreen.class)
public abstract class BlockSendingWordsMixin {
    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    private void chatblock$blockSending(String msg, boolean addToChat, CallbackInfo ci) {
        if (!BlockSendingWords.allowSending(msg)) {
            ci.cancel();
        }
    }
}
*///?}