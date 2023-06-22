package croissantnova.sanitydim.sound;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;

public class SwishSoundInstance extends TickableSound
{
    public SwishSoundInstance()
    {
        super(SoundRegistry.SWISH.get(), SoundCategory.AMBIENT);
    }

    @Override
    public void tick()
    {

    }
}