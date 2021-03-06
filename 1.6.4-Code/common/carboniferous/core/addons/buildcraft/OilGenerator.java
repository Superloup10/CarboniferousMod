package carboniferous.core.addons.buildcraft;

import java.util.Random;

import carboniferous.CarboniferousMod;
import carboniferous.ModBlocks;
import carboniferous.world.biome.BiomeGenBaseCarboniferous;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class OilGenerator implements IWorldGenerator {

	private final BuildcraftApi	api;

	OilGenerator(BuildcraftApi api) {
		this.api = api;
	}

    private void doPopulate(Random rand, World world, int x, int z) {
		BiomeGenBase biome = world.getWorldChunkManager().getBiomeGenAt(x, z);

		if (biome.biomeID == BiomeGenBaseCarboniferous.calamitesSwamp.biomeID && rand.nextFloat() > 0.96)
		{
			// Generate a small deposit

			final int startX = rand.nextInt(10) + 2;
			final int startZ = rand.nextInt(10) + 2;

			for (int j = 128; j > 65; --j) {
				final int i = startX + x;
				final int k = startZ + z;

				final int id = world.getBlockId(i, j, k);
				if (id != 0) {
					if (id == ModBlocks.grass.blockID) {
						api.generateSurfaceDeposit(world, rand, i, j, k, 3);
					}
					break;
				}
			}
		}
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		chunkX = chunkX << 4;
		chunkZ = chunkZ << 4;

		this.doPopulate(random, world, chunkX, chunkZ);
	}

}
