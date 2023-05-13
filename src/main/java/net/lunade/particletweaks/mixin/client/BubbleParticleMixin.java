package net.lunade.particletweaks.mixin.client;

import net.lunade.particletweaks.interfaces.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BubbleParticle.class)
public abstract class BubbleParticleMixin extends TextureSheetParticle implements ParticleTweakInterface {

	@Unique
	private float particleTweaks$scalerDrip = 0.15F;
	@Unique
	private float particleTweaks$prevScaleDrip = 1F;
	@Unique
	private float particleTweaks$scaleDrip = 1F;
	@Unique
	private float particleTweaks$targetScaleDrip = 1F;
	@Unique
	private boolean particleTweaks$useNewSystemDrip = false;
	@Unique
	private boolean particleTweaks$hasSwitchedToShrinkingDrip = false;
	@Unique
	private boolean particleTweaks$canShrinkDrip = true;
	@Unique
	private boolean particleTweaks$fadeInsteadOfShrink = false;
	@Unique
	private boolean particleTweaks$hasSetMaxLifetime;
	@Unique
	private int particleTweaks$maxLifetime;
	@Unique
	private boolean particleTweaks$switchesExit = false;

	protected BubbleParticleMixin(ClientLevel clientLevel, double d, double e, double f) {
		super(clientLevel, d, e, f);
	}

	@Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$getRenderType(CallbackInfoReturnable<ParticleRenderType> info) {
		info.setReturnValue(ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT);
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$runScaling(CallbackInfo info) {
		if (this.particleTweaks$usesNewSystem()) {
			if (!this.particleTweaks$hasSetMaxLifetime) {
				this.particleTweaks$hasSetMaxLifetime = true;
				this.particleTweaks$maxLifetime = this.lifetime;
			}
			this.particleTweaks$calcScale();
			this.lifetime = Math.min(this.lifetime + 1, this.particleTweaks$maxLifetime);
			if (this.particleTweaks$getScale(0F) < 0.5F && !this.particleTweaks$hasSwitchedToShrinking()) {
				this.lifetime = Math.min(this.lifetime + 1, this.particleTweaks$maxLifetime);
			}
			if (this.particleTweaks$runScaleRemoval()) {
				this.level.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, 0, 0, 0);
				this.remove();
				info.cancel();
			}
			BlockPos blockPos = BlockPos.containing(this.x, this.y, this.z);
			FluidState fluidState = this.level.getFluidState(blockPos);
			if (this.particleTweaks$slowsInWater() && fluidState.is(FluidTags.WATER)) {
				this.xd *= 0.9;
				this.yd += 0.02;
				this.yd *= 0.3;
				this.zd *= 0.9;
			}
			if (this.particleTweaks$movesWithWater()) {
				Vec3 flow = fluidState.getFlow(this.level, blockPos);
				this.xd += flow.x() * 0.005;
				this.yd += flow.y() * 0.005;
				this.zd += flow.z() * 0.005;
			}
		}
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/BubbleParticle;remove()V"))
	public void particleTweaks$outOfWater(BubbleParticle particle) {
		if (this.particleTweaks$usesNewSystem()) {
			this.lifetime = 0;
		}
	}

	@Override
	public float particleTweaks$getScale(float partialTick) {
		return this.particleTweaks$usesNewSystem() ? Mth.lerp(partialTick, this.particleTweaks$prevScaleDrip, this.particleTweaks$scaleDrip) : 1F;
	}

	@Override
	public void particleTweaks$calcScale() {
		this.particleTweaks$prevScaleDrip = this.particleTweaks$scaleDrip;
		this.particleTweaks$scaleDrip += (this.particleTweaks$targetScaleDrip - this.particleTweaks$scaleDrip) * this.particleTweaks$scalerDrip;
	}

	@Override
	public boolean particleTweaks$runScaleRemoval() {
		if (this.particleTweaks$usesNewSystem()) {
			this.lifetime -= 1;
			if (this.lifetime <= 0 || this.particleTweaks$hasSwitchedToShrinking()) {
				this.particleTweaks$hasSwitchedToShrinkingDrip = true;
				return true;
			} else {
				this.particleTweaks$targetScaleDrip = 1F;
			}
		}
		return false;
	}

	@Override
	public void particleTweaks$setScaler(float scaler) {
		this.particleTweaks$scalerDrip = scaler;
	}

	@Override
	public void particleTweaks$setNewSystem(boolean set) {
		this.particleTweaks$useNewSystemDrip = set;
	}

	@Override
	public boolean particleTweaks$usesNewSystem() {
		return this.particleTweaks$useNewSystemDrip;
	}

	@Override
	public void particleTweaks$setScalesToZero() {
		this.particleTweaks$prevScaleDrip = 0F;
		this.particleTweaks$scaleDrip = 0F;
	}

	@Override
	public boolean particleTweaks$hasSwitchedToShrinking() {
		return this.particleTweaks$hasSwitchedToShrinkingDrip;
	}

	@Override
	public void particleTweaks$setCanShrink(boolean canShrink) {
		this.particleTweaks$canShrinkDrip = canShrink;
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

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		if (BubbleParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.35F);
			particleTweakInterface.particleTweaks$setScalesToZero();
			particleTweakInterface.particleTweaks$setMovesWithWater(true);
			if (!this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).is(FluidTags.WATER)) {
				this.y = -99999;
				this.yo = -99999;
			}
		}
	}

}
