package croissantnova.sanitydim.capability;

import com.google.common.collect.Maps;
import croissantnova.sanitydim.ActiveSanitySources;
import croissantnova.sanitydim.util.MathHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import java.util.Map;

public class Sanity implements ISanity, IPassiveSanity, IPersistentSanity
{
    private boolean m_dirty = true;
    private int m_emAngerTimer;
    private float m_sanityVal;
    private float m_passive;
    private final int[] m_cds = new int[ActiveSanitySources.AMOUNT];
    private final Map<Integer, Integer> m_itemCds = Maps.newHashMap();

    public Sanity()
    {
    }

    @Override
    public void serializeNBT(CompoundTag tag)
    {
        tag.putFloat("sanity", m_sanityVal);
        tag.putInt("sleeping", m_cds[ActiveSanitySources.SLEEPING]);
        tag.putInt("baby_chicken_spawn", m_cds[ActiveSanitySources.SPAWNING_BABY_CHICKEN]);
        tag.putInt("animal_breeding", m_cds[ActiveSanitySources.BREEDING_ANIMALS]);
        tag.putInt("villager_trade", m_cds[ActiveSanitySources.VILLAGER_TRADE]);
        tag.putInt("shearing", m_cds[ActiveSanitySources.SHEARING]);
        tag.putInt("eating", m_cds[ActiveSanitySources.EATING]);
        tag.putInt("fishing", m_cds[ActiveSanitySources.FISHING]);

        serializeItemCds(tag);
    }

    @Override
    public void deserializeNBT(CompoundTag tag)
    {
        setSanity(tag.getFloat("sanity"));
        m_cds[ActiveSanitySources.SLEEPING] = tag.getInt("sleeping");
        m_cds[ActiveSanitySources.SPAWNING_BABY_CHICKEN] = tag.getInt("baby_chicken_spawn");
        m_cds[ActiveSanitySources.BREEDING_ANIMALS] = tag.getInt("animal_breeding");
        m_cds[ActiveSanitySources.VILLAGER_TRADE] = tag.getInt("villager_trade");
        m_cds[ActiveSanitySources.SHEARING] = tag.getInt("shearing");
        m_cds[ActiveSanitySources.EATING] = tag.getInt("eating");
        m_cds[ActiveSanitySources.FISHING] = tag.getInt("fishing");

        deserializeItemCds(tag);
    }

    public void serialize(FriendlyByteBuf buf)
    {
        buf.writeFloat(m_sanityVal);
        buf.writeFloat(m_passive);
    }

    public void deserialize(FriendlyByteBuf buf)
    {
        m_sanityVal = buf.readFloat();
        m_passive = buf.readFloat();
    }

    @Override
    public float getSanity()
    {
        return m_sanityVal;
    }

    @Override
    public void setSanity(float value)
    {
        m_sanityVal = MathHelper.clampNorm(value);
        m_dirty = true;
    }

    @Override
    public float getPassiveIncrease()
    {
        return m_passive;
    }

    @Override
    public void setPassiveIncrease(float value)
    {
        m_passive = value;
        m_dirty = true;
    }

    public boolean getDirty()
    {
        return m_dirty;
    }

    public void setDirty(boolean value)
    {
        m_dirty = value;
    }

    @Override
    public int[] getActiveSourcesCooldowns()
    {
        return m_cds;
    }

    @Override
    public Map<Integer, Integer> getItemCooldowns()
    {
        return m_itemCds;
    }

    @Override
    public void setEnderManAngerTimer(int value)
    {
        m_emAngerTimer = value;
    }

    @Override
    public int getEnderManAngerTimer()
    {
        return m_emAngerTimer;
    }

    private void serializeItemCds(CompoundTag tag)
    {
        long[] itemCds = new long[m_itemCds.size()];
        int i = 0;

        for (Map.Entry<Integer, Integer> entry : m_itemCds.entrySet())
        {
            long val = entry.getKey();
            val <<= Long.SIZE / 2;
            val |= entry.getValue();

            itemCds[i] = val;

            i++;
        }

        tag.putLongArray("item_cooldowns", itemCds);
    }

    private void deserializeItemCds(CompoundTag tag)
    {
        long[] itemCds = tag.getLongArray("item_cooldowns");
        m_itemCds.clear();

        for (long itemCd : itemCds)
        {
            m_itemCds.put((int)(itemCd >> Long.SIZE / 2), (int)itemCd);
        }
    }
}