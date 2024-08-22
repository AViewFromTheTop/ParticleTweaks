package net.lunade.particletweaks.mixin.client.particlerain;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.lunade.particletweaks.impl.WeatherParticleInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
			particleTweakInterface.particleTweaks$setSlowsInWater(true);
			particleTweakInterface.particleTweaks$setScalesToZero();
			particleTweakInterface.particleTweaks$setFadeInsteadOfScale(true);
		}
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lpigcart/particlerain/particle/RainDropParticle;remove()V"))
	public void particleTweaks$pseudoRemove(RainDropParticle instance) {

	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;createParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)Lnet/minecraft/client/particle/Particle;"))
	public Particle particleTweaks$newSplash(ParticleEngine instance, ParticleOptions particleOptions, double d, double e, double f, double g, double h, double i) {
		if (!this.particleTweaks$hasSpawnedSplash) {
			this.particleTweaks$hasSpawnedSplash = true;
			return Minecraft.getInstance().particleEngine.createParticle(particleOptions, d, e, f, g, h, i);
		}
		return null;
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lpigcart/particlerain/particle/RainDropParticle;remove()V", shift = At.Shift.AFTER))
	public void particleTweaks$tick(CallbackInfo info) {
		if (RainDropParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			if (particleTweakInterface.particleTweaks$usesNewSystem()) {
				if (RainDropParticle.class.cast(this) instanceof WeatherParticleInterface weatherParticleInterface) {
					weatherParticleInterface.particleTweaks$setAlreadyRemoving(true);
				}
				particleTweakInterface.particleTweaks$setScaler(1F);
				RainDropParticle.class.cast(this).age = RainDropParticle.class.cast(this).getLifetime() +  2;
				if (particleTweakInterface.particleTweaks$runScaleRemoval()) {
					RainDropParticle.class.cast(this).remove();
				}
			}
		}
	}

}
