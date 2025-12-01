package net.lunade.particletweaks.scale.mixin.base;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = Particle.class, priority = 1001)
public abstract class ParticleMixin implements ParticleScaleInterface {

	@Unique
	@Nullable
	private ParticleScaleHandler particleTweaks$scaleHandler = this.particleTweaks$createScaleHandler();

	@Inject(method = "tick", at = @At("HEAD"))
	public void particleTweaks$injectRunScaleTick(CallbackInfo info) {
		this.particleTweaks$runScaleTick();
	}

	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	public void particleTweaks$injectRunScaleRemovalTick(CallbackInfo info) {
		this.particleTweaks$runScaleRemovalTick(Particle.class.cast(this), info);
	}

	@Override
	public void particleTweaks$runScaleTick() {
		if (this.particleTweaks$scaleHandler == null) return;
		this.particleTweaks$scaleHandler.runScaleTick(Particle.class.cast(this));
	}

	@Override
	public void particleTweaks$runScaleRemovalTick(Particle particle, CallbackInfo info) {
		if (this.particleTweaks$scaleHandler == null) return;
		if (this.particleTweaks$scaleHandler.runScaleRemovalTick(Particle.class.cast(this))) info.cancel();
	}

	@Override
	public void particleTweaks$setScaleHandler(ParticleScaleHandler scaleHandler) {
		this.particleTweaks$scaleHandler = scaleHandler;
	}

	@Override
	@Nullable
	public ParticleScaleHandler particleTweaks$getScaleHandler() {
		return this.particleTweaks$scaleHandler;
	}

}
