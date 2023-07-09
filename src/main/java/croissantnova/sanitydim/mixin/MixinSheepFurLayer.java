package croissantnova.sanitydim.mixin;

import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.client.render.layer.Blackout;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(SheepFurLayer.class)
public abstract class MixinSheepFurLayer
{
    @ModifyArg(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Sheep;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;outline(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"),
            index = 0)
    private ResourceLocation RenderType_outline(ResourceLocation arg)
    {
        return whatDoYouWantFromMe$getSheepFur(arg);
    }

    @ModifyArg(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Sheep;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/SheepFurLayer;coloredCutoutModelCopyLayerRender(Lnet/minecraft/client/model/EntityModel;Lnet/minecraft/client/model/EntityModel;Lnet/minecraft/resources/ResourceLocation;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFFFFF)V"),
            index = 2)
    private ResourceLocation coloredCutoutModelCopyLayerRender(ResourceLocation arg)
    {
        return whatDoYouWantFromMe$getSheepFur(arg);
    }

    @Unique
    private static ResourceLocation whatDoYouWantFromMe$getSheepFur(ResourceLocation arg)
    {
        AtomicReference<ResourceLocation> toReturn = new AtomicReference<>(arg);
        if (Minecraft.getInstance().player != null)
        {
            Minecraft.getInstance().player.getCapability(SanityProvider.CAP).ifPresent(s ->
            {
                if (s.getSanity() >= Blackout.THRESHOLD)
                    toReturn.set(Blackout.SHEEP_FUR_LOCATION);
            });
        }
        return toReturn.get();
    }
}
