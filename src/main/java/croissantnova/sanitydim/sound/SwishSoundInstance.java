package croissantnova.sanitydim.sound;

import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.sounds.SoundSource;

public class SwishSoundInstance extends AbstractSoundInstance
{
    public SwishSoundInstance()
    {
        super(SoundRegistry.SWISH.get(), SoundSource.AMBIENT);
    }
}