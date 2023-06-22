package croissantnova.sanitydim;

import java.util.*;
import java.util.function.Function;
import croissantnova.sanitydim.capability.*;
import croissantnova.sanitydim.config.ConfigItem;
import croissantnova.sanitydim.config.ConfigItemCategory;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.entity.EntityRegistry;
import croissantnova.sanitydim.entity.InnerEntity;
import croissantnova.sanitydim.entity.InnerEntitySpawner;
import croissantnova.sanitydim.item.ItemRegistry;
import croissantnova.sanitydim.net.InnerEntityCapImplPacket;
import croissantnova.sanitydim.net.PacketHandler;
import croissantnova.sanitydim.net.SanityPacket;
import croissantnova.sanitydim.passive.*;
import croissantnova.sanitydim.util.MathHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import javax.annotation.Nonnull;

public final class SanityProcessor
{
    private static int garlandTimer;
    private static final Random RAND = new Random();

    public static final int MAX_GARLAND_TIMER = 60;
    public static final float SANITY_TARGET_THRESHOLD = .87f;
    public static final List<IPassiveSanitySource> PASSIVE_SANITY_SOURCES = Arrays.asList(
            new Passive(),
            new InWaterOrRain(),
            new Hungry(),
            new EnderManAnger(),
            new Pet(),
            new Monster(),
            new Darkness(),
            new PassiveBlocks(),
            new PlayerCompany(),
            new Jukebox()
    );

    private SanityProcessor() {}

    private static float calcPassive(ServerPlayerEntity player, ISanity sanity)
    {
        ResourceLocation dim = player.level.dimension().location();
        float passive = 0;

        for (IPassiveSanitySource pss : PASSIVE_SANITY_SOURCES)
        {
            float val = pss.get(player, sanity, dim);
            val *= getSanityMultiplier(player, val);
            passive += val;
        }

        garlandTimer--;
        ItemStack headItem = player.getItemBySlot(EquipmentSlotType.HEAD);
        if (headItem.getItem() == ItemRegistry.GARLAND.get())
        {
            // FIXME: garland is hardcoded
            passive -= .00005 * ConfigProxy.getPosMul(dim);
            if (garlandTimer <= 0)
                headItem.hurtAndBreak(player.isInWaterOrRain() ? 2 : 1, player, ent -> {});
        }
        if (garlandTimer <= 0)
            garlandTimer = MAX_GARLAND_TIMER;

        return passive;
    }

    private static void shareSanity(ServerPlayerEntity player, Sanity cap)
    {
        if (cap.getDirty())
        {
            SanityPacket packet = new SanityPacket(cap);
            PacketHandler.CHANNEL_INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
			cap.setDirty(false);
        }
    }

    private static void handlePlayerAte(ServerPlayerEntity player, ItemStack itemStack)
    {
        handleActiveSourceForPlayer(
                player,
                ActiveSanitySources.EATING,
                ConfigProxy::getEatingCooldown,
                dim -> itemStack.getItem().getFoodProperties().getNutrition() * ConfigProxy.getEating(dim));
    }

    public static float getGarlandMultiplier(ServerPlayerEntity player)
    {
        return player.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ItemRegistry.GARLAND.get() ? .92f : 1.0f;
    }

    public static float getSanityMultiplier(ServerPlayerEntity player, float value)
    {
        ResourceLocation dim = player.level.dimension().location();
        return value >= 0 ? ConfigProxy.getNegMul(dim) * getGarlandMultiplier(player) : ConfigProxy.getPosMul(dim);
    }

    public static void addSanity(@Nonnull ISanity sanity, float value, @Nonnull ServerPlayerEntity player)
    {
        if (value == 0.0f)
            return;

        sanity.setSanity(sanity.getSanity() + value * getSanityMultiplier(player, value));
    }

