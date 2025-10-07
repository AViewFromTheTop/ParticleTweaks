package net.lunade.particletweaks.mixin.client.trailer;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(WeatherEffectRenderer.class)
public class WeatherEffectRendererMixin {

	@WrapOperation(
		method = "tickRainParticles",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/BlockPos;offset(III)Lnet/minecraft/core/BlockPos;"
		)
	)
	public BlockPos particleTweaks$extendRainParticleRange(
		BlockPos instance, int i, int j, int k, Operation<BlockPos> original,
		@Local RandomSource random
	) {
		if (ParticleTweaksConfig.TRAILER_SPLASHES) {
			i = random.nextIntBetweenInclusive(-30, 30);
			k = random.nextIntBetweenInclusive(-30, 30);
		}
		return original.call(instance, i, j, k);
	}

	@ModifyExpressionValue(
		method = "tickRainParticles",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/core/particles/ParticleTypes;RAIN:Lnet/minecraft/core/particles/SimpleParticleType;"
		)
	)
	public SimpleParticleType particleTweaks$useRippleOnWater(
		SimpleParticleType original,
		@Local FluidState fluidState
	) {
		if (ParticleTweaksConfig.TRAILER_SPLASHES && fluidState.is(FluidTags.WATER)) return ParticleTweaksParticleTypes.RIPPLE;
		return original;
	}
}
