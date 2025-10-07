package net.lunade.particletweaks.impl;

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
import org.jetbrains.annotations.NotNull;

public class TorchParticleUtil {

	public static void onAnimateTick(
		@NotNull BlockPos pos
	) {
		BlockPos immutablePos = pos.immutable();
		if (!TORCH_TICKS.containsKey(immutablePos)) {
			TORCH_TICKS.put(immutablePos, 0);
		}
	}

	private static final Map<BlockPos, Integer> TORCH_TICKS = new Object2ObjectLinkedOpenHashMap<>();

	public static void clearTorches() {
		TORCH_TICKS.clear();
	}

	public static void clearTorchesInChunk(ChunkPos chunkPos) {
		List<BlockPos> posesToRemove = new ArrayList<>();
		TORCH_TICKS.forEach((blockPos, integer) -> {
			if (new ChunkPos(blockPos).equals(chunkPos)) {
				posesToRemove.add(blockPos);
			}
		});

		posesToRemove.forEach(TORCH_TICKS::remove);
	}

	public static void tickTorches(ClientLevel world) {
		if (!ParticleTweaksConfig.TRAILER_TORCHES) {
			TORCH_TICKS.clear();
			return;
		}

		Minecraft minecraft = Minecraft.getInstance();
		BlockPos cameraPos = minecraft.gameRenderer.getMainCamera().getBlockPosition();

		List<BlockPos> posesToRemove = new ArrayList<>();
		TORCH_TICKS.forEach((blockPos, tickCount) -> {
			if (!onTorchTick(world, blockPos, world.getBlockState(blockPos), world.random, tickCount, cameraPos)) {
				posesToRemove.add(blockPos);
			}
		});

		posesToRemove.forEach(TORCH_TICKS::remove);

		for (BlockPos pos : TORCH_TICKS.keySet()) {
			int tickCount = TORCH_TICKS.getOrDefault(pos, 0) + 1;
			TORCH_TICKS.put(pos, tickCount);
		}
	}

	public static boolean onTorchTick(
		@NotNull Level world,
		BlockPos pos,
		@NotNull BlockState state,
		RandomSource random,
		int tickCount,
		BlockPos cameraPos
	) {
		if (!world.isLoaded(pos)) return false;

		if (!(state.getBlock() instanceof TorchBlock torchBlock)) return false;

		int camDifferenceX = Math.abs(cameraPos.getZ() - pos.getZ());
		int camDifferenceZ = Math.abs(cameraPos.getZ() - pos.getZ());
		if ((camDifferenceX > 16 || camDifferenceZ > 16) && random.nextBoolean()) return true;
		if ((camDifferenceX > 32 || camDifferenceZ > 32)) return true;

		boolean isSoulTorch = torchBlock.flameParticle == ParticleTypes.SOUL_FIRE_FLAME;
		ParticleOptions flareParticle = isSoulTorch ? ParticleTweaksParticleTypes.SOUL_FLARE : ParticleTweaksParticleTypes.FLARE;

		double x = pos.getX() + 0.5D;
		double y = pos.getY() + 0.6D;
		double z = pos.getZ() + 0.5D;

		if (state.getBlock() instanceof WallTorchBlock) {
			Direction direction = state.getValue(WallTorchBlock.FACING);
			Direction opposite = direction.getOpposite();
			x += 0.27D * (double)opposite.getStepX();
			y += 0.15;
			z += 0.27 * (double)opposite.getStepZ();
		}

		if (tickCount % 5 == 0) {
			world.addParticle(flareParticle, x, y - 0.05, z, 0.0D, 0.0D, 0.0D);
		}

		if (tickCount % 8 == 0) {
			world.addParticle(
				tickCount % 16 == 0 ?
					ParticleTweaksParticleTypes.COMFY_SMOKE_B : ParticleTweaksParticleTypes.COMFY_SMOKE_A,
				x,
				y,
				z,
				0.0D,
				0.0D,
				0.0D
			);
		}

		return true;
	}

}
