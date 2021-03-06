package carboniferous.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import carboniferous.CarboniferousMod;
import carboniferous.api.CarboniferousApi;
import carboniferous.api.Properties;
import carboniferous.tileentity.TileEntityGrinder;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class BlockGrinder extends BlockTileEntity {

	public static IIcon iconSideGrinder;
	
	public BlockGrinder() {
		super(Material.rock);
		this.setCreativeTab(null);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityGrinder();
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (par1World.isRemote) {
            return true;
        }
        else {
            TileEntityGrinder tileentitygrinder = (TileEntityGrinder)par1World.getTileEntity(par2, par3, par4);

            if (tileentitygrinder != null) {
                par5EntityPlayer.openGui(CarboniferousMod.INSTANCE, Properties.GUI_ID_GRINDER, par1World, par2, par3, par4);
            }

            return true;
        }
    }
	
	@SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int metadata) {
       if(side == 1) {
    	   return this.blockIcon;
       }
       return this.iconSideGrinder;
    }
	
	@SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.blockIcon = par1IconRegister.registerIcon(Properties.TEX_PACkAGE + "topGrinder");
        this.iconSideGrinder = par1IconRegister.registerIcon(Properties.TEX_PACkAGE + "sideGrinder");
    }
	
	@Override
	public int getRenderType() {
	    return Properties.RENDER_GRINDER;
	}
	
	@Override
    public boolean isOpaqueCube() {
        return false;
    }

	@Override
    public boolean renderAsNormalBlock() {
        return false;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		TileEntity tileEntity = par1World.getTileEntity(par2, par3, par4);
		if(tileEntity instanceof TileEntityGrinder) {
			if(((TileEntityGrinder)tileEntity).clientSpinTime < 80) {
				for(int var1 = 0; var1 < 2; ++var1) {
					double d0 = (double)((float)par2 + 0.2F + par5Random.nextFloat() * 0.6F);
					double d1 = (double)((float)par3 + 0.4F + par5Random.nextFloat() * 0.6F);
					double d2 = (double)((float)par4 + 0.2F + par5Random.nextFloat() * 0.6F);
					par1World.spawnParticle("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
				}
			}
		}
    }
}
