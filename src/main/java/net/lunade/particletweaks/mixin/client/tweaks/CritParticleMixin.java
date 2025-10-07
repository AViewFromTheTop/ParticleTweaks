package net.lunade.particletweaks.mixin.client.tweaks;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.particle.CritParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CritParticle.class, priority = 1001)
public abstract class CritParticleMixin implements ParticleTweakInterface {

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		this.particleTweaks$setNewSystem(true);
		this.particleTweaks$setScaler(0.45F);
		this.particleTweaks$setSwitchesExit(true);
		this.particleTweaks$setScalesToZero();
	}

	@Inject(method = "getLayer", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$getRenderType(CallbackInfoReturnable<SingleQuadParticle.Layer> info) {
		info.setReturnValue(SingleQuadParticle.Layer.TRANSLUCENT);
	}

}
