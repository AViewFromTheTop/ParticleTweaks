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
import net.minecraft.client.particle.BubbleColumnUpParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleTypes;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(BubbleColumnUpParticle.class)
public abstract class BubbleColumnUpParticleMixin extends SingleQuadParticle implements ParticleScaleInterface {

	protected BubbleColumnUpParticleMixin(ClientLevel clientLevel, double d, double e, double f, TextureAtlasSprite textureAtlasSprite) {
		super(clientLevel, d, e, f, textureAtlasSprite);
	}

	@Override
	public @Nullable ParticleScaleHandler particleTweaks$createScaleHandler() {
		if (ParticleTweaksConstants.MAKE_BUBBLES_POP_MOD) return null;
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.2F);
		entrance.setToZero();
		return new ParticleScaleHandler(false, entrance, null);
	}

	@WrapOperation(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/BubbleColumnUpParticle;remove()V"
		)
	)
	public void particleTweaks$removeOutOfWater(BubbleColumnUpParticle instance, Operation<Void> original) {
		if (!ParticleTweaksConstants.MAKE_BUBBLES_POP_MOD) this.level.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, 0, 0, 0);
		original.call(instance);
	}

}
