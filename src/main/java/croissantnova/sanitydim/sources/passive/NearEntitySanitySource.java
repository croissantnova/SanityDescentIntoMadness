package croissantnova.sanitydim.sources.passive;

import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigManager;
import croissantnova.sanitydim.config.custom.PassiveSanityEntity;
import croissantnova.sanitydim.util.EntityHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NearEntitySanitySource implements IPassiveSanitySource {
    private final Set<ResourceLocation> failedEntities = new HashSet<>();

    @Override
    public float get(@NotNull ServerPlayer player, @NotNull ISanity cap, @NotNull ResourceLocation dim) {
        return new SanityCalculator(player, dim).calculate();
    }

    private class SanityCalculator {
        private final ServerPlayer player;
        private final ResourceLocation dim;
        private float sanityChange = 0;

        public SanityCalculator(ServerPlayer player, ResourceLocation dim) {
            this.player = player;
            this.dim = dim;
        }

        public float calculate() {
            List<PassiveSanityEntity> passiveSanityEntityList = ConfigManager.getConfigValues().passiveConfig.sanityEntities.get(dim);

            passiveSanityEntityList.forEach(this::applySanityFrom);

            return sanityChange;
        }

        private void applySanityFrom(PassiveSanityEntity entityConfig) {
            EntityType<?> entityType = entityConfig.getEntityType();
            if (entityType == null) {
                handleFailedEntity(entityConfig);
                return;
            }

            AABB searchArea = player.getBoundingBox().inflate(entityConfig.radius());
            List<? extends Entity> nearbyEntities = EntityHelper.getEntities(player.level(), searchArea, entityType);
            sanityChange += nearbyEntities.size() * entityConfig.sanity();
        }

        private void handleFailedEntity(@NotNull PassiveSanityEntity entityConfig) {
            if (!failedEntities.contains(entityConfig.id())) {
                failedEntities.add(entityConfig.id());
                SanityMod.LOGGER.error("Entity with ID {} is not registered, check your passive sanity config!", entityConfig.id());
            }
        }

    }
}
