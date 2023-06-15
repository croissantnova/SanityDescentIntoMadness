package croissantnova.sanitydim.net;

import java.util.function.Supplier;

import croissantnova.sanitydim.capability.Sanity;
import croissantnova.sanitydim.capability.SanityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class SanityPacket
{
    public Sanity m_cap;
    public FriendlyByteBuf m_buf;

    public SanityPacket()
    {

    }

    public SanityPacket(Sanity cap)
    {
        this.m_cap = cap;
    }

    public static void encode(SanityPacket packet, FriendlyByteBuf buf)
    {
        packet.m_cap.serialize(buf);
    }

    public static SanityPacket decode(FriendlyByteBuf buf)
    {
        SanityPacket packet = new SanityPacket();
        packet.m_buf = buf;
        return packet;
    }

    @SuppressWarnings("resource")
    public static void handle(SanityPacket packet, Supplier<NetworkEvent.Context> ctx)
    {
        if (packet.m_buf == null)
            return;

        ctx.get().enqueueWork(() ->
        {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
            {
                Minecraft.getInstance().player.getCapability(SanityProvider.CAP).ifPresent(s ->
                {
                    if (s instanceof Sanity sc)
                        sc.deserialize(packet.m_buf);
                });
            });
        });
        ctx.get().setPacketHandled(true);
    }
}