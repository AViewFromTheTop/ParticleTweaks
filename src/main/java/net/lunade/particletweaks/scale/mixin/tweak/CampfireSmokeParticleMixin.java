package net.lunade.particletweaks.scale.mixin.tweak;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(CampfireSmokeParticle.class)
public class CampfireSmokeParticleMixin implements ParticleScaleInterface {

	@Override
	public @Nullable ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.15F);
		entrance.setToZero();
		return new ParticleScaleHandler(0.5F, true, entrance, null);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void particleTweaks$injectScaleTicks(CallbackInfo info) {
		this.particleTweaks$runScaleTick();
	}

	@Override
	public void particleTweaks$runScaleRemovalTick(Particle particle, CallbackInfo info) {
	}

}
