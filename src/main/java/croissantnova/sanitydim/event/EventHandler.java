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
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
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
import net.minecraftforge.server.ServerLifecycleHooks;

public class EventHandler
{
    @SubscribeEvent
    public void registerCaps(final RegisterCapabilitiesEvent event)
    {
        event.register(ISanity.class);
        event.register(IInnerEntityCap.class);
    }

    @SubscribeEvent
    public void attachCaps(final AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof Player)
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
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END && event.player instanceof ServerPlayer sp)
        {
            SanityProcessor.tickPlayer(sp);
        }
    }

    @SubscribeEvent
    public void tickLevel(final TickEvent.WorldTickEvent event)
    {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END && event.world instanceof ServerLevel sl)
        {
            SanityProcessor.tickLevel(sl);
        }
    }

    @SubscribeEvent
    public void onLivingDamage(final LivingDamageEvent event)
    {
        if (event.getEntity() instanceof ServerPlayer player)
        {
            SanityProcessor.handlePlayerHurt(player, event.getAmount());
        }
        else if (event.getEntity() instanceof Animal animal && event.getSource().getEntity() instanceof ServerPlayer player)
        {
            SanityProcessor.handlePlayerHurtAnimal(player, animal, event.getAmount());
        }
    }

    @SubscribeEvent
    public void onLivingDeath(final LivingDeathEvent event)
    {
        if (event.getEntity() instanceof TamableAnimal ta && ta.getOwnerUUID() != null)
        {
            SanityProcessor.handlePlayerPetDeath(ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(ta.getOwnerUUID()), ta);
        }
    }

    @SubscribeEvent
    public void onPlayerGotAdvancement(final AdvancementEvent event)
    {
        SanityProcessor.handlePlayerGotAdvancement((ServerPlayer)event.getEntity(), event.getAdvancement());
    }

    @SubscribeEvent
    public void onPlayerBredAnimals(final BabyEntitySpawnEvent event)
    {
        if (event.getCausedByPlayer() instanceof ServerPlayer sp)
        {
            SanityProcessor.handlePlayerBredAnimals(sp);
        }
    }

    @SubscribeEvent
    public void onSleepFinished(final SleepFinishedTimeEvent event)
    {
        if (!event.getWorld().isClientSide())
        {
            for (Player player : event.getWorld().players())
            {
                SanityProcessor.handleActiveSourceForPlayer((ServerPlayer)player, ActiveSanitySources.SLEEPING, ConfigProxy::getSleepingCooldown, ConfigProxy::getSleeping);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerUsedItem(final LivingEntityUseItemEvent.Finish event)
    {
        if (event.getEntity() instanceof ServerPlayer sp)
        {
            SanityProcessor.handlePlayerUsedItem(sp, event.getItem());
        }
    }

    @SubscribeEvent
    public void onItemFished(final ItemFishedEvent event)
    {
        if (event.getEntity() instanceof ServerPlayer sp)
        {
            SanityProcessor.handlePlayerFishedItem(sp);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void tickLocalPlayer(final TickEvent.PlayerTickEvent event)
    {
        if (event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END && event.player instanceof LocalPlayer)
        {
            SoundPlayback.playSounds((LocalPlayer)event.player);
            SanityMod.getInstance().getGui().tick(.95f);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void localLevelLoad(final WorldEvent.Load event)
    {
        if (event.getWorld() instanceof ClientLevel)
        {
            SoundPlayback.onClientLevelLoad((ClientLevel) event.getWorld());
        }
    }

    @SubscribeEvent
    public void registerCommands(final RegisterCommandsEvent event)
    {
        SanityMod.LOGGER.info("Registering sanity command...");
        SanityCommand.register(event.getDispatcher());
    }
}