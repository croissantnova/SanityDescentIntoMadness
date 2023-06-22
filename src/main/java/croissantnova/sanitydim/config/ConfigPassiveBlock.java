package croissantnova.sanitydim.config;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class ConfigPassiveBlock
{
    public float m_sanity;
    public float m_rad;
    public ResourceLocation m_name;
    public Map<String, Boolean> m_props = Maps.newHashMap();
}