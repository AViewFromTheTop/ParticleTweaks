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

package net.lunade.particletweaks.trailer.mixin.big_splash;

import net.lunade.particletweaks.particle.WaveParticle;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.SingleQuadParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@ClientOnly
@Mixin(SingleQuadParticle.Layer.class)
public class SingleQuadParticleLayerMixin {

	@Inject(method = "equals", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$equals(Object o, CallbackInfoReturnable<Boolean> info) {
		if (SingleQuadParticle.Layer.class.cast(this) == WaveParticle.WAVE != (o == WaveParticle.WAVE)) info.setReturnValue(false);
	}
}
