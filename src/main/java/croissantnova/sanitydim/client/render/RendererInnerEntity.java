package croissantnova.sanitydim.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import croissantnova.sanitydim.capability.InnerEntityCapImplProvider;
import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.entity.InnerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public class RendererInnerEntity<T extends InnerEntity & GeoAnimatable> extends GeoEntityRenderer<T>
{
    private final Minecraft m_mc = Minecraft.getInstance();
    private final AtomicBoolean m_shouldRender = new AtomicBoolean(false);
    private final AtomicBoolean m_isTargetMe = new AtomicBoolean(false);

    public RendererInnerEntity(EntityRendererProvider.Context renderManager, GeoModel<T> model)
    {
        super(renderManager, model);
    }

    public boolean shouldRender(T entity)
    {
        if (m_mc.player == null || entity == null)
            return false;

        if (ConfigProxy.getSaneSeeInnerEntities(m_mc.player.level().dimension().location()) || m_mc.player.isCreative() || m_mc.player.isSpectator())
            return true;

        entity.getCapability(InnerEntityCapImplProvider.CAP).ifPresent(iec ->
        {
            m_isTargetMe.set(iec.getPlayerTargetUUID() != null && iec.getPlayerTargetUUID().equals(m_mc.player.getUUID()));
        });
        if (m_isTargetMe.get())
            return true;

        m_mc.player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            m_shouldRender.set(s.getSanity() >= .6f);
        });

        return m_shouldRender.get();
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
    {
        if (shouldRender(entity))
            super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource,
                                    float partialTick)
    {
        return RenderType.entityTranslucent(getTextureLocation(animatable), false);
    }
}
