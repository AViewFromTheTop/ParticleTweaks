package net.lunade.particletweaks.scale.mixin.tweak;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.ParticleTweaksConstants;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = BubbleParticle.class, priority = 1001)
public abstract class BubbleParticleMixin extends SingleQuadParticle implements ParticleScaleInterface {

	protected BubbleParticleMixin(ClientLevel level, double d, double e, double f, TextureAtlasSprite sprite) {
		super(level, d, e, f, sprite);
	}

	@Override
	public ParticleScaleHandler particleTweaks$createScaleHandler() {
		if (ParticleTweaksConstants.MAKE_BUBBLES_POP_MOD) return null;
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.35F);
		entrance.setToZero();
		return new ParticleScaleHandler(0.5F, true, entrance, null);
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$injectScaleTicks(CallbackInfo info) {
		this.particleTweaks$runScaleTick();
		this.particleTweaks$runScaleRemovalTick(this, info);
	}

	@Override
	public void particleTweaks$runScaleRemovalTick(Particle particle, CallbackInfo info) {
		ParticleScaleInterface.super.particleTweaks$runScaleRemovalTick(particle, info);
		if (this.removed) this.level.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, 0, 0, 0);
	}

	@WrapOperation(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/BubbleParticle;remove()V"
		)
	)
	public void particleTweaks$remove(BubbleParticle instance, Operation<Void> original) {
		if (!ParticleTweaksConstants.MAKE_BUBBLES_POP_MOD) this.level.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, 0, 0, 0);
		original.call(instance);
	}

}
