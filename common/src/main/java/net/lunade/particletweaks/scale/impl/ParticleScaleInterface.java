package net.lunade.particletweaks.scale.impl;

import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientOnly
public interface ParticleScaleInterface {
	default @Nullable ParticleScaleHandler particleTweaks$createScaleHandler() {
		return null;
	}

	default void particleTweaks$setScaleHandler(ParticleScaleHandler scaleHandler) {}

	default @Nullable ParticleScaleHandler particleTweaks$getScaleHandler() {
		return null;
	}

	default void particleTweaks$runScaleTick() {}

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
