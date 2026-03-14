package net.lunade.particletweaks.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public class WaveParticle extends SingleQuadParticle {
	private static final Quaternionf EMPTY = new Quaternionf();
	public static final Layer WAVE = new Layer(true, TextureAtlas.LOCATION_PARTICLES, RenderPipelines.TRANSLUCENT_PARTICLE);
	private final SpriteSet spriteSet;
	private final float width;
	private final float strength;

	WaveParticle(
		ClientLevel level,
		double x, double y, double z,
		float width, float strength,
		SpriteSet spriteSet
	) {
		super(level, x, y + 1D - (0.0625D * 2.5D), z, 0D, 0D, 0D, spriteSet.first()); // Places half a pixel down from Y
		this.setSize(width, 1F);
		this.quadSize = 0.75F; // 12 / 16
		this.quadSize *= 1.25F;
		this.spriteSet = spriteSet;
		this.width = width;
		this.strength = strength;
		this.lifetime = 13;
	}

	@Override
	public void tick() {
		if (this.age++ >= this.lifetime) this.remove();

		this.setSpriteFromAge(this.spriteSet);

		final Minecraft minecraft = Minecraft.getInstance();
		Vector3fc leftVector = minecraft.gameRenderer.getMainCamera().leftVector();
		leftVector = new Vector3f(leftVector.x(), 0F, leftVector.z()).normalize();

		final double sin = Math.sin((this.age * Math.PI) / 19D);
		this.xd = sin * (0.015D * leftVector.x());
		this.zd = sin * (0.015D * leftVector.z());
	}

	@Override
	protected void extractRotatedQuad(QuadParticleRenderState quadParticleRenderState, Quaternionf quaternionf, float x, float y, float z, float partialTick) {
		final float width = this.getWidth(partialTick);
		final float halfWidth = width * 0.5F;
		final float height = this.getHeight(partialTick);
		final float quadSize = this.getQuadSize(partialTick);
		final float UA = this.getU0();
		final float UB = this.getU1();
		final float V0 = this.getV0();
		final float V1 = this.getV1();
		final int color = ARGB.colorFromFloat(this.alpha, this.rCol, this.gCol, this.bCol);
		final int lightColor = this.getLightCoords(partialTick);

		for (Direction direction : Direction.Plane.HORIZONTAL) {
			final float offsetX = x - (direction.getStepX() * halfWidth);
			final float offsetZ = z - (direction.getStepZ() * halfWidth);

			quadParticleRenderState.add(
				WAVE,
				offsetX, y, offsetZ,
				direction.ordinal(),
				width, height,
				0F,
				quadSize,
				UA, UB, V0, V1,
				color,
				lightColor
			);
			quadParticleRenderState.add(
				WAVE,
				offsetX, y, offsetZ,
				direction.ordinal(),
				width, height,
				1F,
				quadSize,
				UB, UA, V0, V1,
				color,
				lightColor
			);
		}
	}

	public float getWidth(float tickDelta) {
		return this.width + (((this.age + tickDelta) / (float)this.lifetime) * this.strength * 0.75F);
	}

	public float getHeight(float tickDelta) {
		float heightProgress = (float) Math.sin(((this.age + tickDelta) * Math.PI) / this.lifetime);
		return 1F + (heightProgress * this.strength * 0.1F);
	}

	@Override
	protected Layer getLayer() {
		return WAVE;
	}

	public record OutlineFactory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(
			SimpleParticleType options,
			ClientLevel level,
			double x, double y, double z,
			double xd, double yd, double zd,
			RandomSource random
		) {
			return new WaveParticle(level, x, y, z, (float) xd, (float) yd, this.spriteSet);
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
			final WaveParticle waveParticle = new WaveParticle(level, x, y, z, (float) xd, (float) yd, this.spriteSet);

			int waterColor = level.getBiome(BlockPos.containing(x, y, z)).value().getWaterColor();
			waveParticle.rCol = ARGB.red(waterColor) / 255F;
			waveParticle.bCol = ARGB.blue(waterColor) / 255F;
			waveParticle.gCol = ARGB.green(waterColor) / 255F;

			return waveParticle;
		}
	}

}
