package grimoire.grimoire.network.packet.servertoplayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleCustomPayload",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V"),
            cancellable = true)
    private void onCustomPayload(ClientboundCustomPayloadPacket p_105004_, CallbackInfo ci) {
        System.out.println("Mixins is Complete");
        ResourceLocation identifier = p_105004_.getIdentifier();
        String namespace = identifier.getNamespace();
        String path = identifier.getPath();
        if (namespace.equals("nick") && path.equals("update")) {
            FriendlyByteBuf data = p_105004_.getInternalData();
            byte[] bytes = data.readByteArray();
            ByteArrayDataInput input = ByteStreams.newDataInput(bytes);
            String uuid = input.readUTF();
            String newNick = input.readUTF();
            // TODO save it to your storage
            ci.cancel(); // we handled the packet so we can cancel it
        }
    }
}
