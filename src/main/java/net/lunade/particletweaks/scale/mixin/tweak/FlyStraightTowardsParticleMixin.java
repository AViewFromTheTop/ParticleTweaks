package net.lunade.particletweaks.scale.mixin.tweak;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.particle.FlyStraightTowardsParticle;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = FlyStraightTowardsParticle.class, priority = 1001)
public class FlyStraightTowardsParticleMixin implements ParticleScaleInterface {

	@Override
	public @Nullable ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.3F);
		entrance.setToZero();
		final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.FADE, 0.3F);
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
}
