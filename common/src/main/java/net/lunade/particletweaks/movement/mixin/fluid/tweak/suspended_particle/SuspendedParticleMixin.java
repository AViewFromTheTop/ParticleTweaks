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

package net.lunade.particletweaks.movement.mixin.fluid.tweak.suspended_particle;

import net.lunade.particletweaks.movement.impl.MutableParticleFluidMovementInterface;
import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.SuspendedParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@ClientOnly
@Mixin(SuspendedParticle.class)
public class SuspendedParticleMixin implements ParticleFluidMovementInterface, MutableParticleFluidMovementInterface {

	@Unique
	private boolean particleTweaks$canBurn = true;
	@Unique
	private boolean particleTweaks$slowsInFluid = true;
	@Unique
	private boolean particleTweaks$movesWithFluid = true;

	@Override
	public void particleTweaks$setCanBurn(boolean canBurn) {
		this.particleTweaks$canBurn = canBurn;
	}

	@Override
	public boolean particleTweaks$canBurn() {
		return this.particleTweaks$canBurn;
	}

	@Override
	public void particleTweaks$setSlowsInFluid(boolean slowsInFluid) {
		this.particleTweaks$slowsInFluid = slowsInFluid;
	}

	@Override
	public boolean particleTweaks$slowsInFluid() {
		return this.particleTweaks$slowsInFluid;
	}

	@Override
	public void particleTweaks$setMovesWithFluid(boolean movesWithFluid) {
		this.particleTweaks$movesWithFluid = movesWithFluid;
	}

	@Override
	public boolean particleTweaks$movesWithFluid() {
		return this.particleTweaks$movesWithFluid;
	}
}
