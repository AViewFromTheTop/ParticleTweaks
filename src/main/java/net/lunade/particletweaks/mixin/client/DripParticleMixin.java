package net.lunade.particletweaks.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.particletweaks.impl.FluidFallingCalculator;
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DripParticle.class, priority = 1001)
public abstract class DripParticleMixin extends TextureSheetParticle implements ParticleTweakInterface {

	@Unique
	private boolean particleTweaks$hasSetMaxLifetime;
	@Unique
	private int particleTweaks$maxLifetime;

	protected DripParticleMixin(ClientLevel clientLevel, double d, double e, double f) {
		super(clientLevel, d, e, f);
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
				this.preMoveUpdate();
				this.remove();
				info.cancel();
				return;
			}

			Vec3 fluidMovement = FluidFallingCalculator.handleFluidInteraction(
				this.level,
				new Vec3(this.x, this.y, this.z),
				new Vec3(this.xd, this.yd, this.zd),
				this,
				this.getType().is(FluidTags.LAVA),
				this.particleTweaks$slowsInFluid(),
				this.particleTweaks$movesWithFluid()
			);

			if (fluidMovement != null) {
				this.xd = fluidMovement.x;
				this.yd = fluidMovement.y;
				this.zd = fluidMovement.z;
			} else {
				info.cancel();
			}
		}
	}

	@Shadow
	protected void preMoveUpdate() {}

	@Shadow
	protected abstract Fluid getType();

	@Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$getRenderType(CallbackInfoReturnable<ParticleRenderType> info) {
		info.setReturnValue(ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT);
	}

	@WrapOperation(
		method = "preMoveUpdate",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/DripParticle;remove()V"
		)
	)
	public void particleTweaks$preMoveUpdate(DripParticle instance, Operation<Void> original) {
		if (this.particleTweaks$usesNewSystem()) {
			this.lifetime = 0;
		} else {
			original.call(instance);
		}
	}

	@Override
	public boolean particleTweaks$runScaleRemoval() {
		if (this.particleTweaks$usesNewSystem()) {
			this.lifetime -= 1;
			if (this.lifetime <= 0 || this.particleTweaks$hasSwitchedToShrinking()) {
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

	@Inject(method = "createSporeBlossomFallParticle", at = @At("RETURN"))
	private static void particleTweaks$createSporeBlossomFallParticle(CallbackInfoReturnable<TextureSheetParticle> info) {
		if (info.getReturnValue() instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.15F);
			particleTweakInterface.particleTweaks$setScalesToZero();
			particleTweakInterface.particleTweaks$setSlowsInFluid(true);
			particleTweakInterface.particleTweaks$setMovesWithFluid(true);
		}
	}

	@Inject(method = "createObsidianTearHangParticle", at = @At("RETURN"))
	private static void particleTweaks$createObsidianTearHangParticle(CallbackInfoReturnable<TextureSheetParticle> info) {
		if (info.getReturnValue() instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.15F);
			particleTweakInterface.particleTweaks$setScalesToZero();
			particleTweakInterface.particleTweaks$setCanShrink(false);
		}
	}

	@Inject(method = "createObsidianTearLandParticle", at = @At("RETURN"))
	private static void particleTweaks$createObsidianTearLandParticle(CallbackInfoReturnable<TextureSheetParticle> info) {
		if (info.getReturnValue() instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.15F);
			particleTweakInterface.particleTweaks$setCanShrink(true);
			particleTweakInterface.particleTweaks$setSlowsInFluid(true);
		}
	}

	@Inject(method = "createWaterHangParticle", at = @At("RETURN"))
	private static void particleTweaks$createWaterHangParticle(CallbackInfoReturnable<TextureSheetParticle> info) {
		if (info.getReturnValue() instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.15F);
			particleTweakInterface.particleTweaks$setScalesToZero();
			particleTweakInterface.particleTweaks$setCanShrink(false);
		}
	}

	@Inject(method = "createLavaHangParticle", at = @At("RETURN"))
	private static void particleTweaks$createLavaHangParticle(CallbackInfoReturnable<TextureSheetParticle> info) {
		if (info.getReturnValue() instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.15F);
			particleTweakInterface.particleTweaks$setScalesToZero();
			particleTweakInterface.particleTweaks$setCanShrink(false);
		}
	}

	@Inject(method = "createLavaLandParticle", at = @At("RETURN"))
	private static void particleTweaks$createLavaLandParticle(CallbackInfoReturnable<TextureSheetParticle> info) {
		((ParticleTweakInterface)info.getReturnValue()).particleTweaks$setNewSystem(true);
		((ParticleTweakInterface)info.getReturnValue()).particleTweaks$setScaler(0.15F);
		((ParticleTweakInterface)info.getReturnValue()).particleTweaks$setCanShrink(true);
		((ParticleTweakInterface)info.getReturnValue()).particleTweaks$setSlowsInFluid(true);
	}

	@Inject(method = "createHoneyHangParticle", at = @At("RETURN"))
	private static void particleTweaks$createHoneyHangParticle(CallbackInfoReturnable<TextureSheetParticle> info) {
		if (info.getReturnValue() instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.075F);
			particleTweakInterface.particleTweaks$setScalesToZero();
			particleTweakInterface.particleTweaks$setCanShrink(false);
		}
	}

	@Inject(method = "createHoneyLandParticle", at = @At("RETURN"))
	private static void particleTweaks$createHoneyLandParticle(CallbackInfoReturnable<TextureSheetParticle> info) {
		if (info.getReturnValue() instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.15F);
			particleTweakInterface.particleTweaks$setCanShrink(true);
			particleTweakInterface.particleTweaks$setSlowsInFluid(true);
		}
	}

	@Inject(method = "createDripstoneWaterHangParticle", at = @At("RETURN"))
	private static void particleTweaks$createDripstoneWaterHangParticle(CallbackInfoReturnable<TextureSheetParticle> info) {
		if (info.getReturnValue() instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.15F);
			particleTweakInterface.particleTweaks$setScalesToZero();
			particleTweakInterface.particleTweaks$setCanShrink(false);
		}
	}

	@Inject(method = "createDripstoneLavaHangParticle", at = @At("RETURN"))
	private static void particleTweaks$createDripstoneLavaHangParticle(CallbackInfoReturnable<TextureSheetParticle> info) {
		if (info.getReturnValue() instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.15F);
			particleTweakInterface.particleTweaks$setScalesToZero();
			particleTweakInterface.particleTweaks$setCanShrink(false);
		}
	}

	@Inject(method = "createNectarFallParticle", at = @At("RETURN"))
	private static void particleTweaks$createNectarFallParticle(CallbackInfoReturnable<TextureSheetParticle> info) {
		if (info.getReturnValue() instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.5F);
			particleTweakInterface.particleTweaks$setScalesToZero();
			particleTweakInterface.particleTweaks$setCanShrink(false);
			particleTweakInterface.particleTweaks$setSlowsInFluid(true);
			particleTweakInterface.particleTweaks$setMovesWithFluid(true);
		}
	}

}
