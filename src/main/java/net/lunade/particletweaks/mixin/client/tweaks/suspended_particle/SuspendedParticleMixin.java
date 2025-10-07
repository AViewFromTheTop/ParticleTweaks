package net.lunade.particletweaks.mixin.client.tweaks.suspended_particle;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SuspendedParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SuspendedParticle.class, priority = 1001)
public abstract class SuspendedParticleMixin implements ParticleTweakInterface {

	@Inject(method = "<init>*", at = @At("TAIL"))
	public void particleTweaks$initSuspendedParticles(CallbackInfo info) {
		this.particleTweaks$setNewSystem(true);
		this.particleTweaks$setScalesToZero();
		this.particleTweaks$setSlowsInFluid(true);
		this.particleTweaks$setMovesWithFluid(true);
		this.particleTweaks$setCanBurn(true);
	}

	@Inject(method = "getLayer", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$getRenderType(CallbackInfoReturnable<SingleQuadParticle.Layer> info) {
		info.setReturnValue(SingleQuadParticle.Layer.TRANSLUCENT);
	}
}
