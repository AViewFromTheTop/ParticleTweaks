package net.lunade.particletweaks.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
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
	public @Nullable ParticleScaleHandler particleTweaks$createScaleHandler() {
		final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.25F);
		return new ParticleScaleHandler(false, null, exit);
	}

	@Override
	protected @NotNull Layer getLayer() {
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
			return new PoofParticle(level, x, y, z, xd, yd, zd, this.spriteSet.get(random));
		}
	}
}
