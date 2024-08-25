package net.lunade.particletweaks.mixin.client;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.particle.GlowParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GlowParticle.class, priority = 1001)
public abstract class GlowParticleMixin implements ParticleTweakInterface {

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		this.particleTweaks$setNewSystem(true);
		this.particleTweaks$setScaler(0.375F);
		this.particleTweaks$setScalesToZero();
		this.particleTweaks$setSwitchesExit(true);
		this.particleTweaks$setSlowsInFluid(true);
	}

}
