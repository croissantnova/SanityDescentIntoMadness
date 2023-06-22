package croissantnova.sanitydim.event;

import croissantnova.sanitydim.ActiveSanitySources;
import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.SanityProcessor;
import croissantnova.sanitydim.capability.IInnerEntityCap;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.capability.InnerEntityCapImplProvider;
import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.client.SoundPlayback;
import croissantnova.sanitydim.command.SanityCommand;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.entity.InnerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.world.SleepFinishedTimeEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class EventHandler
{
    @SubscribeEvent
    public void attachCaps(final AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof PlayerEntity)
        {
            event.addCapability(SanityProvider.KEY, new SanityProvider());
        }
        else if (event.getObject() instanceof InnerEntity)
        {
            event.addCapability(InnerEntityCapImplProvider.KEY, new InnerEntityCapImplProvider());
        }
    }

    @SubscribeEvent
    public void tickPlayer(final TickEvent.PlayerTickEvent event)
    {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END && event.player instanceof ServerPlayerEntity)
        {
            SanityProcessor.tickPlayer((ServerPlayerEntity)event.player);
        }
    }

    @SubscribeEvent
    public void tickLevel(final TickEvent.WorldTickEvent event)
    {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END && event.world instanceof ServerWorld)
        {
            SanityProcessor.tickLevel((ServerWorld)event.world);
        }
    }

    @SubscribeEvent
    public void onLivingDamage(final LivingDamageEvent event)
    {
        if (event.getEntityLiving() instanceof ServerPlayerEntity)
        {
            SanityProcessor.handlePlayerHurt((ServerPlayerEntity)event.getEntityLiving(), event.getAmount());
        }
        else if (event.getEntityLiving() instanceof AnimalEntity && event.getSource().getEntity() instanceof ServerPlayerEntity)
        {
            SanityProcessor.handlePlayerHurtAnimal((ServerPlayerEntity)event.getSource().getEntity(), (AnimalEntity)event.getEntity(), event.getAmount());
        }
    }

    @SubscribeEvent
    public void onLivingDeath(final LivingDeathEvent event)
    {
        if (event.getEntityLiving() instanceof TameableEntity && ((TameableEntity)event.getEntityLiving()).getOwnerUUID() != null)
        {
            SanityProcessor.handlePlayerPetDeath(ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(((TameableEntity)event.getEntityLiving()).getOwnerUUID()), ((TameableEntity)event.getEntityLiving()));
        }
    }

    @SubscribeEvent
    public void onPlayerGotAdvancement(final AdvancementEvent event)
    {
        SanityProcessor.handlePlayerGotAdvancement((ServerPlayerEntity)event.getEntity(), event.getAdvancement());
    }

    @SubscribeEvent
    public void onPlayerBredAnimals(final BabyEntitySpawnEvent event)
    {
        if (event.getCausedByPlayer() instanceof ServerPlayerEntity)
        {
            SanityProcessor.handlePlayerBredAnimals((ServerPlayerEntity)event.getCausedByPlayer());
        }
    }

    @SubscribeEvent
    public void onSleepFinished(final SleepFinishedTimeEvent event)
    {
        if (!event.getWorld().isClientSide())
        {
            for (PlayerEntity player : event.getWorld().players())
            {
                SanityProcessor.handleActiveSourceForPlayer((ServerPlayerEntity) player, ActiveSanitySources.SLEEPING, ConfigProxy::getSleepingCooldown, ConfigProxy::getSleeping);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerUsedItem(final LivingEntityUseItemEvent.Finish event)
    {
        if (event.getEntityLiving() instanceof ServerPlayerEntity)
        {
            SanityProcessor.handlePlayerUsedItem((ServerPlayerEntity)event.getEntityLiving(), event.getItem());
        }
    }

    @SubscribeEvent
    public void onItemFished(final ItemFishedEvent event)
    {
        if (event.getEntityLiving() instanceof ServerPlayerEntity)
        {
            SanityProcessor.handlePlayerFishedItem((ServerPlayerEntity)event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public void registerCommands(final RegisterCommandsEvent event)
    {
        SanityMod.LOGGER.info("Registering sanity command...");
        SanityCommand.register(event.getDispatcher());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void tickLocalPlayer(final TickEvent.PlayerTickEvent event)
    {
        if (event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END && event.player instanceof ClientPlayerEntity)
        {
            SoundPlayback.playSounds((ClientPlayerEntity)event.player);
            SanityMod.getInstance().getGui().tick(.95f);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void localLevelLoad(final WorldEvent.Load event)
    {
        if (event.getWorld() instanceof ClientWorld)
        {
            SoundPlayback.onClientLevelLoad((ClientWorld) event.getWorld());
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void renderOverlays(final RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR)
        {
            SanityMod.getInstance().getGui().renderOverlays(event.getMatrixStack(), event.getPartialTicks(), event.getWindow());
        }
    }
}