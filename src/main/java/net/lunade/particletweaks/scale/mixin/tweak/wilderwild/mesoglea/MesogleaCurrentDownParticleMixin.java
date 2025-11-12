package net.lunade.particletweaks.scale.mixin.tweak.wilderwild.mesoglea;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.wilderwild.particle.MesogleaCurrentDownParticle;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(value = MesogleaCurrentDownParticle.class, priority = 1001)
public class MesogleaCurrentDownParticleMixin implements ParticleScaleInterface {

	@Override
	public @Nullable ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.35F);
		entrance.setToZero();
		return new ParticleScaleHandler(0.5F, false, entrance, null);
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$injectScaleTicks(CallbackInfo info) {
		this.particleTweaks$runScaleTick();
		this.particleTweaks$runScaleRemovalTick(Particle.class.cast(this), info);
	}

}
