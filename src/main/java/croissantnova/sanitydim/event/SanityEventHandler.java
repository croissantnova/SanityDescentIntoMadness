package croissantnova.sanitydim.event;

import croissantnova.sanitydim.capability.SanityProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class SanityEventHandler
{

    @SubscribeEvent
    public void onPlayerUseItem(final LivingEntityUseItemEvent.Finish event)
    {
        if (event.getEntity() instanceof ServerPlayer && event.getItem() != null)
        {
            ((ServerPlayer)event.getEntity()).getCapability(SanityProvider.CAP).ifPresent(s ->
            {
                if (event.getItem().getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", "honey_bottle")))
                {
                    s.setSanity(s.getSanity() - .04f);
                }
            });
        }
    }
}