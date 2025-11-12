package net.lunade.particletweaks.trailer.mixin.bubble;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.minecraft.client.particle.BubbleParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BubbleParticle.class)
public class BubbleParticleMixin {

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		if (ParticleTweaksConfig.TRAILER_BUBBLES) BubbleParticle.class.cast(this).lifetime *= 2;
	}

}
