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

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import org.joml.Quaternionf;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientOnly
@Mixin(value = SingleQuadParticle.class, priority = 1001)
public class SingleQuadParticleMixin {

	@Inject(
		method = "extractRotatedQuad(Lnet/minecraft/client/renderer/state/level/QuadParticleRenderState;Lorg/joml/Quaternionf;FFFF)V",
		at = @At("HEAD")
	)
	public void particleTweaks$captureEntranceAndExit(
		QuadParticleRenderState particleTypeRenderState, Quaternionf rotation, float x, float y, float z, float partialTickTime, CallbackInfo info,
		@Share("particleTweaks$scaleHandler") LocalRef<ParticleScaleHandler> scaleHandlerRef
	) {
		if (!(SingleQuadParticle.class.cast(this) instanceof ParticleScaleInterface scaleInterface)) return;

		final ParticleScaleHandler scaleHandler = scaleInterface.particleTweaks$getScaleHandler();
		if (scaleHandler == null) return;

		scaleHandlerRef.set(scaleHandler);
	}

	@ModifyExpressionValue(
		method = "extractRotatedQuad(Lnet/minecraft/client/renderer/state/level/QuadParticleRenderState;Lorg/joml/Quaternionf;FFFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/SingleQuadParticle;getLayer()Lnet/minecraft/client/particle/SingleQuadParticle$Layer;"
		)
	)
	public SingleQuadParticle.Layer particleTweaks$modifyLayer(
		SingleQuadParticle.Layer original,
		@Local(argsOnly = true, ordinal = 3) float partialTickTime,
		@Share("particleTweaks$scaleHandler") LocalRef<ParticleScaleHandler> scaleHandlerRef
	) {
		if (scaleHandlerRef.get() != null) return scaleHandlerRef.get().tryModifyParticleLayer(original, partialTickTime);
		return original;
	}

	@ModifyExpressionValue(
		method = "extractRotatedQuad(Lnet/minecraft/client/renderer/state/level/QuadParticleRenderState;Lorg/joml/Quaternionf;FFFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/SingleQuadParticle;getQuadSize(F)F"
		)
	)
	public float particleTweaks$modifyQuadSize(
		float original,
		@Local(argsOnly = true, ordinal = 3) float partialTickTime,
		@Share("particleTweaks$scaleHandler") LocalRef<ParticleScaleHandler> scaleHandlerRef
	) {
		if (scaleHandlerRef.get() != null) return scaleHandlerRef.get().tryModifyQuadSize(original, partialTickTime);
		return original;
	}

	@ModifyExpressionValue(
		method = "extractRotatedQuad(Lnet/minecraft/client/renderer/state/level/QuadParticleRenderState;Lorg/joml/Quaternionf;FFFF)V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/particle/SingleQuadParticle;alpha:F",
			opcode = Opcodes.GETFIELD
		)
	)
	public float particleTweaks$modifyAlpha(
		float original,
		@Local(argsOnly = true, ordinal = 3) float partialTickTime,
		@Share("particleTweaks$scaleHandler") LocalRef<ParticleScaleHandler> scaleHandlerRef
	) {
		if (scaleHandlerRef.get() != null) return scaleHandlerRef.get().tryModifyAlpha(original, partialTickTime);
		return original;
	}
}
