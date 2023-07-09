package croissantnova.sanitydim.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.math.Matrix4f;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostPass;

public class PostProcessor
{
    private float m_time;
    private final RenderTarget m_swapBuffer;
    private Matrix4f m_orthoMat = new Matrix4f();
    public final List<PostPassEntry> passEntries = new ArrayList<>();

    public PostProcessor()
    {
        Minecraft mc = Minecraft.getInstance();
        m_swapBuffer = new TextureTarget(mc.getMainRenderTarget().width, mc.getMainRenderTarget().height, false, Minecraft.ON_OSX);
        m_swapBuffer.setClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        updateOrthoMatrix();
    }

    public float getTime()
    {
        return m_time;
    }

    public PostPassEntry addPassEntry(String in, String out, Function<PostPass, Boolean> inProcessor, Function<PostPass, Boolean> outProcessor)
    {
        Minecraft mc = Minecraft.getInstance();
        PostPass inPass;
        PostPass outPass;
        try
        {
            inPass = new PostPass(mc.getResourceManager(), in, mc.getMainRenderTarget(), m_swapBuffer);
            inPass.setOrthoMatrix(m_orthoMat);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        try
        {
            outPass = new PostPass(mc.getResourceManager(), out, m_swapBuffer, mc.getMainRenderTarget());
            outPass.setOrthoMatrix(m_orthoMat);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        PostPassEntry entry = new PostPassEntry(inPass, outPass, inProcessor, outProcessor);
        passEntries.add(entry);
        return entry;
    }

    public PostPassEntry addSinglePassEntry(String in, Function<PostPass, Boolean> inProcessor)
    {
        return addPassEntry(in, "blit", inProcessor, null);
    }

    public void render(float partialTicks)
    {
        m_time += partialTicks;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.isCreative() || mc.player.isSpectator() || !ConfigProxy.getRenderPost(mc.player.level.dimension().location()))
            return;

        for (PostPassEntry entry : passEntries)
        {
            if (entry.getInPass() == null || entry.getOutPass() == null ||
                    entry.getInProcessor() != null && !entry.getInProcessor().apply(entry.getInPass()) ||
                    entry.getOutProcessor() != null && !entry.getOutProcessor().apply(entry.getOutPass()))
                continue;

            entry.getInPass().process(partialTicks);
            entry.getOutPass().process(partialTicks);
        }
    }

    public void updateOrthoMatrix()
    {
        Minecraft mc = Minecraft.getInstance();
        m_orthoMat = Matrix4f.orthographic(0.0f, (float)mc.getMainRenderTarget().width, (float)mc.getMainRenderTarget().height, 0.0f, .1f, 1000.0f);
        for (PostPassEntry entry : passEntries)
        {
            entry.getInPass().setOrthoMatrix(m_orthoMat);
            entry.getOutPass().setOrthoMatrix(m_orthoMat);
        }
    }

    public void resize(int w, int h)
    {
        if (m_swapBuffer != null)
            m_swapBuffer.resize(w, h, false);
        updateOrthoMatrix();
    }

    public class PostPassEntry
    {
        private PostPass m_in;
        private PostPass m_out;
        private Function<PostPass, Boolean> m_inProcessor;
        private Function<PostPass, Boolean> m_outProcessor;

        public PostPassEntry(PostPass in, PostPass out, Function<PostPass, Boolean> inProcessor, Function<PostPass, Boolean> outProcessor)
        {
            m_in = in;
            m_out = out;
            m_inProcessor = inProcessor;
            m_outProcessor = outProcessor;
        }

        public PostPass getInPass()
        {
            return m_in;
        }

        public PostPass getOutPass()
        {
            return m_out;
        }

        public Function<PostPass, Boolean> getInProcessor()
        {
            return m_inProcessor;
        }

        public Function<PostPass, Boolean> getOutProcessor()
        {
            return m_outProcessor;
        }
    }
}