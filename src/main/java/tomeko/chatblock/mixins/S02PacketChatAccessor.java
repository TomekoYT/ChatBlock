package tomeko.chatblock.mixins;

//? if 1.8.9 {
/*import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(S02PacketChat.class)
public interface S02PacketChatAccessor {
    @Accessor("chatComponent")
    void hymod$setChatComponent(IChatComponent component);

    @Accessor("type")
    byte hymod$getType();
}
*///?}