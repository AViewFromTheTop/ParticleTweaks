/*
 * Copyright 2026 Lunade Music/AViewFromTheTop
 * This file is part of Particle Tweaks.
 *
 * This program is free software; you can modify it under
 * the terms of version 1 of the FrozenBlock Modding Oasis License
 * as published by FrozenBlock Modding Oasis.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * FrozenBlock Modding Oasis License for more details.
 *
 * You should have received a copy of the FrozenBlock Modding Oasis License
 * along with this program; if not, see <https://github.com/FrozenBlock/Licenses>.
 */

package net.lunade.particletweaks.trailer.mixin.water_movement;

import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.lunade.particletweaks.trailer.api.TrailerFluidParticleSpawner;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientOnly
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

	@Inject(
		method = "baseTick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;checkBelowWorld()V"
		)
	)
	public void particleTweaks$baseTick(CallbackInfo info) {
		final Entity entity = Entity.class.cast(this);
		if (!entity.isInWater() || !ParticleTweaksConfig.TRAILER_WATER_MOVEMENT.get()) return;

		final Vec3 deltaMovement = entity.getDeltaMovement();
		final double movementLength = deltaMovement.length();
		if (movementLength == 0D) return;

		trailerBubbles:{
			if (!ParticleTweaksConfig.TRAILER_BUBBLES.get() || entity.getRandom().nextFloat() >= movementLength * 0.655D) break trailerBubbles;

			final Vec3 randomPosInside = new Vec3(entity.getRandomX(1D), entity.getRandomY(), entity.getRandomZ(1D));
			if (!TrailerFluidParticleSpawner.isUnderFluid(entity.level(), randomPosInside.x, randomPosInside.y, randomPosInside.z)) break trailerBubbles;

			entity.level().addParticle(
				ParticleTypes.BUBBLE,
				randomPosInside.x, randomPosInside.y, randomPosInside.z,
				deltaMovement.x * 1.15D, deltaMovement.y, deltaMovement.z * 1.15D
			);
		}

		trailerWaterMovement:{
			if (entity.getRandom().nextFloat() >= movementLength * 2D) break trailerWaterMovement;

			final Vec3 randomPosInside = new Vec3(entity.getRandomX(1D), entity.getRandomY(), entity.getRandomZ(1D));
			if (!TrailerFluidParticleSpawner.isUnderFluid(entity.level(), randomPosInside.x, randomPosInside.y, randomPosInside.z)) break trailerWaterMovement;

			entity.level().addParticle(
				ParticleTweaksParticleTypes.SMALL_BUBBLE.get(),
				randomPosInside.x, randomPosInside.y, randomPosInside.z,
				deltaMovement.x * 1.5D, Math.clamp(deltaMovement.y, -0.025D, 0.05D), deltaMovement.z * 1.5D
			);
		}
	}
}
