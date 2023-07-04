package croissantnova.sanitydim.client;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.sound.HeartbeatSoundInstance;
import croissantnova.sanitydim.sound.InsanitySoundInstance;
import croissantnova.sanitydim.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.Vec3;

public class SoundPlayback
{
    private static InsanitySoundInstance insanity;
    private static HeartbeatSoundInstance heartbeat;
    private static float insanityTarget;
    private static float heartbeatTarget;
    private static float insanityStart;
    private static float heartbeatStart;
    private static int insanityProgress;
    private static int heartbeatProgress;
    private static int stepCd;
    private static int currentStepCd;
    private static int miscCd;
    private static SoundType currentStepSoundType;
    private static BlockPos currentStepBlockPos;

    private static final int INSANITY_I11N_TIME = 20;
    private static final int HEARTBEAT_I11N_TIME = 60;
    private static final SoundEvent[] MISC_SOUNDS = new SoundEvent[]
    {
            SoundEvents.CREEPER_PRIMED,
            SoundEvents.TNT_PRIMED,
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
    public static final RandomSource RAND = RandomSource.create();

    static
    {
        stepCd = RAND.nextInt(STEP_COOLDOWN_MAX - STEP_COOLDOWN_MIN) + STEP_COOLDOWN_MIN;
        miscCd = RAND.nextInt(MISC_COOLDOWN_MAX - MISC_COOLDOWN_MIN) + MISC_COOLDOWN_MIN;
    }

    private static BlockPos pickFakeStepPos(LocalPlayer player)
    {
        // TODO: make it random
        Vec3 backVec = player.position().add(player.getLookAngle().multiply(-2f, -2f, -2f));
        return new BlockPos((int)backVec.x, (int)backVec.y, (int)backVec.z);
    }

    private static void playFakeSteps(LocalPlayer player, ISanity cap)
    {
        if (--stepCd <= 0)
        {
            stepCd = RAND.nextInt(STEP_COOLDOWN_MAX - STEP_COOLDOWN_MIN) + STEP_COOLDOWN_MIN;
            if (cap.getSanity() >= .4f)
            {
                stepCd = (int)(stepCd * (1f - MathHelper.clampNorm((Mth.inverseLerp(cap.getSanity(), .4f, .9f))) * .4f));
                currentStepSoundType = player.getBlockStateOn().getSoundType();
                currentStepBlockPos = pickFakeStepPos(player);
                currentStepCd = (RAND.nextInt(3) + 2) * 7;
            }
        }
        if (--miscCd <= 0)
        {
            miscCd = RAND.nextInt(MISC_COOLDOWN_MAX - MISC_COOLDOWN_MIN) + MISC_COOLDOWN_MIN;
            if (cap.getSanity() >= .4f)
            {
                miscCd = (int)(miscCd * (1f - MathHelper.clampNorm((Mth.inverseLerp(cap.getSanity(), .4f, .8f))) * .5f));
                SoundEvent sound = MISC_SOUNDS[RAND.nextInt(MISC_SOUNDS.length)];
                player.level().playLocalSound(
                        pickFakeStepPos(player),
                        sound,
                        SoundSource.AMBIENT,
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
                player.level().playLocalSound(
                        currentStepBlockPos,
                        currentStepSoundType.getStepSound(),
                        SoundSource.AMBIENT,
                        currentStepSoundType.getVolume() * .5f,
                        currentStepSoundType.getPitch(),
                        false);
            }
            currentStepCd--;
        }
    }

    public static void playSounds(LocalPlayer player)
    {
        if (player == null || player.isCreative() || player.isSpectator() || !ConfigProxy.getPlaySounds(player.level().dimension().location()))
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
            float insanityFactor = MathHelper.clampNorm(Mth.inverseLerp(cap.getSanity(), .55f, .8f));
            float heartbeatFactor = MathHelper.clampNorm(Mth.inverseLerp(cap.getSanity(), .7f, .8f));
            if (Math.abs(insanityFactor - insanityTarget) >= .05f)
            {
                insanityTarget = insanityFactor;
                insanityStart = insanity.factor;
                insanityProgress = 0;
            }
            else
            {
                if (insanityProgress <= INSANITY_I11N_TIME)
                    insanityTarget = insanityFactor;
                else
                    insanity.factor = insanityFactor;
            }
            if (Math.abs(heartbeatFactor - heartbeatTarget) >= .05f)
            {
                heartbeatTarget = heartbeatFactor;
                heartbeatStart = heartbeat.factor;
                heartbeatProgress = 0;
            }
            else
            {
                if (heartbeatProgress <= HEARTBEAT_I11N_TIME)
                    heartbeatTarget = heartbeatFactor;
                else
                    heartbeat.factor = heartbeatFactor;
            }
            if (insanityProgress <= INSANITY_I11N_TIME)
            {
                insanity.factor = Mth.lerp((float)insanityProgress / INSANITY_I11N_TIME, insanityStart, insanityTarget);
                insanityProgress++;
            }
            if (heartbeatProgress <= HEARTBEAT_I11N_TIME)
            {
                heartbeat.factor = Mth.lerp((float)heartbeatProgress / HEARTBEAT_I11N_TIME, heartbeatStart, heartbeatTarget);
                heartbeatProgress++;
            }

            insanity.setPos(player.getEyePosition());
            heartbeat.setPos(player.getEyePosition());

            playFakeSteps(player, cap);
        });
    }

    public static void onClientLevelLoad(ClientLevel level)
    {
        insanity = null;
        heartbeat = null;
    }
}