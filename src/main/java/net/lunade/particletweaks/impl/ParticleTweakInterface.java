package net.lunade.particletweaks.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ParticleTweakInterface {

	float particleTweaks$getScale(float partialTick);

	void particleTweaks$calcScale();

	boolean particleTweaks$runScaleRemoval();

	void particleTweaks$setScaler(float scaler);

	void particleTweaks$setNewSystem(boolean set);

	boolean particleTweaks$usesNewSystem();

	void particleTweaks$setScalesToZero();

	boolean particleTweaks$hasSwitchedToShrinking();

	void particleTweaks$setSwitchedToShrinking(boolean set);

	boolean particleTweaks$canShrink();

	void particleTweaks$setCanShrink(boolean set);

	void particleTweaks$setFadeInsteadOfScale(boolean set);

	boolean particleTweaks$fadeInsteadOfScale();

	void particleTweaks$setSwitchesExit(boolean set);

	boolean particleTweaks$switchesExit();

	void particleTweaks$setSlowsInFluid(boolean set);

	boolean particleTweaks$slowsInFluid();

	void particleTweaks$setMovesWithFluid(boolean set);

	boolean particleTweaks$movesWithFluid();

	void particleTweaks$setFluidMovementScale(double fluidMovementScale);

	double particleTweaks$getFluidMovementScale();

	void particleTweaks$setCanBurn(boolean set);

	boolean particleTweaks$canBurn();

	void particleTweaks$setScale(float f);

	float particleTweaks$getScale();

	void particleTweaks$setPrevScale(float f);

	float particleTweaks$getPrevScale();

	void particleTweaks$setTargetScale(float f);

	float particleTweaks$getTargetScale();

	void particleTweaks$setMaxAlpha(float maxAlpha);

	float particleTweaks$getMaxAlpha();
}
