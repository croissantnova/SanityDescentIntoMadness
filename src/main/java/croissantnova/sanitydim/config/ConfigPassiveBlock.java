package croissantnova.sanitydim.config;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ConfigPassiveBlock
{
    public float m_sanity;
    public float m_rad;
    public boolean m_naturallyGend;
    public boolean m_isTag;
    public ResourceLocation m_name;
    public Map<String, Boolean> m_props = new HashMap<>();
}