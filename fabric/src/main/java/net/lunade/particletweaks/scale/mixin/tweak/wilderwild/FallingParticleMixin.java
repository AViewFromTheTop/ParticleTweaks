package net.lunade.particletweaks.scale.mixin.tweak.wilderwild;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.wilderwild.particle.FallingParticle;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(FallingParticle.class)
public class FallingParticleMixin implements ParticleScaleInterface {

	@Override
	public ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.3F);
		entrance.setToZero();
		final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.FADE, 0.75F);
		return new ParticleScaleHandler(true, entrance, exit);
	}

	@WrapOperation(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/frozenblock/wilderwild/particle/FallingParticle;remove()V"
		)
	)
	public void particleTweaks$shrinkInsteadOfRemove(FallingParticle instance, Operation<Void> original) {
		if (this.particleTweaks$hasExit()) {
			instance.setLifetime(0);
			instance.yd *= 0.0005;
			return;
		}
		original.call(instance);
	}

}
