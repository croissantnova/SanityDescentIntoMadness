package croissantnova.sanitydim.client.render.layer;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class BlackoutPigEyesLayer<T extends LivingEntity> extends BlackoutEyesLayer<T, PigModel<T>>
{
    public static final ResourceLocation EYES_LOCATION = new ResourceLocation(SanityMod.MODID, "textures/entity/pig_blackout_eyes.png");

    public BlackoutPigEyesLayer(RenderLayerParent<T, PigModel<T>> pRenderer)
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