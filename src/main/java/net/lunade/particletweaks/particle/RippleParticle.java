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
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class RippleParticle extends SingleQuadParticle implements ParticleScaleInterface {
	private static final Vector3f NORMALIZED_QUAT_VECTOR = new Vector3f(0.5F, 0.5F, 0.5F).normalize();
	private static final Quaternionf ROTATION = new Quaternionf()
		.setAngleAxis(0F, NORMALIZED_QUAT_VECTOR.x(), NORMALIZED_QUAT_VECTOR.y(), NORMALIZED_QUAT_VECTOR.z())
		.rotateX(-90F * Mth.DEG_TO_RAD);
	private final SpriteSet spriteSet;

	RippleParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
		super(level, x, y, z, 0D, 0D, 0D, spriteSet.first());
		this.xd = 0D;
		this.yd = 0D;
		this.zd = 0D;
		this.setSize(0.0875F, 0.01F);
		this.spriteSet = spriteSet;
		this.lifetime = 5;
		this.quadSize = 0.35F;
		this.gravity = 0F;
	}

	@Override
	public ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.45F);
		entrance.setToZero();
		final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.FADE, 0.45F);
		return new ParticleScaleHandler(false, entrance, exit);
	}

	@Override
	public void tick() {
		super.tick();
		this.setSpriteFromAge(this.spriteSet);
	}

	@Override
	public void extract(QuadParticleRenderState renderState, Camera camera, float partialTick) {
		this.extractRotatedQuad(renderState, camera, ROTATION, partialTick);
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
			return new RippleParticle(level, x, y, z, this.spriteSet);
		}
	}
}
