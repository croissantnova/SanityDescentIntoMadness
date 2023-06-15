package croissantnova.sanitydim.capability;

import net.minecraft.network.FriendlyByteBuf;

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

    public void serialize(FriendlyByteBuf buf)
    {
        buf.writeBoolean(m_hasTarget);
    }

    public void deserialize(FriendlyByteBuf buf)
    {
        m_hasTarget = buf.readBoolean();
    }
}