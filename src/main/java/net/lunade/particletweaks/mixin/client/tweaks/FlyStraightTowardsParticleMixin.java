package net.lunade.particletweaks.mixin.client.tweaks;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FlyStraightTowardsParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FlyStraightTowardsParticle.class, priority = 1001)
public abstract class FlyStraightTowardsParticleMixin extends SingleQuadParticle implements ParticleTweakInterface {

	protected FlyStraightTowardsParticleMixin(ClientLevel clientLevel, double d, double e, double f, TextureAtlasSprite textureAtlasSprite) {
		super(clientLevel, d, e, f, textureAtlasSprite);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		this.particleTweaks$setNewSystem(true);
		this.particleTweaks$setScaler(0.3F);
		this.particleTweaks$setScalesToZero();
		this.particleTweaks$setSwitchesExit(true);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void particleTweaks$runScaling(CallbackInfo info) {
		if (!this.particleTweaks$usesNewSystem()) return;
		this.particleTweaks$calcScale();
		this.age = Mth.clamp(age - 1, 0, this.lifetime);
		if (this.particleTweaks$getScale(0F) <= 0.85F && !this.particleTweaks$hasSwitchedToShrinking()) {
			this.age = Mth.clamp(age - 1, 0, this.lifetime);
		}
	}

	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	public void particleTweaks$removeOnceSmall(CallbackInfo info) {
		if (this.particleTweaks$usesNewSystem() && this.particleTweaks$runScaleRemoval()) {
			this.remove();
			info.cancel();
		}
	}

	@Inject(method = "getLayer", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$getRenderType(CallbackInfoReturnable<Layer> info) {
		info.setReturnValue(Layer.TRANSLUCENT);
	}

}
