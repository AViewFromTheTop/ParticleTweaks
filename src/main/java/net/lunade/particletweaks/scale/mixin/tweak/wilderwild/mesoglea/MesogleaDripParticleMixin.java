package net.lunade.particletweaks.scale.mixin.tweak.wilderwild.mesoglea;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.wilderwild.particle.MesogleaDripParticle;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(value = MesogleaDripParticle.class, priority = 1001)
public abstract class MesogleaDripParticleMixin extends SingleQuadParticle implements ParticleScaleInterface {

	@Shadow
	protected abstract void preMoveUpdate();

	protected MesogleaDripParticleMixin(ClientLevel level, double d, double e, double f, TextureAtlasSprite sprite) {
		super(level, d, e, f, sprite);
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
			target = "Lnet/frozenblock/wilderwild/particle/MesogleaDripParticle;remove()V"
		)
	)
	public void particleTweaks$shrinkInsteadOfRemove(MesogleaDripParticle instance, Operation<Void> original) {
		if (this.particleTweaks$hasExit()) {
			this.lifetime = 0;
			return;
        }
		original.call(instance);
	}

}
