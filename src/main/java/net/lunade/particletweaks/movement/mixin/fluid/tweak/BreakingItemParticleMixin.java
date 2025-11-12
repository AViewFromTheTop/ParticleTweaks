package net.lunade.particletweaks.movement.mixin.fluid.tweak;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import net.minecraft.client.particle.BreakingItemParticle;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(BreakingItemParticle.class)
public class BreakingItemParticleMixin implements ParticleFluidMovementInterface {

	@Override
	public boolean particleTweaks$slowsInFluid() {
		return true;
	}

	@Override
	public boolean particleTweaks$movesWithFluid() {
		return true;
	}

	@Override
	public double particleTweaks$fluidSlowVerticalScale() {
		return 0.5D;
	}

	@Override
	public double particleTweaks$fluidAdditionalSlowVerticalScaleDownward() {
		return 0.15D;
	}
}
