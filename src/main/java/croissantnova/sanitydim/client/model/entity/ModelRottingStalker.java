package croissantnova.sanitydim.client.model.entity;

import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.entity.RottingStalker;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelRottingStalker extends AnimatedGeoModel<RottingStalker>
{
    public static final ResourceLocation MODEL_LOCATION = new ResourceLocation(SanityMod.MODID, "geo/entity/rotting_stalker.geo.json");
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(SanityMod.MODID, "textures/entity/rotting_stalker.png");
    public static final ResourceLocation ANIMATION_FILE_LOCATION = new ResourceLocation(SanityMod.MODID, "animations/entity/rotting_stalker.animation.json");

    @Override
    public ResourceLocation getModelResource(RottingStalker object)
    {
        return MODEL_LOCATION;
    }

    @Override
    public ResourceLocation getTextureResource(RottingStalker object)
    {
        return TEXTURE_LOCATION;
    }

    @Override
    public ResourceLocation getAnimationResource(RottingStalker animatable)
    {
        return ANIMATION_FILE_LOCATION;
    }
}