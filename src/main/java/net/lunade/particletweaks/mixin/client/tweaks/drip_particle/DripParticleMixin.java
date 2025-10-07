package net.lunade.particletweaks.mixin.client.tweaks.drip_particle;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.particletweaks.impl.FlowingFluidParticleUtil;
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
public abstract class DripParticleMixin extends SingleQuadParticle implements ParticleTweakInterface {

	@Unique
	private boolean particleTweaks$hasSetMaxLifetime;
	@Unique
	private int particleTweaks$maxLifetime;

	protected DripParticleMixin(ClientLevel clientLevel, double d, double e, double f, TextureAtlasSprite textureAtlasSprite) {
		super(clientLevel, d, e, f, textureAtlasSprite);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		this.particleTweaks$setCanBurn(!this.getType().is(FluidTags.LAVA));
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$runScaling(CallbackInfo info) {
		if (!this.particleTweaks$usesNewSystem()) return;

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

		final Vec3 fluidMovement = FlowingFluidParticleUtil.handleFluidInteraction(
			this.level,
			new Vec3(this.x, this.y, this.z),
			new Vec3(this.xd, this.yd, this.zd),
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
		} else {
			info.cancel();
		}
	}

	@Shadow
	protected void preMoveUpdate() {}

	@Shadow
	protected abstract Fluid getType();

	@Inject(method = "getLayer", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$getRenderType(CallbackInfoReturnable<Layer> info) {
		info.setReturnValue(Layer.TRANSLUCENT);
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
			return;
		}
		original.call(instance);
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
