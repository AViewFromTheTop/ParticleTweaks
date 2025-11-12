package net.lunade.particletweaks.scale.mixin.base;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = SingleQuadParticle.class, priority = 1001)
public class SingleQuadParticleMixin {

	@Inject(
		method = "extractRotatedQuad(Lnet/minecraft/client/renderer/state/QuadParticleRenderState;Lorg/joml/Quaternionf;FFFF)V",
		at = @At("HEAD")
	)
	public void particleTweaks$captureEntranceAndExit(
		QuadParticleRenderState renderState, Quaternionf rotation, float x, float y, float z, float partialTick, CallbackInfo info,
		@Share("particleTweaks$entrance") LocalRef<ParticleScaler> entranceRef,
		@Share("particleTweaks$exit") LocalRef<ParticleScaler> exitRef
	) {
		if (!(SingleQuadParticle.class.cast(this) instanceof ParticleScaleInterface scaleInterface)) return;
		final ParticleScaleHandler scaleHandler = scaleInterface.particleTweaks$getScaleHandler();
		if (scaleHandler == null) return;

		entranceRef.set(scaleHandler.entrance());
		exitRef.set(scaleHandler.exit());
	}

	@ModifyExpressionValue(
		method = "extractRotatedQuad(Lnet/minecraft/client/renderer/state/QuadParticleRenderState;Lorg/joml/Quaternionf;FFFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/SingleQuadParticle;getLayer()Lnet/minecraft/client/particle/SingleQuadParticle$Layer;"
		)
	)
	public SingleQuadParticle.Layer particleTweaks$modifyLayer(
		SingleQuadParticle.Layer original,
		@Local(argsOnly = true, ordinal = 3) float partialTick,
		@Share("particleTweaks$entrance") LocalRef<ParticleScaler> entranceRef,
		@Share("particleTweaks$exit") LocalRef<ParticleScaler> exitRef
	) {
		if (original != SingleQuadParticle.Layer.OPAQUE) return original;

		final ParticleScaler entrance = entranceRef.get();
		if (entrance != null && entrance.isFade()) return SingleQuadParticle.Layer.TRANSLUCENT;

		final ParticleScaler exit = exitRef.get();
		if (exit != null && exit.isFade()) return SingleQuadParticle.Layer.TRANSLUCENT;

		return original;
	}

	@ModifyExpressionValue(
		method = "extractRotatedQuad(Lnet/minecraft/client/renderer/state/QuadParticleRenderState;Lorg/joml/Quaternionf;FFFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/SingleQuadParticle;getQuadSize(F)F"
		)
	)
	public float particleTweaks$modifyQuadSize(
		float original,
		@Local(argsOnly = true, ordinal = 3) float partialTick,
		@Share("particleTweaks$entrance") LocalRef<ParticleScaler> entranceRef,
		@Share("particleTweaks$exit") LocalRef<ParticleScaler> exitRef
	) {
		final ParticleScaler entrance = entranceRef.get();
		if (entrance != null && entrance.isSize()) original *= entrance.getScale(partialTick);

		final ParticleScaler exit = exitRef.get();
		if (exit != null && exit.isSize()) original *= exit.getScale(partialTick);

		return original;
	}

	@ModifyExpressionValue(
		method = "extractRotatedQuad(Lnet/minecraft/client/renderer/state/QuadParticleRenderState;Lorg/joml/Quaternionf;FFFF)V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/particle/SingleQuadParticle;alpha:F"
		)
	)
	public float particleTweaks$modifyAlpha(
		float original,
		@Local(argsOnly = true, ordinal = 3) float partialTick,
		@Share("particleTweaks$entrance") LocalRef<ParticleScaler> entranceRef,
		@Share("particleTweaks$exit") LocalRef<ParticleScaler> exitRef
	) {
		final ParticleScaler entrance = entranceRef.get();
		if (entrance != null && entrance.isFade()) original *= entrance.getScale(partialTick);

		final ParticleScaler exit = exitRef.get();
		if (exit != null && exit.isFade()) original *= exit.getScale(partialTick);

		return original;
	}

}
