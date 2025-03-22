package croissantnova.sanitydim.sound;

import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SoundPacketBuilder {

    private static final RandomSource rng = RandomSource.create();

    private SoundEvent soundEvent;
    private SoundSource soundSource;
    private Vec3 vec3;
    private float volume;
    private float pitch;
    private ClientboundSoundPacket soundPacket;

    private SoundPacketBuilder() {}

    public static InitializedStep builder() {
        return new InitializedStep();
    }

    public static class InitializedStep {
        public SoundEventSetStep setSoundEvent(SoundEvent soundEvent) {
            SoundPacketBuilder builder = new SoundPacketBuilder();
            builder.soundEvent = soundEvent;
            return new SoundEventSetStep(builder);
        }
    }

    public static class SoundEventSetStep {
        private final SoundPacketBuilder builder;

        private SoundEventSetStep(SoundPacketBuilder builder) {
            this.builder = builder;
        }

        public SoundSourceSetStep setSoundSource(SoundSource soundSource) {
            builder.soundSource = soundSource;
            return new SoundSourceSetStep(builder);
        }
    }

    public static class SoundSourceSetStep {
        private final SoundPacketBuilder builder;

        private SoundSourceSetStep(SoundPacketBuilder builder) {
            this.builder = builder;
        }

        public Vec3SetStep setVec3(Vec3 vec3) {
            builder.vec3 = vec3;
            return new Vec3SetStep(builder);
        }
    }

    public static class Vec3SetStep {
        private final SoundPacketBuilder builder;

        private Vec3SetStep(SoundPacketBuilder builder) {
            this.builder = builder;
        }

        public VolumeSetStep setVolume(float volume) {
            builder.volume = volume;
            return new VolumeSetStep(builder);
        }
    }

    public static class VolumeSetStep {
        private final SoundPacketBuilder builder;

        private VolumeSetStep(SoundPacketBuilder builder) {
            this.builder = builder;
        }

        public PitchSetStep setPitch(float pitch) {
            builder.pitch = pitch;

            Holder<SoundEvent> soundEventHolder = Holder.direct(SoundEvent.createVariableRangeEvent(builder.soundEvent.getLocation()));
            builder.soundPacket = new ClientboundSoundPacket(
                    soundEventHolder,
                    builder.soundSource,
                    builder.vec3.x,
                    builder.vec3.y,
                    builder.vec3.z,
                    builder.volume,
                    pitch,
                    rng.nextLong()
            );

            return new PitchSetStep(builder);
        }
    }

    public static class PitchSetStep {
        private final SoundPacketBuilder builder;

        private PitchSetStep(SoundPacketBuilder builder) {
            this.builder = builder;
        }

        public void sendPacket(@NotNull ServerPlayer player) {
            player.connection.send(builder.soundPacket);
        }
    }

}
