package croissantnova.sanitydim.capability;

import java.util.Map;

public interface IPersistentSanity
{
    int[] getActiveSourcesCooldowns();

    Map<Integer, Integer> getItemCooldowns();

    void setEnderManAngerTimer(int value);

    int getEnderManAngerTimer();
}