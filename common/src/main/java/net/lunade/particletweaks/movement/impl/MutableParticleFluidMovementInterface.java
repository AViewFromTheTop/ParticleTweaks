package net.lunade.particletweaks.movement.impl;

import net.mehvahdjukaar.candlelight.api.ClientOnly;

@ClientOnly
public interface MutableParticleFluidMovementInterface {
	void particleTweaks$setCanBurn(boolean canBurn);
	void particleTweaks$setSlowsInFluid(boolean slowsInFluid);
	void particleTweaks$setMovesWithFluid(boolean movesWithFluid);
}
