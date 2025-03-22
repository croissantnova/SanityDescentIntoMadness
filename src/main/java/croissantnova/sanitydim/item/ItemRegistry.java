package croissantnova.sanitydim.item;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry
{
    public static final DeferredRegister<Item> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, SanityMod.MOD_ID);

    public static final RegistryObject<Item> GARLAND = DEFERRED_REGISTER.register("garland", GarlandItem::new);

    public static final RegistryObject<Item> NIGHTMARE_FUEL = DEFERRED_REGISTER.register("nightmare_fuel", NightmareFuelItem::new);
    public static final RegistryObject<Item> NIGHTMARE_FLESH = DEFERRED_REGISTER.register("nightmare_flesh", NightmareFleshItem::new);
    public static final RegistryObject<Item> NIGHTMARE_LEATHER = DEFERRED_REGISTER.register("nightmare_leather", NightmareLeatherItem::new);

    public static void register(IEventBus eventBus)
    {
        DEFERRED_REGISTER.register(eventBus);
    }
}