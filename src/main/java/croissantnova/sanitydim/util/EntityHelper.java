package croissantnova.sanitydim.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntityHelper {

    private static final TagKey<EntityType<?>> INNER_ENTITY_DESPAWNABLE_TAG = TagKey.create(
            ForgeRegistries.ENTITY_TYPES.getRegistryKey(),
            new ResourceLocation("sanitydim", "inner_entity_despawnable")
    );

    public static boolean despawnsNearInnerEntities(@NotNull Entity entity) {
        return entity.getType().is(INNER_ENTITY_DESPAWNABLE_TAG);
    }

    public static List<? extends Entity> getEntities(Level level, AABB searchArea, EntityType<?> entityType) {
        return level.getEntities(
                (Entity) null, // necessary cast in order to avoid ambiguity between Entity and EntityTypeTest
                searchArea,
                entity -> entity.getType() == entityType
        );
    }
}
