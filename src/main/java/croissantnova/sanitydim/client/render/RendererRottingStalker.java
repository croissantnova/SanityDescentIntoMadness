package croissantnova.sanitydim.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import croissantnova.sanitydim.client.model.entity.ModelRottingStalker;
import croissantnova.sanitydim.entity.RottingStalker;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.layer.LayerGlowingAreasGeo;
import javax.annotation.Nullable;

public class RendererRottingStalker extends GeoEntityRenderer<RottingStalker>
{
    public RendererRottingStalker(EntityRendererManager manager)
    {
        super(manager, new ModelRottingStalker());

        addLayer(new LayerGlowingAreasGeo<>(this, this::getTextureLocation, getGeoModelProvider()::getModelLocation, RenderType::eyes));
//        super(renderManager, new DefaultedEntityGeoModel<>(new ResourceLocation(SanityMod.MODID, "rotting_stalker"), true));
//
//        addRenderLayer(new CustomGlowingGeoLayer<>(this));
    }

    @Override
    public RenderType getRenderType(RottingStalker animatable, float partialTicks, MatrixStack stack,
                                    @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation)
    {
        return RenderType.entityTranslucent(getTextureLocation(animatable), false);
    }
}