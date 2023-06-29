package net.lunade.particletweaks.mixin.client.particlerain;

import net.lunade.particletweaks.interfaces.ParticleTweakInterface;
import net.lunade.particletweaks.interfaces.WeatherParticleInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pigcart.particlerain.particle.SnowFlakeParticle;

@Mixin(SnowFlakeParticle.class)
public class SnowFlakeParticleMixin {

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		if (SnowFlakeParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.45F);
			particleTweakInterface.particleTweaks$setSwitchesExit(true);
			particleTweakInterface.particleTweaks$setSlowsInWater(true);
			particleTweakInterface.particleTweaks$setMovesWithWater(true);
			particleTweakInterface.particleTweaks$setScalesToZero();
		}
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lpigcart/particlerain/particle/SnowFlakeParticle;remove()V", shift = At.Shift.BEFORE), cancellable = true)
	public void particleTweaks$tick(CallbackInfo info) {
		if (SnowFlakeParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			if (particleTweakInterface.particleTweaks$usesNewSystem()) {
				SnowFlakeParticle.class.cast(this).roll = SnowFlakeParticle.class.cast(this).oRoll;
				if (SnowFlakeParticle.class.cast(this) instanceof WeatherParticleInterface weatherParticleInterface) {
					weatherParticleInterface.particleTweaks$setAlreadyRemoving(true);
				}
				SnowFlakeParticle.class.cast(this).age = SnowFlakeParticle.class.cast(this).getLifetime() + 2;
				if (particleTweakInterface.particleTweaks$runScaleRemoval()) {
					SnowFlakeParticle.class.cast(this).remove();
				}
				info.cancel();
			}
		}
	}

}
