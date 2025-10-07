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
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.RisingParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class CampfireFlareParticle extends RisingParticle {

	CampfireFlareParticle(
		@NotNull ClientLevel level,
		double x, double y, double z,
		double xd, double yd, double zd,
		TextureAtlasSprite sprite
	) {
		super(level, x, y - 0.125D, z, xd, yd, zd, sprite);
		this.setSize(0.01F, 0.02F);
		this.hasPhysics = true;
		this.quadSize = 0.125F;
		this.friction = 1F;

		if (this instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScalesToZero();
			particleTweakInterface.particleTweaks$setSwitchesExit(true);
		}
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
	protected @NotNull Layer getLayer() {
		return Layer.TRANSLUCENT;
	}


	@Environment(EnvType.CLIENT)
	public record Factory(@NotNull SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
		@Override
		@NotNull
		public Particle createParticle(
			@NotNull SimpleParticleType defaultParticleType,
			@NotNull ClientLevel level,
			double x, double y, double z,
			double xd, double yd, double zd,
			RandomSource random
		) {
			CampfireFlareParticle campfireFlareParticle = new CampfireFlareParticle(level, x, y, z, 0D, 0.02D, 0D, this.spriteSet.get(random));
			campfireFlareParticle.setColor(1F, 0.75F, 0F);
			return campfireFlareParticle;
		}
	}

	@Environment(EnvType.CLIENT)
	public record SoulFactory(@NotNull SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
		@Override
		@NotNull
		public Particle createParticle(
			@NotNull SimpleParticleType defaultParticleType,
			@NotNull ClientLevel level,
			double x, double y, double z,
			double xd, double yd, double zd,
			RandomSource random
		) {
			CampfireFlareParticle campfireFlareParticle = new CampfireFlareParticle(level, x, y, z, 0D, 0.02D, 0D, this.spriteSet.get(random));
			campfireFlareParticle.setColor(0F, 1F, 1F);
			return campfireFlareParticle;
		}
	}
}
