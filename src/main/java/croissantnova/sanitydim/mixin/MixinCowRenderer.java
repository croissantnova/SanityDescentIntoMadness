package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.client.render.layer.Blackout;
import croissantnova.sanitydim.client.render.layer.BlackoutCowEyesLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CowRenderer.class)
public abstract class MixinCowRenderer extends MobRenderer<Cow, CowModel<Cow>>
{
    public MixinCowRenderer(EntityRendererProvider.Context pContext, CowModel<Cow> pModel, float pShadowRadius)
    {
        super(pContext, pModel, pShadowRadius);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)V", at = @At("TAIL"))
    private void ctor(EntityRendererProvider.Context ctx, CallbackInfo ci)
    {
        this.addLayer(new BlackoutCowEyesLayer<>(this));
    }

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Cow;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("TAIL"),
            cancellable = true)
    private void getTextureLocation(Cow pEntity, CallbackInfoReturnable<ResourceLocation> ci)
    {
        Player player = Minecraft.getInstance().player;
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            if (s.getSanity() >= Blackout.THRESHOLD)
                ci.setReturnValue(Blackout.COW_LOCATION);
        });
    }
}