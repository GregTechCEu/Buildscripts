package gtexpert.testmod.core;

import gtexpert.testmod.Tags;
import gtexpert.testmod.api.ModValues;
import gtexpert.testmod.api.modules.IModule;
import gtexpert.testmod.api.modules.TModule;
import gtexpert.testmod.common.CommonProxy;
import gtexpert.testmod.module.Modules;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;


@TModule(
        moduleID = Modules.MODULE_CORE,
        containerID = ModValues.MODID,
        name = "TestMod Core",
        description = "Core of TestMod",
        coreModule = true
)
public class CoreModule implements IModule {
    public static final Logger logger = LogManager.getLogger(Tags.MODNAME + " Core");
    @SidedProxy(modId = ModValues.MODID,
            clientSide = "gtexpert.testmod.client.ClientProxy",
            serverSide = "gtexpert.testmod.common.CommonProxy")
    public static CommonProxy proxy;

    @Override
    public @NotNull Logger getLogger() {
        return logger;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);

        logger.info("Hello World!");
    }
}
