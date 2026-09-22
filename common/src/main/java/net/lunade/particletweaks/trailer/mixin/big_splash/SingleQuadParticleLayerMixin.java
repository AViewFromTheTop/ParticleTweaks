package net.lunade.particletweaks.trailer.mixin.big_splash;

import net.lunade.particletweaks.particle.WaveParticle;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.SingleQuadParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@ClientOnly
@Mixin(SingleQuadParticle.Layer.class)
public class SingleQuadParticleLayerMixin {

	@Inject(method = "equals", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$equals(Object o, CallbackInfoReturnable<Boolean> info) {
		if (SingleQuadParticle.Layer.class.cast(this) == WaveParticle.WAVE != (o == WaveParticle.WAVE)) info.setReturnValue(false);
	}
}
