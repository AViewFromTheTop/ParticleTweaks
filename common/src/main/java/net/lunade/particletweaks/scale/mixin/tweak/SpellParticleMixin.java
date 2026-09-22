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

package net.lunade.particletweaks.scale.mixin.tweak;

import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.SpellParticle;
import org.spongepowered.asm.mixin.Mixin;

@ClientOnly
@Mixin(SpellParticle.class)
public class SpellParticleMixin implements ParticleScaleInterface {

	@Override
	public ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, ParticleTweaksConfig.TRAILER_SPELL.get() ? 0.15F : 0.35F);
		entrance.setToZero();
		final ParticleScaler exit = new ParticleScaler(
			ParticleTweaksConfig.TRAILER_SPELL.get() ? ParticleScaler.ScaleMethod.SIZE : ParticleScaler.ScaleMethod.FADE,
			ParticleTweaksConfig.TRAILER_SPELL.get() ? 0.15F : 0.375F
		);
		return new ParticleScaleHandler(false, entrance, exit);
	}
}
