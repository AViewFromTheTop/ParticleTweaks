package net.lunade.particletweaks.movement.mixin.fluid.tweak;

import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.DripParticle;
import org.spongepowered.asm.mixin.Mixin;

@ClientOnly
@Mixin(DripParticle.FallAndLandParticle.class)
public class FallAndLandParticleMixin implements ParticleFluidMovementInterface {

	@Override
	public boolean particleTweaks$slowsInFluid() {
		return true;
	}
}
