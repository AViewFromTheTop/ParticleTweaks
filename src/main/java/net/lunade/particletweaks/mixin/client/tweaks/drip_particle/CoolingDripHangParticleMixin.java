package net.lunade.particletweaks.mixin.client.tweaks.drip_particle;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.particle.DripParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DripParticle.CoolingDripHangParticle.class, priority = 1001)
public abstract class CoolingDripHangParticleMixin implements ParticleTweakInterface {

	@Inject(method = "<init>", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		this.particleTweaks$setNewSystem(true);
		this.particleTweaks$setScaler(0.15F);
		this.particleTweaks$setScalesToZero();
		this.particleTweaks$setCanShrink(false);
	}

}
