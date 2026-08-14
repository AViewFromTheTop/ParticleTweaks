package net.lunade.particletweaks.scale.mixin.tweak;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.ParticleTweaksConstants;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SulfurBubbleParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = SulfurBubbleParticle.class, priority = 1001)
public abstract class SulfurBubbleParticleMixin extends SingleQuadParticle implements ParticleScaleInterface {

	protected SulfurBubbleParticleMixin(ClientLevel level, double d, double e, double f, TextureAtlasSprite sprite) {
		super(level, d, e, f, sprite);
	}

	@Override
	public ParticleScaleHandler particleTweaks$createScaleHandler() {
		if (ParticleTweaksConstants.MAKE_BUBBLES_POP_MOD) return null;
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.35F);
		entrance.setToZero();
		return new ParticleScaleHandler(0.5F, false, entrance, null);
	}

	@Override
	public void particleTweaks$runScaleRemovalTick(Particle particle, CallbackInfo info) {
		ParticleScaleInterface.super.particleTweaks$runScaleRemovalTick(particle, info);
		if (this.removed) this.level.addParticle(ParticleTweaksParticleTypes.SULFUR_BUBBLE_POP, this.x, this.y, this.z, 0D, 0D, 0D);
	}

	@WrapOperation(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/SulfurBubbleParticle;remove()V"
		)
	)
	public void particleTweaks$remove(SulfurBubbleParticle instance, Operation<Void> original) {
		if (!ParticleTweaksConstants.MAKE_BUBBLES_POP_MOD) this.level.addParticle(ParticleTweaksParticleTypes.SULFUR_BUBBLE_POP, this.x, this.y, this.z, 0D, 0D, 0D);
		original.call(instance);
	}
}
