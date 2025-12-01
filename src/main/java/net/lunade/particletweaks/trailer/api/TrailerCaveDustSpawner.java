package net.lunade.particletweaks.trailer.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.levelgen.Heightmap;

@Environment(EnvType.CLIENT)
public class TrailerCaveDustSpawner {

	public static void tick(ClientLevel level) {
		if (!ParticleTweaksConfig.TRAILER_CAVE_DUST) return;
		final Minecraft minecraft = Minecraft.getInstance();
		final BlockPos pos = minecraft.gameRenderer.getMainCamera().blockPosition();
		animateTick(level, pos.getX(), pos.getY(), pos.getZ());
	}

	private static void animateTick(ClientLevel level, int posX, int posY, int posZ) {
		final RandomSource random = level.random;
		final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (int i = 0; i < 20; ++i) {
			spawnCaveDustParticles(level, posX, posY, posZ, 32, random, mutable);
		}
		for (int i = 0; i < 20; ++i) {
			spawnCaveDustParticles(level, posX, posY, posZ, 16, random, mutable);
		}
	}

	private static void spawnCaveDustParticles(ClientLevel level, int posX, int posY, int posZ, int range, RandomSource random, BlockPos.MutableBlockPos mutable) {
		final int x = posX + random.nextIntBetweenInclusive(-range, range);
		final int y = posY + random.nextIntBetweenInclusive(-range, range);
		final int z = posZ + random.nextIntBetweenInclusive(-range, range);
		final int heightMapY = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
		mutable.set(x, y, z);

		if (heightMapY <= y + random.nextInt(32)
			|| !level.getBlockState(mutable).isAir()
			|| level.canSeeSkyFromBelowWater(mutable)
			|| level.getBrightness(LightLayer.SKY, mutable) != 0
			|| random.nextInt(Math.max(level.getBrightness(LightLayer.BLOCK, mutable) - random.nextInt(10), 1)) != 0
		) return;

		final int levelMin = level.getMinY();
		final int levelMax = level.getMaxY();
		final int difference = levelMax - levelMin;
		if (random.nextBoolean() || (random.nextFloat() * (posY / difference)) > 0.0015F) return;

		level.addParticle(
			ParticleTweaksParticleTypes.CAVE_DUST,
			x, y, z,
			0D, 0D, 0D
		);
	}
}
