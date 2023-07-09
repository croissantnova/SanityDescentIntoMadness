package croissantnova.sanitydim.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import croissantnova.sanitydim.capability.SanityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;

public abstract class BlackoutEyesLayer<T extends LivingEntity, M extends EntityModel<T>> extends EyesLayer<T, M>
{
    private boolean m_shouldRender;
    private final RenderType m_renderType;

    public BlackoutEyesLayer(RenderLayerParent<T, M> pRenderer)
    {
        super(pRenderer);
        m_renderType = RenderType.eyes(getEyesLocation());
    }

    public boolean shouldRender()
    {
        Player player = Minecraft.getInstance().player;
        if (player == null || player.isCreative() || player.isSpectator())
            return false;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            m_shouldRender = s.getSanity() >= Blackout.THRESHOLD;
        });

        return m_shouldRender;
    }

    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch)
    {
        if (shouldRender())
            super.render(pMatrixStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTicks, pAgeInTicks, pNetHeadYaw, pHeadPitch);
    }

    @Override
    public @Nonnull RenderType renderType()
    {
        return m_renderType;
    }

    @Nonnull
    public abstract ResourceLocation getEyesLocation();
}
