package net.lunade.particletweaks.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.particle.CritParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = CritParticle.DamageIndicatorProvider.class, priority = 1001)
public class DamageIndicatorParticleMixin {

	@ModifyExpressionValue(
		method = "createParticle(Lnet/minecraft/core/particles/SimpleParticleType;Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDD)Lnet/minecraft/client/particle/Particle;",
		at = @At(
			value = "NEW",
			target = "(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDD)Lnet/minecraft/client/particle/CritParticle;"
		)
	)
	public CritParticle particleTweaks$modifyDamageIndicator(CritParticle original) {
		if (original instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.15F);
			particleTweakInterface.particleTweaks$setSwitchesExit(true);
			particleTweakInterface.particleTweaks$setScalesToZero();
		}
		return original;
	}

}
