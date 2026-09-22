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

package net.lunade.particletweaks.particle;

import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@ClientOnly
public class PoofParticle extends SingleQuadParticle implements ParticleScaleInterface {

	protected PoofParticle(
		ClientLevel level,
		double x, double y, double z,
		double xd, double yd, double zd,
		TextureAtlasSprite sprite
	) {
		super(level, x, y, z, sprite);
		this.gravity = -0.05F;
		this.friction = 0.9F;
		this.xd = xd + (Math.random() * 2D - 1D) * 0.05D;
		this.yd = yd + (Math.random() * 2D - 1D) * 0.05D;
		this.zd = zd + (Math.random() * 2D - 1D) * 0.05D;
		this.quadSize = (0.1F * (this.random.nextFloat() * this.random.nextFloat() * 6F + 1F)) * 1.4F;
		this.lifetime = (int)((6D / ((double)this.random.nextFloat() * 0.8D + 0.2D)) * 0.75D);
	}

	@Override
	public ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.25F);
		return new ParticleScaleHandler(false, null, exit);
	}

	@Override
	protected Layer getLayer() {
		return Layer.OPAQUE;
	}

	public record Provider(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(
			SimpleParticleType options,
			ClientLevel level,
			double x, double y, double z,
			double xd, double yd, double zd,
			RandomSource random
		) {
			return new PoofParticle(level, x, y, z, xd, yd, zd, this.spriteSet.get(random));
		}
	}
}
