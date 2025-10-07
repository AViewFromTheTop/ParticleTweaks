package net.lunade.particletweaks.mixin.client.trailer;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@ModifyExpressionValue(
		method = "makePoofParticles",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/core/particles/ParticleTypes;POOF:Lnet/minecraft/core/particles/SimpleParticleType;"
		)
	)
	public SimpleParticleType particleTweaks$useBubblePoofUnderwater(SimpleParticleType original) {
		return ParticleTweaksConfig.TRAILER_BUBBLES
			&& ParticleTweaksConfig.TRAILER_POOF
			&& LivingEntity.class.cast(this).isUnderWater()
			? ParticleTypes.BUBBLE : original;
	}
}
