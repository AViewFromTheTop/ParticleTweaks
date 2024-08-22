package net.lunade.particletweaks.mixin.client;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SingleQuadParticle.class, priority = 1001)
public abstract class SingleQuadParticleMixin extends Particle implements ParticleTweakInterface {

	@Shadow
	public abstract Particle scale(float scale);

	@Unique
	private float particleTweaks$scaler = 0.15F;
	@Unique
	private float particleTweaks$prevScale = 1F;
	@Unique
	private float particleTweaks$scale = 1F;
	@Unique
	private float particleTweaks$targetScale = 1F;
	@Unique
	private boolean particleTweaks$useNewSystem = false;
	@Unique
	private boolean particleTweaks$hasSwitchedToShrinking = false;
	@Unique
	private boolean particleTweaks$canShrink = true;
	@Unique
	private boolean particleTweaks$fadeInsteadOfShrink = false;
	@Unique
	private boolean particleTweaks$switchesExit = false;

	protected SingleQuadParticleMixin(ClientLevel clientLevel, double d, double e, double f) {
		super(clientLevel, d, e, f);
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

	@Override
	public float particleTweaks$getScale(float partialTick) {
		return this.particleTweaks$usesNewSystem() ? Mth.lerp(partialTick, this.particleTweaks$prevScale, this.particleTweaks$scale) : 1F;
	}

	@Override
	public void particleTweaks$calcScale() {
		this.particleTweaks$prevScale = this.particleTweaks$scale;
		this.particleTweaks$scale += (this.particleTweaks$targetScale - this.particleTweaks$scale) * this.particleTweaks$scaler;
	}

	@Override
	public boolean particleTweaks$runScaleRemoval() {
		if (this.particleTweaks$usesNewSystem()) {
			this.age = Mth.clamp(age + 1, 0, this.lifetime);
			if (this.age >= this.lifetime) {
				this.particleTweaks$hasSwitchedToShrinking = true;
				if (!this.particleTweaks$canShrink) {
					return true;
				}
				this.particleTweaks$targetScale = 0F;
				if (this.particleTweaks$prevScale <= 0.04F) {
					this.particleTweaks$scale = 0F;
				}
				return this.particleTweaks$prevScale == 0F;
			} else {
				this.particleTweaks$targetScale = 1F;
			}
		}
		return false;
	}

	@Override
	public void particleTweaks$setScaler(float scaler) {
		this.particleTweaks$scaler = scaler;
	}

	@Override
	public void particleTweaks$setNewSystem(boolean set) {
		this.particleTweaks$useNewSystem = set;
	}

	@Override
	public boolean particleTweaks$usesNewSystem() {
		return this.particleTweaks$useNewSystem;
	}

	@Override
	public void particleTweaks$setScalesToZero() {
		this.particleTweaks$prevScale = 0F;
		this.particleTweaks$scale = 0F;
	}

	@Override
	public boolean particleTweaks$hasSwitchedToShrinking() {
		return this.particleTweaks$hasSwitchedToShrinking;
	}

	@Override
	public void particleTweaks$setSwitchedToShrinking(boolean set) {
		this.particleTweaks$hasSwitchedToShrinking = set;
	}

	@Override
	public boolean particleTweaks$canShrink() {
		return this.particleTweaks$canShrink;
	}

	@Override
	public void particleTweaks$setCanShrink(boolean canShrink) {
		this.particleTweaks$canShrink = canShrink;
	}

	@Override
	public void particleTweaks$setFadeInsteadOfScale(boolean set) {
		this.particleTweaks$fadeInsteadOfShrink = set;
	}

	@Override
	public boolean particleTweaks$fadeInsteadOfScale() {
		return this.particleTweaks$fadeInsteadOfShrink;
	}

	@Override
	public void particleTweaks$setSwitchesExit(boolean set) {
		this.particleTweaks$switchesExit = set;
	}

	@Override
	public boolean particleTweaks$switchesExit() {
		return this.particleTweaks$switchesExit;
	}

	@Unique
	private boolean particleTweaks$slowsInWater = false;
	@Override
	public void particleTweaks$setSlowsInWater(boolean set) {
		this.particleTweaks$slowsInWater = set;
	}
	@Override
	public boolean particleTweaks$slowsInWater() {
		return this.particleTweaks$slowsInWater;
	}

	@Unique
	private boolean particleTweaks$movesWithWater = false;
	@Override
	public void particleTweaks$setMovesWithWater(boolean set) {
		this.particleTweaks$movesWithWater = set;
	}
	@Override
	public boolean particleTweaks$movesWithWater() {
		return this.particleTweaks$movesWithWater;
	}

	@Override
	public void particleTweaks$setScale(float f) {
		this.particleTweaks$scale = f;
	}

	@Override
	public float particleTweaks$getScale() {
		return this.particleTweaks$scale;
	}

	@Override
	public void particleTweaks$setPrevScale(float f) {
		this.particleTweaks$prevScale = f;
	}

	@Override
	public float particleTweaks$getPrevScale() {
		return this.particleTweaks$prevScale;
	}

	@Override
	public void particleTweaks$setTargetScale(float f) {
		this.particleTweaks$targetScale = f;
	}

	@Override
	public float particleTweaks$getTargetScale() {
		return this.particleTweaks$targetScale;
	}
}
