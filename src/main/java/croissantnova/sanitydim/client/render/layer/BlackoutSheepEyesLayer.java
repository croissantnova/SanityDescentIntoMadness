package croissantnova.sanitydim.client.render.layer;


import croissantnova.sanitydim.SanityMod;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import org.jetbrains.annotations.NotNull;

public class BlackoutSheepEyesLayer<T extends Sheep> extends BlackoutEyesLayer<T, SheepModel<T>>
{
    public static final ResourceLocation EYES_LOCATION = new ResourceLocation(SanityMod.MODID, "textures/entity/sheep_blackout_eyes.png");

    public BlackoutSheepEyesLayer(RenderLayerParent<T, SheepModel<T>> pRenderer)
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