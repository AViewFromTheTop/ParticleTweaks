package net.lunade.particletweaks.scale.mixin.tweak;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.particle.BreakingItemParticle;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(BreakingItemParticle.class)
public class BreakingItemParticleMixin implements ParticleScaleInterface {

	@Override
	public @Nullable ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.FADE, 0.35F);
		return new ParticleScaleHandler(false, null, exit);
	}

}
