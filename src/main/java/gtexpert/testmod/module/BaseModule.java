package gtexpert.testmod.module;

import gtexpert.testmod.api.modules.IModule;
import gtexpert.testmod.api.util.ModUtility;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public abstract class BaseModule implements IModule {

    @NotNull
    @Override
    public Set<ResourceLocation> getDependencyUids() {
        return Collections.singleton(ModUtility.id(Modules.MODULE_CORE));
    }
}
