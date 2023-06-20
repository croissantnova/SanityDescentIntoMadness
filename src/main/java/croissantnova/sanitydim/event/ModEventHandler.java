package croissantnova.sanitydim.event;

import croissantnova.sanitydim.client.render.RendererRottingStalker;
import croissantnova.sanitydim.config.ConfigHandler;
import croissantnova.sanitydim.entity.EntityRegistry;
import croissantnova.sanitydim.entity.RottingStalker;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

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
    public static void onConfigLoading(final ModConfigEvent.Loading event)
    {
        ConfigHandler.onConfigLoading(event);
    }

    @SubscribeEvent
    public static void onConfigReloading(final ModConfigEvent.Reloading event)
    {
        ConfigHandler.onConfigReloading(event);
    }

    @SubscribeEvent
    public static void registerEntityRenderersEvent(final EntityRenderersEvent.RegisterRenderers event)
    {
//        event.registerEntityRenderer(EntityRegistry.CREEPING_NIGHTMARE.get(), RendererCreepingNightmare::new);
//        event.registerEntityRenderer(EntityRegistry.SNEAKING_TERROR.get(), RendererSneakingTerror::new);
        event.registerEntityRenderer(EntityRegistry.ROTTING_STALKER.get(), RendererRottingStalker::new);
    }
}