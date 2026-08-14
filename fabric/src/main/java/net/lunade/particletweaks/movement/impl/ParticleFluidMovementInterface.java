package net.lunade.particletweaks.movement.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.movement.api.ParticleFluidMover;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
public interface ParticleFluidMovementInterface {

	default boolean particleTweaks$canBurn() {
		return false;
	}

	default boolean particleTweaks$spawnsSmokeOnBurn() {
		return true;
	}

	default boolean particleTweaks$slowsInFluid() {
		return false;
	}

	default boolean particleTweaks$movesWithFluid() {
		return false;
	}

	default double particleTweaks$flowMovementScale() {
		return 0.014D;
	}

	default double particleTweaks$fluidSlowHorizontalScale() {
		return 0.8D;
	}

	default double particleTweaks$fluidSlowVerticalScale() {
		return 0.1D;
	}

	default double particleTweaks$additionalYOnFluidSlow() {
		return 0D;
	}

	default double particleTweaks$fluidAdditionalSlowVerticalScaleDownward() {
		return 1D;
	}

	default void particleTweaks$runFluidMovementTick(Particle particle, CallbackInfo info) {
		final Vec3 fluidMovement = ParticleFluidMover.handleFluidInteraction(particle, this);

		if (fluidMovement == null) {
			info.cancel();
			return;
		}

		particle.xd = fluidMovement.x;
		particle.yd = fluidMovement.y;
		particle.zd = fluidMovement.z;
	}

	default void particleTweaks$setTouchingFluid(boolean touchingFluid) {
	}

	default boolean particleTweaks$touchingFluid() {
		return false;
	}

	default void particleTweaks$onTouchingFluidSet(boolean touchingFluid) {
	}

	default void particleTweaks$setTouchingWater(boolean touchingWater) {
	}

	default boolean particleTweaks$touchingWater() {
		return false;
	}

	default void particleTweaks$onTouchingWaterSet(boolean touchingWater) {
	}

	default void particleTweaks$setTouchingLava(boolean touchingLava) {
	}

	default boolean particleTweaks$touchingLava() {
		return false;
	}

	default void particleTweaks$onTouchingLavaSet(boolean touchingLava) {
	}

}
