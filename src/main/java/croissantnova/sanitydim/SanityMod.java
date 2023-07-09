package croissantnova.sanitydim;

import com.mojang.logging.LogUtils;
import croissantnova.sanitydim.client.GuiHandler;
import croissantnova.sanitydim.config.ConfigManager;
import croissantnova.sanitydim.entity.EntityRegistry;
import croissantnova.sanitydim.event.EventHandler;
import croissantnova.sanitydim.event.ModEventHandler;
import croissantnova.sanitydim.item.ItemRegistry;
import croissantnova.sanitydim.net.PacketHandler;
import croissantnova.sanitydim.sound.SoundRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(SanityMod.MODID)
public class SanityMod
{
    @OnlyIn(Dist.CLIENT)
    private GuiHandler m_gui;

    private static SanityMod m_inst;
    public static final String MODID = "sanitydim";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SanityMod()
    {
        m_inst = this;

        ConfigManager.register();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(ModEventHandler::addEntityAttributes);
        modEventBus.addListener(ModEventHandler::onConfigLoading);
        modEventBus.addListener(ModEventHandler::registerEntityRenderersEvent);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        EntityRegistry.register(modEventBus);
        ItemRegistry.register(modEventBus);
        SoundRegistry.register(modEventBus);
    }

    static
    {
        ConfigManager.init();
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        PacketHandler.init();
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        initGui();
        getGui().initOverlays();
    }

    @OnlyIn(Dist.CLIENT)
    public void initGui()
    {
        if (m_gui == null) m_gui = new GuiHandler();
    }

    @OnlyIn(Dist.CLIENT)
    public GuiHandler getGui()
    {
        return m_gui;
    }

    public static SanityMod getInstance()
    {
        return m_inst;
    }
}