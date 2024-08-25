package net.lunade.particletweaks.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.particletweaks.impl.FluidFallingCalculator;
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WaterDropParticle.class, priority = 1001)
public abstract class WaterDropParticleMixin extends TextureSheetParticle implements ParticleTweakInterface {

	@Unique
	private boolean particleTweaks$hasSetMaxLifetime;
	@Unique
	private int particleTweaks$maxLifetime;

	protected WaterDropParticleMixin(ClientLevel clientLevel, double d, double e, double f) {
		super(clientLevel, d, e, f);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		this.particleTweaks$setNewSystem(true);
		this.particleTweaks$setScaler(0.3F);
		this.particleTweaks$setScalesToZero();
		this.particleTweaks$setSwitchesExit(true);
		this.particleTweaks$setSlowsInFluid(true);
		this.particleTweaks$setMovesWithFluid(true);
		this.particleTweaks$setCanBurn(true);
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
				this.remove();
				info.cancel();
			}

			Vec3 fluidMovement = FluidFallingCalculator.handleFluidInteraction(
				this.level,
				new Vec3(this.x, this.y, this.z),
				new Vec3(this.xd, this.yd, this.zd),
				this,
				!this.particleTweaks$canBurn(),
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

	@Unique
	private int particleTweaks$storedLifetime;

	@Inject(
		method = "tick",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/particle/WaterDropParticle;lifetime:I",
			shift = At.Shift.BEFORE
		)
	)
	public void particleTweaks$stopLifetimeCheck(CallbackInfo ci) {
		if (this.particleTweaks$usesNewSystem()) {
			this.particleTweaks$storedLifetime = this.lifetime;
			this.lifetime = 100;
		}
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/particle/WaterDropParticle;lifetime:I",
			shift = At.Shift.AFTER
		)
	)
	public void particleTweaks$fixLifetimeCheck(CallbackInfo ci) {
		if (this.particleTweaks$usesNewSystem()) {
			this.lifetime = this.particleTweaks$storedLifetime - 1;
		}
	}

	@WrapOperation(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/WaterDropParticle;remove()V"
		)
	)
	public void particleTweaks$cancelRemoveOne(WaterDropParticle instance, Operation<Void> original) {
		if (!this.particleTweaks$usesNewSystem()) {
			original.call(instance);
		} else {
			this.lifetime = 0;
			this.particleTweaks$setScaler(0.75F);
			this.yd *= 0.0005;
		}
	}

	@Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$getRenderType(CallbackInfoReturnable<ParticleRenderType> info) {
		info.setReturnValue(ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT);
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
}
