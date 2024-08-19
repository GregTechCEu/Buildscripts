package gtexpert.testmod.module;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import gtexpert.testmod.api.ModValues;
import gtexpert.testmod.api.modules.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class ModuleManager implements IModuleManager {

    private static final ModuleManager INSTANCE = new ModuleManager();
    private static final String MODULE_CFG_FILE_NAME = "modules.cfg";
    private static final String MODULE_CFG_CATEGORY_NAME = "modules";
    private static File configFolder;

    private Map<String, IModuleContainer> containers = new LinkedHashMap<>();
    private final Map<ResourceLocation, IModule> sortedModules = new LinkedHashMap<>();
    private final Set<IModule> loadedModules = new LinkedHashSet<>();

    private IModuleContainer currentContainer;

    private ModuleStage currentStage = ModuleStage.C_SETUP;
    private final Logger logger = LogManager.getLogger("GTExpert Module Loader");
    private Configuration config;

    private ModuleManager() {}

    public static ModuleManager getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isModuleEnabled(ResourceLocation id) {
        return sortedModules.containsKey(id);
    }

    public boolean isModuleEnabled(IModule module) {
        TModule annotation = module.getClass().getAnnotation(TModule.class);
        String comment = getComment(module);
        Property prop = getConfiguration().get(MODULE_CFG_CATEGORY_NAME,
                annotation.containerID() + ":" + annotation.moduleID(), true, comment);
        return prop.getBoolean();
    }

    @Override
    public IModuleContainer getLoadedContainer() {
        return currentContainer;
    }

    @Override
    public ModuleStage getStage() {
        return currentStage;
    }

    @Override
    public boolean hasPassedStage(ModuleStage stage) {
        return currentStage.ordinal() > stage.ordinal();
    }

    @Override
    public void registerContainer(IModuleContainer container) {
        if (currentStage != ModuleStage.C_SETUP) {
            logger.error("Failed to register module container {}, as module loading has already begun", container);
            return;
        }
        Preconditions.checkNotNull(container);
        containers.put(container.getID(), container);
    }

    public void setup(ASMDataTable asmDataTable, File configDirectory) {
        // find and register all containers registered with the @ModuleContainer annotation, then sort them by container
        // name
        discoverContainers(asmDataTable);
        containers = containers.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));

        currentStage = ModuleStage.M_SETUP;
        configFolder = new File(configDirectory, ModValues.MODID);
        Map<String, List<IModule>> modules = getModules(asmDataTable);
        configureModules(modules);

        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.getLogger().debug("Registering event handlers");
            for (Class<?> clazz : module.getEventBusSubscribers()) {
                MinecraftForge.EVENT_BUS.register(clazz);
            }
        }
    }

    public void onConstruction(FMLConstructionEvent event) {
        currentStage = ModuleStage.CONSTRUCTION;
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.getLogger().debug("Construction start");
            module.construction(event);
            module.getLogger().debug("Construction complete");
        }
    }

    public void onPreInit(FMLPreInitializationEvent event) {
        currentStage = ModuleStage.PRE_INIT;
        // Separate loops for strict ordering
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.getLogger().debug("Registering packets");
            module.registerPackets();
        }
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.getLogger().debug("Pre-init start");
            module.preInit(event);
            module.getLogger().debug("Pre-init complete");
        }
    }

    public void onInit(FMLInitializationEvent event) {
        currentStage = ModuleStage.INIT;
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.getLogger().debug("Init start");
            module.init(event);
            module.getLogger().debug("Init complete");
        }
    }

    public void onPostInit(FMLPostInitializationEvent event) {
        currentStage = ModuleStage.POST_INIT;
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.getLogger().debug("Post-init start");
            module.postInit(event);
            module.getLogger().debug("Post-init complete");
        }
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        currentStage = ModuleStage.FINISHED;
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.getLogger().debug("Load-complete start");
            module.loadComplete(event);
            module.getLogger().debug("Load-complete complete");
        }
    }

    public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        currentStage = ModuleStage.SERVER_ABOUT_TO_START;
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.getLogger().debug("Server-about-to-start start");
            module.serverAboutToStart(event);
            module.getLogger().debug("Server-about-to-start complete");
        }
    }

    public void onServerStarting(FMLServerStartingEvent event) {
        currentStage = ModuleStage.SERVER_STARTING;
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.getLogger().debug("Server-starting start");
            module.serverStarting(event);
            module.getLogger().debug("Server-starting complete");
        }
    }

    public void onServerStarted(FMLServerStartedEvent event) {
        currentStage = ModuleStage.SERVER_STARTED;
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.getLogger().debug("Server-started start");
            module.serverStarted(event);
            module.getLogger().debug("Server-started complete");
        }
    }

    public void onServerStopping(FMLServerStoppingEvent event) {
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.serverStopping(event);
        }
    }

    public void onServerStopped(FMLServerStoppedEvent event) {
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.serverStopped(event);
        }
    }

    public void processIMC(ImmutableList<FMLInterModComms.IMCMessage> messages) {
        for (FMLInterModComms.IMCMessage message : messages) {
            for (IModule module : loadedModules) {
                if (module.processIMC(message)) {
                    break;
                }
            }
        }
    }

    public void registerBlocks(RegistryEvent.Register<Block> event) {
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.registerBlocks(event);
        }
    }

    public void registerItems(RegistryEvent.Register<Item> event) {
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.registerItems(event);
        }
    }

    public void registerRecipesHighest(RegistryEvent.Register<IRecipe> event) {
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.registerRecipesHighest(event);
        }
    }

    public void registerRecipesHigh(RegistryEvent.Register<IRecipe> event) {
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.registerRecipesHigh(event);
        }
    }

    public void registerRecipesNormal(RegistryEvent.Register<IRecipe> event) {
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.registerRecipesNormal(event);
        }
    }

    public void registerRecipesLow(RegistryEvent.Register<IRecipe> event) {
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.registerRecipesLow(event);
        }
    }

    public void registerRecipesLowest(RegistryEvent.Register<IRecipe> event) {
        for (IModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.registerRecipesLowest(event);
        }
    }

    private void configureModules(Map<String, List<IModule>> modules) {
        Locale locale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
        Set<ResourceLocation> toLoad = new LinkedHashSet<>();
        Set<IModule> modulesToLoad = new LinkedHashSet<>();
        Configuration config = getConfiguration();
        config.load();
        config.addCustomCategoryComment(MODULE_CFG_CATEGORY_NAME,
                "Module configuration file. Can individually enable/disable modules from GTExpert and its addons");

        for (IModuleContainer container : containers.values()) {
            String containerID = container.getID();
            List<IModule> containerModules = modules.get(containerID);
            IModule coreModule = getCoreModule(containerModules);
            if (coreModule == null) {
                throw new IllegalStateException("Could not find core module for module container " + containerID);
            } else {
                containerModules.remove(coreModule);
                containerModules.add(0, coreModule);
            }

            logger.debug("containterModule size: " + containerModules.size());

            // Remove disabled modules and gather potential modules to load
            Iterator<IModule> iterator = containerModules.iterator();
            while (iterator.hasNext()) {
                IModule module = iterator.next();
                if (!isModuleEnabled(module)) {
                    iterator.remove();
                    logger.debug("Module disabled: {}", module);
                    continue;
                }
                TModule annotation = module.getClass().getAnnotation(TModule.class);
                toLoad.add(new ResourceLocation(containerID, annotation.moduleID()));
                modulesToLoad.add(module);
            }
        }

        // Check any module dependencies
        Iterator<IModule> iterator;
        boolean changed;
        do {
            changed = false;
            iterator = modulesToLoad.iterator();
            while (iterator.hasNext()) {
                IModule module = iterator.next();

                // Check module dependencies
                Set<ResourceLocation> dependencies = module.getDependencyUids();
                if (!toLoad.containsAll(dependencies)) {
                    iterator.remove();
                    changed = true;
                    TModule annotation = module.getClass().getAnnotation(TModule.class);
                    String moduleID = annotation.moduleID();
                    toLoad.remove(new ResourceLocation(moduleID));
                    logger.debug("Module {} is missing at least one of module dependencies: {}, skipping loading...",
                            moduleID, dependencies);
                }
            }
        } while (changed);

        // Sort modules by their module dependencies
        do {
            changed = false;
            iterator = modulesToLoad.iterator();
            while (iterator.hasNext()) {
                IModule module = iterator.next();
                if (sortedModules.keySet().containsAll(module.getDependencyUids())) {
                    iterator.remove();
                    TModule annotation = module.getClass().getAnnotation(TModule.class);
                    sortedModules.put(new ResourceLocation(annotation.containerID(), annotation.moduleID()), module);
                    changed = true;
                    break;
                }
            }
        } while (changed);

        loadedModules.addAll(sortedModules.values());

        if (config.hasChanged()) {
            config.save();
        }
        Locale.setDefault(locale);
    }

    private static IModule getCoreModule(List<IModule> modules) {
        for (IModule module : modules) {
            TModule annotation = module.getClass().getAnnotation(TModule.class);
            if (annotation.coreModule()) {
                return module;
            }
        }
        return null;
    }

    private static String getContainerID(IModule module) {
        TModule annotation = module.getClass().getAnnotation(TModule.class);
        return annotation.containerID();
    }

    private Map<String, List<IModule>> getModules(ASMDataTable table) {
        List<IModule> instances = getInstances(table);
        Map<String, List<IModule>> modules = new LinkedHashMap<>();
        for (IModule module : instances) {
            TModule info = module.getClass().getAnnotation(TModule.class);
            modules.computeIfAbsent(info.containerID(), k -> new ArrayList<>()).add(module);
        }
        return modules;
    }

    @SuppressWarnings("unchecked")
    private List<IModule> getInstances(ASMDataTable table) {
        Set<ASMDataTable.ASMData> dataSet = table.getAll(TModule.class.getCanonicalName());
        List<IModule> instances = new ArrayList<>();
        for (ASMDataTable.ASMData data : dataSet) {
            String moduleID = (String) data.getAnnotationInfo().get("moduleID");
            List<String> modDependencies = (ArrayList<String>) data.getAnnotationInfo().get("modDependencies");
            if (modDependencies == null || modDependencies.stream().allMatch(Loader::isModLoaded)) {
                try {
                    Class<?> clazz = Class.forName(data.getClassName());
                    instances.add((IModule) clazz.newInstance());
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    logger.error("Could not initialize module " + moduleID, e);
                }
            } else {
                logger.debug("Module {} is missing at least one of mod dependencies: {}, skipping loading...", moduleID,
                        modDependencies);
            }
        }
        return instances.stream().sorted((m1, m2) -> {
            TModule m1a = m1.getClass().getAnnotation(TModule.class);
            TModule m2a = m2.getClass().getAnnotation(TModule.class);
            return (m1a.containerID() + ":" + m1a.moduleID()).compareTo(m2a.containerID() + ":" + m2a.moduleID());
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private void discoverContainers(ASMDataTable table) {
        Set<ASMDataTable.ASMData> dataSet = table.getAll(ModuleContainer.class.getCanonicalName());
        for (ASMDataTable.ASMData data : dataSet) {
            try {
                Class<?> clazz = Class.forName(data.getClassName());
                registerContainer((IModuleContainer) clazz.newInstance());
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                logger.error("Could not initialize module container " + data.getClassName(), e);
            }
        }
    }

    private String getComment(IModule module) {
        TModule annotation = module.getClass().getAnnotation(TModule.class);

        String comment = annotation.description();
        Set<ResourceLocation> dependencies = module.getDependencyUids();
        if (!dependencies.isEmpty()) {
            Iterator<ResourceLocation> iterator = dependencies.iterator();
            StringBuilder builder = new StringBuilder(comment);
            builder.append("\n");
            builder.append("Module Dependencies: [ ");
            builder.append(iterator.next());
            while (iterator.hasNext()) {
                builder.append(", ").append(iterator.next());
            }
            builder.append(" ]");
            comment = builder.toString();
        }
        String[] modDependencies = annotation.modDependencies();
        if (modDependencies != null && modDependencies.length > 0) {
            Iterator<String> iterator = Arrays.stream(modDependencies).iterator();
            StringBuilder builder = new StringBuilder(comment);
            builder.append("\n");
            builder.append("Mod Dependencies: [ ");
            builder.append(iterator.next());
            while (iterator.hasNext()) {
                builder.append(", ").append(iterator.next());
            }
            builder.append(" ]");
            comment = builder.toString();
        }
        return comment;
    }

    private Configuration getConfiguration() {
        if (config == null) {
            config = new Configuration(new File(configFolder, MODULE_CFG_FILE_NAME));
        }
        return config;
    }
}
