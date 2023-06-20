package croissantnova.sanitydim.sound;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry
{
    public static DeferredRegister<SoundEvent> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SanityMod.MODID);

    public static final RegistryObject<SoundEvent> INSANITY             = registerSoundEvent("insanity");
    public static final RegistryObject<SoundEvent> HEARTBEAT            = registerSoundEvent("heartbeat");
    public static final RegistryObject<SoundEvent> SWISH                = registerSoundEvent("swish");
    public static final RegistryObject<SoundEvent> FLOWERS_EQUIP        = registerSoundEvent("flowers_equip");
    public static final RegistryObject<SoundEvent> INNER_ENTITY_HURT    = registerSoundEvent("inner_entity_hurt");

    public static RegistryObject<SoundEvent> registerSoundEvent(String name)
    {
        return DEFERRED_REGISTER.register(name, () -> new SoundEvent(new ResourceLocation(SanityMod.MODID, name)));
    }

    public static void register(IEventBus eventBus)
    {
        DEFERRED_REGISTER.register(eventBus);
    }
}