package croissantnova.sanitydim.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.client.GuiHandler;
import net.minecraft.client.renderer.GameRenderer;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer
{
    @Inject(method = "render(FJZ)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;bindWrite(Z)V"))
    private void render(float pPartialTicks, long pFinishTimeNano, boolean pRenderLevel, CallbackInfo ci)
    {
        GameRenderer renderer = (GameRenderer)(Object)this;
        SanityMod inst = SanityMod.getInstance();
        if (renderer != null && inst != null && pRenderLevel && renderer.getMinecraft().level != null)
        {
            GuiHandler gui = inst.getGui();
            gui.initPostProcessor();
            gui.renderPostProcess(pPartialTicks);
        }
    }

    @Inject(method = "resize(II)V", at = @At("TAIL"))
    private void resize(int pWidth, int pHeight, CallbackInfo ci)
    {
        if (SanityMod.getInstance() != null && SanityMod.getInstance().getGui() != null)
            SanityMod.getInstance().getGui().resize(pWidth, pHeight);
    }
}