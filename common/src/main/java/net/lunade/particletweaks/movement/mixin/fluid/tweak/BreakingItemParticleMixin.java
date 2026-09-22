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

package net.lunade.particletweaks.movement.mixin.fluid.tweak;

import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.BreakingItemParticle;
import org.spongepowered.asm.mixin.Mixin;

@ClientOnly
@Mixin(BreakingItemParticle.class)
public class BreakingItemParticleMixin implements ParticleFluidMovementInterface {

	@Override
	public boolean particleTweaks$slowsInFluid() {
		return true;
	}

	@Override
	public boolean particleTweaks$movesWithFluid() {
		return true;
	}

	@Override
	public double particleTweaks$fluidSlowVerticalScale() {
		return 0.5D;
	}

	@Override
	public double particleTweaks$fluidAdditionalSlowVerticalScaleDownward() {
		return 0.15D;
	}
}
