package croissantnova.sanitydim.client.render.layer;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class BlackoutCowEyesLayer<T extends LivingEntity> extends BlackoutEyesLayer<T, CowModel<T>>
{
    public static final ResourceLocation EYES_LOCATION = new ResourceLocation(SanityMod.MODID, "textures/entity/cow_blackout_eyes.png");

    public BlackoutCowEyesLayer(RenderLayerParent<T, CowModel<T>> pRenderer)
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