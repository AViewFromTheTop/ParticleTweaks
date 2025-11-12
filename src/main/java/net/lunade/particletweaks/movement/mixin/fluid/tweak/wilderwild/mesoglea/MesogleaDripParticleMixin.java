package net.lunade.particletweaks.movement.mixin.fluid.tweak.wilderwild.mesoglea;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.wilderwild.particle.MesogleaDripParticle;
import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(MesogleaDripParticle.class)
public abstract class MesogleaDripParticleMixin implements ParticleFluidMovementInterface {

	@Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
	public void particleTweaks$moveWithFluid(CallbackInfo info) {
		this.particleTweaks$runFluidMovementTick(MesogleaDripParticle.class.cast(this), info);
	}

	@Override
	public boolean particleTweaks$canBurn() {
		return true;
	}

	@Override
	public boolean particleTweaks$slowsInFluid() {
		return true;
	}

	@Override
	public boolean particleTweaks$movesWithFluid() {
		return true;
	}

	@Override
	public double particleTweaks$flowMovementScale() {
		return 0.03D;
	}

	@Override
	public double particleTweaks$fluidAdditionalSlowVerticalScaleDownward() {
		return 0.75D;
	}
}
