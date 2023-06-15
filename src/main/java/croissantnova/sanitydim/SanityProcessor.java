package croissantnova.sanitydim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import croissantnova.sanitydim.capability.*;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.entity.InnerEntity;
import croissantnova.sanitydim.item.ItemRegistry;
import croissantnova.sanitydim.net.InnerEntityCapImplPacket;
import croissantnova.sanitydim.net.PacketHandler;
import croissantnova.sanitydim.net.SanityPacket;
import croissantnova.sanitydim.passive.*;
import croissantnova.sanitydim.util.MathHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

public final class SanityProcessor
{
    private static int garlandTimer;
    private static final int MAX_GARLAND_TIMER = 60;
    private static final RandomSource RAND = RandomSource.create();

    public static final float INNER_ENTITY_SPAWN_THRESHOLD = .8f;
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
            new PlayerCompany()
    );

    private SanityProcessor() {}

    private static float calcPassive(ServerPlayer player, ISanity sanity)
    {
        ResourceLocation dim = player.level.dimension().location();
        float passive = 0;

        for (IPassiveSanitySource pss : PASSIVE_SANITY_SOURCES)
        {
            float val = pss.get(player, sanity, dim);
            val *= val >= 0 ? ConfigProxy.getNegMul(dim) : ConfigProxy.getPosMul(dim);
            passive += val;
        }

        garlandTimer--;
        ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);
        if (headItem.is(ItemRegistry.GARLAND.get()))
        {
            // FIXME: garland is hardcoded
            passive -= .00005 * ConfigProxy.getPosMul(dim);
            if (garlandTimer <= 0)
                headItem.hurtAndBreak(1, player, ent -> {});
        }
        if (garlandTimer <= 0)
            garlandTimer = MAX_GARLAND_TIMER;

        return passive;
    }

    private static void shareSanity(ServerPlayer player, Sanity cap)
    {
        if (cap.getDirty())
        {
            SanityPacket packet = new SanityPacket(cap);
            PacketHandler.CHANNEL_INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
			cap.setDirty(false);
        }
    }

    public static void tickPlayer(final ServerPlayer player)
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
            if (s instanceof IPassiveSanity ps)
            {
                ps.setPassiveIncrease(snapshot != s.getSanity() ? passive : 0);
            }
            if (s instanceof IPersistentSanity ps)
            {
                int[] cds = ps.getActiveSourcesCds();
                for (int i = 0; i < cds.length; ++i)
                    cds[i] = Mth.clamp(cds[i] - 1, 0, Integer.MAX_VALUE);
            }
            if (s instanceof Sanity)
                shareSanity(player, (Sanity)s);
        });
        InnerEntitySpawner.trySpawnForPlayer(player, INNER_ENTITY_SPAWN_THRESHOLD);
    }

    public static void tickLevel(final ServerLevel level)
    {
        for (Entity ent : level.getEntities().getAll())
        {
            if (ent instanceof InnerEntity ie)
            {
                ie.getCapability(InnerEntityCapImplProvider.CAP).ifPresent(iec ->
                {
                    if (iec instanceof InnerEntityCapImpl ieci)
                    {
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

    public static List<Player> getInsanePlayersInArea(final Level levelIn, BlockPos center, int blockRadius)
    {
        if (levelIn == null || center == null)
            return null;
        List<Player> list = new ArrayList<Player>();
        for (Player player : levelIn.getEntitiesOfClass(
                Player.class,
                new AABB(center.offset(blockRadius, blockRadius, blockRadius), center.offset(-blockRadius, -blockRadius, -blockRadius))))
        {
            player.getCapability(SanityProvider.CAP).ifPresent(s ->
            {
                if (s.getSanity() >= SANITY_TARGET_THRESHOLD)
                    list.add(player);
            });
        }
        return list;
    }

    public static Player getMostInsanePlayer(final Level levelIn)
    {
        if (levelIn == null)
            return null;

        Player toReturn = null;
        float maxSanity = Float.MIN_VALUE;
        for (Player player : levelIn.players())
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
            ServerPlayer player,
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

            if (s instanceof IPersistentSanity ps && cd > 0.0f)
            {
                int timePassed = cd - ps.getActiveSourcesCds()[id];
                Sanity.addSanity(s, sanitySupplier.apply(dimLoc) * MathHelper.clampNorm((float) timePassed / cd), dimLoc);
//                s.setSanity(s.getSanity() + sanitySupplier.apply(dimLoc) * MathHelper.clampNorm((float) timePassed / cd));
                ps.getActiveSourcesCds()[id] = cd;
            }
            else
                Sanity.addSanity(s, sanitySupplier.apply(dimLoc), dimLoc);
//                s.setSanity(s.getSanity() + sanitySupplier.apply(dimLoc));
        });
    }

    public static void handlePlayerHurt(ServerPlayer player, float amount)
    {
        if (player == null || player.isCreative() || player.isSpectator() || amount <= 0)
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            ResourceLocation dimLoc = player.level.dimension().location();
            Sanity.addSanity(s, amount * ConfigProxy.getHurtRatio(dimLoc), dimLoc);
//            s.setSanity(s.getSanity() + amount * ConfigProxy.getHurtRatio(player.level.dimension().location()));
        });
    }

    public static void handlePlayerHurtAnimal(ServerPlayer player, Animal animal, float amount)
    {
        if (player == null || player.isCreative() || player.isSpectator() || amount <= 0)
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            ResourceLocation dimLoc = player.level.dimension().location();
            Sanity.addSanity(s, amount * ConfigProxy.getAnimalHurtRatio(player.level.dimension().location()) * (animal.isBaby() ? 2.0f : 1.0f), dimLoc);
//            s.setSanity(s.getSanity() + amount * ConfigProxy.getAnimalHurtRatio(player.level.dimension().location()) * (animal.isBaby() ? 2.0f : 1.0f));
        });
    }

    public static void handlePlayerPetDeath(ServerPlayer player, TamableAnimal pet)
    {
        if (player == null || player.isCreative() || player.isSpectator() || pet.isOwnedBy(player))
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            ResourceLocation dimLoc = player.level.dimension().location();
            Sanity.addSanity(s, ConfigProxy.getPetDeath(player.level.dimension().location()), dimLoc);
//            s.setSanity(s.getSanity() + ConfigProxy.getPetDeath(player.level.dimension().location()));
        });
    }

    public static void handlePlayerEnderManAngered(ServerPlayer player)
    {
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            if (s instanceof IPersistentSanity ps && ps.getEnderManAngerTimer() <= 0)
            {
                ps.setEnderManAngerTimer(100);
            }
        });
    }

    public static void handlePlayerGotAdvancement(ServerPlayer player, Advancement adv)
    {
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        if (adv.getDisplay() == null || !adv.getDisplay().shouldAnnounceChat())
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            ResourceLocation dimLoc = player.level.dimension().location();
            Sanity.addSanity(s, ConfigProxy.getAdvancement(dimLoc), dimLoc);
//            s.setSanity(s.getSanity() + ConfigProxy.getAdvancement(player.level.dimension().location()));
        });
    }

    public static void handlePlayerBredAnimals(ServerPlayer player)
    {
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        handleActiveSourceForPlayer(player, ActiveSanitySources.BREEDING_ANIMALS, ConfigProxy::getAnimalBreedingCooldown, ConfigProxy::getAnimalBreeding);
    }

    public static void handlePlayerTradedWithVillager(ServerPlayer player)
    {
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        handleActiveSourceForPlayer(player, ActiveSanitySources.VILLAGER_TRADE, ConfigProxy::getVillagerTradeCooldown, ConfigProxy::getVillagerTrade);
    }

    public static void handlePlayerUsedShears(ServerPlayer player)
    {
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        handleActiveSourceForPlayer(player, ActiveSanitySources.SHEARING, ConfigProxy::getShearingCooldown, ConfigProxy::getShearing);
    }

    public static void handlePlayerSpawnedChicken(ServerPlayer player)
    {
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        handleActiveSourceForPlayer(player, ActiveSanitySources.SPAWNING_BABY_CHICKEN, ConfigProxy::getBabyChickenSpawningCooldown, ConfigProxy::getBabyChickenSpawning);
    }

    public static void handlePlayerAte(ServerPlayer player, ItemStack itemStack)
    {
        if (player == null || player.isCreative() || player.isSpectator() || itemStack.getFoodProperties(player) == null)
            return;

        handleActiveSourceForPlayer(
                player,
                ActiveSanitySources.EATING,
                ConfigProxy::getEatingCooldown,
                dim -> itemStack.getFoodProperties(player).getNutrition() * ConfigProxy.getEating(dim));
    }
}