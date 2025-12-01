package net.lunade.particletweaks.trailer.mixin.splash;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.lunade.particletweaks.trailer.api.TrailerFluidParticleSpawner;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow
	public abstract double getX();
	@Shadow
	public abstract double getZ();
	@Shadow
	@Final
	protected RandomSource random;
	@Shadow
	public abstract double getY();

	@WrapOperation(
		method = "doWaterSplashEffect",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V",
			ordinal = 0
		)
	)
	public void particleTweaks$replacePoppingBubbles(
		Level instance, ParticleOptions options, double x, double y, double z, double xd, double yd, double zd, Operation<Void> original
	) {
		if ((ParticleTweaksConfig.TRAILER_BUBBLES || ParticleTweaksConfig.TRAILER_SPLASHES) && !TrailerFluidParticleSpawner.isUnderFluid(instance, x, y - 0.35D, z)) {
			if (!ParticleTweaksConfig.TRAILER_SPLASHES) return;
			options = ParticleTweaksParticleTypes.SPLASH;
		}
		original.call(instance, options, x, y, z, xd, yd, zd);
	}

}
