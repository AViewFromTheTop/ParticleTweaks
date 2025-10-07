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
import net.lunade.particletweaks.impl.FlowingFluidParticleUtil;
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.RisingParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class SmallBubbleParticle extends RisingParticle {
	private final Vec3 direction;
	private final float swaySpeed;

	SmallBubbleParticle(
		@NotNull ClientLevel level,
		double x, double y, double z,
		double xd, double yd, double zd,
		TextureAtlasSprite sprite
	) {
		super(level, x, y - 0.125D, z, xd, yd, zd, sprite);
		this.setSize(0.01F, 0.01F);
		this.lifetime *= 2;
		this.yd = yd;
		this.quadSize = this.quadSize * (this.random.nextFloat() * 0.6F + 0.2F);
		this.lifetime = (int)(16D / (Math.random() * 0.8D + 0.2D));
		this.friction = 1F;
		this.hasPhysics = true;

		this.swaySpeed = (0.125F - (float)yd) * 80F;
		this.direction = new Vec3(1D, 0D, 0D).yRot((random.nextFloat() * 360F) * Mth.DEG_TO_RAD);

		int waterColor = level.getBiome(BlockPos.containing(x, y, z)).value().getWaterColor();
		this.rCol = Math.clamp(((ARGB.red(waterColor) / 255F) * (float)level.random.triangle(1.3D, 0.3D)), 0F, 1F);
		this.bCol = Math.clamp(((ARGB.blue(waterColor) / 255F) * (float)level.random.triangle(1.3D, 0.3D)), 0F, 1F);
		this.gCol = Math.clamp(((ARGB.green(waterColor) / 255F) * (float)level.random.triangle(1.3D, 0.3D)), 0F, 1F);

		if (this instanceof ParticleTweakInterface particleTweakInterface) {
			particleTweakInterface.particleTweaks$setNewSystem(true);
			particleTweakInterface.particleTweaks$setScalesToZero();
			particleTweakInterface.particleTweaks$setScaler(0.4F);
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (this.onGround || !FlowingFluidParticleUtil.isUnderFluid(this.level, this.x, this.y + 0.5D, this.z)) {
			this.age = this.lifetime;
		}

		final double sin = Math.sin((this.age * Math.PI) / (this.swaySpeed));
		this.xd = sin * (this.yd * this.direction.x()) * 0.35D;
		this.zd = sin * (this.yd * this.direction.z()) * 0.35D;
	}

	@Override
	protected Layer getLayer() {
		return Layer.OPAQUE;
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
			return new SmallBubbleParticle(level, x, y, z, xd, yd, zd, this.spriteSet.get(random));
		}
	}
}
