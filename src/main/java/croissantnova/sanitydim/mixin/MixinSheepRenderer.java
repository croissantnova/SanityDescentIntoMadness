package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.client.render.layer.Blackout;
import croissantnova.sanitydim.client.render.layer.BlackoutSheepEyesLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SheepRenderer.class)
public abstract class MixinSheepRenderer extends MobRenderer<Sheep, SheepModel<Sheep>>
{
    public MixinSheepRenderer(EntityRendererProvider.Context pContext, SheepModel<Sheep> pModel, float pShadowRadius)
    {
        super(pContext, pModel, pShadowRadius);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)V", at = @At("TAIL"))
    private void ctor(EntityRendererProvider.Context ctx, CallbackInfo ci)
    {
        this.addLayer(new BlackoutSheepEyesLayer<>(this));
    }

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Sheep;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("TAIL"),
            cancellable = true)
    private void getTextureLocation(Sheep pEntity, CallbackInfoReturnable<ResourceLocation> ci)
    {
        Player player = Minecraft.getInstance().player;
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        player.getCapability(SanityProvider.CAP).ifPresent(s ->
        {
            if (s.getSanity() >= Blackout.THRESHOLD)
                ci.setReturnValue(Blackout.SHEEP_LOCATION);
        });
    }
}
