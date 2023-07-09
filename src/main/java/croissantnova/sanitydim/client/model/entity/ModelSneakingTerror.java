package croissantnova.sanitydim.client.model.entity;

import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.entity.SneakingTerror;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelSneakingTerror extends AnimatedGeoModel<SneakingTerror>
{
    public static final ResourceLocation MODEL_LOCATION = new ResourceLocation(SanityMod.MODID, "geo/entity/sneaking_terror.geo.json");
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(SanityMod.MODID, "textures/entity/sneaking_terror.png");
    public static final ResourceLocation ANIMATION_FILE_LOCATION = new ResourceLocation(SanityMod.MODID, "animations/entity/sneaking_terror.animation.json");

    @Override
    public ResourceLocation getModelLocation(SneakingTerror object)
    {
        return MODEL_LOCATION;
    }

    @Override
    public ResourceLocation getTextureLocation(SneakingTerror object)
    {
        return TEXTURE_LOCATION;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(SneakingTerror animatable)
    {
        return ANIMATION_FILE_LOCATION;
    }
}
