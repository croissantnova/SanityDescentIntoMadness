package croissantnova.sanitydim.util;

import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;

public final class LevelHelper {

    private LevelHelper() {}

    public static boolean isPeaceful(Level level) {
        return level.getDifficulty().equals(Difficulty.PEACEFUL);
    }
}
