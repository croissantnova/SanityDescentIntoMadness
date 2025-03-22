package croissantnova.sanitydim.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import croissantnova.sanitydim.api.PlayerSanityAPI;
import croissantnova.sanitydim.capability.InnerEntityCapImplProvider;
import croissantnova.sanitydim.entity.NightmareEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public class NightmareEntityRenderer<T extends NightmareEntity & GeoAnimatable> extends GeoEntityRenderer<T> {
    private final Minecraft minecraftClientInstance = Minecraft.getInstance();
    private final AtomicBoolean shouldRender = new AtomicBoolean(false);
    private final AtomicBoolean isTargetMe = new AtomicBoolean(false);

    public NightmareEntityRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
    }

    public boolean shouldRender(T entity) {
        LocalPlayer player = minecraftClientInstance.player;
        return player != null
                && entity != null
                && (PlayerSanityAPI.canSeeNightmares(player) || isTargeting(entity, player));
    }

    private boolean isTargeting(T entity, Player player) {
        return entity.getCapability(InnerEntityCapImplProvider.CAP)
                .map(iec -> iec.getPlayerTargetUUID()
                        .filter(uuid -> uuid.equals(player.getUUID()))
                        .isPresent()
                ).orElse(false);
    }

    @Override
    public void render(@NotNull T entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (shouldRender(entity)) {
            super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        }
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource,
                                    float partialTick) {
        return RenderType.entityTranslucent(getTextureLocation(animatable), false);
    }
}
