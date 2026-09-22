package net.lunade.particletweaks.movement.mixin.fluid.tweak;

import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.WakeParticle;
import org.spongepowered.asm.mixin.Mixin;

@ClientOnly
@Mixin(WakeParticle.class)
public class WakeParticleMixin implements ParticleFluidMovementInterface {

	@Override
	public boolean particleTweaks$slowsInFluid() {
		return true;
	}

	@Override
	public boolean particleTweaks$canBurn() {
		return true;
	}

	@Override
	public boolean particleTweaks$movesWithFluid() {
		return true;
	}

	@Override
	public double particleTweaks$fluidSlowHorizontalScale() {
		return 0.9D;
	}

	@Override
	public double particleTweaks$fluidSlowVerticalScale() {
		return 0.2D;
	}

	@Override
	public double particleTweaks$additionalYOnFluidSlow() {
		return 0.02D;
	}
}
