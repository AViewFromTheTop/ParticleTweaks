package net.lunade.particletweaks.trailer.mixin.ambient_water;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(WaterFluid.class)
public class WaterFluidMixin {

	@WrapOperation(
		method = "animateTick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"
		)
	)
	public void particleTweaks$useSmallBubble(Level instance, ParticleOptions options, double x, double y, double z, double xd, double yd, double zd, Operation<Void> original) {
		if (!ParticleTweaksConfig.TRAILER_AMBIENT_WATER) {
			original.call(instance, options, x, y, z, xd, yd, zd);
			return;
		}

		if (instance.getRandom().nextFloat() >= 0.175F) return;
		original.call(
			instance,
			ParticleTweaksParticleTypes.SMALL_BUBBLE,
			x, y, z,
			xd,
			instance.getRandom().nextDouble() * (instance.getRandom().nextFloat() <= 0.1F ? 0.05D : 0.0125D),
			zd
		);
	}

}
