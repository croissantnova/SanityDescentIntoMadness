package croissantnova.sanitydim.config;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ConfigPassiveBlock
{
    public float sanity;
    public float radius;
    public boolean naturallyGenerated;
    public boolean isTag;
    public ResourceLocation name;
    public Map<String, Boolean> blockProperties = new HashMap<>();
}