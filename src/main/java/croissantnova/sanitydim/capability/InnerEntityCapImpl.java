package croissantnova.sanitydim.capability;

import net.minecraft.network.PacketBuffer;

public class InnerEntityCapImpl implements IInnerEntityCap
{
    private boolean m_dirty;
    private boolean m_hasTarget;

    @Override
    public boolean hasTarget()
    {
        return m_hasTarget;
    }

    @Override
    public void setHasTarget(boolean value)
    {
        m_hasTarget = value;
        setDirty(true);
    }

    public boolean getDirty()
    {
        return m_dirty;
    }

    public void setDirty(boolean value)
    {
        m_dirty = value;
    }

    public void serialize(PacketBuffer buf)
    {
        buf.writeBoolean(m_hasTarget);
    }

    public void deserialize(PacketBuffer buf)
    {
        m_hasTarget = buf.readBoolean();
    }
}