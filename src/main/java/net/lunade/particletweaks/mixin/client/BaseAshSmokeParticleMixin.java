package net.lunade.particletweaks.mixin.client;

import net.lunade.particletweaks.interfaces.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BaseAshSmokeParticle.class, priority = 1001)
public abstract class BaseAshSmokeParticleMixin extends TextureSheetParticle {

	protected BaseAshSmokeParticleMixin(ClientLevel clientLevel, double d, double e, double f) {
		super(clientLevel, d, e, f);
	}

	@Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$getRenderType(CallbackInfoReturnable<ParticleRenderType> info) {
		info.setReturnValue(ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		if (BaseAshSmokeParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.25F);
			particleTweakInterface.particleTweaks$setFadeInsteadOfScale(true);
			particleTweakInterface.particleTweaks$setSlowsInWater(true);
			particleTweakInterface.particleTweaks$setMovesWithWater(true);
		}
	}

	@Inject(method = "getQuadSize", at = @At("RETURN"), cancellable = true)
	public void particleTweaks$getQuadSize(float partialTicks, CallbackInfoReturnable<Float> info) {
		if (BaseAshSmokeParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			boolean switched = particleTweakInterface.particleTweaks$hasSwitchedToShrinking() && particleTweakInterface.particleTweaks$switchesExit();
			if (!particleTweakInterface.particleTweaks$fadeInsteadOfScale() && !switched) {
				if (!particleTweakInterface.particleTweaks$fadeInsteadOfScale()) {
					info.setReturnValue(info.getReturnValue() * particleTweakInterface.particleTweaks$getScale(partialTicks));
				} else {
					this.alpha = particleTweakInterface.particleTweaks$getScale(partialTicks);
				}
			}
		}
	}

}
