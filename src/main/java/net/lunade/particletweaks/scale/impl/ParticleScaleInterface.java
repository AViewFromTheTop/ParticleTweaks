package net.lunade.particletweaks.scale.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
public interface ParticleScaleInterface {
	@Nullable default ParticleScaleHandler particleTweaks$createScaleHandler() {
		return null;
	}

	default void particleTweaks$setScaleHandler(ParticleScaleHandler scaleHandler) {
	}

	@Nullable default ParticleScaleHandler particleTweaks$getScaleHandler() {
		return null;
	}

	default void particleTweaks$runScaleTick() {
	}

	default void particleTweaks$runScaleRemovalTick(Particle particle, CallbackInfo info) {
		final ParticleScaleHandler scaleHandler = particleTweaks$getScaleHandler();
		if (scaleHandler == null) return;
		if (scaleHandler.runScaleRemovalTick(particle)) info.cancel();
	}

	default boolean particleTweaks$hasExit() {
		final ParticleScaleHandler scaleHandler = particleTweaks$getScaleHandler();
		return scaleHandler != null && scaleHandler.exit() != null;
	}
}
