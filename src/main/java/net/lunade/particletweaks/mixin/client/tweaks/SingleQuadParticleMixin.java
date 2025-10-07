package net.lunade.particletweaks.mixin.client.tweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = SingleQuadParticle.class, priority = 1001)
public abstract class SingleQuadParticleMixin extends Particle implements ParticleTweakInterface {

	@Shadow
	protected float alpha;

	@Shadow
	protected abstract void setAlpha(float f);

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

	@ModifyExpressionValue(
		method = "extractRotatedQuad(Lnet/minecraft/client/renderer/state/QuadParticleRenderState;Lorg/joml/Quaternionf;FFFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/SingleQuadParticle;getQuadSize(F)F"
		)
	)
	public float particleTweaks$modifyQuadSize(
		float original,
		QuadParticleRenderState quadParticleRenderState, Quaternionf quaternionf, float f, float g, float h, float partialTicks
	) {
		if (!this.particleTweaks$usesNewSystem()) return original;
		final boolean switched = this.particleTweaks$hasSwitchedToShrinking() && this.particleTweaks$switchesExit();
		if (!this.particleTweaks$fadeInsteadOfScale() && !switched) {
			return original * this.particleTweaks$getScale(partialTicks);
		} else {
			this.setAlpha(this.particleTweaks$getScale(partialTicks) * this.particleTweaks$getMaxAlpha());
		}
		return original;
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
				if (!this.particleTweaks$canShrink) return true;
				this.particleTweaks$targetScale = 0F;
				if (this.particleTweaks$prevScale <= 0.04F) this.particleTweaks$scale = 0F;
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
	private boolean particleTweaks$slowsInFluid = false;
	@Override
	public void particleTweaks$setSlowsInFluid(boolean set) {
		this.particleTweaks$slowsInFluid = set;
	}
	@Override
	public boolean particleTweaks$slowsInFluid() {
		return this.particleTweaks$slowsInFluid;
	}

	@Unique
	private boolean particleTweaks$movesWithFluid = false;
	@Override
	public void particleTweaks$setMovesWithFluid(boolean set) {
		this.particleTweaks$movesWithFluid = set;
	}
	@Override
	public boolean particleTweaks$movesWithFluid() {
		return this.particleTweaks$movesWithFluid;
	}

	@Unique
	private double particleTweaks$fluidMovementScale = 0.015D;
	@Override
	public void particleTweaks$setFluidMovementScale(double fluidMovementScale) {
		this.particleTweaks$fluidMovementScale = fluidMovementScale;
	}
	@Override
	public double particleTweaks$getFluidMovementScale() {
		return this.particleTweaks$fluidMovementScale;
	}

	@Unique
	private boolean particleTweaks$canBurn = false;

	@Override
	public void particleTweaks$setCanBurn(boolean set) {
		this.particleTweaks$canBurn = set;
	}

	@Override
	public boolean particleTweaks$canBurn() {
		return this.particleTweaks$canBurn;
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

	@Unique
	private float particleTweaks$maxAlpha = 1F;
	@Override
	public void particleTweaks$setMaxAlpha(float f) {
		this.particleTweaks$maxAlpha = f;
	}
	@Override
	public float particleTweaks$getMaxAlpha() {
		return this.particleTweaks$maxAlpha;
	}
}
