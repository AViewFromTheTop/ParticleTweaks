package net.lunade.particletweaks.mixin.client.wilderwild;

import net.frozenblock.wilderwild.particle.MesogleaDripParticle;
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({
	MesogleaDripParticle.BMesogleaLandProvider.class,
	MesogleaDripParticle.BPMesogleaLandProvider.class,
	MesogleaDripParticle.LMesogleaLandProvider.class,
	MesogleaDripParticle.PMesogleaLandProvider.class,
	MesogleaDripParticle.PPMesogleaLandProvider.class,
	MesogleaDripParticle.RMesogleaLandProvider.class,
	MesogleaDripParticle.YMesogleaLandProvider.class,
})
public class MesogleaLandParticleMixin {

	@Inject(method = "createParticle*", at = @At("TAIL"))
	public void particleTweaks$mesogleaLanding(CallbackInfoReturnable<Particle> info) {
		if (info.getReturnValue() instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.15F);
			particleTweakInterface.particleTweaks$setCanShrink(true);
			particleTweakInterface.particleTweaks$setSlowsInFluid(true);
			particleTweakInterface.particleTweaks$setFadeInsteadOfScale(true);
			particleTweakInterface.particleTweaks$setCanBurn(true);
		}
	}

}
