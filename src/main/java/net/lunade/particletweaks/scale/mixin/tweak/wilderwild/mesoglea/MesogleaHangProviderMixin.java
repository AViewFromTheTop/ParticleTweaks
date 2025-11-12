package net.lunade.particletweaks.scale.mixin.tweak.wilderwild.mesoglea;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.wilderwild.particle.MesogleaDripParticle;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin({
	MesogleaDripParticle.BMesogleaHangProvider.class,
	MesogleaDripParticle.BPMesogleaHangProvider.class,
	MesogleaDripParticle.LMesogleaHangProvider.class,
	MesogleaDripParticle.PMesogleaHangProvider.class,
	MesogleaDripParticle.PPMesogleaHangProvider.class,
	MesogleaDripParticle.RMesogleaHangProvider.class,
	MesogleaDripParticle.YMesogleaHangProvider.class,
})
public class MesogleaHangProviderMixin {

	@ModifyReturnValue(
		method = "createParticle*",
		at = @At("RETURN")
	)
	public Particle particleTweaks$createScaleHandler(Particle original) {
		if (!(original instanceof ParticleScaleInterface scaleInterface)) return original;

		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.15F);
		entrance.setToZero();
		final ParticleScaleHandler scaleHandler = new ParticleScaleHandler(true, entrance, null);

		scaleInterface.particleTweaks$setScaleHandler(scaleHandler);

		return original;
	}

}
