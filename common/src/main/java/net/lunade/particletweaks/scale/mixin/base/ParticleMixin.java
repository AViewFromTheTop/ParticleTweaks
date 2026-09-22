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

package net.lunade.particletweaks.scale.mixin.base;

import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientOnly
@Mixin(value = Particle.class, priority = 1001)
public abstract class ParticleMixin implements ParticleScaleInterface {

	@Unique
	@Nullable
	private ParticleScaleHandler particleTweaks$scaleHandler = this.particleTweaks$createScaleHandler();

	@Inject(method = "tick", at = @At("HEAD"))
	public void particleTweaks$injectRunScaleTick(CallbackInfo info) {
		this.particleTweaks$runScaleTick();
	}

	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	public void particleTweaks$injectRunScaleRemovalTick(CallbackInfo info) {
		this.particleTweaks$runScaleRemovalTick(Particle.class.cast(this), info);
	}

	@Override
	public void particleTweaks$runScaleTick() {
		if (this.particleTweaks$scaleHandler == null) return;
		this.particleTweaks$scaleHandler.runScaleTick(Particle.class.cast(this));
	}

	@Override
	public void particleTweaks$runScaleRemovalTick(Particle particle, CallbackInfo info) {
		if (this.particleTweaks$scaleHandler == null) return;
		if (this.particleTweaks$scaleHandler.runScaleRemovalTick(Particle.class.cast(this))) info.cancel();
	}

	@Override
	public void particleTweaks$setScaleHandler(ParticleScaleHandler scaleHandler) {
		this.particleTweaks$scaleHandler = scaleHandler;
	}

	@Override
	@Nullable
	public ParticleScaleHandler particleTweaks$getScaleHandler() {
		return this.particleTweaks$scaleHandler;
	}
}
