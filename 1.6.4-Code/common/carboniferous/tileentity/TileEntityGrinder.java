package carboniferous.tileentity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.logging.Level;

import carboniferous.api.Properties;
import carboniferous.core.helper.ItemStackHelper;
import carboniferous.recipe.CarboniferousRecipes;
import carboniferous.recipe.GrinderManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeDirection;

/**
 * @author ProPercivalalb
 **/
public class TileEntityGrinder extends TileEntityCarboniferous implements ISidedInventory {

	//Variables about the tank
	public int   defaultGrindSpeed	   = 200;
	public int   numberOfSlots 	  	   = 6;
	
	public int  slotInput1Index        = 0;
	public int  slotInput2Index        = 1;
	public int  slotOutput1Index       = 2;
	public int  slotOutput2Index       = 3;
	public int  slotGrindingItemIndex  = 4;
	public int  slotFuelIndex          = 5;
	
	//Variables that can change
	public double grindSpeedIncrement = 1;
	public float rotation = 0.0F;
	public float clientSpinTime = 81;
	public int grindingStoneDamage = 0;
    public int grinderBurnTime = 0;
    public int currentItemBurnTime = 0;
	
	private ItemStack[] contents = new ItemStack[numberOfSlots];
	public int grindTime;

	@Override
	public int getSizeInventory() {return contents.length;}
	@Override
    public ItemStack getStackInSlot(int par1) {return this.contents[par1];}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
        if (this.contents[par1] != null) {ItemStack itemstack;
        	if (this.contents[par1].stackSize <= par2) {
        		itemstack = this.contents[par1]; 
        		this.contents[par1] = null; 
        		return itemstack; 
        	}
            else {
                itemstack = this.contents[par1].splitStack(par2);
                if (this.contents[par1].stackSize == 0) {
                    this.contents[par1] = null;
                }
                return itemstack;
            }
        }
        else {
            return null;
        }
    }

	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (this.contents[par1] != null) {
            ItemStack itemstack = this.contents[par1];
            this.contents[par1] = null;
            return itemstack;
        }
        else {
            return null;
        }
    }

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		this.contents[par1] = par2ItemStack;
        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    public boolean isInvNameLocalized() { return customName != null && customName.length() > 0; }
    public String getInvName() { return (this.isInvNameLocalized() ? this.customName : invName()); }
    public void setInvName(String customName) { this.customName = customName; }
	
	@Override
	public int getInventoryStackLimit() {return 64;}
	
	@Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;}
    
    @Override
    public void openChest() {}
    @Override
    public void closeChest() {}
    
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		return true;
	}

	//The slots it can put the items in from a certain side
	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		switch(var1) {
		case 0: return new int[] {0, 1}; //Bottom
		case 1: return new int[] {0, 1}; //Top
		case 2: return new int[] {2, 3}; //
		case 3: return new int[] {2, 3}; //
		case 4: return new int[] {2, 3}; // 
		case 5: return new int[] {2, 3}; //
		default: return null;
		}
	}

	//Whether the items in valid going into a certain side
	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return this.isItemValidForSlot(i, itemstack);
	}

	//Weather the items going out are valid from a certain slot
	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return true;
	}
	
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
	    super.readFromNBT(par1NBTTagCompound);
	    NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
	    this.contents = new ItemStack[this.getSizeInventory()];

	    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
	        NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
	        byte b0 = nbttagcompound1.getByte("Slot");

	        if (b0 >= 0 && b0 < this.contents.length) {
	            this.contents[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
	        }
	    }
	    
	    this.grindTime = par1NBTTagCompound.getShort("GrindTime");
        this.grinderBurnTime = par1NBTTagCompound.getShort("BurnTime");
        this.currentItemBurnTime = getItemBurnTime(this.contents[slotFuelIndex]);
	}

	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
	    super.writeToNBT(par1NBTTagCompound);
	    NBTTagList nbttaglist = new NBTTagList();
	    par1NBTTagCompound.setShort("GrindTime", (short)this.grindTime);
        par1NBTTagCompound.setShort("BurnTime", (short)this.grinderBurnTime);

	    for (int i = 0; i < this.contents.length; ++i) {
	        if (this.contents[i] != null) {
	            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
	            nbttagcompound1.setByte("Slot", (byte)i);
	            this.contents[i].writeToNBT(nbttagcompound1);
	            nbttaglist.appendTag(nbttagcompound1);
	        }
	    }
	    
	    par1NBTTagCompound.setTag("Items", nbttaglist);
	}

	@Override
	public boolean shouldSendUpdates() {
		return true;
	}
	
	@Override
	public String invName() {
		return "container.grinder";
	}
	
	public boolean hasGrindingStone() {
		if(this.contents[this.slotGrindingItemIndex] == null) return false;
		int dur = CarboniferousRecipes.getComponentDurability(this.contents[this.slotGrindingItemIndex]);
		if(dur != -1 || dur != 0) {
			return true;
		}
		return false;
	}
	
	public void damageGrindingStone(int damage) {
		if(this.contents[this.slotGrindingItemIndex] == null) return;
		int dur = CarboniferousRecipes.getComponentDurability(this.contents[this.slotGrindingItemIndex]);
		if(dur != -1 || dur != 0) {
			ItemStackHelper.makeSureItemHasTagCompound(this.contents[this.slotGrindingItemIndex]);
			int oldDamage = ItemStackHelper.getInt(this.contents[this.slotGrindingItemIndex], "grindDamage");
			if(dur < oldDamage) {
				this.contents[this.slotGrindingItemIndex] = null;
			}
			else {
				ItemStackHelper.setInteger(this.contents[this.slotGrindingItemIndex], "grindDamage", oldDamage + damage);
			}
		}
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		if (this.worldObj.isRemote) {
			this.clientSpinTime++;
		}
		
        if (this.grinderBurnTime > 0) {
            --this.grinderBurnTime;
        }

		
		if (!this.worldObj.isRemote) {
			if (this.grinderBurnTime == 0 && this.canGrind() && this.hasGrindingStone()) {
                this.currentItemBurnTime = this.grinderBurnTime = getItemBurnTime(this.contents[slotFuelIndex]);

                if (this.grinderBurnTime > 0) {

                    if (this.contents[slotFuelIndex] != null) {
                        --this.contents[slotFuelIndex].stackSize;

                        if (this.contents[slotFuelIndex].stackSize == 0)
                        {
                            this.contents[slotFuelIndex] = this.contents[slotFuelIndex].getItem().getContainerItemStack(contents[slotFuelIndex]);
                        }
                    }
                }
            }
			
			if (this.isBurning() && this.canGrind() && this.hasGrindingStone()) {
	            ++this.grindTime;
	            this.damageGrindingStone(1);
	            if(this.grindTime % 20 == 18) {
	            	try {
	            		Packet250CustomPayload pack = new Packet250CustomPayload();
	       		 		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	       		 		DataOutputStream data = new DataOutputStream(bytes);
	       		 		data.writeInt(xCoord);
	       		 		data.writeInt(yCoord);
	       		 		data.writeInt(zCoord);
	       		 		data.writeInt(this.worldObj.rand.nextInt(4));
	       		 		pack.channel = Properties.PACKET_GRIND_SOUND;
	       		 		pack.data = bytes.toByteArray();
	       		 		pack.length = bytes.size();
	        	    	WorldServer server = (WorldServer)this.worldObj;
	        	    	server.getMinecraftServer().getConfigurationManager().sendToAllNear(this.xCoord, this.yCoord, this.zCoord, 30D, this.worldObj.provider.dimensionId, pack);
	            	}
	            	catch(Exception e) {
	            		e.printStackTrace();
	            	}
	            }
	            if (this.grindTime >= defaultGrindSpeed) {
	                this.grindTime = 0;
	                this.grindItem();
	            }
	            
	            
	        }
	        else {
	            this.grindTime = 0;
	        }
		}
	}
	  
	private boolean canGrind() {
	    ItemStack itemstack = GrinderManager.findMatchingRecipe(this.contents[0], this.contents[1], this.worldObj);
	    if (itemstack == null) return false;
	    if (this.contents[slotOutput1Index] == null || this.contents[slotOutput2Index] == null) return true;
        if(this.contents[slotOutput1Index].isItemEqual(itemstack)) {
        	 int result = this.contents[slotOutput1Index].stackSize + itemstack.stackSize;
        	 if(result <= getInventoryStackLimit() && result <= itemstack.getMaxStackSize()) {
        		 return true;
        	 }
        }
        if(this.contents[slotOutput2Index].isItemEqual(itemstack)) {
       	 	int result = this.contents[slotOutput2Index].stackSize + itemstack.stackSize;
       	 	if(result <= getInventoryStackLimit() && result <= itemstack.getMaxStackSize()) {
       		 	return true;
       	 	}
       }
        return false;
	}

	public void grindItem() {
	    if (this.canGrind()) {
	        ItemStack itemstack = GrinderManager.findMatchingRecipe(this.contents[0], this.contents[1], this.worldObj);
	        int result1 = (this.contents[slotOutput1Index] != null ? this.contents[slotOutput1Index].stackSize : 0) + itemstack.stackSize;
	        int result2 =  (this.contents[slotOutput2Index] != null ? this.contents[slotOutput2Index].stackSize : 0) + itemstack.stackSize;
	        
	        if (this.contents[slotOutput1Index] == null) {
	            this.contents[slotOutput1Index] = itemstack.copy();
	        }
	        else if (this.contents[slotOutput1Index].isItemEqual(itemstack) && result1 <= getInventoryStackLimit() && result1 <= itemstack.getMaxStackSize()) {
	        	this.contents[slotOutput1Index].stackSize += itemstack.stackSize;
	        }
	        else if (this.contents[slotOutput2Index] == null) {
	            this.contents[slotOutput2Index] = itemstack.copy();
	        }
	        else if (this.contents[slotOutput2Index].isItemEqual(itemstack) && result2 <= getInventoryStackLimit() && result2 <= itemstack.getMaxStackSize()) {
	        	this.contents[slotOutput2Index].stackSize += itemstack.stackSize;
	        }

	        if(this.contents[0] != null) {
	        	--this.contents[0].stackSize;
		        if (this.contents[0].stackSize <= 0) {
		            this.contents[0] = null;
		        }
	        }
	        
	        if(this.contents[1] != null) {
	        	--this.contents[1].stackSize;
		        if (this.contents[1].stackSize <= 0) {
		            this.contents[1] = null;
		        }
	        }
	    }
	}
	
	public int getGrindProgressScaled(int i) {
		return this.grindTime * i / defaultGrindSpeed;
	}

	public int getGrindingStoneDamageScaled(int i) {
		if(this.contents[this.slotGrindingItemIndex] == null) return 0;
		int dur = CarboniferousRecipes.getComponentDurability(this.contents[this.slotGrindingItemIndex]);
		if(dur != -1 || dur != 0) {
			return ItemStackHelper.getInt(this.contents[this.slotGrindingItemIndex], "grindDamage") * i / dur;
		}
		return 0;
	}
	
	public static int getItemBurnTime(ItemStack par0ItemStack) {
		if (par0ItemStack == null) {
	        return 0;
	    }
	    else {
	    	int i = par0ItemStack.getItem().itemID;
	        Item item = par0ItemStack.getItem();

	        if (par0ItemStack.getItem() instanceof ItemBlock && Block.blocksList[i] != null) {
	            Block block = Block.blocksList[i];

	            if (block == Block.woodSingleSlab) {
	              return 150 * 3;
	            }

	            if (block.blockMaterial == Material.wood) {
	                return 300 * 3;
	            }
	        }

	        if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD")) return 200 * 3;
	        if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD")) return 200 * 3;
	        if (item instanceof ItemHoe && ((ItemHoe) item).getMaterialName().equals("WOOD")) return 200 * 3;
	        if (i == Item.stick.itemID) return 100 * 3;
	        if (i == Item.coal.itemID) return 1600 * 3;
	        if (i == Item.bucketLava.itemID) return 20000 * 3;
	        if (i == Block.sapling.blockID) return 100 * 3;
	        if (i == Item.blazeRod.itemID) return 2400 * 3;
	        return GameRegistry.getFuelValue(par0ItemStack) * 3;
	    }
	}

	public static boolean isItemFuel(ItemStack par0ItemStack) {
		return getItemBurnTime(par0ItemStack) > 0;
	}
	
    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int par1) {
        if (this.currentItemBurnTime == 0)
        {
            this.currentItemBurnTime = 200;
        }

        return this.grinderBurnTime * par1 / this.currentItemBurnTime;
    }
    
    public boolean isBurning() {
        return this.grinderBurnTime > 0;
    }
}
