package net.lunade.particletweaks.scale.mixin.tweak;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.particle.TerrainParticle;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(TerrainParticle.class)
public class TerrainParticleMixin implements ParticleScaleInterface {

	@Override
	public ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.FADE, 0.35F);
		return new ParticleScaleHandler(false, null, exit);
	}

}
