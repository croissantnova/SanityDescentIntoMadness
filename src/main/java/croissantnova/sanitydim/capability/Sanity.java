package croissantnova.sanitydim.capability;

import croissantnova.sanitydim.ActiveSanitySources;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.util.MathHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class Sanity implements ISanity, IPassiveSanity, IPersistentSanity
{
    private boolean m_dirty = true;
    private int m_emAngerTimer;
    private float m_sanityVal;
    private float m_passive;
    private final int[] m_cds = new int[ActiveSanitySources.AMOUNT];

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
    public int[] getActiveSourcesCds()
    {
        return m_cds;
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

    public static void addSanity(@NotNull ISanity sanity, float value, @NotNull ResourceLocation dim)
    {
        float mul = value >= 0 ? ConfigProxy.getNegMul(dim) : ConfigProxy.getPosMul(dim);
        sanity.setSanity(sanity.getSanity() + value * mul);
    }

    public static void addSanity(@NotNull ISanity sanity, float value, @NotNull Player player)
    {
        addSanity(sanity, value, player.level.dimension().location());
    }
}