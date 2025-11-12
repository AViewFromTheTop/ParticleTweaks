package net.lunade.particletweaks.movement.mixin.fluid.tweak.drip_particle;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lunade.particletweaks.movement.impl.MutableParticleFluidMovementInterface;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DripParticle.LavaLandProvider.class)
public class LavaLandProviderMixin {

	@ModifyReturnValue(
		method = "createParticle*",
		at = @At("RETURN")
	)
	public Particle particleTweaks$setUnderwaterParticleProperties(Particle original) {
		if (!(original instanceof MutableParticleFluidMovementInterface mutableFluidParticle)) return original;
		mutableFluidParticle.particleTweaks$setSlowsInFluid(true);
		return original;
	}
}
