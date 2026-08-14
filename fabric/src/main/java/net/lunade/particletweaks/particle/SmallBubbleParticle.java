package net.lunade.particletweaks.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.particle.api.WaterColorGetter;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.lunade.particletweaks.trailer.api.TrailerFluidParticleSpawner;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.RisingParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class SmallBubbleParticle extends RisingParticle implements ParticleScaleInterface {
	private final Vec3 direction;
	private final float swaySpeed;

	SmallBubbleParticle(
		ClientLevel level,
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

		final int waterColor = WaterColorGetter.getWaterColor(level, x, y, z);
		this.rCol = Math.clamp(((ARGB.red(waterColor) / 255F) * (float) level.getRandom().triangle(1.3D, 0.3D)), 0F, 1F);
		this.bCol = Math.clamp(((ARGB.blue(waterColor) / 255F) * (float) level.getRandom().triangle(1.3D, 0.3D)), 0F, 1F);
		this.gCol = Math.clamp(((ARGB.green(waterColor) / 255F) * (float) level.getRandom().triangle(1.3D, 0.3D)), 0F, 1F);
	}

	@Override
	public ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.4F);
		entrance.setToZero();
		final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.35F);
		return new ParticleScaleHandler(false, entrance, exit);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.onGround || !TrailerFluidParticleSpawner.isUnderFluid(this.level, this.x, this.y + 0.5D, this.z)) {
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

	public record Provider(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(
			SimpleParticleType options,
			ClientLevel level,
			double x, double y, double z,
			double xd, double yd, double zd,
			RandomSource random
		) {
			return new SmallBubbleParticle(level, x, y, z, xd, yd, zd, this.spriteSet.get(random));
		}
	}
}
