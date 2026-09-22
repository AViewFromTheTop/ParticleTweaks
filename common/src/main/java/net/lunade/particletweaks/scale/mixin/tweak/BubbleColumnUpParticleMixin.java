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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.particletweaks.ParticleTweaksConstants;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubbleColumnUpParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@ClientOnly
@Mixin(BubbleColumnUpParticle.class)
public abstract class BubbleColumnUpParticleMixin extends SingleQuadParticle implements ParticleScaleInterface {

	protected BubbleColumnUpParticleMixin(ClientLevel level, double d, double e, double f, TextureAtlasSprite sprite) {
		super(level, d, e, f, sprite);
	}

	@Override
	public ParticleScaleHandler particleTweaks$createScaleHandler() {
		if (ParticleTweaksConstants.MAKE_BUBBLES_POP_MOD) return null;
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.2F);
		entrance.setToZero();
		return new ParticleScaleHandler(false, entrance, null);
	}

	@WrapOperation(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/BubbleColumnUpParticle;remove()V"
		)
	)
	public void particleTweaks$removeOutOfWater(BubbleColumnUpParticle instance, Operation<Void> original) {
		if (!ParticleTweaksConstants.MAKE_BUBBLES_POP_MOD) this.level.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, 0, 0, 0);
		original.call(instance);
	}
}
