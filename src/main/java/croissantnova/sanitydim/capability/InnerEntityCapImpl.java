package croissantnova.sanitydim.capability;

import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class InnerEntityCapImpl implements IInnerEntityCap
{
    private boolean dirty;
    private boolean hasTarget;
    private @Nullable UUID playerUuid;

    @Override
    public boolean hasTarget()
    {
        return hasTarget;
    }

    @Override
    public void setHasTarget(boolean value)
    {
        hasTarget = value;
        setDirty(true);
    }

    @Override
    public Optional<UUID> getPlayerTargetUUID()
    {
        return Optional.ofNullable(playerUuid);
    }

    @Override
    public void setPlayerTargetUUID(UUID value)
    {
        playerUuid = value;
    }

    public boolean getDirty()
    {
        return dirty;
    }

    public void setDirty(boolean value)
    {
        dirty = value;
    }

    public void serialize(FriendlyByteBuf buf)
    {
        buf.writeBoolean(hasTarget);
        if (playerUuid != null)
            buf.writeUUID(playerUuid);
    }

    public void deserialize(FriendlyByteBuf buf)
    {
        hasTarget = buf.readBoolean();
        playerUuid = buf.isReadable() ? buf.readUUID() : null;
    }
}