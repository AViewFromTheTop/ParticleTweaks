package net.lunade.particletweaks;

import net.fabricmc.api.ClientModInitializer;
import net.mehvahdjukaar.candlelight.api.ClientOnly;

@ClientOnly
public final class ParticleTweaksFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ParticleTweaksClient.init();
	}
}
