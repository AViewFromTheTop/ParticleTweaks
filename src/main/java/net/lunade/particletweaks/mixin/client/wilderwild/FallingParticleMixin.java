package net.lunade.particletweaks.mixin.client.wilderwild;

import net.frozenblock.wilderwild.particle.FallingParticle;
import net.lunade.particletweaks.interfaces.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingParticle.class)
public abstract class FallingParticleMixin extends TextureSheetParticle {

	protected FallingParticleMixin(ClientLevel clientLevel, double d, double e, double f) {
		super(clientLevel, d, e, f);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		if (FallingParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScaler(0.3F);
			particleTweakInterface.particleTweaks$setScalesToZero();
			particleTweakInterface.particleTweaks$setSwitchesExit(true);
			particleTweakInterface.particleTweaks$setSlowsInWater(true);
			particleTweakInterface.particleTweaks$setMovesWithWater(true);
		}
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/frozenblock/wilderwild/particle/FallingParticle;remove()V"))
	public void particleTweaks$cancelRemoveOne(FallingParticle fallingParticle) {
		if (fallingParticle instanceof ParticleTweakInterface particleTweakInterface) {
			if (!particleTweakInterface.particleTweaks$usesNewSystem()) {
				fallingParticle.remove();
			} else {
				fallingParticle.setLifetime(0);
				particleTweakInterface.particleTweaks$setScaler(0.75F);
				this.yd *= 0.0005;
			}
		}
	}

}
