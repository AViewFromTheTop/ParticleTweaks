package net.lunade.particletweaks.scale.mixin.tweak;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.particle.CritParticle;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(CritParticle.DamageIndicatorProvider.class)
public class DamageIndicatorProviderMixin {

	@ModifyReturnValue(
		method = "createParticle*",
		at = @At("RETURN")
	)
	public Particle particleTweaks$createScaleHandler(Particle original) {
		if (!(original instanceof ParticleScaleInterface scaleInterface)) return original;

		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.2F);
		entrance.setToZero();
		final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.FADE, 0.3F);
		final ParticleScaleHandler scaleHandler = new ParticleScaleHandler(false, entrance, exit);

		scaleInterface.particleTweaks$setScaleHandler(scaleHandler);

		return original;
	}

}
