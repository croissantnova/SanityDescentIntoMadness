package croissantnova.sanitydim.client.render;

import croissantnova.sanitydim.client.model.entity.ModelSneakingTerror;
import croissantnova.sanitydim.entity.SneakingTerror;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.layer.LayerGlowingAreasGeo;

public class RendererSneakingTerror extends RendererInnerEntity<SneakingTerror>
{
    public RendererSneakingTerror(EntityRendererProvider.Context renderManager)
    {
        super(renderManager, new ModelSneakingTerror());

        addLayer(new LayerGlowingAreasGeo<>(this, this::getTextureLocation, getGeoModelProvider()::getModelResource, RenderType::eyes));
    }
}
