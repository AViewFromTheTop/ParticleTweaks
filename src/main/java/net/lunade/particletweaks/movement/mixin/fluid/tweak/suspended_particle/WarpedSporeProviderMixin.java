package net.lunade.particletweaks.movement.mixin.fluid.tweak.suspended_particle;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lunade.particletweaks.movement.impl.MutableParticleFluidMovementInterface;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SuspendedParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SuspendedParticle.WarpedSporeProvider.class)
public class WarpedSporeProviderMixin {

	@ModifyReturnValue(
		method = "createParticle*",
		at = @At("RETURN")
	)
	public Particle particleTweaks$setFluidParticleProperties(Particle original) {
		if (!(original instanceof MutableParticleFluidMovementInterface mutableFluidParticle)) return original;
		mutableFluidParticle.particleTweaks$setCanBurn(false);
		return original;
	}
}
