package net.lunade.particletweaks.mixin.client.particlerain;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.lunade.particletweaks.impl.WeatherParticleInterface;
import net.minecraft.client.particle.ParticleRenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pigcart.particlerain.particle.DesertDustParticle;

@Mixin(DesertDustParticle.class)
public class DesertDustParticleMixin {

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		if (DesertDustParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.25F);
			particleTweakInterface.particleTweaks$setScalesToZero();
			particleTweakInterface.particleTweaks$setSwitchesExit(true);
			particleTweakInterface.particleTweaks$setSlowsInFluid(true);
			particleTweakInterface.particleTweaks$setMovesWithFluid(true);
		}
	}

	@WrapOperation(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lpigcart/particlerain/particle/DesertDustParticle;remove()V"
		)
	)
	public void particleTweaks$tick(DesertDustParticle instance, Operation<Void> original) {
		if (instance instanceof ParticleTweakInterface particleTweakInterface && particleTweakInterface.particleTweaks$usesNewSystem()) {
			if (instance instanceof WeatherParticleInterface weatherParticleInterface) {
				weatherParticleInterface.particleTweaks$setAlreadyRemoving(true);
			}
			instance.age = instance.getLifetime() + 2;
			if (particleTweakInterface.particleTweaks$runScaleRemoval()) {
				original.call(instance);
			}
		}
	}

	@Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$getRenderType(CallbackInfoReturnable<ParticleRenderType> info) {
		info.setReturnValue(ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT);
	}

}
