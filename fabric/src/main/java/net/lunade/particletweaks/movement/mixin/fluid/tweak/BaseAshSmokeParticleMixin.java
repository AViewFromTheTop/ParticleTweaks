package net.lunade.particletweaks.movement.mixin.fluid.tweak;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(BaseAshSmokeParticle.class)
public class BaseAshSmokeParticleMixin implements ParticleFluidMovementInterface {

	@Override
	public boolean particleTweaks$slowsInFluid() {
		return true;
	}

	@Override
	public boolean particleTweaks$movesWithFluid() {
		return true;
	}
}
