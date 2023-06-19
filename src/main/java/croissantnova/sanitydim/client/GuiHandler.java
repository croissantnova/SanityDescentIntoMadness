package croissantnova.sanitydim.client;

import java.util.Random;
import java.util.function.Function;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.capability.IPassiveSanity;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.config.SanityIndicatorLocation;
import croissantnova.sanitydim.sound.SwishSoundInstance;
import croissantnova.sanitydim.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class GuiHandler
{
    public static final ResourceLocation SANITY_INDICATOR = new ResourceLocation(SanityMod.MODID, "textures/sanity_indicator.png");
    public static final MutableComponent[] HINTS0;
    public static final MutableComponent[] HINTS1;

    private final Minecraft m_mc;
    private ISanity m_cap;
    private final float m_passiveThreshold = .0002f;
    private final float m_maxArrowTimer = 23.99f;
    private final float m_maxArrowTimerSmall = 15.99f;
    private PostProcessor m_post;
    private final Random m_random = new Random();
    private int m_indicatorOffset;
    private int m_hintOffsetX;
    private int m_hintOffsetY;
    private float m_prevSanity;
    private float m_flashTimer;
    private float m_flashSanityGain;
    private float m_arrowTimer;
    private float m_hintTimer;
    private float m_showingHintTimer;
    private float m_maxShowingHintTimer;
    private MutableComponent m_hint;

    public GuiHandler()
    {
        m_mc = Minecraft.getInstance();
    }

    static
    {
        HINTS0 = new MutableComponent[11];
        for (int i = 0; i < HINTS0.length; i++)
        {
            HINTS0[i] = Component.translatable("gui." + SanityMod.MODID + ".hint0" + i);
        }
        HINTS1 = new MutableComponent[8];
        for (int i = 0; i < HINTS1.length; i++)
        {
            HINTS1[i] = Component.translatable("gui." + SanityMod.MODID + ".hint1" + i);
        }
    }

    private void initSanityPostProcess()
    {
        // FIXME: weird vignette appearing suddenly at .4f sanity
        Minecraft mc = Minecraft.getInstance();
        m_post.addSinglePassEntry("insanity", pass ->
        {
            return processPlayer(mc.player, cap ->
            {
                if (cap.getSanity() < .4f)
                    return false;
                pass.getEffect().safeGetUniform("DesaturateFactor").set(MathHelper.clampNorm(Mth.inverseLerp(cap.getSanity(), .4f, .8f)) * .69f);
                pass.getEffect().safeGetUniform("SpreadFactor").set(MathHelper.clampNorm(Mth.inverseLerp(cap.getSanity(), .4f, .8f)) * 1.43f);
                return true;
            });
        });
        m_post.addSinglePassEntry("chromatical", pass ->
        {
            return processPlayer(mc.player, cap ->
            {
                if (cap.getSanity() < .4f)
                    return false;
                pass.getEffect().safeGetUniform("Factor").set(MathHelper.clampNorm(Mth.inverseLerp(cap.getSanity(), .4f, .8f)) * .1f);
                pass.getEffect().safeGetUniform("TimeTotal").set(m_post.getTime() / 20.0f);
                return true;
            });
        });
    }

    private boolean processPlayer(LocalPlayer player, Function<ISanity, Boolean> action)
    {
        ISanity cap;
        return player != null &&
                (!player.isCreative() && !player.isSpectator()) &&
                (cap = player.getCapability(SanityProvider.CAP).orElse(null)) != null &&
                cap.getSanity() > 0 &&
                action.apply(cap);
    }

    private void renderSanityIndicator(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int scw, int sch)
    {
        if (
                m_mc.player == null ||
                m_mc.player.isCreative() ||
                m_mc.player.isSpectator() ||
                m_cap == null ||
                !ConfigProxy.getRenderIndicator(m_mc.player.level().dimension().location()))
            return;

        ResourceLocation dim = m_mc.player.level().dimension().location();
        float scale = ConfigProxy.getIndicatorScale(dim);
        if (scale <= 0f)
            return;

        SanityIndicatorLocation loc = ConfigProxy.getIndicatorLocation(dim);
        PoseStack poseStack = guiGraphics.pose();

        poseStack.pushPose();

        if (loc == SanityIndicatorLocation.HOTBAR_LEFT)
            poseStack.translate(scw / 2f - 97f - (!m_mc.player.getOffhandItem().isEmpty() ? 29f : 0f), sch - 5f, 0f);
        else if (loc == SanityIndicatorLocation.HOTBAR_RIGHT)
            poseStack.translate(97f, 0f, 0f);
        else if (loc == SanityIndicatorLocation.TOP_LEFT)
            poseStack.translate(5f, 5f, 0f);
        else if (loc == SanityIndicatorLocation.TOP_RIGHT)
            poseStack.translate(scw - 5f, 5f, 0f);
        else if (loc == SanityIndicatorLocation.BOTTOM_LEFT)
            poseStack.translate(5f, sch - 5f, 0f);
        else if (loc == SanityIndicatorLocation.BOTTOM_RIGHT)
            poseStack.translate(scw - 5f, sch - 5f, 0f);

        poseStack.scale(scale, scale, 1f);

        int texw = 256;
        int texh = 128;
        int spritew = 33;
        int spriteh = 24;
        int x = 0;
        int y = 0;

        if (loc == SanityIndicatorLocation.HOTBAR_LEFT || loc == SanityIndicatorLocation.BOTTOM_RIGHT)
        {
            x = -spritew;
            y = -spriteh;
        }
        else if (loc == SanityIndicatorLocation.HOTBAR_RIGHT || loc == SanityIndicatorLocation.BOTTOM_LEFT)
        {
            y = -spriteh;
        }
        else if (loc == SanityIndicatorLocation.TOP_RIGHT)
        {
            x = -spritew;
        }

        if (ConfigProxy.getTwitchIndicator(m_mc.player.level().dimension().location()))
            y += m_indicatorOffset;
        int vOffset = Math.round(m_cap.getSanity() * (spriteh - 2)) + 1;

        RenderSystem.setShaderTexture(0, SANITY_INDICATOR);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        // bg
        guiGraphics.blit(SANITY_INDICATOR, x, y, 0, 0, 0, spritew, spriteh, texw, texh);
//        ForgeGui.blit(poseStack, x, y, 0, 0, 0, spritew, spriteh, texw, texh);
        if (m_flashTimer > 0 && ((int) m_flashTimer / 3) % 2 == 0)
        {
            // bg flash
            guiGraphics.blit(SANITY_INDICATOR, x, y, 0, spritew, 0, spritew, spriteh, texw, texh);
//            ForgeGui.blit(poseStack, x, y, 0, spritew, 0, spritew, spriteh, texw, texh);
            if (m_flashSanityGain > 0)
            {
                int flashOffset = Math.round((m_cap.getSanity() - m_flashSanityGain) * (spriteh - 2)) + 1;
                // brain flash
                guiGraphics.blit(SANITY_INDICATOR, x, y + flashOffset, 0, spritew * 3, flashOffset, spritew, spriteh - flashOffset, texw, texh);
//                ForgeGui.blit(poseStack, x, y + flashOffset, 0, spritew * 3, flashOffset, spritew, spriteh - flashOffset, texw, texh);
            }
        }
        // brain
        guiGraphics.blit(SANITY_INDICATOR, x, y + vOffset, 0, spritew * 2, vOffset, spritew, spriteh - vOffset, texw, texh);
//        ForgeGui.blit(poseStack, x, y + vOffset, 0, spritew * 2, vOffset, spritew, spriteh - vOffset, texw, texh);
        if (m_cap instanceof IPassiveSanity)
        {
            float p = ((IPassiveSanity)m_cap).getPassiveIncrease();
            float absp = 0;
            int os = 0;
            if (p != 0)
            {
                if ((absp = Math.abs(p)) >= m_passiveThreshold)
                {
                    m_arrowTimer = Mth.clamp(m_arrowTimer, 0f, m_maxArrowTimer);
                    os = (m_arrowTimer >= 12f && m_arrowTimer <= 15f) || (m_arrowTimer >= 0f && m_arrowTimer <= 3f) ?
                            0 : (((int) m_arrowTimer / 3) % 2 == 0 ? 2 : 1);
                    os *= m_arrowTimer > 12f ? 1 : -1;
                }
                else
                {
                    m_arrowTimer = Mth.clamp(m_arrowTimer, 0f, m_maxArrowTimerSmall);
                    os = ((int) m_arrowTimer / 4) % 2;
                    os *= m_arrowTimer > 8f ? 1 : -1;
                }

                if (p > 0)
                {
                    if (absp < m_passiveThreshold)
                    {
                        guiGraphics.blit(SANITY_INDICATOR, x, y + os, 0, 0, spriteh, spritew, spriteh, texw, texh);
                        guiGraphics.blit(SANITY_INDICATOR, x, y + vOffset, 0, spritew, spriteh + vOffset - os, spritew, spriteh - vOffset + os, texw, texh);
//                        ForgeGui.blit(poseStack, x, y + os, 0, 0, spriteh, spritew, spriteh, texw, texh);
//                        ForgeGui.blit(poseStack, x, y + vOffset, 0, spritew, spriteh + vOffset - os, spritew, spriteh - vOffset + os, texw, texh);
                    }
                    else
                    {
                        guiGraphics.blit(SANITY_INDICATOR, x, y + os, 0, spritew * 2, spriteh, spritew, spriteh, texw, texh);
                        guiGraphics.blit(SANITY_INDICATOR, x, y + vOffset, 0, spritew * 3, spriteh + vOffset - os, spritew, spriteh - vOffset + os, texw, texh);
//                        ForgeGui.blit(poseStack, x, y + os, 0, spritew * 2, spriteh, spritew, spriteh, texw, texh);
//                        ForgeGui.blit(poseStack, x, y + vOffset, 0, spritew * 3, spriteh + vOffset - os, spritew, spriteh - vOffset + os, texw, texh);
                    }
                }
                else
                {
                    if (absp < m_passiveThreshold)
                    {
                        guiGraphics.blit(SANITY_INDICATOR, x, y + os, 0, 0, spriteh * 2, spritew, spriteh, texw, texh);
                        guiGraphics.blit(SANITY_INDICATOR, x, y + vOffset, 0, spritew, spriteh * 2 + vOffset - os, spritew, spriteh - vOffset + os, texw, texh);
//                        ForgeGui.blit(poseStack, x, y + os, 0, 0, spriteh * 2, spritew, spriteh, texw, texh);
//                        ForgeGui.blit(poseStack, x, y + vOffset, 0, spritew, spriteh * 2 + vOffset - os, spritew, spriteh - vOffset + os, texw, texh);
                    }
                    else
                    {
                        guiGraphics.blit(SANITY_INDICATOR, x, y + os, 0, spritew * 2, spriteh * 2, spritew, spriteh, texw, texh);
                        guiGraphics.blit(SANITY_INDICATOR, x, y + vOffset, 0, spritew * 3, spriteh * 2 + vOffset - os, spritew, spriteh - vOffset + os, texw, texh);
//                        ForgeGui.blit(poseStack, x, y + os, 0, spritew * 2, spriteh * 2, spritew, spriteh, texw, texh);
//                        ForgeGui.blit(poseStack, x, y + vOffset, 0, spritew * 3, spriteh * 2 + vOffset - os, spritew, spriteh - vOffset + os, texw, texh);
                    }
                }
            }
        }

        poseStack.popPose();
    }

    private void renderHint(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int scw, int sch)
    {
        if (m_mc.player == null || m_mc.player.isCreative() || m_mc.player.isSpectator() || m_hint == null || m_cap == null || m_cap.getSanity() < .5f
            || !ConfigProxy.getRenderHint(m_mc.player.level().dimension().location()))
            return;

        PoseStack poseStack = guiGraphics.pose();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        poseStack.pushPose();
        poseStack.translate(scw / 2d, sch / 2d, 0d);
        poseStack.scale(2f, 2f, 1f);

        float o = ((int) m_showingHintTimer % 10) / 10f;
        o = ((int) m_showingHintTimer / 10) % 2 == 0 ? o : 1 - o;
        int opacity = Mth.clamp((int)(Mth.lerp(o, (m_showingHintTimer >= m_maxShowingHintTimer - 9f) || m_showingHintTimer < 10f ? 0f : .5f, 1f) * 0xFF), 0x10, 0xEF) << 24;

        float pX = -gui.getFont().width(m_hint) / 2f;
        float pY = -gui.getFont().lineHeight / 2f;
        if (ConfigProxy.getTwitchHint(m_mc.player.level().dimension().location()))
        {
            pX += m_hintOffsetX;
            pY += m_hintOffsetY;
        }

        gui.getFont().drawInBatch(m_hint, pX, pY, 0xFFFFFF | opacity, true, poseStack.last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
//        gui.getFont().drawShadow(poseStack, m_hint, pX, pY, 0xFFFFFF | opacity);

        poseStack.popPose();
        RenderSystem.disableBlend();
    }

    private void tickHint(float dt)
    {
        if (m_cap.getSanity() <= .4f || m_mc.player == null || !ConfigProxy.getRenderHint(m_mc.player.level().dimension().location()))
            return;

        if (m_hintTimer <= 0f && m_showingHintTimer <= 0f)
        {
            int id;
            if (m_cap.getSanity() <= .7f)
            {
                id = m_random.nextInt(HINTS0.length);
                m_hint = HINTS0[id];
                m_hintTimer = 2000;

                if (ConfigProxy.getPlaySounds(m_mc.player.level().dimension().location()) && id == 2)
                    m_mc.getSoundManager().play(new SwishSoundInstance());
            }
            else
            {
                id = m_random.nextInt(HINTS1.length);
                m_hint = HINTS1[id];
                m_hintTimer = 600;

                if (ConfigProxy.getPlaySounds(m_mc.player.level().dimension().location()) && id == 0)
                    m_mc.getSoundManager().play(new SwishSoundInstance());
            }

            m_showingHintTimer = (m_maxShowingHintTimer = 199f);
        }
        if (m_showingHintTimer > 0f)
            m_showingHintTimer -= dt;
        else
            m_hintTimer = MathHelper.clamp(m_hintTimer - dt, 0, Float.MAX_VALUE);
    }

    public void tick(float dt)
    {
        if (m_mc.player == null || m_mc.isPaused() || m_mc.player.isCreative() || m_mc.player.isSpectator())
            return;

        m_cap = m_mc.player.getCapability(SanityProvider.CAP).orElse(null);
        if (m_cap == null)
            return;

        if (m_flashTimer > 0)
            m_flashTimer -= dt;

        float sanityGain = m_cap.getSanity() - m_prevSanity;
        if (Math.abs(sanityGain) > 0.02f)
            m_flashTimer = 20;
        m_flashSanityGain = m_flashTimer <= 0 ? 0 : m_flashSanityGain + sanityGain;

        if (m_cap instanceof IPassiveSanity)
        {
            if (m_arrowTimer <= 0)
                m_arrowTimer = 23.99f;
            float p = ((IPassiveSanity)m_cap).getPassiveIncrease();
            if (p != 0)
                m_arrowTimer -= dt;
        }

        if (m_cap.getSanity() >= .7f)
        {
            m_indicatorOffset = m_random.nextInt(3) - 1;
            m_hintOffsetX = m_random.nextInt(3) - 1;
            m_hintOffsetY = m_random.nextInt(3) - 1;
        }
        else
        {
            m_indicatorOffset = 0;
            m_hintOffsetX = 0;
            m_hintOffsetY = 0;
        }

        tickHint(dt);

        m_prevSanity = m_cap.getSanity();
    }

    public void initOverlays(final RegisterGuiOverlaysEvent event)
    {
        event.registerBelow(VanillaGuiOverlay.HOTBAR.id(), SanityMod.MODID.concat(".sanity_indicator"), this::renderSanityIndicator);
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), SanityMod.MODID.concat(".hint"), this::renderHint);
    }

    public void initPostProcessor()
    {
        if (m_post != null) return;

        m_post = new PostProcessor();
        initSanityPostProcess();
    }

    public PostProcessor getPostProcessor()
    {
        return m_post;
    }

    public void renderPostProcess(float partialTicks)
    {
        if (m_post == null) return;

        m_post.render(partialTicks);
    }

    public void resize(int w, int h)
    {
        if (m_post == null) return;

        m_post.resize(w, h);
    }
}