package net.lunade.particletweaks.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.RisingParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FlameParticle.class, priority = 1001)
public abstract class FlameParticleMixin extends RisingParticle implements ParticleTweakInterface {

	protected FlameParticleMixin(ClientLevel world, double d, double e, double f, double g, double h, double i) {
		super(world, d, e, f, g, h, i);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		this.particleTweaks$setNewSystem(true);
		this.particleTweaks$setScaler(0.15F);
		this.particleTweaks$setScalesToZero();
		this.particleTweaks$setCanShrink(false);
	}

	@Inject(method = "getQuadSize", at = @At("RETURN"), cancellable = true)
	public void particleTweaks$getQuadSize(float partialTicks, CallbackInfoReturnable<Float> info) {
		if (this.particleTweaks$usesNewSystem() && !this.particleTweaks$fadeInsteadOfScale()) {
			float scale = this.particleTweaks$getScale(partialTicks);
			info.setReturnValue(info.getReturnValue() * scale);
		}
	}

	@WrapOperation(
		method = "getLightColor",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/Mth;clamp(FFF)F"
		)
	)
	public float particleTweaks$fixLight(
		float value, float min, float max, Operation<Float> original,
		float partialTicks
	) {
		if (this.particleTweaks$usesNewSystem()) {
			return this.particleTweaks$getScale(partialTicks);
		}
		return original.call(value, min, max);
	}

	@ModifyConstant(
		method = "getQuadSize",
		constant = @Constant(floatValue = 0.5F, ordinal = 0)
	)
	public float particleTweaks$getQuadSize(float constant) {
		return 1F;
	}

}
