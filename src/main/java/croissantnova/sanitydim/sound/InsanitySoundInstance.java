package croissantnova.sanitydim.sound;

import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;

public final class InsanitySoundInstance extends TickableSound
{
    public float factor = 0f;

    public InsanitySoundInstance()
    {
        super(SoundRegistry.INSANITY.get(), SoundCategory.AMBIENT);
        volume = 0;
        delay = 0;
        looping = true;
    }

    @Override
    public void tick()
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null)
            volume = factor * ConfigProxy.getInsanityVolume(mc.player.level.dimension().location());
        else
            volume = 0;
    }

    public void setPos(Vector3d pos)
    {
        x = pos.x;
        y = pos.y;
        z = pos.z;
    }

    public boolean canStartSilent()
    {
        return true;
    }

    public void doStop()
    {
        stop();
    }
}