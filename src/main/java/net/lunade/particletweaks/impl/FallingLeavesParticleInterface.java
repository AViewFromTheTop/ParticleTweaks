package net.lunade.particletweaks.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface FallingLeavesParticleInterface {

	void particleTweaks$setInWater(boolean inWater);

	boolean particleTweaks$inWater();

}
