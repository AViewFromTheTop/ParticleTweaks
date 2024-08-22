package net.lunade.particletweaks.mixin.client;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CampfireSmokeParticle.class, priority = 1001)
public abstract class CampfireSmokeParticleMixin extends TextureSheetParticle {

	protected CampfireSmokeParticleMixin(ClientLevel clientLevel, double d, double e, double f) {
		super(clientLevel, d, e, f);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		if (this instanceof ParticleTweakInterface particleTweakInterface) {
            particleTweakInterface.particleTweaks$setNewSystem(true);
            particleTweakInterface.particleTweaks$setScaler(0.15F);
            particleTweakInterface.particleTweaks$setScalesToZero();
            particleTweakInterface.particleTweaks$setCanShrink(false);
		}
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void particleTweaks$runScaling(CallbackInfo info) {
		if (this instanceof ParticleTweakInterface particleTweakInterface) {
			if (particleTweakInterface.particleTweaks$usesNewSystem()) {
				particleTweakInterface.particleTweaks$calcScale();
			}
		}
	}

}
