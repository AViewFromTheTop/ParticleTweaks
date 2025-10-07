package net.lunade.particletweaks.mixin.client.tweaks;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.particle.PlayerCloudParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerCloudParticle.class, priority = 1001)
public abstract class PlayerCloudParticleMixin implements ParticleTweakInterface {

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		this.particleTweaks$setNewSystem(true);
		this.particleTweaks$setScaler(0.25F);
		this.particleTweaks$setFadeInsteadOfScale(true);
		this.particleTweaks$setSlowsInFluid(true);
	}

}
