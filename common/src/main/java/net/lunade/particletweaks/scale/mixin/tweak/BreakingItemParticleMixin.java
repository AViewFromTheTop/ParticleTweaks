package net.lunade.particletweaks.scale.mixin.tweak;

import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.BreakingItemParticle;
import org.spongepowered.asm.mixin.Mixin;

@ClientOnly
@Mixin(BreakingItemParticle.class)
public class BreakingItemParticleMixin implements ParticleScaleInterface {

	@Override
	public ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.FADE, 0.35F);
		return new ParticleScaleHandler(false, null, exit);
	}
}
