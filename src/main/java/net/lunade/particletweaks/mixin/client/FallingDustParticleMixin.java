package net.lunade.particletweaks.mixin.client;

import net.lunade.particletweaks.interfaces.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FallingDustParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FallingDustParticle.class)
public abstract class FallingDustParticleMixin extends TextureSheetParticle implements ParticleTweakInterface {

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

	protected FallingDustParticleMixin(ClientLevel clientLevel, double d, double e, double f) {
		super(clientLevel, d, e, f);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		if (FallingDustParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.25F);
			particleTweakInterface.particleTweaks$setScalesToZero();
			particleTweakInterface.particleTweaks$setSwitchesExit(true);
			particleTweakInterface.particleTweaks$setSlowsInWater(true);
		}
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void particleTweaks$runScaling(CallbackInfo info) {
		if (FallingDustParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			if (particleTweakInterface.particleTweaks$usesNewSystem()) {
				particleTweakInterface.particleTweaks$calcScale();
				this.age = Mth.clamp(age - 1, 0, this.lifetime);
				if (particleTweakInterface.particleTweaks$getScale(0F) <= 0.85F && !particleTweakInterface.particleTweaks$hasSwitchedToShrinking()) {
					this.age = Mth.clamp(age - 1, 0, this.lifetime);
				}
			}
			if (particleTweakInterface.particleTweaks$slowsInWater() && this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).is(FluidTags.WATER)) {
				this.xd *= 0.98;
				this.yd *= 0.5;
				this.zd *= 0.98;
			}
		}
	}

	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	public void particleTweaks$removeOnceSmall(CallbackInfo info) {
		if (FallingDustParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			if (particleTweakInterface.particleTweaks$usesNewSystem()) {
				if (particleTweakInterface.particleTweaks$runScaleRemoval()) {
					FallingDustParticle.class.cast(this).remove();
					info.cancel();
				}
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
				if (this.particleTweaks$prevScale <= 0.005F) {
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
}
