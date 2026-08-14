package net.lunade.particletweaks.movement.mixin.fluid.tweak;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import net.minecraft.client.particle.FallingLeavesParticle;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = FallingLeavesParticle.class, priority = 1001)
public class FallingLeavesParticleMixin implements ParticleFluidMovementInterface {

	@Shadow
	@Final
	@Mutable
	private float windBig;
	@Shadow
	@Final
	@Mutable
	private double zaFlowScale;
	@Shadow
	@Final
	@Mutable
	private double xaFlowScale;

	@Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
	public void particleTweaks$moveWithFluid(CallbackInfo info) {
		this.particleTweaks$runFluidMovementTick(FallingLeavesParticle.class.cast(this), info);
	}

	@ModifyExpressionValue(
		method = "tick",
		at = {
			@At(
				value = "FIELD",
				target = "Lnet/minecraft/client/particle/FallingLeavesParticle;flowAway:Z"
			),
			@At(
				value = "FIELD",
				target = "Lnet/minecraft/client/particle/FallingLeavesParticle;swirl:Z"
			)
		}
	)
	public boolean particleTweaks$dontFlowOrSwirlInFluid(boolean original) {
		if (this.particleTweaks$touchingFluid()) return false;
		return original;
	}

	@Override
	public boolean particleTweaks$canBurn() {
		return true;
	}

	@Override
	public boolean particleTweaks$slowsInFluid() {
		return true;
	}

	@Override
	public boolean particleTweaks$movesWithFluid() {
		return true;
	}

	@Override
	public double particleTweaks$fluidAdditionalSlowVerticalScaleDownward() {
		return 0.15D;
	}

	@Override
	public void particleTweaks$runFluidMovementTick(Particle particle, CallbackInfo info) {
		ParticleFluidMovementInterface.super.particleTweaks$runFluidMovementTick(particle, info);
		if (FallingLeavesParticle.class.cast(this).removed) return;
		if (!this.particleTweaks$touchingFluid()) return;

		this.windBig = 0F;
		this.zaFlowScale = 0F;
		this.xaFlowScale = 0F;
		FallingLeavesParticle.class.cast(this).yd += 0.005D;
	}
}
