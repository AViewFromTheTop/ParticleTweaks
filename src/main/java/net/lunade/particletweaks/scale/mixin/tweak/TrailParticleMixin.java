package net.lunade.particletweaks.scale.mixin.tweak;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TrailParticle;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = TrailParticle.class, priority = 1001)
public class TrailParticleMixin implements ParticleScaleInterface {

	@Override
	public @Nullable ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.35F);
		entrance.setToZero();
		final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.FADE, 0.35F);
		return new ParticleScaleHandler(0.35F, false, 10, entrance, exit);
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$injectScaleTicks(CallbackInfo info) {
		this.particleTweaks$runScaleTick();
		this.particleTweaks$runScaleRemovalTick(Particle.class.cast(this), info);
	}

}
