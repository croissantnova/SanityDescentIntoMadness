package croissantnova.sanitydim.util;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SanityMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerHelper {
    public static MinecraftServer server;

    public static int runCommand(String command) {
        return server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withSuppressedOutput(), command);
    }

    public static int runCommand(String command, Object... args) {
        return runCommand(String.format(command, args));
    }




    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent event) {
        server = event.getServer();
        SanityMod.LOGGER.info("Started ServerHelper");
    }
}
