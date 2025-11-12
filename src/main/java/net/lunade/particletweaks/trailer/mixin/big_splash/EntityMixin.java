package net.lunade.particletweaks.trailer.mixin.big_splash;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.particle.WaveSeedParticle;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow
	private EntityDimensions dimensions;

	@Shadow
	public abstract double getX();

	@Shadow
	public abstract double getZ();

	@Shadow
	private Level level;

	@Shadow
	@Final
	protected RandomSource random;

	@Shadow
	public abstract double getY();

	@Inject(
		method = "doWaterSplashEffect",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;gameEvent(Lnet/minecraft/core/Holder;)V",
			shift = At.Shift.BEFORE
		)
	)
	public void particleTweaks$doWaterSplashEffect(CallbackInfo info) {
		if (!ParticleTweaksConfig.TRAILER_WAVES) return;

		final Entity entity = Entity.class.cast(this);
		final Vec3 vec3 = entity.getDeltaMovement();
		final EntityDimensions entityDimensions = this.dimensions;
		final float width = entityDimensions.width();

		final double ySpeed = Math.abs(vec3.y);
		final double strength = ySpeed * ((entity instanceof Player) ? 0.45D : width * 1.1D);
		boolean useGenericSplash = false;
		if (strength >= 0.4D) {
			final BlockPos pos = entity.blockPosition();
			final BlockState state = this.level.getBlockState(pos);
			final FluidState fluidState = state.getFluidState();

			if (fluidState.isSourceOfType(Fluids.WATER) && state.getCollisionShape(this.level, pos).isEmpty()) {
				int waterSurface = pos.getY() + 1;
				for (int i = 1; true; i++) {
					if (i == 4) return;
					final BlockState aboveState = this.level.getBlockState(pos.above(i));
					final FluidState aboveFluidState = aboveState.getFluidState();
					if (aboveFluidState.isSourceOfType(Fluids.WATER)) {
						waterSurface += 1;
					} else if (!aboveFluidState.isEmpty()) {
						return;
					} else {
						break;
					}
				}
				entity.level().addAlwaysVisibleParticle(
					ParticleTweaksParticleTypes.WAVE_SEED,
					this.getX(),
					waterSurface,
					this.getZ(),
					width,
					strength - 0.35D,
					0F
				);
			} else {
				useGenericSplash = fluidState.is(FluidTags.WATER);
			}
		} else {
			useGenericSplash = true;
		}

		if (useGenericSplash && vec3.horizontalDistance() != 0D) {
			BlockPos pos = entity.blockPosition();
			int waterSurface = pos.getY() + 1;
			for (int i = 1; true; i++) {
				if (i == 4) return;
				final BlockState aboveState = this.level.getBlockState(pos.above(i));
				final FluidState aboveFluidState = aboveState.getFluidState();
				if (aboveFluidState.isSourceOfType(Fluids.WATER)) {
					waterSurface += 1;
				} else if (!aboveFluidState.isEmpty()) {
					return;
				} else {
					break;
				}
			}
			WaveSeedParticle.spawnSplashParticles(
				this.level,
				this.getX(),
				waterSurface,
				this.getZ(),
				this.random,
				this.random.nextInt(2, 5),
				width,
				0.1F,
				0.2F
			);
		}
	}
}
