package net.lunade.particletweaks.mixin.client;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SuspendedParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SuspendedParticle.WarpedSporeProvider.class, priority = 1001)
public class WarpedSporeProviderMixin {

	@Inject(method = "createParticle*", at = @At("RETURN"))
	public void particleTweaks$createParticle(CallbackInfoReturnable<Particle> info) {
		if (info.getReturnValue() instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setCanBurn(false);
		}
	}

}
