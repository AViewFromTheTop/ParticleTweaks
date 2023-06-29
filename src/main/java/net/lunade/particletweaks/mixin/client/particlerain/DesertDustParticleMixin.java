package net.lunade.particletweaks.mixin.client.particlerain;

import net.lunade.particletweaks.interfaces.ParticleTweakInterface;
import net.lunade.particletweaks.interfaces.WeatherParticleInterface;
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
			particleTweakInterface.particleTweaks$setSlowsInWater(true);
			particleTweakInterface.particleTweaks$setMovesWithWater(true);
		}
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lpigcart/particlerain/particle/DesertDustParticle;remove()V", shift = At.Shift.BEFORE), cancellable = true)
	public void particleTweaks$tick(CallbackInfo info) {
		if (DesertDustParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			if (particleTweakInterface.particleTweaks$usesNewSystem()) {
				if (DesertDustParticle.class.cast(this) instanceof WeatherParticleInterface weatherParticleInterface) {
					weatherParticleInterface.particleTweaks$setAlreadyRemoving(true);
				}
				DesertDustParticle.class.cast(this).age = DesertDustParticle.class.cast(this).getLifetime() + 2;
				if (particleTweakInterface.particleTweaks$runScaleRemoval()) {
					DesertDustParticle.class.cast(this).remove();
				}
				info.cancel();
			}
		}
	}

	@Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$getRenderType(CallbackInfoReturnable<ParticleRenderType> info) {
		info.setReturnValue(ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT);
	}

}
