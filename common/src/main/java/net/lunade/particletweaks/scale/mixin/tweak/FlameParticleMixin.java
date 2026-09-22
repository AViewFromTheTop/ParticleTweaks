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
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.FlameParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@ClientOnly
@Mixin(value = FlameParticle.class, priority = 1001)
public class FlameParticleMixin implements ParticleScaleInterface {

	@Override
	public ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.15F);
		entrance.setToZero();
		return new ParticleScaleHandler(false, entrance, null);
	}

	@WrapOperation(
		method = "getLightCoords",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/LightCoordsUtil;addSmoothBlockEmission(IF)I"
		)
	)
	public int particleTweaks$fixLight(
		int lightCoords, float blockLightEmission, Operation<Integer> original,
		float partialTicks
	) {
		final ParticleScaleHandler scaleHandler = particleTweaks$getScaleHandler();
		if (scaleHandler == null) return original.call(lightCoords, blockLightEmission);

		float scale = 1F;

		final ParticleScaler entrance = scaleHandler.entrance();
		if (entrance != null) scale *= entrance.getScale(partialTicks);

		final ParticleScaler exit = scaleHandler.exit();
		if (exit != null) scale *= exit.getScale(partialTicks);

		return original.call(lightCoords, Math.clamp(scale, 0F, 1F));
	}

	@ModifyConstant(
		method = "getQuadSize",
		constant = @Constant(floatValue = 0.5F, ordinal = 0)
	)
	public float particleTweaks$modifyQuadSize(float constant) {
		return 1F;
	}
}
