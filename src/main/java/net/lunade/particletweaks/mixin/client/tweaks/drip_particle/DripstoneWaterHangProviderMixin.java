package net.lunade.particletweaks.mixin.client.tweaks.drip_particle;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DripParticle.DripstoneWaterHangProvider.class, priority = 1001)
public class DripstoneWaterHangProviderMixin {

	@Inject(
		method = "createParticle(Lnet/minecraft/core/particles/SimpleParticleType;Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/util/RandomSource;)Lnet/minecraft/client/particle/Particle;",
		at = @At("RETURN")
	)
	private static void particleTweaks$createWaterHangParticle(CallbackInfoReturnable<SingleQuadParticle> info) {
		if (!(info.getReturnValue() instanceof ParticleTweakInterface particleTweakInterface)) return;
		particleTweakInterface.particleTweaks$setNewSystem(true);
		particleTweakInterface.particleTweaks$setScaler(0.5F);
		particleTweakInterface.particleTweaks$setScalesToZero();
		particleTweakInterface.particleTweaks$setCanShrink(false);
		particleTweakInterface.particleTweaks$setSlowsInFluid(true);
		particleTweakInterface.particleTweaks$setMovesWithFluid(true);
	}

}
