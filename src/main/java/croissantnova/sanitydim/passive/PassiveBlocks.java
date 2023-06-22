package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.block.BlockStateHelper;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigPassiveBlock;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.util.BlockPosHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraftforge.registries.ForgeRegistries;
import javax.annotation.Nonnull;
import java.util.Map;

public class PassiveBlocks implements IPassiveSanitySource
{
    @Override
    public float get(@Nonnull ServerPlayerEntity player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim)
    {
        float result = 0;

        for (ConfigPassiveBlock block : ConfigProxy.getPassiveBlocks(dim))
        {
            if (block.m_sanity == 0.0f)
                continue;

            Block regBlock = ForgeRegistries.BLOCKS.getValue(block.m_name);
            if (regBlock == null)
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

                        if (regBlock == stateAt.getBlock())
                        {
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

                            BlockRayTraceResult hit = player.level.clip(new RayTraceContext(
                                    player.getEyePosition(1f),
                                    BlockPosHelper.getCenter(posAt),
                                    RayTraceContext.BlockMode.COLLIDER,
                                    RayTraceContext.FluidMode.NONE,
                                    player));
                            if (hit.getType() == BlockRayTraceResult.Type.MISS)
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