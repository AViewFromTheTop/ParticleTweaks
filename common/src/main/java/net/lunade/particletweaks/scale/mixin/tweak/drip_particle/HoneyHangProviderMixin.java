package net.lunade.particletweaks.scale.mixin.tweak.drip_particle;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@ClientOnly
@Mixin(DripParticle.HoneyHangProvider.class)
public class HoneyHangProviderMixin {

	@ModifyReturnValue(
		method = "createParticle*",
		at = @At("RETURN")
	)
	public Particle particleTweaks$createScaleHandler(Particle original) {
		if (!(original instanceof ParticleScaleInterface scaleInterface)) return original;

		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.15F);
		entrance.setToZero();
		final ParticleScaleHandler scaleHandler = new ParticleScaleHandler(false, entrance, null);

		scaleInterface.particleTweaks$setScaleHandler(scaleHandler);

		return original;
	}
}
