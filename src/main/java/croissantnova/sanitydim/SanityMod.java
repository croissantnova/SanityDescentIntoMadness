package croissantnova.sanitydim;

import croissantnova.sanitydim.capability.*;
import croissantnova.sanitydim.client.GuiHandler;
import croissantnova.sanitydim.client.render.RendererRottingStalker;
import croissantnova.sanitydim.config.ConfigHandler;
import croissantnova.sanitydim.entity.EntityRegistry;
import croissantnova.sanitydim.event.ModEventHandler;
import croissantnova.sanitydim.event.EventHandler;
import croissantnova.sanitydim.item.ItemRegistry;
import croissantnova.sanitydim.sound.SoundRegistry;
import croissantnova.sanitydim.net.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = SanityMod.MODID, name = SanityMod.NAME, version = SanityMod.VERSION)
public class SanityMod
{
    @SideOnly(Side.CLIENT)
    private GuiHandler m_gui;

    private static SanityMod m_inst;
    public static final String MODID = "sanitydim";
    public static final String NAME = "Sanity: Descent Into Madness";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LogManager.getLogger();

    public SanityMod()
    {
        m_inst = this;

        ConfigHandler.register();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(ModEventHandler::addEntityAttributes);
        modEventBus.addListener(ModEventHandler::onConfigLoading);
//        modEventBus.addListener(ModEventHandler::registerOverlaysEvent);
//        modEventBus.addListener(ModEventHandler::registerEntityRenderersEvent);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        EntityRegistry.register(modEventBus);
        ItemRegistry.register(modEventBus);
        SoundRegistry.register(modEventBus);
    }

    static
    {
        ConfigHandler.init();
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        PacketHandler.init();
        CapabilityManager.INSTANCE.register(ISanity.class, new SanityStorage(), Sanity::new);
        CapabilityManager.INSTANCE.register(IInnerEntityCap.class, new InnerEntityCapImplStorage(), InnerEntityCapImpl::new);
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        initGui();
//        getGui().initOverlays();

        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.ROTTING_STALKER.get(), RendererRottingStalker::new);
        //EntityRenderers.register(EntityRegistry.SHADE_CHOMPER.get(), RendererShadeChomper::new);
    }

    @SideOnly(Side.CLIENT)
    public void initGui()
    {
        if (m_gui == null) m_gui = new GuiHandler();
    }

    @SideOnly(Side.CLIENT)
    public GuiHandler getGui()
    {
        return m_gui;
    }

    public static SanityMod getInstance()
    {
        return m_inst;
    }
}