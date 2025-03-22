package croissantnova.sanitydim.capability;

import net.minecraft.world.phys.Vec3;

import java.util.Map;

public interface IPersistentSanity
{
    int[] getActiveSourcesCooldowns();

    Map<Integer, Integer> getItemCooldowns();

    Map<Integer, Integer> getBrokenBlocksCooldowns();

    void setEnderManAngerTimer(int value);

    int getEnderManAngerTimer();

    void setDeathScore(float value);

    float getDeathScore();

    void setStuckMotionMultiplier(Vec3 multiplier);

    Vec3 getStuckMotionMultiplier();
}