package croissantnova.sanitydim.client.render;

import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.entity.RottingStalker;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import javax.annotation.Nullable;

public class RendererRottingStalker extends GeoEntityRenderer<RottingStalker>
{
    public RendererRottingStalker(EntityRendererProvider.Context renderManager)
    {
        super(renderManager, new DefaultedEntityGeoModel<>(new ResourceLocation(SanityMod.MODID, "rotting_stalker"), true));

        addRenderLayer(new CustomGlowingGeoLayer<>(this));
    }

    @Override
    public RenderType getRenderType(RottingStalker animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource,
                                    float partialTick)
    {
        return RenderType.entityTranslucent(getTextureLocation(animatable), false);
    }
}