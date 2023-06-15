package croissantnova.sanitydim.item;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry
{
    public static final DeferredRegister<Item> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, SanityMod.MODID);

    public static final RegistryObject<Item> GARLAND = DEFERRED_REGISTER.register("garland", GarlandItem::new);

    public static void register(IEventBus eventBus)
    {
        DEFERRED_REGISTER.register(eventBus);
    }
}