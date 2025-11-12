package net.lunade.particletweaks.trailer.mixin.spell;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = SpellParticle.class, priority = 1002)
public abstract class SpellParticleMixin extends SingleQuadParticle {

	@Shadow
	@Final
	private static RandomSource RANDOM;

	@Unique
	private float particleTweaks$yRotPerTick;
	@Unique
	private float particleTweaks$yRot;
	@Unique
	private float particleTweaks$prevYRot;
	@Unique
	private float particleTweaks$zRotPerTick;

	protected SpellParticleMixin(ClientLevel clientLevel, double d, double e, double f, TextureAtlasSprite textureAtlasSprite) {
		super(clientLevel, d, e, f, textureAtlasSprite);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		if (!ParticleTweaksConfig.TRAILER_SPELL) return;
		this.particleTweaks$yRotPerTick = (RANDOM.nextFloat() - 0.5F) * 0.075F;
		this.particleTweaks$zRotPerTick = (RANDOM.nextFloat() - 0.5F) * 0.075F;
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void particleTweaks$tick(CallbackInfo info) {
		if (!ParticleTweaksConfig.TRAILER_SPELL) return;
		this.oRoll = this.roll;
		this.roll += this.particleTweaks$zRotPerTick;

		this.particleTweaks$prevYRot = this.particleTweaks$yRot;
		this.particleTweaks$yRot += this.particleTweaks$yRotPerTick;
	}

	@Override
	public @NotNull FacingCameraMode getFacingCameraMode() {
		if (!ParticleTweaksConfig.TRAILER_CAMPFIRES) return super.getFacingCameraMode();
		return (rotation, camera, partialTick) -> {
			rotation.set(camera.rotation());
			rotation.rotateZ(Mth.lerp(partialTick, SpellParticleMixin.this.oRoll, SpellParticleMixin.this.roll));
			rotation.rotateY(Mth.lerp(partialTick, SpellParticleMixin.this.particleTweaks$prevYRot, SpellParticleMixin.this.particleTweaks$yRot));
		};
	}

}
