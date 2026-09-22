/*
 * Copyright 2026 Lunade Music/AViewFromTheTop
 * This file is part of Particle Tweaks.
 *
 * This program is free software; you can modify it under
 * the terms of version 1 of the FrozenBlock Modding Oasis License
 * as published by FrozenBlock Modding Oasis.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * FrozenBlock Modding Oasis License for more details.
 *
 * You should have received a copy of the FrozenBlock Modding Oasis License
 * along with this program; if not, see <https://github.com/FrozenBlock/Licenses>.
 */

package net.lunade.particletweaks.trailer.mixin.spell;

import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientOnly
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

	protected SpellParticleMixin(ClientLevel level, double d, double e, double f, TextureAtlasSprite sprite) {
		super(level, d, e, f, sprite);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		if (!ParticleTweaksConfig.TRAILER_SPELL.get()) return;
		this.particleTweaks$yRotPerTick = (RANDOM.nextFloat() - 0.5F) * 0.075F;
		this.particleTweaks$zRotPerTick = (RANDOM.nextFloat() - 0.5F) * 0.075F;
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void particleTweaks$tick(CallbackInfo info) {
		if (!ParticleTweaksConfig.TRAILER_SPELL.get()) return;
		this.oRoll = this.roll;
		this.roll += this.particleTweaks$zRotPerTick;

		this.particleTweaks$prevYRot = this.particleTweaks$yRot;
		this.particleTweaks$yRot += this.particleTweaks$yRotPerTick;
	}

	@Override
	public FacingCameraMode getFacingCameraMode() {
		if (!ParticleTweaksConfig.TRAILER_CAMPFIRES.get()) return super.getFacingCameraMode();
		return (rotation, camera, partialTick) -> {
			rotation.set(camera.rotation());
			rotation.rotateZ(Mth.lerp(partialTick, SpellParticleMixin.this.oRoll, SpellParticleMixin.this.roll));
			rotation.rotateY(Mth.lerp(partialTick, SpellParticleMixin.this.particleTweaks$prevYRot, SpellParticleMixin.this.particleTweaks$yRot));
		};
	}
}