    public static void tickPlayer(final ServerPlayerEntity player)
    {
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            ResourceLocation dim = player.level.dimension().location();

            float passive = calcPassive(player, s);
            float snapshot = s.getSanity();
            // passive premultiplied so no need for Sanity#addSanity
            s.setSanity(s.getSanity() + passive);
            if (s instanceof IPassiveSanity)
            {
                ((IPassiveSanity)s).setPassiveIncrease(snapshot != s.getSanity() ? passive : 0);
            }
            if (s instanceof IPersistentSanity)
            {
                IPersistentSanity ps = (IPersistentSanity)s;

                int[] cds = ps.getActiveSourcesCooldowns();
                for (int i = 0; i < cds.length; ++i)
                    cds[i] = net.minecraft.util.math.MathHelper.clamp(cds[i] - 1, 0, Integer.MAX_VALUE);

                Map<Integer, Integer> itemCds = ps.getItemCooldowns();
                for (Iterator<Map.Entry<Integer, Integer>> it = itemCds.entrySet().iterator(); it.hasNext();)
                {
                    Map.Entry<Integer, Integer> entry = it.next();
                    itemCds.put(entry.getKey(), itemCds.get(entry.getKey()) - 1);
                    if (itemCds.get(entry.getKey()) <= 0)
                        it.remove();
                }
            }
            if (s instanceof Sanity)
                shareSanity(player, (Sanity)s);
        });
        InnerEntitySpawner.trySpawnForPlayer(player);
    }

    public static void tickLevel(final ServerWorld level)
    {
        for (Entity ent : level.getEntities(EntityRegistry.ROTTING_STALKER.get(), e -> true))
        {
            if (ent instanceof InnerEntity)
            {
                InnerEntity ie = (InnerEntity)ent;
                ie.getCapability(InnerEntityCapImplProvider.CAP).ifPresent(iec ->
                {
                    if (iec instanceof InnerEntityCapImpl)
                    {
                        InnerEntityCapImpl ieci = (InnerEntityCapImpl)iec;

                        if (ieci.hasTarget() && ie.getTarget() == null || !ieci.hasTarget() && ie.getTarget() != null)
                            ieci.setHasTarget(ie.getTarget() != null);

                        if (ieci.getDirty())
                        {
                            InnerEntityCapImplPacket packet = new InnerEntityCapImplPacket(ieci);
                            packet.m_id = ent.getId();
                            PacketHandler.CHANNEL_INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> ent), packet);
                        }
                    }
                });
            }
        }
    }

    public static List<ServerPlayerEntity> getInsanePlayersInArea(final World levelIn, BlockPos center, int blockRadius)
    {
        if (levelIn == null || center == null)
            return null;
        List<ServerPlayerEntity> list = new ArrayList<ServerPlayerEntity>();
        for (ServerPlayerEntity player : levelIn.getEntitiesOfClass(
                ServerPlayerEntity.class,
                new AxisAlignedBB(center.offset(blockRadius, blockRadius, blockRadius), center.offset(-blockRadius, -blockRadius, -blockRadius))))
        {
            player.getCapability(SanityProvider.CAP).ifPresent(s ->
            {
                if (s.getSanity() >= SANITY_TARGET_THRESHOLD)
                    list.add(player);
            });
        }
        return list;
    }

    public static PlayerEntity getMostInsanePlayer(final World levelIn)
    {
        if (levelIn == null)
            return null;

        PlayerEntity toReturn = null;
        float maxSanity = Float.MIN_VALUE;
        for (PlayerEntity player : levelIn.players())
        {
            if (player.isCreative() || player.isSpectator())
                continue;
            ISanity s = player.getCapability(SanityProvider.CAP).orElse(null);
            if (s == null)
                continue;
            float sanity = s.getSanity();
            if (sanity >= SANITY_TARGET_THRESHOLD && sanity > maxSanity)
            {
                maxSanity = s.getSanity();
                toReturn = player;
            }
        }
        return toReturn;
    }

    public static void handleActiveSourceForPlayer(
            ServerPlayerEntity player,
            int id,
            Function<ResourceLocation, Integer> cdSupplier,
            Function<ResourceLocation, Float> sanitySupplier)
    {
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            ResourceLocation dimLoc = player.level.dimension().location();
            int cd = cdSupplier.apply(dimLoc);

            if (s instanceof IPersistentSanity && cd > 0.0f)
            {
                IPersistentSanity ps = (IPersistentSanity)s;
                int timePassed = cd - ps.getActiveSourcesCooldowns()[id];
                addSanity(s, sanitySupplier.apply(dimLoc) * MathHelper.clampNorm((float) timePassed / cd), player);
//                s.setSanity(s.getSanity() + sanitySupplier.apply(dimLoc) * MathHelper.clampNorm((float) timePassed / cd));
                ps.getActiveSourcesCooldowns()[id] = cd;
            }
            else
                addSanity(s, sanitySupplier.apply(dimLoc), player);
//                s.setSanity(s.getSanity() + sanitySupplier.apply(dimLoc));
        });
    }

    public static void handlePlayerHurt(ServerPlayerEntity player, float amount)
    {
        if (player == null || player.isCreative() || player.isSpectator() || amount <= 0)
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            ResourceLocation dimLoc = player.level.dimension().location();
            addSanity(s, amount * ConfigProxy.getHurtRatio(dimLoc), player);
//            s.setSanity(s.getSanity() + amount * ConfigProxy.getHurtRatio(player.level.dimension().location()));
        });
    }

    public static void handlePlayerHurtAnimal(ServerPlayerEntity player, AnimalEntity animal, float amount)
    {
        if (player == null || player.isCreative() || player.isSpectator() || amount <= 0)
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            ResourceLocation dimLoc = player.level.dimension().location();
            addSanity(s, amount * ConfigProxy.getAnimalHurtRatio(player.level.dimension().location()) * (animal.isBaby() ? 2.0f : 1.0f), player);
//            s.setSanity(s.getSanity() + amount * ConfigProxy.getAnimalHurtRatio(player.level.dimension().location()) * (animal.isBaby() ? 2.0f : 1.0f));
        });
    }

    public static void handlePlayerPetDeath(ServerPlayerEntity player, TameableEntity pet)
    {
        if (player == null || player.isCreative() || player.isSpectator() || pet.isOwnedBy(player))
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            ResourceLocation dimLoc = player.level.dimension().location();
            addSanity(s, ConfigProxy.getPetDeath(player.level.dimension().location()), player);
//            s.setSanity(s.getSanity() + ConfigProxy.getPetDeath(player.level.dimension().location()));
        });
    }

    public static void handlePlayerEnderManAngered(ServerPlayerEntity player)
    {
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            if (s instanceof IPersistentSanity && ((IPersistentSanity)s).getEnderManAngerTimer() <= 0)
            {
                ((IPersistentSanity)s).setEnderManAngerTimer(100);
            }
        });
    }

    public static void handlePlayerGotAdvancement(ServerPlayerEntity player, Advancement adv)
    {
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        if (adv.getDisplay() == null || !adv.getDisplay().shouldAnnounceChat())
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            ResourceLocation dimLoc = player.level.dimension().location();
            addSanity(s, ConfigProxy.getAdvancement(dimLoc), player);
//            s.setSanity(s.getSanity() + ConfigProxy.getAdvancement(player.level.dimension().location()));
        });
    }

    public static void handlePlayerBredAnimals(ServerPlayerEntity player)
    {
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        handleActiveSourceForPlayer(player, ActiveSanitySources.BREEDING_ANIMALS, ConfigProxy::getAnimalBreedingCooldown, ConfigProxy::getAnimalBreeding);
    }

    public static void handlePlayerTradedWithVillager(ServerPlayerEntity player)
    {
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        handleActiveSourceForPlayer(player, ActiveSanitySources.VILLAGER_TRADE, ConfigProxy::getVillagerTradeCooldown, ConfigProxy::getVillagerTrade);
    }

    public static void handlePlayerUsedShears(ServerPlayerEntity player)
    {
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        handleActiveSourceForPlayer(player, ActiveSanitySources.SHEARING, ConfigProxy::getShearingCooldown, ConfigProxy::getShearing);
    }

    public static void handlePlayerSpawnedChicken(ServerPlayerEntity player)
    {
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        handleActiveSourceForPlayer(player, ActiveSanitySources.SPAWNING_BABY_CHICKEN, ConfigProxy::getBabyChickenSpawningCooldown, ConfigProxy::getBabyChickenSpawning);
    }

    public static void handlePlayerUsedItem(ServerPlayerEntity player, ItemStack itemStack)
    {
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            if (s instanceof IPersistentSanity)
            {
                IPersistentSanity ps = (IPersistentSanity)s;
                ResourceLocation dim = player.level.dimension().location();
                Map<Integer, Integer> itemCds = ps.getItemCooldowns();

                for (ConfigItem citem : ConfigProxy.getItems(dim))
                {
                    if (!(itemStack.getItem() == ForgeRegistries.ITEMS.getValue(citem.m_name)))
                        continue;

                    if (!ConfigProxy.getIdToItemCat(dim).containsKey(citem.m_cat))
                    {
                        SanityMod.LOGGER.warn("player " + player.getDisplayName().getString() + " used " + citem.m_name + " from category " + citem.m_cat + ", but no such category is present");
                        return;
                    }

                    ConfigItemCategory cat = ConfigProxy.getIdToItemCat(dim).get(citem.m_cat);
                    if (cat.m_cd <= 0)
                    {
                        addSanity(s, citem.m_sanity, player);
                        return;
                    }

                    if (!itemCds.containsKey(citem.m_cat) || itemCds.get(citem.m_cat) <= 0)
                    {
                        addSanity(s, citem.m_sanity, player);
                    }
                    else
                    {
                        int timePassed = cat.m_cd - itemCds.get(citem.m_cat);
                        addSanity(s, citem.m_sanity * MathHelper.clampNorm((float)timePassed / cat.m_cd), player);
                    }

                    itemCds.put(citem.m_cat, cat.m_cd);

                    return;
                }
            }

            if (itemStack.isEdible())
                handlePlayerAte(player, itemStack);
        });
    }

    public static void handlePlayerFishedItem(ServerPlayerEntity player)
    {
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        handleActiveSourceForPlayer(
                player,
                ActiveSanitySources.FISHING,
                ConfigProxy::getFishingCooldown,
                ConfigProxy::getFishing
        );
    }
}