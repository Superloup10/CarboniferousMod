package carboniferous.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import carboniferous.ModBlocks;
import carboniferous.api.Properties;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTilledDirt extends Block
{
    public static IIcon field_94441_a;
    public static IIcon field_94440_b;
    
    public BlockTilledDirt()
    {
        super(Material.ground);
        this.setTickRandomly(true);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
        this.setLightOpacity(0);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return AxisAlignedBB.getBoundingBox(par2 + 0, par3 + 0, par4 + 0, par2 + 1, par3 + 1, par4 + 1);
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int par1, int par2)
    {
        return par1 == 1 ? (par2 > 0 ? this.field_94441_a : this.field_94440_b) : ModBlocks.dirt.getBlockTextureFromSide(par1);
    }
    
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if(!this.isWaterNearby(par1World, par2, par3, par4) && !par1World.canLightningStrikeAt(par2, par3 + 1, par4))
        {
            int l = par1World.getBlockMetadata(par2, par3, par4);
            
            if(l > 0)
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, l - 1, 2);
            }
            else if(!this.isCropsNearby(par1World, par2, par3, par4))
            {
                par1World.setBlock(par2, par3, par4, ModBlocks.dirt);
            }
        }
        else
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 7, 2);
        }
    }
    
    @Override
    public void onFallenUpon(World par1World, int par2, int par3, int par4, Entity par5Entity, float par6)
    {
        if(!par1World.isRemote && par1World.rand.nextFloat() < par6 - 0.5F)
        {
            if(!(par5Entity instanceof EntityPlayer) && !par1World.getGameRules().getGameRuleBooleanValue("mobGriefing"))
            {
                return;
            }
            
            par1World.setBlock(par2, par3, par4, ModBlocks.dirt);
        }
    }
    
    private boolean isCropsNearby(World par1World, int par2, int par3, int par4)
    {
        byte b0 = 0;
        
        for(int l = par2 - b0; l <= par2 + b0; ++l)
        {
            for(int i1 = par4 - b0; i1 <= par4 + b0; ++i1)
            {
                
                Block plant = par1World.getBlock(l, par3 + 1, i1);
                if(plant instanceof IPlantable && canSustainPlant(par1World, par2, par3, par4, ForgeDirection.UP, (IPlantable)plant))
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean isWaterNearby(World par1World, int par2, int par3, int par4)
    {
        for(int l = par2 - 4; l <= par2 + 4; ++l)
        {
            for(int i1 = par3; i1 <= par3 + 1; ++i1)
            {
                for(int j1 = par4 - 4; j1 <= par4 + 4; ++j1)
                {
                    if(par1World.getBlock(l, i1, j1).getMaterial() == Material.water)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
    {
        super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
        Material material = par1World.getBlock(par2, par3 + 1, par4).getMaterial();
        
        if(material.isSolid())
        {
            par1World.setBlock(par2, par3, par4, ModBlocks.dirt);
        }
    }
    
    @Override
    public Item getItemDropped(int par1, Random par2Random, int par3)
    {
        return Item.getItemFromBlock(ModBlocks.dirt);
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        return new ItemStack(Item.getItemFromBlock(ModBlocks.dirt));
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.field_94441_a = par1IconRegister.registerIcon(Properties.TEX_PACkAGE + "tilledDirt_wet");
        this.field_94440_b = par1IconRegister.registerIcon(Properties.TEX_PACkAGE + "tilledDirt_dry");
    }
    
    @Override
    public boolean isFertile(World world, int x, int y, int z)
    {
        return world.getBlockMetadata(x, y, z) > 0;
    }
    
    @Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plant)
    {
        EnumPlantType plantType = plant.getPlantType(world, x, y + 1, z);
        return plantType == EnumPlantType.Crop ? true : false;
    }
}