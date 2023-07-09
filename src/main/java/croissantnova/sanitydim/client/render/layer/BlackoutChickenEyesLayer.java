package croissantnova.sanitydim.client.render.layer;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class BlackoutChickenEyesLayer<T extends LivingEntity> extends BlackoutEyesLayer<T, ChickenModel<T>>
{
    public static final ResourceLocation EYES_LOCATION = new ResourceLocation(SanityMod.MODID, "textures/entity/chicken_blackout_eyes.png");

    public BlackoutChickenEyesLayer(RenderLayerParent<T, ChickenModel<T>> pRenderer)
    {
        super(pRenderer);
    }

    @NotNull
    @Override
    public ResourceLocation getEyesLocation()
    {
        return EYES_LOCATION;
    }
}