package gtexpert.testmod.api.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public enum Mods {

    AEAdditions(Names.AE_ADDITIONS),
    AE2FluidCrafting(Names.AE2_FLUID_CRAFTING),
    AppliedEnergistics2(Names.APPLIED_ENERGISTICS2),
    Avaritia(Names.AVARITIA),
    Avaritiaaddons(Names.AVARITIAADDONS),
    Baubles(Names.BAUBLES),
    Botany(Names.BOTANY),
    Chisel(Names.CHISEL),
    CTM(Names.CONNECTED_TEXTURES_MOD),
    CraftTweaker(Names.CRAFT_TWEAKER),
    DraconicAdditions(Names.DRACONIC_ADDITIONS),
    DraconicEvolution(Names.DRACONIC_EVOLUTION),
    EnderCore(Names.ENDER_CORE),
    EnderIO(Names.ENDER_IO),
    EnderIOEndergy(Names.ENDER_ENDERGY),
    EnderIOMachines(Names.ENDER_MACHINES),
    EnderIOConduits(Names.ENDER_CONDUITS),
    EnderIOAE2Conduits(Names.ENDER_AE2_CONDUITS),
    ExtraBees(Names.EXTRA_BEES),
    ExtraCPUs(Names.EXTRA_CPUS),
    ExtraTrees(Names.EXTRA_TREES),
    Forestry(Names.FORESTRY),
//    ForestryApiculture(Names.FORESTRY, forestryModule(Names.FORESTRY_APICULTURE)),
//    ForestryArboriculture(Names.FORESTRY, forestryModule(Names.FORESTRY_ARBORICULTURE)),
//    ForestryCharcoal(Names.FORESTRY, forestryModule(Names.FORESTRY_CHARCOAL)),
//    ForestryCore(Names.FORESTRY, forestryModule(Names.FORESTRY_CORE)),
//    ForestryEnergy(Names.FORESTRY, forestryModule(Names.FORESTRY_ENERGY)),
//    ForestryFactory(Names.FORESTRY, forestryModule(Names.FORESTRY_FACTORY)),
//    ForestryWorktable(Names.FORESTRY, forestryModule(Names.FORESTRY_WORKTABLE)),
//    ForestryFarming(Names.FORESTRY, forestryModule(Names.FORESTRY_FARMING)),
//    ForestryClimatology(Names.FORESTRY, forestryModule(Names.FORESTRY_CLIMATOLOGY)),
//    ForestryGreenhouse(Names.FORESTRY, forestryModule(Names.FORESTRY_GREENHOUSE)),
//    ForestryFluids(Names.FORESTRY, forestryModule(Names.FORESTRY_FLUIDS)),
//    ForestryFood(Names.FORESTRY, forestryModule(Names.FORESTRY_FOOD)),
//    ForestryLepidopterology(Names.FORESTRY, forestryModule(Names.FORESTRY_LEPIDOPTEROLOGY)),
//    ForestryMail(Names.FORESTRY, forestryModule(Names.FORESTRY_MAIL)),
//    ForestryCrate(Names.FORESTRY, forestryModule(Names.FORESTRY_CRATE)),
//    ForestryBackpacks(Names.FORESTRY, forestryModule(Names.FORESTRY_BACKPACKS)),
//    ForestryDatabase(Names.FORESTRY, forestryModule(Names.FORESTRY_DATABASE)),
//    ForestrySorting(Names.FORESTRY, forestryModule(Names.FORESTRY_SORTING)),
//    ForestryBook(Names.FORESTRY, forestryModule(Names.FORESTRY_BOOK)),
//    ForestryCultivation(Names.FORESTRY, forestryModule(Names.FORESTRY_CULTIVATION)),
//    ForestryResearch(Names.FORESTRY, forestryModule(Names.FORESTRY_RESEARCH)),
    GalacticraftCore(Names.GALACTICRAFT_CORE),
    Genetics(Names.GENETICS),
    Gendustry(Names.GENDUSTRY),
    GregicalityMultiblocks(Names.GREGICALITY_MULTIBLOCKS),
    GregTech(Names.GREGTECH),
    GregTechFoodOption(Names.GREGTECH_FOOD_OPTION),
    GroovyScript(Names.GROOVY_SCRIPT),
    HWYLA(Names.HWYLA),
    InventoryTweaks(Names.INVENTORY_TWEAKS),
    JourneyMap(Names.JOURNEY_MAP),
    JustEnoughItems(Names.JUST_ENOUGH_ITEMS),
    MagicBees(Names.MAGIC_BEES),
    ModularUI(Names.MODULRAUI),
    MixinBooter(Names.MIXINBOOTER),
    NeevesAE2(Names.NEEVES_AE2),
    Nothirium(Names.NOTHIRIUM),
    NuclearCraft(Names.NUCLEAR_CRAFT, versionExcludes("2o")),
    NuclearCraftOverhauled(Names.NUCLEAR_CRAFT, versionContains("2o")),
    OpenComputers(Names.OPEN_COMPUTERS),
    ProjectRedCore(Names.PROJECT_RED_CORE),
    ProjectRedIllumination(Names.PROJECT_RED_ILLUMINATION),
    Railcraft(Names.RAILCRAFT),
    RefinedStorage(Names.REFINED_STORAGE),
    Thaumcraft(Names.THAUMCRAFT),
    ThaumicEnergistics(Names.THAUMIC_ENERGISTICS),
    TheOneProbe(Names.THE_ONE_PROBE),
    TinkersConstruct(Names.TINKERS_CONSTRUCT),
    TOPAddons(Names.TOP_ADDONS),
    Vanilla(Names.VANILLA),

    // Special Optifine handler, but consolidated here for simplicity
    Optifine(null) {

        @Override
        public boolean isModLoaded() {
            if (this.modLoaded == null) {
                try {
                    Class<?> c = Class.forName("net.optifine.shaders.Shaders");
                    Field f = c.getDeclaredField("shaderPackLoaded");
                    f.setAccessible(true);
                    this.modLoaded = f.getBoolean(null);
                } catch (Exception ignored) {
                    this.modLoaded = false;
                }
            }
            return this.modLoaded;
        }
    };

    public static class Names {

        public static final String AE_ADDITIONS = "aeadditions";
        public static final String AE2_FLUID_CRAFTING = "ae2fc";
        public static final String APPLIED_ENERGISTICS2 = "appliedenergistics2";
        public static final String AVARITIA = "avaritia";
        public static final String AVARITIAADDONS = "avaritiaddons";
        public static final String BAUBLES = "baubles";
        public static final String BOTANY = "botany";
        public static final String CHISEL = "chisel";
        public static final String CONNECTED_TEXTURES_MOD = "ctm";
        public static final String CRAFT_TWEAKER = "crafttweaker";
        public static final String DRACONIC_ADDITIONS = "draconicadditions";
        public static final String DRACONIC_EVOLUTION = "draconicevolution";
        public static final String ENDER_CORE = "endercore";
        public static final String ENDER_IO = "enderio";
        public static final String ENDER_ENDERGY = "enderioendergy";
        public static final String ENDER_MACHINES = "enderiomachines";
        public static final String ENDER_CONDUITS = "enderioconduits";
        public static final String ENDER_AE2_CONDUITS = "enderioconduitsappliedenergistics";
        public static final String EXTRA_BEES = "extrabees";
        public static final String EXTRA_CPUS = "extracpus";
        public static final String EXTRA_TREES = "extratrees";
        public static final String FORESTRY = "forestry";
        public static final String FORESTRY_APICULTURE = "apiculture";
        public static final String FORESTRY_ARBORICULTURE = "arboriculture";
        public static final String FORESTRY_CHARCOAL = "charcoal";
        public static final String FORESTRY_CORE = "core";
        public static final String FORESTRY_ENERGY = "energy";
        public static final String FORESTRY_FACTORY = "factory";
        public static final String FORESTRY_WORKTABLE = "worktable";
        public static final String FORESTRY_FARMING = "farming";
        public static final String FORESTRY_CLIMATOLOGY = "climatology";
        public static final String FORESTRY_GREENHOUSE = "greenhouse";
        public static final String FORESTRY_FLUIDS = "fluids";
        public static final String FORESTRY_FOOD = "food";
        public static final String FORESTRY_LEPIDOPTEROLOGY = "lepidopterology";
        public static final String FORESTRY_MAIL = "mail";
        public static final String FORESTRY_CRATE = "crates";
        public static final String FORESTRY_BACKPACKS = "backpacks";
        public static final String FORESTRY_DATABASE = "database";
        public static final String FORESTRY_SORTING = "sorting";
        public static final String FORESTRY_BOOK = "book";
        public static final String FORESTRY_CULTIVATION = "cultivation";
        public static final String FORESTRY_RESEARCH = "research";
        public static final String GALACTICRAFT_CORE = "galacticraftcore";
        public static final String GENETICS = "genetics";
        public static final String GENDUSTRY = "gendustry";
        public static final String GREGICALITY_MULTIBLOCKS = "gcym";
        public static final String GREGTECH = "gregtech";
        public static final String GREGTECH_FOOD_OPTION = "gregtechfoodoption";
        public static final String GROOVY_SCRIPT = "groovyscript";
        public static final String HWYLA = "hwyla";
        public static final String INVENTORY_TWEAKS = "inventorytweaks";
        public static final String JOURNEY_MAP = "journeymap";
        public static final String JUST_ENOUGH_ITEMS = "jei";
        public static final String MAGIC_BEES = "magicbees";
        public static final String MODULRAUI = "modularui";
        public static final String MIXINBOOTER = "mixinbooter";
        public static final String NEEVES_AE2 = "nae2";
        public static final String NOTHIRIUM = "nothirium";
        public static final String NUCLEAR_CRAFT = "nuclearcraft";
        public static final String OPEN_COMPUTERS = "opencomputers";
        public static final String PROJECT_RED_CORE = "projred-core";
        public static final String PROJECT_RED_ILLUMINATION = "projectred-illumination";
        public static final String RAILCRAFT = "railcraft";
        public static final String REFINED_STORAGE = "refinedstorage";
        public static final String THAUMCRAFT = "thaumcraft";
        public static final String THAUMIC_ENERGISTICS = "haumicenergistics";
        public static final String THE_ONE_PROBE = "theoneprobe";
        public static final String TINKERS_CONSTRUCT = "tconstruct";
        public static final String TOP_ADDONS = "topaddons";
        public static final String VANILLA = "minecraft";
    }

    private final String ID;
    private final Function<Mods, Boolean> extraCheck;
    protected Boolean modLoaded;

    Mods(String ID) {
        this.ID = ID;
        this.extraCheck = null;
    }

    /**
     * @param extraCheck A supplier that can be used to test additional factors, such as
     *                   checking if a mod is at a specific version, or a sub-mod is loaded.
     *                   Used in cases like NC vs NCO, where the mod id is the same
     *                   so the version has to be parsed to test which is loaded.
     *                   Another case is checking for specific Forestry modules, checking
     *                   if Forestry is loaded and if a specific module is enabled.
     */
    Mods(String ID, Function<Mods, Boolean> extraCheck) {
        this.ID = ID;
        this.extraCheck = extraCheck;
    }

    public boolean isModLoaded() {
        if (this.modLoaded == null) {
            this.modLoaded = Loader.isModLoaded(this.ID);
            if (this.modLoaded) {
                if (this.extraCheck != null && !this.extraCheck.apply(this)) {
                    this.modLoaded = false;
                }
            }
        }
        return this.modLoaded;
    }

    /**
     * Throw an exception if this mod is found to be loaded.
     * <strong>This must be called in or after
     * {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent}!</strong>
     */
    public void throwIncompatibilityIfLoaded(String... customMessages) {
        if (isModLoaded()) {
            String modName = TextFormatting.BOLD + ID + TextFormatting.RESET;
            List<String> messages = new ArrayList<>();
            messages.add(modName + " mod detected, this mod is incompatible with GregTech CE Unofficial.");
            messages.addAll(Arrays.asList(customMessages));
            if (FMLLaunchHandler.side() == Side.SERVER) {
                throw new RuntimeException(String.join(",", messages));
            } else {
                throwClientIncompatibility(messages);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private static void throwClientIncompatibility(List<String> messages) {
        throw new ModIncompatibilityException(messages);
    }

    public ItemStack getItem(@NotNull String name) {
        return getItem(name, 1, 0, null);
    }

    @NotNull
    public ItemStack getItem(@NotNull String name, int count) {
        return getItem(name, count, 0, null);
    }

    @NotNull
    public ItemStack getItem(@NotNull String name, int count, int meta) {
        return getItem(name, count, meta, null);
    }

    @NotNull
    public ItemStack getItem(@NotNull String name, int count, int meta, @Nullable String nbt) {
        // The following statement is intentional.
        return GameRegistry.makeItemStack(ID + ":" + name, meta, count, nbt);
    }

    @NotNull
    public ResourceLocation getResource(@NotNull String path) {
        return new ResourceLocation(ID, path);
    }

    // Helpers for the extra checker

    /** Test if the mod version string contains the passed value. */
    private static Function<Mods, Boolean> versionContains(String versionPart) {
        return mod -> {
            if (mod.ID == null) return false;
            if (!mod.isModLoaded()) return false;
            ModContainer container = Loader.instance().getIndexedModList().get(mod.ID);
            if (container == null) return false;
            return container.getVersion().contains(versionPart);
        };
    }

    /** Test if the mod version string does not contain the passed value. */
    private static Function<Mods, Boolean> versionExcludes(String versionPart) {
        return mod -> {
            if (mod.ID == null) return false;
            if (!mod.isModLoaded()) return false;
            ModContainer container = Loader.instance().getIndexedModList().get(mod.ID);
            if (container == null) return false;
            return !container.getVersion().contains(versionPart);
        };
    }

    /** Test if a specific Forestry module is enabled. */
//    private static Function<Mods, Boolean> forestryModule(String moduleID) {
//        if (Forestry.isModLoaded()) {
//            return mod -> forestry.modules.ModuleHelper.isEnabled(moduleID);
//        } else {
//            return $ -> false;
//        }
//    }
}
