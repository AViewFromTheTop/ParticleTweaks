package net.lunade.particletweaks.scale.mixin.tweak.drip_particle;

import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.DripParticle;
import org.spongepowered.asm.mixin.Mixin;

@ClientOnly
@Mixin(DripParticle.CoolingDripHangParticle.class)
public class CoolingDripHangParticleMixin implements ParticleScaleInterface {

	@Override
	public ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.15F);
		entrance.setToZero();
		return new ParticleScaleHandler(false, entrance, null);
	}
}
