package net.lunade.particletweaks.mixin.client;

import net.lunade.particletweaks.interfaces.ParticleTweakInterface;
import net.minecraft.client.particle.BreakingItemParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BreakingItemParticle.class, priority = 1001)
public class BreakingItemParticleMixin {

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		if (BreakingItemParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.35F);
			particleTweakInterface.particleTweaks$setFadeInsteadOfScale(true);
			particleTweakInterface.particleTweaks$setSlowsInWater(true);
			particleTweakInterface.particleTweaks$setMovesWithWater(true);
		}
	}

}
