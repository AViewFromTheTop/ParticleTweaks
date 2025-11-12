package net.lunade.particletweaks.movement.mixin.fluid.tweak;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import net.minecraft.client.particle.SquidInkParticle;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(SquidInkParticle.class)
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
