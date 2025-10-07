package net.lunade.particletweaks.mixin.client.tweaks;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.TrailParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TrailParticle.class, priority = 1001)
public abstract class TrailParticleMixin extends SingleQuadParticle implements ParticleTweakInterface {

	protected TrailParticleMixin(ClientLevel clientLevel, double d, double e, double f, TextureAtlasSprite textureAtlasSprite) {
		super(clientLevel, d, e, f, textureAtlasSprite);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		this.particleTweaks$setNewSystem(true);
		this.particleTweaks$setScaler(0.35F);
		this.particleTweaks$setSwitchesExit(true);
		this.particleTweaks$setScalesToZero();
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void particleTweaks$runScaling(CallbackInfo info) {
		if (this.particleTweaks$usesNewSystem()) {
			this.particleTweaks$calcScale();
			this.particleTweaks$runScaleRemoval();
		}
	}

	@Override
	public boolean particleTweaks$runScaleRemoval() {
		if (this.particleTweaks$usesNewSystem()) {
			if (this.age >= this.lifetime - 10) {
				this.particleTweaks$setSwitchedToShrinking(true);
				if (!this.particleTweaks$canShrink()) return true;
				this.particleTweaks$setTargetScale(0F);
				if (this.particleTweaks$getPrevScale() <= 0.04F) {
					this.particleTweaks$setScale(0F);
				}
				return this.particleTweaks$getPrevScale() == 0F;
			} else {
				this.particleTweaks$setTargetScale(1F);
			}
		}
		return false;
	}

	@Inject(method = "getLayer", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$getRenderType(CallbackInfoReturnable<Layer> info) {
		info.setReturnValue(Layer.TRANSLUCENT);
	}

}
