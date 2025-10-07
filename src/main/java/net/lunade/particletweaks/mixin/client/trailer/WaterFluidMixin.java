package net.lunade.particletweaks.mixin.client.trailer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.impl.FlowingFluidParticleUtil;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
	public void particleTweaks$useSmallBubble(
		Level instance, ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Void> original
	) {
		if (ParticleTweaksConfig.TRAILER_AMBIENT_WATER) {
			if (instance.random.nextFloat() <= 0.175F) {
				original.call(
					instance,
					ParticleTweaksParticleTypes.SMALL_BUBBLE,
					x,
					y,
					z,
					velocityX,
					instance.random.nextDouble() * (instance.random.nextFloat() <= 0.1F ? 0.05D : 0.0125D),
					velocityZ
				);
			}
		} else {
			original.call(instance, parameters, x, y, z, velocityX, velocityY, velocityZ);
		}
	}

	@Inject(method = "animateTick", at = @At("TAIL"))
	public void particleTweaks$animateTick(Level world, BlockPos pos, FluidState state, RandomSource random, CallbackInfo info) {
		FlowingFluidParticleUtil.onAnimateTick(
			world,
			pos,
			state,
			random,
			3,
			3,
			1,
			true,
			true,
			ParticleTweaksParticleTypes.FLOWING_WATER
		);
	}
}
