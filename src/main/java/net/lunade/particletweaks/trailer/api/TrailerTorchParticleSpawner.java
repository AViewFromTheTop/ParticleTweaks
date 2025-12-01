package net.lunade.particletweaks.trailer.api;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TrailerTorchParticleSpawner {

	public static void onAnimateTick(BlockPos pos) {
		final BlockPos immutablePos = pos.immutable();
		if (!TORCH_TICKS.containsKey(immutablePos)) TORCH_TICKS.put(immutablePos, 0);
	}

	private static final Map<BlockPos, Integer> TORCH_TICKS = new Object2ObjectLinkedOpenHashMap<>();

	public static void clearTorches() {
		TORCH_TICKS.clear();
	}

	public static void clearTorchesInChunk(ChunkPos chunkPos) {
		final List<BlockPos> posesToRemove = new ArrayList<>();
		TORCH_TICKS.forEach((blockPos, integer) -> {
			if (new ChunkPos(blockPos).equals(chunkPos)) posesToRemove.add(blockPos);
		});

		posesToRemove.forEach(TORCH_TICKS::remove);
	}

	public static void tickTorches(ClientLevel level) {
		if (!ParticleTweaksConfig.TRAILER_TORCHES) {
			TORCH_TICKS.clear();
			return;
		}

		final Minecraft minecraft = Minecraft.getInstance();
		final BlockPos cameraPos = minecraft.gameRenderer.getMainCamera().blockPosition();

		final List<BlockPos> posesToRemove = new ArrayList<>();
		TORCH_TICKS.forEach((blockPos, tickCount) -> {
			if (!onTorchTick(level, blockPos, level.getBlockState(blockPos), level.random, tickCount, cameraPos)) posesToRemove.add(blockPos);
		});

		posesToRemove.forEach(TORCH_TICKS::remove);

		for (BlockPos pos : TORCH_TICKS.keySet()) {
			final int tickCount = TORCH_TICKS.getOrDefault(pos, 0) + 1;
			TORCH_TICKS.put(pos, tickCount);
		}
	}

	public static boolean onTorchTick(
		Level level,
		BlockPos pos,
		BlockState state,
		RandomSource random,
		int tickCount,
		BlockPos cameraPos
	) {
		if (!level.isLoaded(pos)) return false;
		if (!(state.getBlock() instanceof TorchBlock torchBlock)) return false;

		final int camDifferenceX = Math.abs(cameraPos.getZ() - pos.getZ());
		final int camDifferenceZ = Math.abs(cameraPos.getZ() - pos.getZ());
		if ((camDifferenceX > 16 || camDifferenceZ > 16) && random.nextBoolean()) return true;
		if ((camDifferenceX > 32 || camDifferenceZ > 32)) return true;

		double x = pos.getX() + 0.5D;
		double y = pos.getY() + 0.6D;
		double z = pos.getZ() + 0.5D;

		if (state.getBlock() instanceof WallTorchBlock) {
			final Direction opposite = state.getValue(WallTorchBlock.FACING).getOpposite();
			x += 0.27D * (double)opposite.getStepX();
			y += 0.15;
			z += 0.27 * (double)opposite.getStepZ();
		}

		final ParticleOptions flareParticle =
			torchBlock.flameParticle == ParticleTypes.SOUL_FIRE_FLAME ? ParticleTweaksParticleTypes.SOUL_FLARE
				: torchBlock.flameParticle == ParticleTypes.COPPER_FIRE_FLAME ? ParticleTweaksParticleTypes.COPPER_FLARE
				: ParticleTweaksParticleTypes.FLARE;
		if (tickCount % 5 == 0) level.addParticle(flareParticle, x, y - 0.05D, z, 0D, 0D, 0D);

		if (tickCount % 8 == 0) {
			level.addParticle(
				tickCount % 16 == 0 ? ParticleTweaksParticleTypes.COMFY_SMOKE_B : ParticleTweaksParticleTypes.COMFY_SMOKE_A,
				x, y, z,
				0D, 0D, 0D
			);
		}

		return true;
	}

}
