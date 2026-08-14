package net.lunade.particletweaks.scale.mixin.tweak.suspended_particle;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SuspendedParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(SuspendedParticle.UnderwaterProvider.class)
public class UnderwaterProviderMixin {

	@ModifyReturnValue(
		method = "createParticle*",
		at = @At("RETURN")
	)
	public Particle particleTweaks$createScaleHandler(Particle original) {
		if (!(original instanceof ParticleScaleInterface scaleInterface)) return original;

		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.05F);
		entrance.setToZero();
		final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.1F);
		final ParticleScaleHandler scaleHandler = new ParticleScaleHandler(0.95F, false, entrance, exit);

		scaleInterface.particleTweaks$setScaleHandler(scaleHandler);

		return original;
	}

}
