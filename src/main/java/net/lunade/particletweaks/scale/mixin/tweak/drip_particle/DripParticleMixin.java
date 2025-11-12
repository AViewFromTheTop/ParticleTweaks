package net.lunade.particletweaks.scale.mixin.tweak.drip_particle;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = DripParticle.class, priority = 1001)
public abstract class DripParticleMixin extends SingleQuadParticle implements ParticleScaleInterface {

	@Shadow
	protected abstract void preMoveUpdate();

	protected DripParticleMixin(ClientLevel clientLevel, double d, double e, double f, TextureAtlasSprite textureAtlasSprite) {
		super(clientLevel, d, e, f, textureAtlasSprite);
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$injectScaleTicks(CallbackInfo info) {
		this.particleTweaks$runScaleTick();
		this.particleTweaks$runScaleRemovalTick(this, info);
	}

	@Override
	public void particleTweaks$runScaleRemovalTick(Particle particle, CallbackInfo info) {
		ParticleScaleInterface.super.particleTweaks$runScaleRemovalTick(particle, info);
		if (this.removed) this.preMoveUpdate();
	}

	@WrapOperation(
		method = "preMoveUpdate",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/DripParticle;remove()V"
		)
	)
	public void particleTweaks$shrinkInsteadOfRemove(DripParticle instance, Operation<Void> original) {
		if (this.particleTweaks$hasExit()) {
			this.lifetime = 0;
			return;
		}
		original.call(instance);
	}

}
