package net.lunade.particletweaks.mixin.client;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.particle.SpellParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SpellParticle.class, priority = 1001)
public class SpellParticleMixin{

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		if (SpellParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.15F);
			particleTweakInterface.particleTweaks$setScalesToZero();
			particleTweakInterface.particleTweaks$setSwitchesExit(true);
		}
	}

}
