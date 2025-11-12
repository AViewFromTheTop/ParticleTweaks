package net.lunade.particletweaks.movement.mixin.fluid.tweak.drip_particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.movement.impl.MutableParticleFluidMovementInterface;
import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(DripParticle.class)
public abstract class DripParticleMixin implements ParticleFluidMovementInterface, MutableParticleFluidMovementInterface {

	@Shadow
	protected abstract Fluid getType();

	@Unique
	private boolean particleTweaks$slowsInFluid = false;
	@Unique
	private boolean particleTweaks$movesWithFluid = false;

	@Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
	public void particleTweaks$moveWithFluid(CallbackInfo info) {
		this.particleTweaks$runFluidMovementTick(DripParticle.class.cast(this), info);
	}

	@Override
	public void particleTweaks$setCanBurn(boolean canBurn) {
	}

	@Override
	public boolean particleTweaks$canBurn() {
		return !this.getType().is(FluidTags.LAVA);
	}

	@Override
	public void particleTweaks$setSlowsInFluid(boolean slowsInFluid) {
		this.particleTweaks$slowsInFluid = slowsInFluid;
	}

	@Override
	public boolean particleTweaks$slowsInFluid() {
		return this.particleTweaks$slowsInFluid;
	}

	@Override
	public void particleTweaks$setMovesWithFluid(boolean movesWithFluid) {
		this.particleTweaks$movesWithFluid = movesWithFluid;
	}

	@Override
	public boolean particleTweaks$movesWithFluid() {
		return this.particleTweaks$movesWithFluid;
	}
}
