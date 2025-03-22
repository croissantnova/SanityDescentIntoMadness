package croissantnova.sanitydim.capability;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public interface IInnerEntityCap
{
    boolean hasTarget();

    void setHasTarget(boolean value);

    Optional<UUID> getPlayerTargetUUID();

    void setPlayerTargetUUID(UUID value);
}