package net.lunade.particletweaks.scale.mixin.tweak;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = WaterDropParticle.class, priority = 1001)
public abstract class WaterDropParticleMixin extends SingleQuadParticle implements ParticleScaleInterface {

	protected WaterDropParticleMixin(ClientLevel clientLevel, double d, double e, double f, TextureAtlasSprite textureAtlasSprite) {
		super(clientLevel, d, e, f, textureAtlasSprite);
	}

	@Override
	public @Nullable ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.3F);
		entrance.setToZero();
		final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.FADE, 0.85F);
		return new ParticleScaleHandler(0.5F, true, entrance, exit);
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$injectScaleTicks(CallbackInfo info) {
		this.particleTweaks$runScaleTick();
		this.particleTweaks$runScaleRemovalTick(this, info);
	}

	@ModifyConstant(
		method = "tick",
		constant = @Constant(expandZeroConditions = Constant.Condition.GREATER_THAN_ZERO)
	)
	public int particleTweaks$circumventLifetimeCheck(int original) {
		return -Integer.MAX_VALUE;
	}

	@WrapOperation(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/WaterDropParticle;remove()V"
		)
	)
	public void particleTweaks$shrinkInsteadOfRemove(WaterDropParticle instance, Operation<Void> original) {
		if (this.particleTweaks$hasExit()) {
			this.lifetime = 0;
			return;
		}
		original.call(instance);
	}

}
