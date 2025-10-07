package net.lunade.particletweaks.mixin.client.tweaks.suspended_particle;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SuspendedParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SuspendedParticle.UnderwaterProvider.class, priority = 1001)
public class UnderwaterProviderMixin {

	@Inject(method = "createParticle(Lnet/minecraft/core/particles/SimpleParticleType;Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/util/RandomSource;)Lnet/minecraft/client/particle/Particle;", at = @At("TAIL"))
	public void particleTweaks$createParticle(CallbackInfoReturnable<Particle> info) {
		if (!(info.getReturnValue() instanceof ParticleTweakInterface particleTweakInterface)) return;
		particleTweakInterface.particleTweaks$setNewSystem(true);
		particleTweakInterface.particleTweaks$setScaler(0.05F);
		particleTweakInterface.particleTweaks$setScalesToZero();
		particleTweakInterface.particleTweaks$setSlowsInFluid(false);
		particleTweakInterface.particleTweaks$setMovesWithFluid(false);
		particleTweakInterface.particleTweaks$setCanBurn(true);
	}
}
