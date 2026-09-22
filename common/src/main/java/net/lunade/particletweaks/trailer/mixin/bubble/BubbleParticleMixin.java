package net.lunade.particletweaks.trailer.mixin.bubble;

import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.BubbleParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientOnly
@Mixin(BubbleParticle.class)
public class BubbleParticleMixin {

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		if (ParticleTweaksConfig.TRAILER_BUBBLES.get()) BubbleParticle.class.cast(this).lifetime *= 2;
	}
}
