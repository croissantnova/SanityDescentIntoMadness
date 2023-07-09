package croissantnova.sanitydim.passive;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.util.BlockPosHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Jukebox implements IPassiveSanitySource
{
    public static final List<BlockPos> JUKEBOXES = new ArrayList<>();
    public static final List<BlockPos> UNSETTLING_JUKEBOXES = new ArrayList<>();

    @Override
    public float get(@Nonnull ServerPlayer player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim)
    {
        for (BlockPos blockPos : UNSETTLING_JUKEBOXES)
        {
            if (player.getEyePosition().distanceTo(BlockPosHelper.getCenter(blockPos)) <= 60)
                return ConfigProxy.getJukeboxUnsettling(dim);
        }
        for (BlockPos blockPos : JUKEBOXES)
        {
            if (player.getEyePosition().distanceTo(BlockPosHelper.getCenter(blockPos)) <= 60)
                return ConfigProxy.getJukeboxPleasant(dim);
        }

        return 0;
    }

    public static boolean isMusicDiscUnsettling(Item disc)
    {
        return
                disc == Items.MUSIC_DISC_5 ||
                disc == Items.MUSIC_DISC_11 ||
                disc == Items.MUSIC_DISC_13;
    }

    public static void handleJukeboxStartedPlaying(BlockPos blockPos, ItemStack record)
    {
        if (!record.is(ItemTags.MUSIC_DISCS))
            return;

        boolean unsettling = isMusicDiscUnsettling(record.getItem());
        if (unsettling && !Jukebox.UNSETTLING_JUKEBOXES.contains(blockPos))
            Jukebox.UNSETTLING_JUKEBOXES.add(blockPos);
        else if (!unsettling)
            Jukebox.JUKEBOXES.add(blockPos);
    }

    public static void handleJukeboxStoppedPlaying(BlockPos blockPos, ItemStack record)
    {
        for (; Jukebox.JUKEBOXES.remove(blockPos););
        for (; Jukebox.UNSETTLING_JUKEBOXES.remove(blockPos););
    }
}