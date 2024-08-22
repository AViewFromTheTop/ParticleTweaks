package net.lunade.particletweaks.mixin.client.particlerain;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.lunade.particletweaks.impl.WeatherParticleInterface;
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

	@WrapOperation(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lpigcart/particlerain/particle/SnowFlakeParticle;remove()V"
		)
	)
	public void particleTweaks$tick(SnowFlakeParticle instance, Operation<Void> original) {
		if (instance instanceof ParticleTweakInterface particleTweakInterface && particleTweakInterface.particleTweaks$usesNewSystem()) {
			instance.roll = instance.oRoll;
			if (instance instanceof WeatherParticleInterface weatherParticleInterface) {
				weatherParticleInterface.particleTweaks$setAlreadyRemoving(true);
			}
			instance.age = instance.getLifetime() + 2;
			if (particleTweakInterface.particleTweaks$runScaleRemoval()) {
				original.call(instance);
			}
		}
	}

}
