package net.lunade.particletweaks.scale.mixin.tweak;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.particle.FlameParticle;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Environment(EnvType.CLIENT)
@Mixin(value = FlameParticle.class, priority = 1001)
public class FlameParticleMixin implements ParticleScaleInterface {

	@Override
	public @Nullable ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.15F);
		entrance.setToZero();
		return new ParticleScaleHandler(false, entrance, null);
	}

	@ModifyExpressionValue(
		method = "getLightColor",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/Mth;clamp(FFF)F"
		)
	)
	public float particleTweaks$fixLight(
		float original,
		float partialTick
	) {
		final ParticleScaleHandler scaleHandler = particleTweaks$getScaleHandler();
		if (scaleHandler == null) return original;

		float scale = 1F;

		final ParticleScaler entrance = scaleHandler.entrance();
		if (entrance != null) scale *= entrance.getScale(partialTick);

		final ParticleScaler exit = scaleHandler.exit();
		if (exit != null) scale *= exit.getScale(partialTick);

		return scale;
	}

	@ModifyConstant(
		method = "getQuadSize",
		constant = @Constant(floatValue = 0.5F, ordinal = 0)
	)
	public float particleTweaks$modifyQuadSize(float constant) {
		return 1F;
	}

}
