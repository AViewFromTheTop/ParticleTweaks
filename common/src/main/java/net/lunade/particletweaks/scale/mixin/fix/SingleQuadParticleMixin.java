package net.lunade.particletweaks.scale.mixin.fix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@ClientOnly
@Mixin(value = SingleQuadParticle.class, priority = 1001)
public class SingleQuadParticleMixin {

	@WrapOperation(
		method = "setSpriteFromAge",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/particle/SpriteSet;get(II)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"
		)
	)
	public TextureAtlasSprite particleTweaks$fixOutOfIndexCrash(SpriteSet instance, int age, int lifetime, Operation<TextureAtlasSprite> original) {
		return original.call(instance, Math.min(age, lifetime), lifetime);
	}
}
