package carboniferous.core.addons.buildcraft;

import java.util.Collection;
import java.util.logging.Level;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;

import carboniferous.CarboniferousMod;
import carboniferous.ModBlocks;
import carboniferous.api.Properties;
import carboniferous.core.helper.LogHelper;
import carboniferous.core.addons.PluginEvent;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * @author ProPercivalalb
 **/
public class BuildcraftPlugin {

    private static final String MOD_ID   = "BuildCraft|Core";
    private static final String MOD_NAME = "BuildCraft";
    private Optional<BuildcraftApi> api = Optional.absent();

    @ForgeSubscribe
    public void init(PluginEvent.Init event) {
        if (!api.isPresent()) {
        	return;
        }
        for(int var1 = 0; var1 < 4; ++var1) {
            this.api.get().addBuildcraftFacade(ModBlocks.logs_1.blockID, var1);
            this.api.get().addBuildcraftFacade(ModBlocks.planks_1.blockID, var1);
        }
        this.api.get().addBuildcraftFacade(ModBlocks.dirt.blockID, 0);
        for(int var1 = 0; var1 < 5; ++var1) {
        	this.api.get().addBuildcraftFacade(ModBlocks.multiBlock1.blockID, var1);
        }
        this.addOilSpawns();
    }
    
    private void addOilSpawns() {
		if (!api.get().modifyWorld() || !api.isPresent()) {
			return;
		}

		GameRegistry.registerWorldGenerator(new OilGenerator(api.get()));
	}

    @ForgeSubscribe
    public void postInit(PluginEvent.Post event) {
        api = Optional.absent();
    }

    @ForgeSubscribe
    public void preInit(PluginEvent.Pre event) {
        if (!Loader.isModLoaded(MOD_ID)) {
        	return;
        }
        LogHelper.log(Level.INFO, "Loading " + MOD_NAME + " Addon");
        try {
            api = Optional.of(new BuildcraftApi());
        } 
        catch (final Exception ex) {
            ex.printStackTrace();
            LogHelper.log(Level.WARNING, "Failed to load " + MOD_NAME + " Addon");
            api = Optional.absent();
        }
    }

}
