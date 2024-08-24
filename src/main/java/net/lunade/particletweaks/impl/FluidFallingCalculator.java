package net.lunade.particletweaks.impl;

import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FluidFallingCalculator {

	public static @Nullable Vec3 handleFluidInteraction(
		Level level,
		Vec3 pos,
		Vec3 movement,
		Particle particle,
		boolean safeInLavaOrFire,
		boolean slowsInFluid,
		boolean flowsWithFluid
	) {
		if (!safeInLavaOrFire || slowsInFluid || flowsWithFluid) {
			BlockPos blockPos = BlockPos.containing(pos);
			BlockState blockState = level.getBlockState(blockPos);
			FluidState fluidState = blockState.getFluidState();
			boolean isFluidHighEnough = false;
			boolean atRiskOfLava = !safeInLavaOrFire && fluidState.is(FluidTags.LAVA);

			if (slowsInFluid || flowsWithFluid || atRiskOfLava) {
				isFluidHighEnough = !fluidState.isEmpty() && (fluidState.getHeight(level, blockPos) + (float) blockPos.getY()) >= pos.y;
			}

			boolean willBurn = atRiskOfLava && isFluidHighEnough;
			if (!safeInLavaOrFire && fluidState.isEmpty() && blockState.is(BlockTags.FIRE)) {
				AABB shape = blockState.getShape(level, blockPos).bounds().move(blockPos);
				if (shape.contains(pos)) {
					willBurn = true;
				}
			}

			if (willBurn) {
				level.addParticle(
					ParticleTypes.SMOKE,
					pos.x,
					pos.y,
					pos.z,
					0D,
					0D,
					0D
				);
				particle.remove();
				return null;
			}

			if (slowsInFluid && isFluidHighEnough) {
				movement = new Vec3(movement.x * 0.8D, movement.y * 0.1D, movement.z * 0.8D);
			}

			if (flowsWithFluid && isFluidHighEnough) {
				Vec3 flow = fluidState.getFlow(level, blockPos);
				movement = movement.add(flow.scale(0.015D));
			}
		}

		return movement;
	}

}
