package net.lunade.particletweaks.mixin.client;

import net.lunade.particletweaks.interfaces.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FallingDustParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FallingDustParticle.class, priority = 1001)
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
			particleTweakInterface.particleTweaks$setMovesWithWater(true);
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
			BlockPos blockPos = BlockPos.containing(this.x, this.y, this.z);
			FluidState fluidState = this.level.getFluidState(blockPos);
			boolean isFluidHighEnough = false;
			boolean slowsInWater = particleTweakInterface.particleTweaks$slowsInWater();
			boolean movesWithWater = particleTweakInterface.particleTweaks$movesWithWater();
			if (slowsInWater || movesWithWater) {
				isFluidHighEnough = !fluidState.isEmpty() && (fluidState.getHeight(this.level, blockPos) + (float)blockPos.getY()) >= this.y;
			}

			if (slowsInWater && isFluidHighEnough) {
				this.xd *= 0.9;
				this.yd += 0.01;
				this.yd *= 0.2;
				this.zd *= 0.9;
			}
			if (movesWithWater && isFluidHighEnough) {
				Vec3 flow = fluidState.getFlow(this.level, blockPos);
				this.xd += flow.x() * 0.005;
				this.yd += flow.y() * 0.005;
				this.zd += flow.z() * 0.005;
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
}
