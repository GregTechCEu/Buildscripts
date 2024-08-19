package gtexpert.testmod.api.modules;


import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.*;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * All modules must implement this interface.
 * <p>
 * Provides methods for responding to FML lifecycle events, adding event bus subscriber classes, and processing IMC
 * messages.
 */
public interface IModule {

    /**
     * What other modules this module depends on.
     * <p>
     * e.g. <code>new ResourceLocation("testmod", "foo_module")</code> represents a dependency on the module
     * "foo_module" in the container "testmod"
     */
    @NotNull
    default Set<ResourceLocation> getDependencyUids() {
        return Collections.emptySet();
    }

    default void construction(FMLConstructionEvent event) {}

    default void preInit(FMLPreInitializationEvent event) {}

    default void init(FMLInitializationEvent event) {}

    default void postInit(FMLPostInitializationEvent event) {}

    default void loadComplete(FMLLoadCompleteEvent event) {}

    default void serverAboutToStart(FMLServerAboutToStartEvent event) {}

    default void serverStarting(FMLServerStartingEvent event) {}

    default void serverStarted(FMLServerStartedEvent event) {}

    default void serverStopping(FMLServerStoppingEvent event) {}

    default void serverStopped(FMLServerStoppedEvent event) {}

    default void registerItems(RegistryEvent.Register<Item> event) {}

    default void registerBlocks(RegistryEvent.Register<Block> event) {}

    default void registerRecipesHighest(RegistryEvent.Register<IRecipe> event) {}

    default void registerRecipesHigh(RegistryEvent.Register<IRecipe> event) {}

    default void registerRecipesNormal(RegistryEvent.Register<IRecipe> event) {}

    default void registerRecipesLow(RegistryEvent.Register<IRecipe> event) {}

    default void registerRecipesLowest(RegistryEvent.Register<IRecipe> event) {}

    /**
     * Register packets using TestMod's packet handling API here.
     */
    default void registerPackets() {}

    /**
     * @return A list of classes to subscribe to the Forge event bus.
     *         As the class gets subscribed, not any specific instance, event handlers must be static!
     */
    @NotNull
    default List<Class<?>> getEventBusSubscribers() {
        return Collections.emptyList();
    }

    default boolean processIMC(FMLInterModComms.IMCMessage message) {
        return false;
    }

    /**
     * @return A logger to use for this module.
     */
    @NotNull
    Logger getLogger();
}