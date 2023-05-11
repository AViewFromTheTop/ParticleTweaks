package net.lunade.particletweaks.mixin.client;

import net.lunade.particletweaks.interfaces.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.PlayerCloudParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerCloudParticle.class)
public abstract class PlayerCloudParticleMixin extends TextureSheetParticle {

	protected PlayerCloudParticleMixin(ClientLevel clientLevel, double d, double e, double f) {
		super(clientLevel, d, e, f);
	}

	/*

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		if (PlayerCloudParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.25F);
			particleTweakInterface.particleTweaks$setFadeInsteadOfScale(true);
			particleTweakInterface.particleTweaks$setSlowsInWater(true);
		}
	}

	@Inject(method = "getQuadSize", at = @At("RETURN"), cancellable = true)
	public void particleTweaks$getQuadSize(float partialTicks, CallbackInfoReturnable<Float> info) {
		if (PlayerCloudParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			if (particleTweakInterface.particleTweaks$usesNewSystem()) {
				boolean switched = particleTweakInterface.particleTweaks$hasSwitchedToShrinking() && particleTweakInterface.particleTweaks$switchesExit();
				if (!particleTweakInterface.particleTweaks$fadeInsteadOfScale() && !switched) {
					info.setReturnValue(info.getReturnValue() * particleTweakInterface.particleTweaks$getScale(partialTicks));
				} else {
					this.alpha = particleTweakInterface.particleTweaks$getScale(partialTicks);
				}
			}
		}
	}

	@Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$getRenderType(CallbackInfoReturnable<ParticleRenderType> info) {
		info.setReturnValue(ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT);
	}

	 */

}
