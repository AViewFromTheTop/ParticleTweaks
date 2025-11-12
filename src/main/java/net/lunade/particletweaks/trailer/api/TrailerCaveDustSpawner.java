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
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class TrailerCaveDustSpawner {

	public static void tick(ClientLevel world) {
		if (!ParticleTweaksConfig.TRAILER_CAVE_DUST) return;
		final Minecraft minecraft = Minecraft.getInstance();
		final BlockPos pos = minecraft.gameRenderer.getMainCamera().getBlockPosition();
		animateTick(world, pos.getX(), pos.getY(), pos.getZ());
	}

	private static void animateTick(@NotNull ClientLevel level, int posX, int posY, int posZ) {
		final RandomSource random = level.random;
		final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
		for (int i = 0; i < 20; ++i) {
			spawnCaveDustParticles(level, posX, posY, posZ, 32, random, mutableBlockPos);
		}
		for (int i = 0; i < 20; ++i) {
			spawnCaveDustParticles(level, posX, posY, posZ, 16, random, mutableBlockPos);
		}
	}

	private static void spawnCaveDustParticles(
		@NotNull ClientLevel level, int posX, int posY, int posZ, int range, @NotNull RandomSource random, @NotNull BlockPos.MutableBlockPos blockPos
	) {
		final int i = posX + random.nextIntBetweenInclusive(-range, range);
		final int j = posY + random.nextIntBetweenInclusive(-range, range);
		final int k = posZ + random.nextIntBetweenInclusive(-range, range);
		final int heightMapY = level.getHeight(Heightmap.Types.WORLD_SURFACE, i, k);
		blockPos.set(i, j, k);

		if (heightMapY > j + random.nextInt(32)
			&& level.getBlockState(blockPos).isAir()
			&& !level.canSeeSkyFromBelowWater(blockPos)
			&& level.getBrightness(LightLayer.SKY, blockPos) == 0
			&& random.nextInt(Math.max(level.getBrightness(LightLayer.BLOCK, blockPos) - random.nextInt(10), 1)) == 0
		) {
			final int levelMin = level.getMinY();
			final int levelMax = level.getMaxY();
			final int difference = levelMax - levelMin;
			if (random.nextBoolean() && (random.nextFloat() * (posY / difference)) <= 0.0015F) {
				level.addParticle(
					ParticleTweaksParticleTypes.CAVE_DUST,
					i, j, k,
					0D, 0D, 0D
				);
			}
		}
	}
}
