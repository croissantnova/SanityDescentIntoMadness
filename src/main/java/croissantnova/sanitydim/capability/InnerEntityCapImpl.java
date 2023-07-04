package croissantnova.sanitydim.capability;

import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class InnerEntityCapImpl implements IInnerEntityCap
{
    private boolean m_dirty;
    private boolean m_hasTarget;
    private UUID m_playerUuid;

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

    @Override
    public UUID getPlayerTargetUUID()
    {
        return m_playerUuid;
    }

    @Override
    public void setPlayerTargetUUID(UUID value)
    {
        m_playerUuid = value;
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
        if (m_playerUuid != null)
            buf.writeUUID(m_playerUuid);
    }

    public void deserialize(FriendlyByteBuf buf)
    {
        m_hasTarget = buf.readBoolean();
        m_playerUuid = buf.isReadable() ? buf.readUUID() : null;
    }
}