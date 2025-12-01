package net.lunade.particletweaks.trailer.api;

import java.util.ArrayList;
import java.util.List;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class TrailerFluidParticleSpawner {

	public static boolean isUnderFluid(Level level, double x, double y, double z) {
		final BlockPos pos = BlockPos.containing(x, y, z);
		final FluidState fluidState = level.getBlockState(pos).getFluidState();
		return !fluidState.isEmpty() && (fluidState.getHeight(level, pos) + (float) pos.getY()) >= y;
	}

	public static void onAnimateTick(
		Level level,
		BlockPos pos,
		FluidState state,
		RandomSource random,
		int maxCount,
		int horizontalChance,
		int downChance,
		boolean horizontalParticles,
		boolean createCascades,
		ParticleOptions particle
	) {
		final boolean isSource = state.isSource();
		final Vec3 rawFlow = state.getFlow(level, pos);
		if (!(!isSource || rawFlow.horizontalDistance() != 0D) || state.isEmpty()) return;

		final int count = maxCount == 1 ? 1 : random.nextInt(1, maxCount);
		final Vec3 flowVec = rawFlow.normalize();
		final float fluidHeight = state.getHeight(level, pos);
		final boolean isDown = state.getValue(FlowingFluid.FALLING);
		if ((isDown || horizontalParticles) && random.nextInt(isDown ? downChance : horizontalChance) == 0 && ParticleTweaksConfig.TRAILER_FLOWING_FLUIDS) {
			if (!isDown) {
				final List<Direction> possibleFlowingDirections = new ArrayList<>();
				Vec3 flow1 = new Vec3(flowVec.x, 0D, 0D);
				Vec3 flow2 = new Vec3(0D, 0D, flowVec.z);
				if (flow1.horizontalDistance() > 0D) possibleFlowingDirections.add(Direction.getApproximateNearest(flow1));
				if (flow2.horizontalDistance() > 0D) possibleFlowingDirections.add(Direction.getApproximateNearest(flow2));

				if (!possibleFlowingDirections.isEmpty()) {
					for (int i = 0; i < count; i++) {
						spawnParticleFromDirection(
							level,
							pos,
							possibleFlowingDirections.get((int) (Math.random() * possibleFlowingDirections.size())),
							1,
							false,
							0D,
							0.225D,
							0.3D,
							fluidHeight,
							random,
							particle
						);
					}
				}
			} else {
				for (Direction direction : Direction.Plane.HORIZONTAL) {
					spawnParticleFromDirection(
						level,
						pos,
						direction,
						count,
						true,
						0D,
						0.075D,
						0.1D,
						fluidHeight,
						random,
						particle
					);
				}
			}
		}

		if (!isSource && createCascades && ParticleTweaksConfig.TRAILER_CASCADES) {
			if (isDown) {
				final FluidState belowFluidState = level.getFluidState(pos.below());
				if (belowFluidState.isSource()) {
					final BlockPos immutablePos = pos.immutable();
					if (!CASCADES.contains(immutablePos)) CASCADES.add(immutablePos);
				}
			} else {
				final List<Direction> possibleFlowingDirections = new ArrayList<>();
				final Vec3 flow1 = new Vec3(flowVec.x, 0D, 0D);
				final Vec3 flow2 = new Vec3(0D, 0D, flowVec.z);
				if (flow1.horizontalDistance() > 0D) possibleFlowingDirections.add(Direction.getApproximateNearest(flow1));
				if (flow2.horizontalDistance() > 0D) possibleFlowingDirections.add(Direction.getApproximateNearest(flow2));

				for (Direction direction : possibleFlowingDirections) {
					final BlockPos flowingToPos = pos.relative(direction);
					final BlockState flowingToBlockState = level.getBlockState(flowingToPos);
					if (flowingToBlockState.getCollisionShape(level, flowingToPos).isEmpty()) {
						final FluidState flowingEndFluidState = level.getFluidState(flowingToPos.below());
						if (flowingEndFluidState.isSource()) {
							final BlockPos immutablePos = pos.immutable();
							if (!CASCADES.contains(immutablePos)) CASCADES.add(immutablePos);
						}
					}
				}
			}
		}
	}

	private static void spawnParticleFromDirection(
		Level level,
		BlockPos pos,
		Direction direction,
		int count,
		boolean isFalling,
		double yVelocity,
		double minVelocityScale,
		double maxVelocityScale,
		float fluidHeight,
		RandomSource random,
		ParticleOptions particle
	) {
		final BlockPos otherPos = pos.relative(direction);
		final BlockState otherState = level.getBlockState(otherPos);
		if (!otherState.getCollisionShape(level, otherPos).isEmpty() || !(otherState.getFluidState().isEmpty() || !isFalling)) return;

		final Vec3 directionOffset = Vec3.atLowerCornerOf(direction.getUnitVec3i()).scale(0.5D);
		final Vec3 offsetPos = pos.getBottomCenter().add(isFalling ? directionOffset : Vec3.ZERO);
		for (int i = 0; i < count; i++) {
			final double yOffset = isFalling ? random.nextDouble() * fluidHeight : fluidHeight;
			final Vec3 particleOffsetPos = offsetPos.add(
				random.triangle(0D, 0.65D) * Math.abs(direction.getStepZ()),
				yOffset,
				random.triangle(0D, 0.65D) * Math.abs(direction.getStepX())
			);
			final Vec3 velocity = directionOffset
				.scale(0.75D)
				.scale(random.triangle((minVelocityScale + maxVelocityScale) * 0.5D, maxVelocityScale - minVelocityScale));
			level.addParticle(
				particle,
				particleOffsetPos.x, particleOffsetPos.y, particleOffsetPos.z,
				velocity.x, yVelocity, velocity.z
			);
		}
	}

	private static final ArrayList<BlockPos> CASCADES = new ArrayList<>();

	public static void clearCascades() {
		CASCADES.clear();
	}

	public static void clearCascadesInChunk(ChunkPos chunkPos) {
		CASCADES.removeIf(blockPos -> (new ChunkPos(blockPos).equals(chunkPos)));
	}

	public static void tickCascades(ClientLevel level) {
		if (!ParticleTweaksConfig.TRAILER_CASCADES) {
			CASCADES.clear();
			return;
		}

		final Minecraft minecraft = Minecraft.getInstance();
		final BlockPos cameraPos = minecraft.gameRenderer.getMainCamera().blockPosition();
		CASCADES.removeIf(blockPos ->
			!onCascadeTick(
				level,
				blockPos,
				level.getFluidState(blockPos),
				level.random,
				cameraPos
			)
		);
	}

	public static boolean onCascadeTick(Level level, BlockPos pos, FluidState state, RandomSource random, BlockPos cameraPos) {
		if (!level.isLoaded(pos)) return false;

		final int camDifferenceX = Math.abs(cameraPos.getZ() - pos.getZ());
		final int camDifferenceZ = Math.abs(cameraPos.getZ() - pos.getZ());
		if ((camDifferenceX > 16 || camDifferenceZ > 16) && random.nextBoolean()) return true;
		if ((camDifferenceX > 32 || camDifferenceZ > 32)) return true;

		if (state.isSource() || state.isEmpty()) return false;

		int cascadeStrength = 1;
		float largeCascadeChance = 0F;
		Vec3 rawFlow = state.getFlow(level, pos);
		final List<Direction> validDirections = new ArrayList<>();

		final Vec3 flowVec = rawFlow.normalize();
		final boolean isDown = state.hasProperty(FlowingFluid.FALLING) && state.getValue(FlowingFluid.FALLING);

		if (isDown) {
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				final BlockPos otherPos = pos.relative(direction);
				final BlockState otherBlockState = level.getBlockState(otherPos);
				final FluidState otherFluidState = otherBlockState.getFluidState();
				if (otherFluidState.is(state.getType()) && otherFluidState.hasProperty(FlowingFluid.FALLING) && otherFluidState.getValue(FlowingFluid.FALLING)) {
					cascadeStrength += 1;
				} else if (otherFluidState.isEmpty() && otherBlockState.getCollisionShape(level, pos).isEmpty()) {
					final BlockPos belowOtherPos = otherPos.below();
					final FluidState belowOtherFluidState = level.getFluidState(belowOtherPos);
					if (belowOtherFluidState.isSource()) validDirections.add(direction);
				}
			}

			int fallingFromHeight = 1;
			for (int i = 1; i < 20; i++) {
				final FluidState aboveFluidState = level.getFluidState(pos.above(i));
				if (aboveFluidState.is(state.getType()) && aboveFluidState.hasProperty(FlowingFluid.FALLING) && aboveFluidState.getValue(FlowingFluid.FALLING)) {
					fallingFromHeight = i;
				} else {
					break;
				}
			}

			largeCascadeChance = fallingFromHeight >= 10 ? 1F - ((20F - fallingFromHeight) / 10F) : 0F;
			cascadeStrength += Math.min(fallingFromHeight, 1);
		} else {
			final List<Direction> possibleFlowingDirections = new ArrayList<>();
			final Vec3 flow1 = new Vec3(flowVec.x, 0D, 0D);
			final Vec3 flow2 = new Vec3(0D, 0D, flowVec.z);
			if (flow1.horizontalDistance() > 0D) possibleFlowingDirections.add(Direction.getApproximateNearest(flow1));
			if (flow2.horizontalDistance() > 0D) possibleFlowingDirections.add(Direction.getApproximateNearest(flow2));

			for (Direction direction : possibleFlowingDirections) {
				final BlockPos flowingToPos = pos.relative(direction);
				final BlockState flowingToBlockState = level.getBlockState(flowingToPos);
				if (flowingToBlockState.getCollisionShape(level, flowingToPos).isEmpty()) {
					final FluidState flowingEndFluidState = level.getFluidState(flowingToPos.below());
					if (flowingEndFluidState.isSource()) {
						validDirections.add(direction);
					}
				}
			}
		}

		if (!validDirections.isEmpty()) {
			for (Direction direction : validDirections) {
				final int firstStrength = (int) (cascadeStrength * 1.25D);
				final int secondStrength = (int) (cascadeStrength * 1.5D);
				final boolean largeCascade = random.nextFloat() <= largeCascadeChance;
				final boolean isSmallWater = !largeCascade && random.nextFloat() <= 0.05F;
				spawnParticleFromDirection(
					level,
					pos,
					direction,
					random.nextInt(firstStrength, Math.max(firstStrength + 1, secondStrength)),
					true,
					largeCascade ? 0.2D : isSmallWater ? 0.2D : 0.125D,
					isSmallWater ? 0.2D : largeCascade ? 0.1D : 0.05D,
					isSmallWater ? 0.4D : largeCascade ? 0.3D : 0.225D,
					largeCascade ? 0.3F : 0.05F,
					random,
					largeCascade ?
						random.nextBoolean() ? ParticleTweaksParticleTypes.CASCADE_A : ParticleTweaksParticleTypes.CASCADE_B
						: isSmallWater ?
						ParticleTweaksParticleTypes.FLOWING_WATER : ParticleTweaksParticleTypes.SMALL_CASCADE
				);
			}
			return true;
		}
		return false;
	}

}
