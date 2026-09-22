package net.lunade.particletweaks.scale.mixin.tweak;

import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.GeyserBaseParticle;
import org.spongepowered.asm.mixin.Mixin;

@ClientOnly
@Mixin(GeyserBaseParticle.class)
public class GeyserBaseParticleMixin implements ParticleScaleInterface {

	@Override
	public ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.75F);
		entrance.setToZero();
		final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.65F);
		return new ParticleScaleHandler(false, entrance, exit);
	}
}
