package carboniferous.world.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenLimestone extends WorldGeneratorCarboniferous {
	
    /** The block ID of the ore to be placed using this generator. */
    private int minableBlockId;
    private int minableBlockMeta = 0;

    /** The number of blocks to generate. */
    private int numberOfBlocks;
    private int field_94523_c;

    public WorldGenLimestone(int par1, int par2)
    {
        this(par1, par2, Block.stone.blockID);
    }

    public WorldGenLimestone(int par1, int par2, int par3)
    {
    	super(false);
        this.minableBlockId = par1;
        this.numberOfBlocks = par2;
        this.field_94523_c = par3;
    }

    public WorldGenLimestone(int id, int meta, int number, int target)
    {
        this(id, number, target);
        minableBlockMeta = meta;
    }

    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        float f = par2Random.nextFloat() * (float)Math.PI;
        double d0 = (double)((float)(par3 + 8) + MathHelper.sin(f) * (float)this.numberOfBlocks / 8.0F);
        double d1 = (double)((float)(par3 + 8) - MathHelper.sin(f) * (float)this.numberOfBlocks / 8.0F);
        double d2 = (double)((float)(par5 + 8) + MathHelper.cos(f) * (float)this.numberOfBlocks / 8.0F);
        double d3 = (double)((float)(par5 + 8) - MathHelper.cos(f) * (float)this.numberOfBlocks / 8.0F);
        double d4 = (double)(par4 + par2Random.nextInt(3) - 2);
        double d5 = (double)(par4 + par2Random.nextInt(3) - 2);

        for (int l = 0; l <= this.numberOfBlocks; ++l)
        {
            double d6 = d0 + (d1 - d0) * (double)l / (double)this.numberOfBlocks;
            double d7 = d4 + (d5 - d4) * (double)l / (double)this.numberOfBlocks;
            double d8 = d2 + (d3 - d2) * (double)l / (double)this.numberOfBlocks;
            double d9 = par2Random.nextDouble() * (double)this.numberOfBlocks / 16.0D;
            double d10 = (double)(MathHelper.sin((float)l * (float)Math.PI / (float)this.numberOfBlocks) + 1.0F) * d9 + 1.0D;
            double d11 = (double)(MathHelper.sin((float)l * (float)Math.PI / (float)this.numberOfBlocks) + 1.0F) * d9 + 1.0D;
            int i1 = MathHelper.floor_double(d6 - d10 / 2.0D);
            int j1 = MathHelper.floor_double(d7 - d11 / 2.0D);
            int k1 = MathHelper.floor_double(d8 - d10 / 2.0D);
            int l1 = MathHelper.floor_double(d6 + d10 / 2.0D);
            int i2 = MathHelper.floor_double(d7 + d11 / 2.0D);
            int j2 = MathHelper.floor_double(d8 + d10 / 2.0D);

            for (int k2 = i1; k2 <= l1; ++k2)
            {
                double d12 = ((double)k2 + 0.5D - d6) / (d10 / 2.0D);

                if (d12 * d12 < 1.0D)
                {
                    for (int l2 = j1; l2 <= i2; ++l2)
                    {
                        double d13 = ((double)l2 + 0.5D - d7) / (d11 / 2.0D);

                        if (d12 * d12 + d13 * d13 < 1.0D)
                        {
                            for (int i3 = k1; i3 <= j2; ++i3)
                            {
                                double d14 = ((double)i3 + 0.5D - d8) / (d10 / 2.0D);

                                Block block = Block.blocksList[this.getBlockIfChunkExists(par1World, k2, l2, i3)];
                                if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D && (block != null && block.isGenMineableReplaceable(par1World, k2, l2, i3, field_94523_c)))
                                {
                                    this.setBlockandMetadataIfChunkExists(par1World, k2, l2, i3, this.minableBlockId, minableBlockMeta);
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
    
    public void setBlockandMetadataIfChunkExists(World world, int x, int y, int z, int blockId, int metadata) {
		if (world.getChunkProvider().chunkExists(x >> 4, z >> 4)){
			try{
				world.setBlock(x, y, z, blockId, metadata, 2);
			}catch(Exception e){
				//Work around to stop crash
			}
		}
	}
}
