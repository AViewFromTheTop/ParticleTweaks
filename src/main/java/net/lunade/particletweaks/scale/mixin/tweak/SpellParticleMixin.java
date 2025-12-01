package net.lunade.particletweaks.scale.mixin.tweak;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.particle.SpellParticle;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(SpellParticle.class)
public class SpellParticleMixin implements ParticleScaleInterface {

	@Override
	public ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, ParticleTweaksConfig.TRAILER_SPELL ? 0.15F : 0.35F);
		entrance.setToZero();
		final ParticleScaler exit = new ParticleScaler(
			ParticleTweaksConfig.TRAILER_SPELL ? ParticleScaler.ScaleMethod.SIZE : ParticleScaler.ScaleMethod.FADE,
			ParticleTweaksConfig.TRAILER_SPELL ? 0.15F : 0.375F
		);
		return new ParticleScaleHandler(false, entrance, exit);
	}
}
