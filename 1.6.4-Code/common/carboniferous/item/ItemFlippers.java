package carboniferous.item;

import java.util.logging.Level;

import carboniferous.api.CarboniferousApi;
import carboniferous.api.Properties;
import carboniferous.client.model.ModelFlippers;
import carboniferous.core.proxy.ClientProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class ItemFlippers extends ItemArmor {

	public static IIcon icon;
	
	public ItemFlippers(int par1, int par2, int par3) {
		super(par1, CarboniferousApi.armorFlippers, par2, par3);
		this.setCreativeTab(null);
	}

	@SideOnly(Side.CLIENT)
	@Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
		return ClientProxy.flippers;
    }
	
	@Override
	public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
		return armorType == 3 ? true : false;
	}
	
	@Override
	public boolean hasColor(ItemStack par1ItemStack) {
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public boolean requiresMultipleRenderPasses() {
        return false;
    }
	
	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack) {
		if(!player.capabilities.isFlying) {
			if(player.isInWater()) {
				player.motionX *= 1.17D;
				player.motionZ *= 1.17D;
				if((player.isSneaking())) {
					player.motionY = 0.0D;
				}
				//else if(!player.isSneaking() && player.isJumping){
				//	player.motionY = 0.15D;
				//}
			}
			else if(!player.isInWater() && player.onGround) {
				player.motionX *= 0.55D;
				player.motionY *= 0.53D;
				player.motionZ *= 0.55D;
			}
		}
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer) {
		return Properties.TEX_ARMOR_FLIPPERS;
    }
	
 	@SideOnly(Side.CLIENT)
 	@Override
 	public void registerIcons(IconRegister par1IconRegister) {
 		this.icon = par1IconRegister.registerIcon(Properties.TEX_PACkAGE + "flippers");
	}
 	
 	@Override
 	public IIcon getIconFromDamage(int par1) {
 	    return this.icon;
 	}
}
