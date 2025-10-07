package net.lunade.particletweaks.mixin.client.splash;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.particle.WaveParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(SingleQuadParticle.Layer.class)
public class SingleQuadParticleLayerMixin {

	@Inject(method = "equals", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$equals(Object object, CallbackInfoReturnable<Boolean> info) {
		if (SingleQuadParticle.Layer.class.cast(this) == WaveParticle.WAVE != (object == WaveParticle.WAVE)) info.setReturnValue(false);
	}

}
