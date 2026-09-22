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

package net.lunade.particletweaks.trailer.mixin.ripple;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.FluidState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@ClientOnly
@Mixin(ClientLevel.class)
public class ClientLevelMixin {

	@WrapOperation(
		method = "tickWeatherEffects",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/BlockPos;offset(III)Lnet/minecraft/core/BlockPos;"
		)
	)
	public BlockPos particleTweaks$extendRainParticleRange(
		BlockPos instance, int x, int y, int z, Operation<BlockPos> original,
		@Local(name = "random") RandomSource random
	) {
		if (ParticleTweaksConfig.TRAILER_RIPPLES.get()) {
			x = (int) (x * 2.5D);
			z = (int) (z * 2.5D);
		}
		return original.call(instance, x, y, z);
	}

	@ModifyExpressionValue(
		method = "tickWeatherEffects",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/core/particles/ParticleTypes;RAIN:Lnet/minecraft/core/particles/SimpleParticleType;",
			opcode = Opcodes.GETSTATIC
		)
	)
	public SimpleParticleType particleTweaks$useRippleOnWater(
		SimpleParticleType original,
		@Local(name = "fluid") FluidState fluid
	) {
		if (ParticleTweaksConfig.TRAILER_RIPPLES.get() && fluid.is(FluidTags.WATER)) return ParticleTweaksParticleTypes.RIPPLE.get();
		return original;
	}
}
