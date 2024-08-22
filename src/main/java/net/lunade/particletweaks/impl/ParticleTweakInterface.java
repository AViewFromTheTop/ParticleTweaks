package net.lunade.particletweaks.impl;

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

	void particleTweaks$setSlowsInWater(boolean set);

	boolean particleTweaks$slowsInWater();

	void particleTweaks$setMovesWithWater(boolean set);

	boolean particleTweaks$movesWithWater();

	void particleTweaks$setScale(float f);

	float particleTweaks$getScale();

	void particleTweaks$setPrevScale(float f);

	float particleTweaks$getPrevScale();

	void particleTweaks$setTargetScale(float f);

	float particleTweaks$getTargetScale();
}
