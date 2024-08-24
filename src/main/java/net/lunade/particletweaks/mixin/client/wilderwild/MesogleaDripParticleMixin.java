package net.lunade.particletweaks.mixin.client.wilderwild;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.frozenblock.wilderwild.particle.MesogleaDripParticle;
import net.lunade.particletweaks.impl.FluidFallingCalculator;
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MesogleaDripParticle.class, priority = 1001)
public abstract class MesogleaDripParticleMixin extends TextureSheetParticle implements ParticleTweakInterface {

	@Unique
	private boolean particleTweaks$hasSetMaxLifetime;
	@Unique
	private int particleTweaks$maxLifetime;

	protected MesogleaDripParticleMixin(ClientLevel clientLevel, double d, double e, double f) {
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
			}

			Vec3 fluidMovement = FluidFallingCalculator.handleFluidInteraction(
				this.level,
				new Vec3(this.x, this.y, this.z),
				new Vec3(this.xd, this.yd, this.zd),
				Particle.class.cast(this),
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

	@Shadow
	protected void preMoveUpdate() {}

	@Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$getRenderType(CallbackInfoReturnable<ParticleRenderType> info) {
		info.setReturnValue(ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT);
	}

	@WrapOperation(
		method = "preMoveUpdate",
		at = @At(
			value = "INVOKE",
			target = "Lnet/frozenblock/wilderwild/particle/MesogleaDripParticle;remove()V"
		)
	)
	public void particleTweaks$preMoveUpdate(MesogleaDripParticle instance, Operation<Void> original) {
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

}
