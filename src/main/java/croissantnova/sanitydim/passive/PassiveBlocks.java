package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.block.BlockStateHelper;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.capability.SanityLevelChunkProvider;
import croissantnova.sanitydim.config.ConfigPassiveBlock;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class PassiveBlocks implements IPassiveSanitySource
{
    @Override
    public float get(@Nonnull ServerPlayer player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim)
    {
        float result = 0;

        for (ConfigPassiveBlock block : ConfigProxy.getPassiveBlocks(dim))
        {
            if (block.m_sanity == 0.0f)
                continue;

            Block regBlock = null;
            if (!block.m_isTag && ((regBlock = ForgeRegistries.BLOCKS.getValue(block.m_name)) == null || regBlock.defaultBlockState().isAir()))
                continue;

            boolean flag = false;
            for (float x = (float)player.position().x - block.m_rad; x < player.position().x + block.m_rad; ++x)
            {
                if (flag) break;
                for (float y = (float)player.position().y - block.m_rad; y < player.position().y + block.m_rad; ++y)
                {
                    if (flag) break;
                    for (float z = (float)player.position().z - block.m_rad; z < player.position().z + block.m_rad; ++z)
                    {
                        BlockPos posAt = new BlockPos((int)x, (int)y, (int)z);
                        BlockState stateAt = player.level.getBlockState(posAt);

                        if (block.m_isTag && stateAt.getTags().anyMatch(tag -> tag.location().equals(block.m_name)) || regBlock == stateAt.getBlock())
                        {
                            if (block.m_naturallyGend)
                            {
                                AtomicBoolean placedArtificially = new AtomicBoolean(false);
                                player.getLevel().getChunkAt(posAt).getCapability(SanityLevelChunkProvider.CAP).ifPresent(sl ->
                                {
                                    if (sl.getArtificiallyPlacedBlocks().contains(posAt))
                                        placedArtificially.set(true);
                                });
                                if (placedArtificially.get())
                                    continue;
                            }

                            boolean flag1 = false;
                            for (Map.Entry<String, Boolean> entry : block.m_props.entrySet())
                            {
                                BooleanProperty prop = BlockStateHelper.getBooleanProperty(stateAt, entry.getKey());
                                if (prop != null && stateAt.getValue(prop) != entry.getValue())
                                {
                                    flag1 = true;
                                    break;
                                }
                            }
                            if (flag1)
                                continue;

                            HitResult hit = player.level.clip(new ClipContext(
                                    player.getEyePosition(),
                                    posAt.getCenter(),
                                    ClipContext.Block.COLLIDER,
                                    ClipContext.Fluid.NONE,
                                    player));
                            if (hit.getType() == HitResult.Type.MISS)
                            {
                                result += block.m_sanity;
                                flag = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return result;
    }
}