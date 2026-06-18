package net.lunade.particletweaks.trailer.mixin.torch;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.trailer.api.TrailerTorchParticleSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(WallTorchBlock.class)
public class WallTorchBlockMixin {

	@WrapWithCondition(
		method = "animateTick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V",
			ordinal = 0
		)
	)
	public boolean particleTweaks$trailerSmoke(Level instance, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {
		return !ParticleTweaksConfig.TRAILER_TORCHES.get();
	}

	@WrapOperation(
		method = "animateTick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V",
			ordinal = 1
		)
	)
	public void particleTweaks$animateTick(
		Level instance, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd, Operation<Void> original,
		BlockState state, Level level, BlockPos pos
	) {
		if (!ParticleTweaksConfig.TRAILER_TORCHES.get()) {
			original.call(instance, particle, x, y, z, xd, yd, zd);
			return;
		}

		TrailerTorchParticleSpawner.onAnimateTick(pos);
		if (instance.getRandom().nextBoolean()) return;

		final Minecraft minecraft = Minecraft.getInstance();
		final Vec3 cameraPos = minecraft.gameRenderer.mainCamera().position();
		final Vec3 posDiff = cameraPos
			.subtract(0D, cameraPos.y, 0D)
			.subtract(new Vec3(x, 0D, z))
			.normalize().scale(0.0625D);
		original.call(instance, particle, x + posDiff.x, y - 0.125D, z + posDiff.z, xd, yd, zd);
	}

}
