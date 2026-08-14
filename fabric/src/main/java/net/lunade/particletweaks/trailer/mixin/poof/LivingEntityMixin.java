package net.lunade.particletweaks.trailer.mixin.poof;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@ModifyExpressionValue(
		method = "makePoofParticles",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/core/particles/ParticleTypes;POOF:Lnet/minecraft/core/particles/SimpleParticleType;",
			opcode = Opcodes.GETSTATIC
		)
	)
	public SimpleParticleType particleTweaks$useBubblePoofUnderwater(SimpleParticleType original) {
		return ParticleTweaksConfig.TRAILER_BUBBLE_POOF.get() && LivingEntity.class.cast(this).isUnderWater() ? ParticleTypes.BUBBLE : original;
	}
}
