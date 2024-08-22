package net.lunade.particletweaks.mixin.client;

import net.lunade.particletweaks.impl.FluidFallingCalculator;
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Particle.class, priority = 1001)
public abstract class ParticleMixin {

	@Shadow
	public int age;
	@Shadow
	protected int lifetime;
	@Shadow @Final
	protected ClientLevel level;
	@Shadow
	public double x;
	@Shadow
	public double y;
	@Shadow
	public double z;
	@Shadow
	protected double xd;
	@Shadow
	protected double yd;
	@Shadow
	protected double zd;

	@Shadow
	public abstract void remove();

	@Shadow
	protected float gravity;

	@Inject(method = "tick", at = @At("HEAD"))
	public void particleTweaks$runScaling(CallbackInfo info) {
		if (Particle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			if (particleTweakInterface.particleTweaks$usesNewSystem()) {
				particleTweakInterface.particleTweaks$calcScale();
				this.age = Mth.clamp(age - 1, 0, this.lifetime);
				if (particleTweakInterface.particleTweaks$getScale(0F) <= 0.85F && !particleTweakInterface.particleTweaks$hasSwitchedToShrinking()) {
					this.age = Mth.clamp(age - 1, 0, this.lifetime);
				}
			}
			BlockPos blockPos = BlockPos.containing(this.x, this.y, this.z);
			FluidState fluidState = this.level.getFluidState(blockPos);
			boolean isFluidHighEnough = false;
			boolean slowsInWater = particleTweakInterface.particleTweaks$slowsInWater();
			boolean movesWithWater = particleTweakInterface.particleTweaks$movesWithWater();
			if (slowsInWater || movesWithWater) {
				isFluidHighEnough = !fluidState.isEmpty() && (fluidState.getHeight(this.level, blockPos) + (float)blockPos.getY()) >= this.y;
			}

			if (slowsInWater && isFluidHighEnough) {
				this.xd *= 0.8;
				this.yd = FluidFallingCalculator.getFluidFallingAdjustedMovement(this.gravity, this.yd * 0.016D);
				this.yd += 0.06D;
				this.zd *= 0.8;
			}
			if (movesWithWater && isFluidHighEnough) {
				Vec3 flow = fluidState.getFlow(this.level, blockPos);
				this.xd += flow.x() * 0.005D;
				this.yd += flow.y() * 0.005D;
				this.zd += flow.z() * 0.005D;
			}
		}
	}

	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	public void particleTweaks$removeOnceSmall(CallbackInfo info) {
		if (Particle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface) {
			if (particleTweakInterface.particleTweaks$usesNewSystem()) {
				if (particleTweakInterface.particleTweaks$runScaleRemoval()) {
					this.remove();
					info.cancel();
				}
			}
		}
	}

}
