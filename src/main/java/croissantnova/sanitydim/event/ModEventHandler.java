package croissantnova.sanitydim.event;

import croissantnova.sanitydim.client.render.RendererRottingStalker;
import croissantnova.sanitydim.config.ConfigHandler;
import croissantnova.sanitydim.entity.EntityRegistry;
import croissantnova.sanitydim.entity.RottingStalker;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

public class ModEventHandler
{
    @SubscribeEvent
    public static void addEntityAttributes(final EntityAttributeCreationEvent event)
    {
//        event.put(EntityRegistry.CREEPING_NIGHTMARE.get(), CreepingNightmare.buildAttributes());
//        event.put(EntityRegistry.SNEAKING_TERROR.get(), SneakingTerror.buildAttributes());
        event.put(EntityRegistry.ROTTING_STALKER.get(), RottingStalker.buildAttributes());
    }

    @SubscribeEvent
    public static void onConfigLoading(final ModConfig.Loading event)
    {
        ConfigHandler.onConfigLoading(event);
    }

    @SubscribeEvent
    public static void onConfigReloading(final ModConfig.Reloading event)
    {
        ConfigHandler.onConfigReloading(event);
    }
}