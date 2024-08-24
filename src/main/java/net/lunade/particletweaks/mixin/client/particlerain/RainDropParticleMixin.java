package net.lunade.particletweaks.mixin.client.particlerain;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.lunade.particletweaks.impl.WeatherParticleInterface;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pigcart.particlerain.particle.RainDropParticle;

@Mixin(RainDropParticle.class)
public class RainDropParticleMixin {

	@Unique
	public boolean particleTweaks$hasSpawnedSplash;

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		if (RainDropParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.35F);
			particleTweakInterface.particleTweaks$setSlowsInFluid(true);
			particleTweakInterface.particleTweaks$setScalesToZero();
			particleTweakInterface.particleTweaks$setFadeInsteadOfScale(true);
		}
	}

	@WrapOperation(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/ParticleEngine;createParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)Lnet/minecraft/client/particle/Particle;"
		)
	)
	public Particle particleTweaks$newSplash(
		ParticleEngine instance,
		ParticleOptions options,
		double x,
		double y,
		double z,
		double velocityX,
		double velocityY,
		double velocityZ,
		Operation<Particle> original
	) {
		if (!this.particleTweaks$hasSpawnedSplash) {
			this.particleTweaks$hasSpawnedSplash = true;
			return original.call(instance, options, x, y, z, velocityX, velocityY, velocityZ);
		}
		return null;
	}

	@WrapOperation(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lpigcart/particlerain/particle/RainDropParticle;remove()V"
		)
	)
	public void particleTweaks$tick(RainDropParticle instance, Operation<Void> original) {
		if (instance instanceof ParticleTweakInterface particleTweakInterface && particleTweakInterface.particleTweaks$usesNewSystem()) {
			if (instance instanceof WeatherParticleInterface weatherParticleInterface) {
				weatherParticleInterface.particleTweaks$setAlreadyRemoving(true);
			}
			particleTweakInterface.particleTweaks$setScaler(1F);
			instance.age = instance.getLifetime() + 2;
			if (particleTweakInterface.particleTweaks$runScaleRemoval()) {
				original.call(instance);
			}
		}
	}

}
