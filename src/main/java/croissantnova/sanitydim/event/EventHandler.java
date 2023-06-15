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
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.event.entity.player.TradeWithVillagerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ForgeRegistries;
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
    public void tickLevel(final TickEvent.LevelTickEvent event)
    {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END && event.level instanceof ServerLevel sl)
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
    public void onPlayerGotAdvancement(final AdvancementEvent.AdvancementEarnEvent event)
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
        if (!event.getLevel().isClientSide())
        {
            for (Player player : event.getLevel().players())
            {
                SanityProcessor.handleActiveSourceForPlayer((ServerPlayer)player, ActiveSanitySources.SLEEPING, ConfigProxy::getSleepingCooldown, ConfigProxy::getSleeping);
            }
        }
    }

    @SubscribeEvent
    public void onTradeWithVillager(final TradeWithVillagerEvent event)
    {
        if (event.getEntity() instanceof ServerPlayer sp)
        {
            SanityProcessor.handlePlayerTradedWithVillager(sp);
        }
    }

    @SubscribeEvent
    public void onPlayerUseItem(final LivingEntityUseItemEvent.Finish event)
    {
        if (event.getEntity() instanceof ServerPlayer sp && event.getItem().getItem().isEdible())
        {
            SanityProcessor.handlePlayerAte(sp, event.getItem());
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void tickLocalPlayer(final TickEvent.PlayerTickEvent event)
    {
        if (event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END && event.player instanceof LocalPlayer)
        {
            SoundPlayback.playSounds((LocalPlayer)event.player);
            SanityMod.getInstance().getGui().tick(Minecraft.getInstance().getPartialTick());
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void localLevelLoad(final LevelEvent.Load event)
    {
        if (event.getLevel() instanceof ClientLevel)
        {
            SoundPlayback.onClientLevelLoad((ClientLevel) event.getLevel());
        }
    }

    @SubscribeEvent
    public void registerCommands(final RegisterCommandsEvent event)
    {
        SanityMod.LOGGER.info("Registering sanity command...");
        SanityCommand.register(event.getDispatcher());
    }
}