package net.lunade.particletweaks.mixin.client.tweaks.wilderwild;

import net.frozenblock.wilderwild.particle.SeedParticle;
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(SeedParticle.class)
public abstract class SeedParticleMixin extends SingleQuadParticle {

	protected SeedParticleMixin(ClientLevel clientLevel, double d, double e, double f, TextureAtlasSprite textureAtlasSprite) {
		super(clientLevel, d, e, f, textureAtlasSprite);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		if (!(SeedParticle.class.cast(this) instanceof ParticleTweakInterface particleTweakInterface)) return;
		particleTweakInterface.particleTweaks$setNewSystem(true);
		particleTweakInterface.particleTweaks$setScaler(0.3F);
		particleTweakInterface.particleTweaks$setScalesToZero();
		particleTweakInterface.particleTweaks$setSwitchesExit(true);
		particleTweakInterface.particleTweaks$setMovesWithFluid(true);
		particleTweakInterface.particleTweaks$setCanBurn(true);
	}

}
