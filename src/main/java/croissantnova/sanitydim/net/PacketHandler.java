package croissantnova.sanitydim.net;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler
{
    private static int packetId = 0;

    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL_INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(SanityMod.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    public static void init()
    {
        CHANNEL_INSTANCE.registerMessage(packetId++, SanityPacket.class, SanityPacket::encode, SanityPacket::decode, SanityPacket::handle);
        CHANNEL_INSTANCE.registerMessage(
                packetId++,
                InnerEntityCapImplPacket.class,
                InnerEntityCapImplPacket::encode,
                InnerEntityCapImplPacket::decode,
                InnerEntityCapImplPacket::handle);
    }
}