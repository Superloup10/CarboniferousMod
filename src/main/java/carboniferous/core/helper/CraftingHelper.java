package carboniferous.core.helper;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import carboniferous.ModBlocks;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * @author ProPercivalalb
 */
public class CraftingHelper
{
    
    public static void addShapedRecipe(Object item, Object[] ingredientArray)
    {
        ItemStack result = ItemStackHelper.convertObjectToItemStack(item);
        GameRegistry.addRecipe(result, ingredientArray);
    }
    
    public static void addShapelessRecipe(Object item, Object[] ingredientArray)
    {
        ItemStack result = ItemStackHelper.convertObjectToItemStack(item);
        GameRegistry.addShapelessRecipe(result, ingredientArray);
    }
    
    public static void addRecipe(IRecipe irecipe)
    {
        GameRegistry.addRecipe(irecipe);
    }
    
    public static void addArmorRecipe(Item helmet, Item chestplate, Item leggings, Item boots, Item material)
    {
        Item[] output = new Item[] {helmet, chestplate, leggings, boots};
        ItemStack input = new ItemStack(material, 1, 10);
        Object[][] template = new Object[][] {new Object[] {"XXX", "X X", 'X', input}, new Object[] {"X X", "XXX", "XXX", 'X', input}, new Object[] {"XXX", "X X", "X X", 'X', input}, new Object[] {"X X", "X X", 'X', input}};
        for(int i = 0; i < output.length; i++)
            GameRegistry.addShapedRecipe(new ItemStack(output[i]), template[i]);
    }
    
    public static void addToolsRecipe(Item sword, Item pickaxe, Item axe, Item shovel, Item hoe, Block material)
    {
        Item[] output = new Item[] {sword, pickaxe, shovel};
        ItemStack input = new ItemStack(material, 1, 1);
        Item stick = Items.stick;
        Object[][] template = new Object[][] {new Object[] {"X", "X", "Y", 'X', input, 'Y', stick}, new Object[] {"XXX", " Y ", " Y ", 'X', input, 'Y', stick}, new Object[] {"X", "Y", "Y", 'X', input, 'Y', stick}};
        for(int i = 0; i < output.length; i++)
            GameRegistry.addShapedRecipe(new ItemStack(output[i]), template[i]);
        GameRegistry.addShapedRecipe(new ItemStack(axe), new Object[] {"XX", "XY", " Y", 'X', input, 'Y', stick});
        GameRegistry.addShapedRecipe(new ItemStack(axe), new Object[] {"XX", "YX", "Y ", 'X', input, 'Y', stick});
        GameRegistry.addShapedRecipe(new ItemStack(hoe), new Object[] {"XX", "Y ", "Y ", 'X', input, 'Y', stick});
        GameRegistry.addShapedRecipe(new ItemStack(hoe), new Object[] {"XX", " Y", " Y", 'X', input, 'Y', stick});
    }
    
    public static void addStairsRecipe(Block[] output, Block material)
    {
        int j = 0;
        ItemStack input = null;
        for(int i = 0; i < output.length; i++)
        {
            if(material.equals(ModBlocks.multiBlock1) && j < 5)
            {
                j = j + 2;
                input = new ItemStack(material, 1, j);
                GameRegistry.addShapedRecipe(new ItemStack(output[i], 4), new Object[] {"X  ", "XX ", "XXX", 'X', input});
                GameRegistry.addShapedRecipe(new ItemStack(output[i], 4), new Object[] {"  X", " XX", "XXX", 'X', input});
            }
            input = new ItemStack(material, 1, i);
            GameRegistry.addShapedRecipe(new ItemStack(output[i], 4), new Object[] {"X  ", "XX ", "XXX", 'X', input});
            GameRegistry.addShapedRecipe(new ItemStack(output[i], 4), new Object[] {"  X", " XX", "XXX", 'X', input});
        }
    }
    
    public static void addSlabRecipe(Block output, Block material, int[] metadata)
    {
        for(int i = 0; i < metadata.length; i++)
            GameRegistry.addShapedRecipe(new ItemStack(output, 6, metadata[i]), new Object[] {"XXX", 'X', new ItemStack(material, 1, metadata[i])});
    }
    
    public static void addEnchantedRecipe(Object item, Enchantment enchantment, int enchantmentLevel, Object[] ingredientArray)
    {
        ItemStack result = ItemStackHelper.convertObjectToItemStack(item);
        if(enchantment != null)
        {
            ItemStackHelper.addEnchantment(result, enchantment, enchantmentLevel);
        }
        
        GameRegistry.addRecipe(result, ingredientArray);
    }
}
