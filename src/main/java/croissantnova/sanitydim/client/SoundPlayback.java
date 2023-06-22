package croissantnova.sanitydim.client;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.sound.HeartbeatSoundInstance;
import croissantnova.sanitydim.sound.InsanitySoundInstance;
import croissantnova.sanitydim.util.MathHelper;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import java.util.Random;

public class SoundPlayback
{
    private static InsanitySoundInstance insanity;
    private static HeartbeatSoundInstance heartbeat;
    private static int stepCd;
    private static int currentStepCd;
    private static int miscCd;
    private static SoundType currentStepSoundType;
    private static BlockPos currentStepBlockPos;
    private static final SoundEvent[] MISC_SOUNDS = new SoundEvent[]
    {
            SoundEvents.CREEPER_PRIMED,
            SoundEvents.SKELETON_AMBIENT,
            SoundEvents.SKELETON_STEP,
            SoundEvents.ZOMBIE_AMBIENT,
            SoundEvents.ZOMBIE_STEP,
            SoundEvents.ENDERMAN_AMBIENT,
            SoundEvents.HOSTILE_BIG_FALL,
            SoundEvents.CHEST_OPEN,
            SoundEvents.CHEST_CLOSE,
            SoundEvents.WOODEN_DOOR_OPEN,
            SoundEvents.WOODEN_TRAPDOOR_OPEN,
            SoundEvents.WOLF_GROWL,
    };
    public static final int STEP_COOLDOWN_MIN = 600;
    public static final int STEP_COOLDOWN_MAX = 1200;
    public static final int MISC_COOLDOWN_MIN = 800;
    public static final int MISC_COOLDOWN_MAX = 1400;
    public static final Random RAND = new Random();

    static
    {
        stepCd = RAND.nextInt(STEP_COOLDOWN_MAX - STEP_COOLDOWN_MIN) + STEP_COOLDOWN_MIN;
        miscCd = RAND.nextInt(MISC_COOLDOWN_MAX - MISC_COOLDOWN_MIN) + MISC_COOLDOWN_MIN;
    }

    private static BlockPos pickFakeStepPos(ClientPlayerEntity player)
    {
        // TODO: make it random
        Vector3d backVec = player.position().add(player.getLookAngle().multiply(-2f, -2f, -2f));
        return new BlockPos((int)backVec.x, (int)backVec.y, (int)backVec.z);
    }

    private static void playFakeSteps(ClientPlayerEntity player, ISanity cap)
    {
        if (--stepCd <= 0)
        {
            stepCd = RAND.nextInt(STEP_COOLDOWN_MAX - STEP_COOLDOWN_MIN) + STEP_COOLDOWN_MIN;
            if (cap.getSanity() >= .4f)
            {
                stepCd = (int)(stepCd * (1f - MathHelper.clampNorm((net.minecraft.util.math.MathHelper.inverseLerp(cap.getSanity(), .4f, .9f))) * .4f));
                currentStepSoundType = player.clientLevel.getBlockState(player.blockPosition()).getSoundType();
                currentStepBlockPos = pickFakeStepPos(player);
                currentStepCd = (RAND.nextInt(3) + 2) * 7;
            }
        }
        if (--miscCd <= 0)
        {
            miscCd = RAND.nextInt(MISC_COOLDOWN_MAX - MISC_COOLDOWN_MIN) + MISC_COOLDOWN_MIN;
            if (cap.getSanity() >= .4f)
            {
                miscCd = (int)(miscCd * (1f - MathHelper.clampNorm((net.minecraft.util.math.MathHelper.inverseLerp(cap.getSanity(), .4f, .8f))) * .5f));
                SoundEvent sound = MISC_SOUNDS[RAND.nextInt(MISC_SOUNDS.length)];
                BlockPos fakeStepPos = pickFakeStepPos(player);
                player.clientLevel.playLocalSound(
                        fakeStepPos.getX() + .5f,
                        fakeStepPos.getY() + .5f,
                        fakeStepPos.getZ() + .5f,
                        sound,
                        SoundCategory.AMBIENT,
                        1.0f,
                        .5f,
                        false
                );
            }
        }
        if (currentStepCd > 0)
        {
            if (currentStepCd % 7 == 0)
            {
                player.clientLevel.playLocalSound(
                        currentStepBlockPos.getX() + .5f,
                        currentStepBlockPos.getY() + .5f,
                        currentStepBlockPos.getZ() + .5f,
                        currentStepSoundType.getStepSound(),
                        SoundCategory.AMBIENT,
                        currentStepSoundType.getVolume() * .5f,
                        currentStepSoundType.getPitch(),
                        false);
            }
            currentStepCd--;
        }
    }

    public static void playSounds(ClientPlayerEntity player)
    {
        if (player == null || player.isCreative() || player.isSpectator() || !ConfigProxy.getPlaySounds(player.clientLevel.dimension().location()))
        {
            if (insanity != null)
                insanity.doStop();
            if (heartbeat != null)
                heartbeat.doStop();

            return;
        }

        Minecraft mc = Minecraft.getInstance();

        if (insanity == null || insanity.isStopped())
        {
            insanity = new InsanitySoundInstance();
            mc.getSoundManager().play(insanity);
        }
        if (heartbeat == null || heartbeat.isStopped())
        {
            heartbeat = new HeartbeatSoundInstance();
            mc.getSoundManager().play(heartbeat);
        }
        player.getCapability(SanityProvider.CAP).ifPresent(cap ->
        {
            insanity.factor = (float)MathHelper.clampNorm(net.minecraft.util.math.MathHelper.inverseLerp(cap.getSanity(), .55f, .8f));
            heartbeat.factor = (float)MathHelper.clampNorm(net.minecraft.util.math.MathHelper.inverseLerp(cap.getSanity(), .7f, .8f));
            insanity.setPos(player.getEyePosition(1f));
            heartbeat.setPos(player.getEyePosition(1f));

            playFakeSteps(player, cap);
        });
    }

    public static void onClientLevelLoad(ClientWorld level)
    {
        insanity = null;
        heartbeat = null;
    }
}