package net.lunade.particletweaks.movement.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface MutableParticleFluidMovementInterface {
	void particleTweaks$setCanBurn(boolean canBurn);
	void particleTweaks$setSlowsInFluid(boolean slowsInFluid);
	void particleTweaks$setMovesWithFluid(boolean movesWithFluid);
}
