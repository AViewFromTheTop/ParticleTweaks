package net.lunade.particletweaks.particle;

import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WaveSeedParticle extends NoRenderParticle {
	private final float width;
	private final float strength;

	protected WaveSeedParticle(ClientLevel level, double x, double y, double z, double width, double strength) {
		super(level, x, y, z);
		this.lifetime = 13;
		this.width = (float) width;
		this.strength = (float) strength;

		level.addAlwaysVisibleParticle(
			ParticleTweaksParticleTypes.WAVE_OUTLINE,
			true,
			x, y, z,
			width + 0.25F,
			strength,
			0F
		);
		level.addAlwaysVisibleParticle(
			ParticleTweaksParticleTypes.WAVE,
			true,
			x, y, z,
			width + 0.25F,
			strength,
			0F
		);
		spawnSplashParticles(
			this.level,
			this.x, this.y, this.z,
			this.random,
			Math.max(5, Math.min(50, (int) (calculateParticleStrength(this.strength, 1F, 0.1F) * this.width * 25))),
			this.width,
			this.strength * 0.5F,
			0.3F
		);
	}

	public static void spawnSplashParticles(
		Level level, double x, double y, double z, RandomSource random, int count, float width, float strength, float horizontalStrengthScale
	) {
		strength = Math.min(0.4F, Math.max(0.2F, strength * 2F));
		for (int i = 0; i < count; i++) {
			Vec3 rotation = new Vec3(1D, 0D, 0D).yRot((random.nextFloat() * 360F) * Mth.DEG_TO_RAD);
			Vec3 velocity = rotation.scale(strength * horizontalStrengthScale);
			level.addParticle(
				ParticleTweaksParticleTypes.SPLASH,
				x + rotation.x * width * random.nextFloat(),
				y + rotation.y,
				z + rotation.z * width * random.nextFloat(),
				velocity.x,
				strength + (0.15D * random.nextFloat()),
				velocity.z
			);
		}
	}

	private static float calculateParticleStrength(float strength, float max, float min) {
		return Math.min(max, Math.max(min, strength * 2F));
	}

	@Override
	public void tick() {
		if (this.age++ >= this.lifetime) this.remove();

		if (this.age == 9 && this.strength > 0.15D) {
			spawnSplashParticles(
				this.level,
				this.x, this.y, this.z,
				this.random,
				Math.max(5, Math.min(50, (int) (calculateParticleStrength(this.strength, 1F, 0.1F) * this.width * 30))),
				this.width * 0.75F,
				this.strength,
				0.1F
			);
			this.level.addAlwaysVisibleParticle(
				ParticleTweaksParticleTypes.WAVE_OUTLINE,
				true,
				this.x, this.y, this.z,
				this.width,
				this.strength,
				0F
			);
			this.level.addAlwaysVisibleParticle(
				ParticleTweaksParticleTypes.WAVE,
				true,
				this.x, this.y, this.z,
				this.width,
				this.strength,
				0F
			);
		}
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
			return new WaveSeedParticle(level, x, y, z, xd, yd);
		}
	}
}
