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

package net.lunade.particletweaks.scale.impl;

import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientOnly
public interface ParticleScaleInterface {
	default @Nullable ParticleScaleHandler particleTweaks$createScaleHandler() {
		return null;
	}

	default void particleTweaks$setScaleHandler(ParticleScaleHandler scaleHandler) {}

	default @Nullable ParticleScaleHandler particleTweaks$getScaleHandler() {
		return null;
	}

	default void particleTweaks$runScaleTick() {}

	default void particleTweaks$runScaleRemovalTick(Particle particle, CallbackInfo info) {
		final ParticleScaleHandler scaleHandler = particleTweaks$getScaleHandler();
		if (scaleHandler == null) return;
		if (scaleHandler.runScaleRemovalTick(particle)) info.cancel();
	}

	default boolean particleTweaks$hasExit() {
		final ParticleScaleHandler scaleHandler = particleTweaks$getScaleHandler();
		return scaleHandler != null && scaleHandler.exit() != null;
	}
}
