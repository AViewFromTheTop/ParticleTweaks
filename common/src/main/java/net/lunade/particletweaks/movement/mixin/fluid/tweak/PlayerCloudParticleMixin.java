package net.lunade.particletweaks.movement.mixin.fluid.tweak;

import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.PlayerCloudParticle;
import org.spongepowered.asm.mixin.Mixin;

@ClientOnly
@Mixin(PlayerCloudParticle.class)
public class PlayerCloudParticleMixin implements ParticleFluidMovementInterface {

	@Override
	public boolean particleTweaks$slowsInFluid() {
		return true;
	}
}
