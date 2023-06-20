package croissantnova.sanitydim.entity;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry
{
    public static final DeferredRegister<EntityType<?>> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, SanityMod.MODID);

//    public static final RegistryObject<EntityType<CreepingNightmare>> CREEPING_NIGHTMARE
//            = DEFERRED_REGISTER.register(
//            "creeping_nightmare",
//            () -> EntityType.Builder.of(CreepingNightmare::new, MobCategory.CREATURE).sized(1.3f, 2.9f).fireImmune().build("creeping_nightmare"));
//
//    public static final RegistryObject<EntityType<SneakingTerror>> SNEAKING_TERROR
//            = DEFERRED_REGISTER.register("sneaking_terror",
//            () -> EntityType.Builder.of(SneakingTerror::new, MobCategory.CREATURE).sized(1.3f, 2.9f).fireImmune().build("sneaking_terror"));

    public static final RegistryObject<EntityType<RottingStalker>> ROTTING_STALKER
            = DEFERRED_REGISTER.register("rotting_stalker",
            () -> EntityType.Builder.of(RottingStalker::new, MobCategory.MONSTER).sized(1f, 2.9f).fireImmune().build("rotting_stalker"));

    /*public static final RegistryObject<EntityType<ShadeChomper>> SHADE_CHOMPER
            = DEFERRED_REGISTER.register("shade_chomper",
            () -> EntityType.Builder.of(ShadeChomper::new, MobCategory.CREATURE).sized(1.0f, 2.0f).fireImmune().build("shade_chomper"));

     */
    public static void register(IEventBus eventBus)
    {
        DEFERRED_REGISTER.register(eventBus);
    }
}