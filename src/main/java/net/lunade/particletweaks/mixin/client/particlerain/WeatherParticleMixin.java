package net.lunade.particletweaks.mixin.client.particlerain;

import net.lunade.particletweaks.interfaces.WeatherParticleInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pigcart.particlerain.particle.WeatherParticle;

@Mixin(WeatherParticle.class)
public class WeatherParticleMixin implements WeatherParticleInterface {

	@Unique
	public boolean particleTweaks$alreadyShouldRemove;

	@Inject(method = "shouldRemove()Z", remap = false, at = @At("HEAD"), cancellable = true)
	public void particleTweaks$shouldRemove(CallbackInfoReturnable<Boolean> info) {
		if (this.particleTweaks$alreadyShouldRemove) {
			info.setReturnValue(true);
		}
	}

	@Override
	public void particleTweaks$setAlreadyRemoving(boolean bl) {
		this.particleTweaks$alreadyShouldRemove = bl;
	}
}
