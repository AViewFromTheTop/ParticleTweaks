package net.lunade.particletweaks.mixin.client;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.particle.TerrainParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TerrainParticle.class, priority = 999)
public class TerrainParticleMixin {

	@Inject(
		method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;)V",
		at = @At("TAIL")
	)
	private void particleTweaks$initNonPos(CallbackInfo info) {
		if (TerrainParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.35F);
			particleTweakInterface.particleTweaks$setFadeInsteadOfScale(true);
			particleTweakInterface.particleTweaks$setSlowsInFluid(true);
			particleTweakInterface.particleTweaks$setMovesWithFluid(true);
		}
	}

	@Inject(
		method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V",
		at = @At("TAIL")
	)
	private void particleTweaks$init(CallbackInfo info) {
		if (TerrainParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.35F);
			particleTweakInterface.particleTweaks$setFadeInsteadOfScale(true);
			particleTweakInterface.particleTweaks$setSlowsInFluid(true);
			particleTweakInterface.particleTweaks$setMovesWithFluid(true);
		}
	}

}
