package croissantnova.sanitydim.net;

import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.capability.*;
import croissantnova.sanitydim.entity.InnerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class InnerEntityCapImplPacket
{
    public int m_id;
    public InnerEntityCapImpl m_cap;
    public FriendlyByteBuf m_buf;

    public InnerEntityCapImplPacket()
    {
    }

    public InnerEntityCapImplPacket(InnerEntityCapImpl cap)
    {
        this.m_cap = cap;
    }

    public static void encode(InnerEntityCapImplPacket packet, FriendlyByteBuf buf)
    {
        buf.writeInt(packet.m_id);
        packet.m_cap.serialize(buf);
    }

    public static InnerEntityCapImplPacket decode(FriendlyByteBuf buf)
    {
        int id = buf.readInt();
        InnerEntityCapImplPacket packet = new InnerEntityCapImplPacket();
        packet.m_id = id;
        packet.m_buf = buf;
        return packet;
    }

    public static void handle(InnerEntityCapImplPacket packet, Supplier<NetworkEvent.Context> ctx)
    {
        if (packet.m_buf == null)
            return;

        ctx.get().enqueueWork(() ->
        {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
            {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player == null)
                    return;

                Entity ent = Minecraft.getInstance().player.level.getEntity(packet.m_id);

                if (!(ent instanceof InnerEntity))
                    return;

                ent.getCapability(InnerEntityCapImplProvider.CAP).ifPresent(iec ->
                {
                    if (iec instanceof InnerEntityCapImpl ieci)
                        ieci.deserialize(packet.m_buf);
                });
            });
        });
        ctx.get().setPacketHandled(true);
    }
}