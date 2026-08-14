package net.lunade.particletweaks.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.RisingParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class FlareParticle extends RisingParticle {
	private float xDir;
	private float zDir;
	private float rStart = 1F;
	private float rEnd = 1F;
	private float gStart = 1F;
	private float gEnd = 1F;
	private float bStart = 1F;
	private float bEnd = 1F;

	protected FlareParticle(
		ClientLevel level,
		double x, double y, double z,
		double xd, double yd, double zd,
		TextureAtlasSprite sprite
	) {
		super(level, x, y, z, 0D, 0D, 0D, sprite);
		Vec3 rotation = new Vec3(1D, 0D, 0D).yRot((this.random.nextFloat() * 360F) * Mth.DEG_TO_RAD);
		this.xDir = (float) rotation.x;
		this.zDir = (float) rotation.z;
		this.friction = 0.9F;
		this.yd = 0.03D;
		this.quadSize = 0.075F;
		this.lifetime = (int)(6D / ((double)this.random.nextFloat() * 0.8D + 0.2D)) + 15;
		double sin = Math.cos(0D / (this.lifetime - 3));
		this.xd = sin * (0.1D) * this.xDir;
		this.zd = sin * (0.1D) * this.zDir;
		this.setSize(0.0325F, 0.0325F);
	}

	@Override
	public void tick() {
		super.tick();
		final double sin = Math.cos((this.age * Math.PI) / (this.lifetime - 3));
		this.xd = sin * (0.025D) * this.xDir;
		this.zd = sin * (0.025D) * this.zDir;
	}

	@Override
	public int getLightCoords(float tickDelta) {
		final float percentageLived = ((float)this.age + tickDelta) / (float)this.lifetime;
		return (int) Math.max(240F * (1F - percentageLived), super.getLightCoords(tickDelta));
	}

	@Override
	public float getQuadSize(float tickDelta) {
		final float percentageLived = ((float)this.age + tickDelta) / (float)this.lifetime;
		this.rCol = Mth.lerp(percentageLived, this.rStart, this.rEnd);
		this.gCol = Mth.lerp(percentageLived, this.gStart, this.gEnd);
		this.bCol = Mth.lerp(percentageLived, this.bStart, this.bEnd);
		return this.quadSize * (1F - percentageLived);
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
			final FlareParticle flareParticle = new FlareParticle(level, x, y, z, xd, yd, zd, this.spriteSet.get(random));
			flareParticle.rEnd = 0.5F;
			flareParticle.bStart = 0F;
			flareParticle.bEnd = 0F;
			flareParticle.gEnd = 0F;
			return flareParticle;
		}
	}

	public record SoulProvider(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(
			SimpleParticleType options,
			ClientLevel level,
			double x, double y, double z,
			double xd, double yd, double zd,
			RandomSource random
		) {
			final FlareParticle flareParticle = new FlareParticle(level, x, y, z, xd, yd, zd, this.spriteSet.get(random));
			flareParticle.rStart = 0F;
			flareParticle.rEnd = 0.55F;
			flareParticle.bStart = 1F;
			flareParticle.bEnd = 0F;
			flareParticle.gStart = 1F;
			flareParticle.gEnd = 0.25F;
			return flareParticle;
		}
	}

	public record CopperProvider(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(
			SimpleParticleType options,
			ClientLevel level,
			double x, double y, double z,
			double xd, double yd, double zd,
			RandomSource random
		) {
			final FlareParticle flareParticle = new FlareParticle(level, x, y, z, xd, yd, zd, this.spriteSet.get(random));
			flareParticle.rStart = 0.6F;
			flareParticle.rEnd = 0.2F;
			flareParticle.bStart = 0.6F;
			flareParticle.bEnd = 0.3F;
			flareParticle.gStart = 0.922F;
			flareParticle.gEnd = 0.5F;
			return flareParticle;
		}
	}
}
