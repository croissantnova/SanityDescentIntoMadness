package croissantnova.sanitydim.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import org.jetbrains.annotations.NotNull;

public final class PlayerHelper {

    private PlayerHelper() {}

    private static final RandomSource rng = RandomSource.create();

    public static @NotNull ResourceLocation getDim(@NotNull Player player) {
        return player.level().dimension().location();
    }

    @SuppressWarnings("deprecation")
    public static float getSolarLightLevel(@NotNull Player player) {
        return player.getLightLevelDependentMagicValue();
    }

    // Booleans

    public static boolean isHoldingSword(@NotNull Player player) {
        ItemStack mainHandItem = player.getMainHandItem();
        return mainHandItem.getItem() instanceof SwordItem;
    }

    public static boolean isMortal(@NotNull Player player) {
        return !isImmortal(player);
    }

    public static boolean isImmortal(@NotNull Player player) {
        return player.isCreative() || player.isSpectator();
    }

    /**
     * Uses similar code to {@code net.minecraft.world.entity.Mob#isSunBurnTick()}
     */
    public static boolean isPlayerInSunlight(@NotNull Player player) {
        if (player.level().isDay() && !player.level().isClientSide) {
            float solarLightLevel = PlayerHelper.getSolarLightLevel(player);
            BlockPos blockpos = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ());
            
            return solarLightLevel > 0.5F && player.level().canSeeSky(blockpos);
        }
        return false;
    }
}
