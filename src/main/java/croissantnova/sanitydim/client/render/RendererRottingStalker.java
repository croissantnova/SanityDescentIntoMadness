package croissantnova.sanitydim.client.render;

import croissantnova.sanitydim.client.model.entity.ModelRottingStalker;
import croissantnova.sanitydim.entity.RottingStalker;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.layer.LayerGlowingAreasGeo;

public class RendererRottingStalker extends RendererInnerEntity<RottingStalker>
{
    public RendererRottingStalker(EntityRendererProvider.Context context)
    {
        super(context, new ModelRottingStalker());

        addLayer(new LayerGlowingAreasGeo<>(this, this::getTextureLocation, getGeoModelProvider()::getModelLocation, RenderType::eyes));
//        super(renderManager, new DefaultedEntityGeoModel<>(new ResourceLocation(SanityMod.MODID, "rotting_stalker"), true));
//
//        addRenderLayer(new CustomGlowingGeoLayer<>(this));
    }
}