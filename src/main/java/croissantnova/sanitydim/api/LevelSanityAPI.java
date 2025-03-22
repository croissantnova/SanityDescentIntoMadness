package croissantnova.sanitydim.api;

import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class LevelSanityAPI {

    private LevelSanityAPI() {}

    public static boolean isNightmareTime(@NotNull Level level) {
        return level.isNight() || level.isThundering();
    }
}
