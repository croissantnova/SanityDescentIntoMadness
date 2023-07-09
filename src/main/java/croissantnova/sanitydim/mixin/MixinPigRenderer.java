package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.client.render.layer.Blackout;
import croissantnova.sanitydim.client.render.layer.BlackoutPigEyesLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PigRenderer.class)
public abstract class MixinPigRenderer extends MobRenderer<Pig, PigModel<Pig>>
{
    public MixinPigRenderer(EntityRendererProvider.Context pContext, PigModel<Pig> pModel, float pShadowRadius)
    {
        super(pContext, pModel, pShadowRadius);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)V", at = @At("TAIL"))
    private void ctor(EntityRendererProvider.Context ctx, CallbackInfo ci)
    {
        this.addLayer(new BlackoutPigEyesLayer<>(this));
    }

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Pig;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("TAIL"),
            cancellable = true)
    private void getTextureLocation(Pig pEntity, CallbackInfoReturnable<ResourceLocation> ci)
    {
        Player player = Minecraft.getInstance().player;
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            if (s.getSanity() >= Blackout.THRESHOLD)
                ci.setReturnValue(Blackout.PIG_LOCATION);
        });
    }
}