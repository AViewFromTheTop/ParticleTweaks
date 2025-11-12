package net.lunade.particletweaks.scale.mixin.tweak;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.particle.FallingLeavesParticle;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = FallingLeavesParticle.class, priority = 1001)
public class FallingLeavesParticleMixin implements ParticleScaleInterface {

	@Override
	public @Nullable ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.25F);
		entrance.setToZero();
		final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.FADE, 0.1F);
		return new ParticleScaleHandler(false, entrance, exit);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void particleTweaks$injectRunScaleTick(CallbackInfo info) {
		this.particleTweaks$runScaleTick();
	}

	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	public void particleTweaks$injectRunScaleRemovalTick(CallbackInfo info) {
		this.particleTweaks$runScaleRemovalTick(Particle.class.cast(this), info);
	}

	@WrapOperation(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/FallingLeavesParticle;remove()V",
			ordinal = 1
		)
	)
	public void particleTweaks$startShrinkInsteadOfRemove(FallingLeavesParticle instance, Operation<Void> original) {
		if (this.particleTweaks$hasExit()) {
			instance.age = instance.lifetime;
			return;
		}
		original.call(instance);
	}
}
