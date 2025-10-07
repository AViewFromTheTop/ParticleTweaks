package net.lunade.particletweaks.mixin.client.tweaks;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BreakingItemParticle.class, priority = 999)
public abstract class BreakingItemParticleMixin implements ParticleTweakInterface {

	@Inject(method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDLnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V", at = @At("TAIL"))
	private void particleTweaks$init(ClientLevel clientLevel, double x, double y, double z, TextureAtlasSprite sprite, CallbackInfo info) {
		this.particleTweaks$setNewSystem(true);
		this.particleTweaks$setScaler(0.35F);
		this.particleTweaks$setFadeInsteadOfScale(true);
		this.particleTweaks$setSlowsInFluid(true);
		this.particleTweaks$setMovesWithFluid(true);
	}

}
