package net.lunade.particletweaks.mixin.client.tweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.particletweaks.impl.FallingLeavesParticleInterface;
import net.lunade.particletweaks.impl.FlowingFluidParticleUtil;
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FallingLeavesParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FallingLeavesParticle.class, priority = 1001)
public abstract class FallingLeavesParticleMixin extends TextureSheetParticle implements ParticleTweakInterface {

	@Mutable
	@Shadow
	@Final
	private float windBig;
	@Shadow
	private double zaFlowScale;
	@Shadow
	private double xaFlowScale;
	@Unique
	private boolean particleTweaks$shouldStartRemoval;

	protected FallingLeavesParticleMixin(ClientLevel clientLevel, double d, double e, double f) {
		super(clientLevel, d, e, f);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		this.particleTweaks$setNewSystem(true);
		this.particleTweaks$setScaler(0.25F);
		this.particleTweaks$setSwitchesExit(true);
		this.particleTweaks$setScalesToZero();
		this.particleTweaks$setSlowsInFluid(true);
		this.particleTweaks$setMovesWithFluid(true);
		this.particleTweaks$setCanBurn(true);
	}

	@WrapWithCondition(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/FallingLeavesParticle;remove()V",
			ordinal = 0
		)
	)
	public boolean particleTweaks$preventRemoval(FallingLeavesParticle instance) {
		this.particleTweaks$shouldStartRemoval = true;
		return false;
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$runScaling(CallbackInfo info) {
		if (this.particleTweaks$usesNewSystem()) {
			this.particleTweaks$calcScale();

			Vec3 currentPos = new Vec3(this.x, this.y, this.z);
			Vec3 currentMovement = new Vec3(this.xd, this.yd, this.zd);
			Vec3 fluidMovement = FlowingFluidParticleUtil.handleFluidInteraction(
				this.level,
				currentPos,
				currentMovement,
				this,
				!this.particleTweaks$canBurn(),
				this.particleTweaks$slowsInFluid(),
				this.particleTweaks$movesWithFluid(),
				this.particleTweaks$getFluidMovementScale()
			);

			if (fluidMovement != null) {
				this.xd = fluidMovement.x;
				this.yd = fluidMovement.y;
				this.zd = fluidMovement.z;

				if (this instanceof FallingLeavesParticleInterface fallingLeavesParticleInterface) {
					boolean inWater = !fluidMovement.equals(currentMovement);
					fallingLeavesParticleInterface.particleTweaks$setInWater(inWater);
					if (inWater) {
						this.windBig = 0F;
						this.zaFlowScale = 0F;
						this.xaFlowScale = 0F;
						BlockPos blockPos = BlockPos.containing(currentPos);
						BlockState blockState = this.level.getBlockState(blockPos);
						FluidState fluidState = blockState.getFluidState();
						float fluidHeight = fluidState.getHeight(this.level, blockPos);
						float worldFluidHeight = fluidHeight + (float) blockPos.getY();
						boolean isFluidHighEnough = !fluidState.isEmpty() && worldFluidHeight >= this.y;
						if (isFluidHighEnough) {
							this.yd += 0.01D;
						}
					}
				}
			} else {
				info.cancel();
			}
		}
	}

	@ModifyExpressionValue(
		method = "tick",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/particle/FallingLeavesParticle;flowAway:Z"
		)
	)
	public boolean particleTweaks$dontFlowInWater(boolean original) {
		if (this instanceof FallingLeavesParticleInterface fallingLeavesParticleInterface) {
			if (fallingLeavesParticleInterface.particleTweaks$inWater()) return false;
		}
		return original;
	}

	@ModifyExpressionValue(
		method = "tick",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/particle/FallingLeavesParticle;swirl:Z"
		)
	)
	public boolean particleTweaks$dontSwirlInWater(boolean original) {
		if (this instanceof FallingLeavesParticleInterface fallingLeavesParticleInterface) {
			if (fallingLeavesParticleInterface.particleTweaks$inWater()) return false;
		}
		return original;
	}

	@WrapOperation(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/FallingLeavesParticle;remove()V",
			ordinal = 1
		)
	)
	public void particleTweaks$onRemove(FallingLeavesParticle instance, Operation<Void> original) {
		if (instance instanceof ParticleTweakInterface particleTweakInterface && particleTweakInterface.particleTweaks$usesNewSystem()) {
			this.particleTweaks$shouldStartRemoval = true;
		} else {
			original.call(instance);
		}
	}

	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	public void particleTweaks$removeOnceSmall(CallbackInfo info) {
		if (this.particleTweaks$usesNewSystem() && this.particleTweaks$runScaleRemoval()) {
			this.remove();
			info.cancel();
		}
	}

	@Override
	public boolean particleTweaks$runScaleRemoval() {
		if (this.particleTweaks$usesNewSystem()) {
			if (this.particleTweaks$shouldStartRemoval) {
				this.particleTweaks$setSwitchedToShrinking(true);
				if (!this.particleTweaks$canShrink()) {
					return true;
				}
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

	@Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$getRenderType(CallbackInfoReturnable<ParticleRenderType> info) {
		info.setReturnValue(ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT);
	}

}
