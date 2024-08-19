package gtexpert.testmod.common;

import gtexpert.testmod.api.ModValues;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber(modid = ModValues.MODID)
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {}
}
