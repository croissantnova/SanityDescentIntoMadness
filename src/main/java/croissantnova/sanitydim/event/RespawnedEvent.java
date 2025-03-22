package croissantnova.sanitydim.event;

import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.api.PlayerSanityAPI;
import croissantnova.sanitydim.config.ConfigManager;
import croissantnova.sanitydim.config.registry.ConfigRegistry;
import croissantnova.sanitydim.util.DeathScoreHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber(modid = SanityMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class RespawnedEvent {

    @SubscribeEvent
    public static void respawnedEvent(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        applyRespawnedCloseToDeath(player);
        applyDeathFrequency(player);
    }

    private static void applyDeathFrequency(@NotNull Player player) {
        AtomicReference<Float> sanityChange = new AtomicReference<>(0f);

        DeathScoreHelper dsh = new DeathScoreHelper(player);
        if (!dsh.isDeathSanityEnabled()) {
            return;
        }

        boolean damned = dsh.isDamned();
        if (dsh.isDeathStackingEnabled()) {
            dsh.incrementDeathScore();
            sanityChange.set(dsh.calculateTotalSanityLossFromDeaths());
        }
        else {
            sanityChange.set(dsh.getSanityPerDeath());
        }

        if (sanityChange.get() != 0f) {
            PlayerSanityAPI.addSanity((ServerPlayer) player, sanityChange.get());
            if (damned) {
                player.displayClientMessage(Component.literal("Your soul feels damned..."), true);
            }
            // Prevents the message from being displayed every time the player dies when death stacking is disabled
            else if (dsh.isDeathStackingEnabled()) {
                player.displayClientMessage(Component.literal("Your soul feels heavier..."), true);
            }
        }
    }

    private static void applyRespawnedCloseToDeath(@NotNull Player player) {
        Optional<GlobalPos> lastDeathLocation = player.getLastDeathLocation();
        BlockPos blockPos = lastDeathLocation.map(GlobalPos::pos).orElse(null);
        if (blockPos == null) {
            return;
        }

        double distance = player.position().distanceTo(blockPos.getCenter());

        ConfigRegistry config = ConfigManager.getConfigValues();
        ResourceLocation dim = player.level().dimension().location();
        double maxDistance = config.active_respawnedNearLastDeath_distance.get(dim);

        if (distance > maxDistance || maxDistance == 0) {
            return;
        }

        float distancePercentage = 1f - (float) (distance / maxDistance);

        float sanityChange = config.active_respawnedNearLastDeath.get(dim).floatValue() * distancePercentage;
        PlayerSanityAPI.addSanity((ServerPlayer) player, sanityChange);
    }
}
