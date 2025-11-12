package net.lunade.particletweaks.movement.mixin.fluid.tweak;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import net.minecraft.client.particle.WaterCurrentDownParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = WaterCurrentDownParticle.class, priority = 1001)
public class WaterCurrentDownParticleMixin implements ParticleFluidMovementInterface {

	@Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
	public void particleTweaks$moveWithFluid(CallbackInfo info) {
		this.particleTweaks$runFluidMovementTick(WaterCurrentDownParticle.class.cast(this), info);
	}

	@Override
	public boolean particleTweaks$canBurn() {
		return true;
	}
}
