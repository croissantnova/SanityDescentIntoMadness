package croissantnova.sanitydim.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.util.math.vector.Matrix4f;

public class PostProcessor
{
    private float m_time;
    private final Framebuffer m_swapBuffer;
    private Matrix4f m_orthoMat = new Matrix4f();
    public final List<PostPassEntry> m_passEntries = new ArrayList<>();

    public PostProcessor()
    {
        Minecraft mc = Minecraft.getInstance();
        m_swapBuffer = new Framebuffer(mc.getMainRenderTarget().width, mc.getMainRenderTarget().height, false, Minecraft.ON_OSX);
        m_swapBuffer.setClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        updateOrthoMatrix();
    }

    public float getTime()
    {
        return m_time;
    }

    public PostPassEntry addPassEntry(String in, String out, Function<Shader, Boolean> inProcessor, Function<Shader, Boolean> outProcessor)
    {
        Minecraft mc = Minecraft.getInstance();
        Shader inPass;
        Shader outPass;
        try
        {
            inPass = new Shader(mc.getResourceManager(), in, mc.getMainRenderTarget(), m_swapBuffer);
            inPass.setOrthoMatrix(m_orthoMat);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        try
        {
            outPass = new Shader(mc.getResourceManager(), out, m_swapBuffer, mc.getMainRenderTarget());
            outPass.setOrthoMatrix(m_orthoMat);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        PostPassEntry entry = new PostPassEntry(inPass, outPass, inProcessor, outProcessor);
        m_passEntries.add(entry);
        return entry;
    }

    public PostPassEntry addSinglePassEntry(String in, Function<Shader, Boolean> inProcessor)
    {
        return addPassEntry(in, "blit", inProcessor, null);
    }

    public void render(float partialTicks)
    {
        m_time += partialTicks;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.isCreative() || mc.player.isSpectator() || !ConfigProxy.getRenderPost(mc.player.level.dimension().location()))
            return;

        for (PostPassEntry entry : m_passEntries)
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
        m_orthoMat = Matrix4f.orthographic((float)mc.getMainRenderTarget().width, (float)mc.getMainRenderTarget().height, .1f, 1000.0f);
        for (PostPassEntry entry : m_passEntries)
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

    public static class PostPassEntry
    {
        private final Shader m_in;
        private final Shader m_out;
        private final Function<Shader, Boolean> m_inProcessor;
        private final Function<Shader, Boolean> m_outProcessor;

        public PostPassEntry(Shader in, Shader out, Function<Shader, Boolean> inProcessor, Function<Shader, Boolean> outProcessor)
        {
            m_in = in;
            m_out = out;
            m_inProcessor = inProcessor;
            m_outProcessor = outProcessor;
        }

        public Shader getInPass()
        {
            return m_in;
        }

        public Shader getOutPass()
        {
            return m_out;
        }

        public Function<Shader, Boolean> getInProcessor()
        {
            return m_inProcessor;
        }

        public Function<Shader, Boolean> getOutProcessor()
        {
            return m_outProcessor;
        }
    }
}