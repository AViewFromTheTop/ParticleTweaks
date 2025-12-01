/*
 * Copyright 2023-2024 FrozenBlock
 * This file is part of Wilder Wild.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, see <https://www.gnu.org/licenses/>.
 */

package net.lunade.particletweaks.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.RisingParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class CampfireFlareParticle extends RisingParticle implements ParticleScaleInterface {

	CampfireFlareParticle(
		ClientLevel level,
		double x, double y, double z,
		double xd, double yd, double zd,
		TextureAtlasSprite sprite
	) {
		super(level, x, y - 0.125D, z, xd, yd, zd, sprite);
		this.setSize(0.01F, 0.02F);
		this.hasPhysics = true;
		this.quadSize = 0.125F;
		this.friction = 1F;
	}

	@Override
	public ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.15F);
		entrance.setToZero();
		final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.15F);
		return new ParticleScaleHandler(false, entrance, exit);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.y == this.yo || this.onGround) this.age = this.lifetime;
	}

	@Override
	protected int getLightColor(float tint) {
		return 240;
	}

	@Override
	protected Layer getLayer() {
		return Layer.TRANSLUCENT;
	}

	public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(
			SimpleParticleType options,
			ClientLevel level,
			double x, double y, double z,
			double xd, double yd, double zd,
			RandomSource random
		) {
			CampfireFlareParticle campfireFlareParticle = new CampfireFlareParticle(level, x, y, z, 0D, 0.02D, 0D, this.spriteSet.get(random));
			campfireFlareParticle.setColor(1F, 0.75F, 0F);
			return campfireFlareParticle;
		}
	}

	public record SoulFactory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(
			SimpleParticleType options,
			ClientLevel level,
			double x, double y, double z,
			double xd, double yd, double zd,
			RandomSource random
		) {
			CampfireFlareParticle campfireFlareParticle = new CampfireFlareParticle(level, x, y, z, 0D, 0.02D, 0D, this.spriteSet.get(random));
			campfireFlareParticle.setColor(0F, 1F, 1F);
			return campfireFlareParticle;
		}
	}

	public record CopperFactory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(
			SimpleParticleType options,
			ClientLevel level,
			double x, double y, double z,
			double xd, double yd, double zd,
			RandomSource random
		) {
			CampfireFlareParticle campfireFlareParticle = new CampfireFlareParticle(level, x, y, z, 0D, 0.02D, 0D, this.spriteSet.get(random));
			campfireFlareParticle.setColor(0F, 1F, 0.2F);
			return campfireFlareParticle;
		}
	}
}
