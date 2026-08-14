package net.lunade.particletweaks.movement.mixin.fluid.tweak.suspended_particle;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lunade.particletweaks.movement.impl.MutableParticleFluidMovementInterface;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SuspendedParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SuspendedParticle.UnderwaterProvider.class)
public class UnderwaterProviderMixin {

	@ModifyReturnValue(
		method = "createParticle*",
		at = @At("RETURN")
	)
	public Particle particleTweaks$setUnderwaterParticleProperties(Particle original) {
		if (!(original instanceof MutableParticleFluidMovementInterface mutableFluidParticle)) return original;
		mutableFluidParticle.particleTweaks$setSlowsInFluid(false);
		mutableFluidParticle.particleTweaks$setMovesWithFluid(false);
		return original;
	}
}
