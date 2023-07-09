package croissantnova.sanitydim.client.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import java.lang.reflect.Method;

public class CustomGlowingGeoLayer<T extends GeoAnimatable> extends AutoGlowingGeoLayer<T>
{
    private static Method getEmissiveResource;

    static
    {
        try
        {
            getEmissiveResource = AutoGlowingTexture.class.getDeclaredMethod("getEmissiveResource", ResourceLocation.class);
            getEmissiveResource.setAccessible(true);
        }
        catch (Exception ignored)
        {
            getEmissiveResource = null;
        }
    }

    public CustomGlowingGeoLayer(GeoRenderer<T> renderer)
    {
        super(renderer);
    }

    @Override
    protected RenderType getRenderType(T animatable)
    {
        if (getEmissiveResource != null)
        {
            ResourceLocation res = null;
            try
            {
                res = (ResourceLocation) getEmissiveResource.invoke(AutoGlowingTexture.class, getTextureResource(animatable));
            }
            catch (Exception ignored) {}

            if (res != null)
                return RenderType.eyes(res);
        }

        return null;
    }
}