package croissantnova.sanitydim.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import croissantnova.sanitydim.client.model.entity.ModelRottingStalker;
import croissantnova.sanitydim.entity.RottingStalker;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.layer.LayerGlowingAreasGeo;

import javax.annotation.Nullable;

public class RendererRottingStalker extends GeoEntityRenderer<RottingStalker>
{
    public RendererRottingStalker(EntityRendererProvider.Context context)
    {
        super(context, new ModelRottingStalker());

        addLayer(new LayerGlowingAreasGeo<>(this, this::getTextureLocation, getGeoModelProvider()::getModelLocation, RenderType::eyes));
//        super(renderManager, new DefaultedEntityGeoModel<>(new ResourceLocation(SanityMod.MODID, "rotting_stalker"), true));
//
//        addRenderLayer(new CustomGlowingGeoLayer<>(this));
    }

    @Override
    public RenderType getRenderType(RottingStalker animatable, float partialTick, PoseStack poseStack,
                                    @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight,
                                    ResourceLocation texture)
    {
        return RenderType.entityTranslucent(getTextureLocation(animatable), false);
    }
}