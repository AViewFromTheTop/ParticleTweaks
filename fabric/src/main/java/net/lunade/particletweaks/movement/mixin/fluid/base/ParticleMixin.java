package net.lunade.particletweaks.movement.mixin.fluid.base;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = Particle.class, priority = 1001)
public class ParticleMixin implements ParticleFluidMovementInterface {

	@Unique
	private boolean particleTweaks$touchingFluid;

	@Unique
	private boolean particleTweaks$touchingWater;

	@Unique
	private boolean particleTweaks$touchingLava;

	@Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
	public void particleTweaks$moveWithFluid(CallbackInfo info) {
		this.particleTweaks$runFluidMovementTick(Particle.class.cast(this), info);
	}

	@Override
	public void particleTweaks$setTouchingFluid(boolean touchingFluid) {
		this.particleTweaks$touchingFluid = touchingFluid;
		this.particleTweaks$onTouchingFluidSet(touchingFluid);
	}

	@Override
	public boolean particleTweaks$touchingFluid() {
		return this.particleTweaks$touchingFluid;
	}

	@Override
	public void particleTweaks$setTouchingWater(boolean touchingWater) {
		this.particleTweaks$touchingWater = touchingWater;
		this.particleTweaks$onTouchingWaterSet(touchingWater);
	}

	@Override
	public boolean particleTweaks$touchingWater() {
		return this.particleTweaks$touchingWater;
	}

	@Override
	public void particleTweaks$setTouchingLava(boolean touchingLava) {
		this.particleTweaks$touchingLava = touchingLava;
		this.particleTweaks$onTouchingLavaSet(touchingLava);
	}

	@Override
	public boolean particleTweaks$touchingLava() {
		return this.particleTweaks$touchingLava;
	}
}
