package croissantnova.sanitydim.client.render.layer;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.resources.ResourceLocation;

public abstract class Blackout
{
    public static final float THRESHOLD = .7f;

    public static final ResourceLocation CHICKEN_LOCATION       = new ResourceLocation(SanityMod.MODID, "textures/entity/chicken_blackout.png");
    public static final ResourceLocation COW_LOCATION           = new ResourceLocation(SanityMod.MODID, "textures/entity/cow_blackout.png");
    public static final ResourceLocation PIG_LOCATION           = new ResourceLocation(SanityMod.MODID, "textures/entity/pig_blackout.png");
    public static final ResourceLocation SHEEP_LOCATION         = new ResourceLocation(SanityMod.MODID, "textures/entity/sheep_blackout.png");

    public static final ResourceLocation SHEEP_FUR_LOCATION     = new ResourceLocation(SanityMod.MODID, "textures/entity/sheep_fur_blackout.png");
}