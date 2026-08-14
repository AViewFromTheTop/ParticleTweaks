package net.lunade.particletweaks.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.RisingParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public class ComfySmokeParticle extends RisingParticle implements ParticleScaleInterface {
	private final SpriteSet spriteSet;

	ComfySmokeParticle(
		ClientLevel level,
		double x, double y, double z,
		double xd, double yd, double zd,
		SpriteSet spriteSet
	) {
		super(level, x, y - 0.125D, z, xd, yd, zd, spriteSet.first());
		this.setSize(0.01F, 0.02F);
		this.spriteSet = spriteSet;
		this.hasPhysics = true;
		this.alpha = 0.7F;
		this.lifetime = 35;
		this.quadSize *= 1.25F;

		this.rCol = 129F / 255F;
		this.gCol = 124F / 255F;
		this.bCol = 118F / 255F;
	}

	@Override
	public ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.25F);
		entrance.setToZero();
		final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.FADE, 0.2F);
		return new ParticleScaleHandler(false, entrance, exit);
	}

	@Override
	public void tick() {
		super.tick();
		this.setSpriteFromAge(this.spriteSet);

		final Minecraft minecraft = Minecraft.getInstance();
		Vector3fc leftVector = minecraft.gameRenderer.mainCamera().leftVector();
		leftVector = new Vector3f(leftVector.x(), 0F, leftVector.z()).normalize();

		double sin = Math.sin((this.age * Math.PI) / 19D);
		this.xd = sin * (0.015D * leftVector.x());
		this.zd = sin * (0.015D * leftVector.z());
	}


	@Override
	protected Layer getLayer() {
		return Layer.TRANSLUCENT;
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
			return new ComfySmokeParticle(level, x, y, z, 0D, 0.075D, 0D, this.spriteSet);
		}
	}
}
