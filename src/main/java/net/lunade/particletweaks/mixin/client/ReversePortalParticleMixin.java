package net.lunade.particletweaks.mixin.client;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ReversePortalParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ReversePortalParticle.class, priority = 1001)
public abstract class ReversePortalParticleMixin extends TextureSheetParticle {

	protected ReversePortalParticleMixin(ClientLevel clientLevel, double d, double e, double f) {
		super(clientLevel, d, e, f);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void particleTweaks$runScaling(CallbackInfo info) {
		if (this instanceof ParticleTweakInterface particleTweakInterface) {
			if (particleTweakInterface.particleTweaks$usesNewSystem()) {
				particleTweakInterface.particleTweaks$calcScale();
				this.age = Mth.clamp(age - 1, 0, this.lifetime);
				if (particleTweakInterface.particleTweaks$getScale(0F) <= 0.85F && !particleTweakInterface.particleTweaks$hasSwitchedToShrinking()) {
					this.age = Mth.clamp(age - 1, 0, this.lifetime);
				}
			}
		}
	}

	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	public void particleTweaks$removeOnceSmall(CallbackInfo info) {
		if (this instanceof ParticleTweakInterface particleTweakInterface) {
			if (particleTweakInterface.particleTweaks$usesNewSystem()) {
				if (particleTweakInterface.particleTweaks$runScaleRemoval()) {
					this.remove();
					info.cancel();
				}
			}
		}
	}

	@Inject(method = "getQuadSize", at = @At("RETURN"), cancellable = true)
	public void particleTweaks$getQuadSize(float partialTicks, CallbackInfoReturnable<Float> info) {
		if (this instanceof ParticleTweakInterface particleTweakInterface) {
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

}
