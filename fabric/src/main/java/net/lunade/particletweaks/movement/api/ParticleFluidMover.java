package net.lunade.particletweaks.movement.api;

import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
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

public class ParticleFluidMover {

	public static @Nullable Vec3 handleFluidInteraction(Particle particle, ParticleFluidMovementInterface fluidParticle) {
		final Level level = particle.level;
		final Vec3 particlePos = new Vec3(particle.x, particle.y, particle.z);
		Vec3 movement = new Vec3(particle.xd, particle.yd, particle.zd);

		final boolean canBurn = fluidParticle.particleTweaks$canBurn();
		final boolean slowsInFluid = fluidParticle.particleTweaks$slowsInFluid();
		final boolean movesWithFluid = fluidParticle.particleTweaks$movesWithFluid();
		if (!canBurn && !slowsInFluid && !movesWithFluid) return movement;

		final BlockPos pos = BlockPos.containing(particlePos);
		final BlockState state = level.getBlockState(pos);
		final FluidState fluidState = state.getFluidState();
		final boolean isFluidEmpty = fluidState.isEmpty();
		final boolean isWater = !isFluidEmpty && fluidState.is(FluidTags.WATER);
		final boolean isLava = !isFluidEmpty && !isWater && fluidState.is(FluidTags.LAVA);
		final boolean isFluidHighEnough = !isFluidEmpty && (fluidState.getHeight(level, pos) + pos.getY()) >= particlePos.y;

		fluidParticle.particleTweaks$setTouchingFluid(isFluidHighEnough && !isFluidEmpty);
		fluidParticle.particleTweaks$setTouchingWater(isFluidHighEnough && isWater);
		fluidParticle.particleTweaks$setTouchingLava(isFluidHighEnough && isLava);

		burnParticle: {
			if (!canBurn) break burnParticle;
			if (isLava) {
				if (!isFluidHighEnough) break burnParticle;
			} else if (state.is(BlockTags.FIRE)) {
				final AABB shape = state.getShape(level, pos).bounds().move(pos);
				if (!shape.contains(particlePos)) break burnParticle;
			} else {
				break burnParticle;
			}

			if (fluidParticle.particleTweaks$spawnsSmokeOnBurn()) {
				level.addParticle(
					ParticleTypes.SMOKE,
					particlePos.x, particlePos.y, particlePos.z,
					0D, 0D, 0D
				);
			}
			particle.remove();
			return null;
		}

		if (!isFluidHighEnough) return movement;

		if (slowsInFluid) {
			final double horizontalScale = fluidParticle.particleTweaks$fluidSlowHorizontalScale();
			final double verticalScale = fluidParticle.particleTweaks$fluidSlowVerticalScale();
			final double additionalY = fluidParticle.particleTweaks$additionalYOnFluidSlow();
			movement = new Vec3(movement.x * horizontalScale, (movement.y * verticalScale) + additionalY, movement.z * horizontalScale);
			if (movement.y < 0D) movement = new Vec3(movement.x, movement.y * fluidParticle.particleTweaks$fluidAdditionalSlowVerticalScaleDownward(), movement.z);
		}

		if (movesWithFluid) {
			final Vec3 flow = fluidState.getFlow(level, pos);
			movement = movement.add(flow.scale(fluidParticle.particleTweaks$flowMovementScale()));
		}

		return movement;
	}

}
