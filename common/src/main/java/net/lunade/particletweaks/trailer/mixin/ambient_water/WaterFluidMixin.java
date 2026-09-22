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

package net.lunade.particletweaks.trailer.mixin.ambient_water;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@ClientOnly
@Mixin(WaterFluid.class)
public class WaterFluidMixin {

	@WrapOperation(
		method = "animateTick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"
		)
	)
	public void particleTweaks$useSmallBubble(Level instance, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd, Operation<Void> original) {
		if (!ParticleTweaksConfig.TRAILER_AMBIENT_WATER.get()) {
			original.call(instance, particle, x, y, z, xd, yd, zd);
			return;
		}

		if (instance.getRandom().nextFloat() >= 0.175F) return;
		original.call(
			instance,
			ParticleTweaksParticleTypes.SMALL_BUBBLE.get(),
			x, y, z,
			xd,
			instance.getRandom().nextDouble() * (instance.getRandom().nextFloat() <= 0.1F ? 0.05D : 0.0125D),
			zd
		);
	}
}
