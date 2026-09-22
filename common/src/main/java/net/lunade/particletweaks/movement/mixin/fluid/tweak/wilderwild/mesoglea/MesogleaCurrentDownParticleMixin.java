package net.lunade.particletweaks.movement.mixin.fluid.tweak.wilderwild.mesoglea;

import net.frozenblock.wilderwild.particle.MesogleaCurrentDownParticle;
import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@ClientOnly
@Mixin(value = MesogleaCurrentDownParticle.class, priority = 1001)
public class MesogleaCurrentDownParticleMixin implements ParticleFluidMovementInterface {

	@Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
	public void particleTweaks$moveWithFluid(CallbackInfo info) {
		this.particleTweaks$runFluidMovementTick(MesogleaCurrentDownParticle.class.cast(this), info);
	}

	@Override
	public boolean particleTweaks$canBurn() {
		return true;
	}

}
