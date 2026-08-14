package net.lunade.particletweaks.movement.mixin.fluid.tweak.suspended_particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.movement.impl.MutableParticleFluidMovementInterface;
import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import net.minecraft.client.particle.SuspendedParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(SuspendedParticle.class)
public class SuspendedParticleMixin implements ParticleFluidMovementInterface, MutableParticleFluidMovementInterface {

	@Unique
	private boolean particleTweaks$canBurn = true;
	@Unique
	private boolean particleTweaks$slowsInFluid = true;
	@Unique
	private boolean particleTweaks$movesWithFluid = true;

	@Override
	public void particleTweaks$setCanBurn(boolean canBurn) {
		this.particleTweaks$canBurn = canBurn;
	}

	@Override
	public boolean particleTweaks$canBurn() {
		return this.particleTweaks$canBurn;
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
