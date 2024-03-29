package minecrafttransportsimulator.mcinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import minecrafttransportsimulator.MasterLoader;
import minecrafttransportsimulator.items.components.AItemBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

/**Interface to the core MC system.  This class has methods for registrations
 * file locations, and other core things that are common to clients and servers.
 * Client-specific things go into {@link InterfaceClient}, rendering goes into
 * {@link InterfaceRender}.
 *
 * @author don_bruce
 */
@SuppressWarnings("deprecation")
public class InterfaceCore{
	private static final List<String> queuedLogs = new ArrayList<String>();
	
	/**
	 *  Returns the game version for this current instance.
	 */
	public static String getGameVersion(){
		return Loader.instance().getMCVersionString().substring("Minecraft ".length());
	}
	
	/**
	 *  Returns true if the mod with the passed-in modID is present.
	 */
	public static boolean isModPresent(String modID){
		return Loader.isModLoaded(modID);
	}
	
	/**
	 *  Returns the text-based name for the passed-in mod.
	 */
	public static String getModName(String modID){
		return Loader.instance().getIndexedModList().get(modID).getName();
	}
	
	/**
	 *  Returns a new stack for the passed-in item.  Note that this is only valid for items
	 *  that have {@link AItemBase#autoGenerate()} as true.
	 */
	public static WrapperItemStack getAutoGeneratedStack(AItemBase item, WrapperNBT data){
		WrapperItemStack newStack = new WrapperItemStack(new ItemStack(BuilderItem.itemMap.get(item)));
		newStack.setData(data);
		return newStack;
	}
	
	/**
	 *  Returns a new stack for the item properties.  Or an empty stack if the name is invalid.
	 */
	public static WrapperItemStack getStackForProperties(String name, int meta, int qty){
		return new WrapperItemStack(new ItemStack(Item.getByNameOrId(name), qty, meta));
	}
	
	/**
	 *  Returns the registry name for the passed-in stack.  Can be used in conjunction with
	 *  {@link #getStackForProperties(String, int, int)} to get a new stack later.
	 */
	public static String getStackItemName(WrapperItemStack stack){
		return Item.REGISTRY.getNameForObject(stack.stack.getItem()).toString();
	}
	
	/**
	 *  Returns true if both stacks are Oredict compatible.
	 */
	public static boolean isOredictMatch(WrapperItemStack stackA, WrapperItemStack stackB){
		return OreDictionary.itemMatches(stackA.stack, stackB.stack, false);
	}
	
	/**
	 *  Returns all possible stacks that could be used for the passed-in OreDict name.
	 */
	public static List<WrapperItemStack> getOredictMaterials(String oreName){
		NonNullList<ItemStack> oreDictStacks = OreDictionary.getOres(oreName, true);
		List<WrapperItemStack> stacks = new ArrayList<WrapperItemStack>();
		for(ItemStack stack : oreDictStacks){
			stacks.add(new WrapperItemStack(stack.copy()));
		}
		return stacks;
	}
	
	/**
	 *  Returns the text-based name for the passed-in fluid.
	 *  Returns "INVALID" if the name does not exist.
	 */
	public static String getFluidName(String fluidID){
		return FluidRegistry.getFluid(fluidID) != null ? new FluidStack(FluidRegistry.getFluid(fluidID), 1).getLocalizedName() : "INVALID";
	}
	
	/**
	 *  Returns all fluids currently in the game.
	 */
	public static Map<String, String> getAllFluids(){
		Map<String, String> fluidIDsToNames = new HashMap<String, String>();
		for(String fluidID : FluidRegistry.getRegisteredFluids().keySet()){
			fluidIDsToNames.put(fluidID, new FluidStack(FluidRegistry.getFluid(fluidID), 1).getLocalizedName());
		}
		return fluidIDsToNames;
	}

	/**
	 *  Returns the translation of the passed-in text from the lang file.
	 *  Put here to prevent the need for referencing the MC class directly, which
	 *  will change during updates.
	 */
	public static String translate(String text){
		return  I18n.translateToLocal(text);
	}
	
	/**
	 *  Logs an error to the logging system.  Used when things don't work right.
	 */
	public static void logError(String message){
		if(MasterLoader.logger == null){
			queuedLogs.add(MasterLoader.MODID.toUpperCase() + "ERROR: " + message);
		}else{
			MasterLoader.logger.error(MasterLoader.MODID.toUpperCase() + "ERROR: " + message);
		}
	}
	
	/**
     * Called to send queued logs to the logger.  This is required as the logger
     * gets created during pre-init, but logs can be generated during construction.
     */
    public static void flushLogQueue(){
    	for(String log : queuedLogs){
    		logError(log);
    	}
    }
}
