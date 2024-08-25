package net.lunade.particletweaks.mixin.client;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PortalParticle.class, priority = 1001)
public abstract class PortalParticleMixin extends TextureSheetParticle implements ParticleTweakInterface {

	protected PortalParticleMixin(ClientLevel clientLevel, double d, double e, double f) {
		super(clientLevel, d, e, f);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		this.particleTweaks$setNewSystem(true);
		this.particleTweaks$setScaler(0.45F);
		this.particleTweaks$setScalesToZero();
		this.particleTweaks$setFadeInsteadOfScale(true);
		this.particleTweaks$setSwitchesExit(true);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void particleTweaks$runScaling(CallbackInfo info) {
		if (this.particleTweaks$usesNewSystem()) {
			this.particleTweaks$calcScale();
			this.age = Mth.clamp(age - 1, 0, this.lifetime);
			if (this.particleTweaks$getScale(0F) <= 0.85F && !this.particleTweaks$hasSwitchedToShrinking()) {
				this.age = Mth.clamp(age - 1, 0, this.lifetime);
			}
		}
	}

	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	public void particleTweaks$removeOnceSmall(CallbackInfo info) {
		if (this.particleTweaks$usesNewSystem()) {
			if (this.particleTweaks$runScaleRemoval()) {
				this.remove();
				info.cancel();
			}
		}
	}

	@Inject(method = "getQuadSize", at = @At("RETURN"), cancellable = true)
	public void particleTweaks$getQuadSize(float partialTicks, CallbackInfoReturnable<Float> info) {
		if (this.particleTweaks$usesNewSystem()) {
			boolean switched = this.particleTweaks$hasSwitchedToShrinking() && this.particleTweaks$switchesExit();
			if (!this.particleTweaks$fadeInsteadOfScale() && !switched) {
				info.setReturnValue(info.getReturnValue() * this.particleTweaks$getScale(partialTicks));
			} else {
				this.alpha = this.particleTweaks$getScale(partialTicks);
			}
		}
	}

	@Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$getRenderType(CallbackInfoReturnable<ParticleRenderType> info) {
		info.setReturnValue(ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT);
	}

}
