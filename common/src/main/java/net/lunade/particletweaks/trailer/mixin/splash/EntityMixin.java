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

package net.lunade.particletweaks.trailer.mixin.splash;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.lunade.particletweaks.trailer.api.TrailerFluidParticleSpawner;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

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

	@WrapOperation(
		method = "doWaterSplashEffect",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V",
			ordinal = 0
		)
	)
	public void particleTweaks$replacePoppingBubbles(
		Level instance, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd, Operation<Void> original
	) {
		if ((ParticleTweaksConfig.TRAILER_BUBBLES.get() || ParticleTweaksConfig.TRAILER_SPLASHES.get()) && !TrailerFluidParticleSpawner.isUnderFluid(instance, x, y - 0.35D, z)) {
			if (!ParticleTweaksConfig.TRAILER_SPLASHES.get()) return;
			particle = ParticleTweaksParticleTypes.SPLASH.get();
		}
		original.call(instance, particle, x, y, z, xd, yd, zd);
	}
}
