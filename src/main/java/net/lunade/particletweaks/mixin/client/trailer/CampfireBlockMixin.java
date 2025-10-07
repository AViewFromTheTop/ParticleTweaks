package net.lunade.particletweaks.mixin.client.trailer;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

	@WrapWithCondition(
		method = "animateTick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"
		)
	)
	public boolean particleTweaks$removeLava(Level instance, ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		return !ParticleTweaksConfig.TRAILER_CAMPFIRES;
	}

	@Inject(
		method = "animateTick",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/level/block/CampfireBlock;spawnParticles:Z",
			ordinal = 0,
			shift = At.Shift.BEFORE
		)
	)
	public void particleTweaks$AddFlares(BlockState state, Level world, BlockPos pos, RandomSource random, CallbackInfo info) {
		if (!ParticleTweaksConfig.TRAILER_CAMPFIRES) return;
		ParticleOptions particle = ParticleTweaksParticleTypes.CAMPFIRE_FLARE;
		if (state.is(Blocks.SOUL_CAMPFIRE)) particle = ParticleTweaksParticleTypes.SOUL_CAMPFIRE_FLARE;

		world.addParticle(
			particle,
			(double) pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.7D,
			(double) pos.getY() + 0.5D + (random.nextDouble()) * 0.25D,
			(double) pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.7D,
			random.nextFloat() * 0.5F,
			0.02D,
			random.nextFloat() * 0.5F
		);
	}

}
