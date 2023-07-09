package croissantnova.sanitydim.client.render;

import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.entity.RottingStalker;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class RendererRottingStalker extends RendererInnerEntity<RottingStalker>
{
    public RendererRottingStalker(EntityRendererProvider.Context renderManager)
    {
        super(renderManager, new DefaultedEntityGeoModel<>(new ResourceLocation(SanityMod.MODID, "rotting_stalker"), true));

        addRenderLayer(new CustomGlowingGeoLayer<>(this));
    }
}