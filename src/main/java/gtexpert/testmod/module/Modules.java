package gtexpert.testmod.module;

import gtexpert.testmod.api.ModValues;
import gtexpert.testmod.api.modules.IModuleContainer;

public class Modules implements IModuleContainer {

    public static final String MODULE_CORE = "core";
    public static final String MODULE_TOOLS = "tools";
    public static final String MODULE_INTEGRATION = "integration";

    @Override
    public String getID() {
        return ModValues.MODID;
    }
}
