package net.lunade.particletweaks.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.movement.impl.MutableParticleFluidMovementInterface;
import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.api.ParticleScaler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class FluidFlowParticle extends SingleQuadParticle implements ParticleScaleInterface, ParticleFluidMovementInterface, MutableParticleFluidMovementInterface {
	private static final int LAVA_COLOR = 16743195;
	private final SpriteSet spriteSet;
	private boolean canBurn;
	private boolean slowsInFluid;
	private double flowMovementScale;
	private boolean isLava;
	private boolean floatOnFluid;
	private boolean endWhenUnderFluid;
	private boolean spawnsRipples;
	private boolean hasSpawnedRipple;

	FluidFlowParticle(
		ClientLevel level,
		double x, double y, double z,
		double xd, double yd, double zd,
		SpriteSet spriteSet
	) {
		super(level, x, y, z, xd, yd, zd, spriteSet.first());
		this.xd = xd;
		this.yd = yd;
		this.zd = zd;
		this.spriteSet = spriteSet;
		this.gravity = 0.9F;
	}

	@Override
	public void tick() {
		super.tick();

		this.setSpriteFromAge(this.spriteSet);

		if (this.onGround) {
			this.age = this.lifetime;
			return;
		}

		final BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
		final FluidState fluidState = this.level.getBlockState(pos).getFluidState();
		final float fluidHeight = fluidState.getHeight(this.level, pos);
		final float worldFluidHeight = fluidHeight + (float) pos.getY();
		final boolean isFluidHighEnough = !fluidState.isEmpty() && worldFluidHeight >= this.y;
		if (isFluidHighEnough) {
			if (fluidState.getFlow(this.level, pos).horizontalDistance() == 0D) this.age = Math.clamp(this.age + 3, 0, this.lifetime);
			if (this.floatOnFluid) {
				if (!fluidState.hasProperty(FlowingFluid.FALLING) || !fluidState.getValue(FlowingFluid.FALLING)) {
					if (this.yd < 0.01D) this.yd += 0.05D;
					this.yd += (0F - this.yd) * 0.4D;
					this.y += ((pos.getY() + fluidHeight) - this.y) * 0.5D;
				}
			}

			if (this.endWhenUnderFluid || this.spawnsRipples) this.age = this.lifetime;
			if (this.spawnsRipples
				&& !this.hasSpawnedRipple
				&& worldFluidHeight < this.yo
				&& this.level.getFluidState(pos.above()).isEmpty()
			) {
				this.hasSpawnedRipple = true;
				this.level.addParticle(
					ParticleTweaksParticleTypes.RIPPLE,
					this.x, (pos.getY() + fluidHeight), this.z,
					0D, 0D, 0D
				);
			}
		}
	}

	@Override
	protected int getLightColor(float tint) {
		return this.isLava ? 240 : super.getLightColor(tint);
	}

	@Override
	protected @NotNull Layer getLayer() {
		return Layer.TRANSLUCENT;
	}

	@Override
	public double particleTweaks$flowMovementScale() {
		return this.flowMovementScale;
	}

	@Override
	public void particleTweaks$setCanBurn(boolean canBurn) {
		this.canBurn = canBurn;
	}

	@Override
	public boolean particleTweaks$canBurn() {
		return this.canBurn;
	}

	@Override
	public void particleTweaks$setSlowsInFluid(boolean slowsInFluid) {
		this.slowsInFluid = slowsInFluid;
	}

	@Override
	public boolean particleTweaks$slowsInFluid() {
		return this.slowsInFluid;
	}

	@Override
	public void particleTweaks$setMovesWithFluid(boolean movesWithFluid) {
	}

	@Override
	public boolean particleTweaks$movesWithFluid() {
		return true;
	}

	@Environment(EnvType.CLIENT)
	public record LavaFactory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
		@Override
		@NotNull
		public Particle createParticle(
			@NotNull SimpleParticleType defaultParticleType,
			@NotNull ClientLevel level,
			double x, double y, double z,
			double xd, double yd, double zd,
			RandomSource random
		) {
			final FluidFlowParticle lavaParticle = new FluidFlowParticle(level, x, y, z, xd, yd, zd, this.spriteSet);

			lavaParticle.rCol = ARGB.red(LAVA_COLOR) / 255F;
			lavaParticle.bCol = ARGB.blue(LAVA_COLOR) / 255F;
			lavaParticle.gCol = ARGB.green(LAVA_COLOR) / 255F;

			lavaParticle.isLava = true;
			lavaParticle.quadSize *= 0.75F;
			lavaParticle.floatOnFluid = false;
			lavaParticle.endWhenUnderFluid = false;
			lavaParticle.setSize(0.078125F, 0.078125F);

			lavaParticle.flowMovementScale = 0.05D;
			lavaParticle.particleTweaks$setCanBurn(false);

			final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.5F);
			entrance.setToZero();
			final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.FADE, 0.5F);
			lavaParticle.particleTweaks$setScaleHandler(new ParticleScaleHandler(false, entrance, exit));

			return lavaParticle;
		}
	}

	@Environment(EnvType.CLIENT)
	public record WaterFactory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
		@Override
		@NotNull
		public Particle createParticle(
			@NotNull SimpleParticleType defaultParticleType,
			@NotNull ClientLevel level,
			double x, double y, double z,
			double xd, double yd, double zd,
			RandomSource random
		) {
			final FluidFlowParticle waterParticle = new FluidFlowParticle(level, x, y, z, xd, yd, zd, this.spriteSet);

			int waterColor = level.getBiome(BlockPos.containing(x, y, z)).value().getWaterColor();
			waterParticle.rCol = Math.clamp(((ARGB.red(waterColor) / 255F) * (float)random.triangle(1.3D, 0.3D)), 0F, 1F);
			waterParticle.bCol = Math.clamp(((ARGB.blue(waterColor) / 255F) * (float)random.triangle(1.3D, 0.3D)), 0F, 1F);
			waterParticle.gCol = Math.clamp(((ARGB.green(waterColor) / 255F) * (float)random.triangle(1.3D, 0.3D)), 0F, 1F);

			waterParticle.alpha = 0.6F;
			waterParticle.quadSize *= 0.5F;
			waterParticle.floatOnFluid = true;
			waterParticle.endWhenUnderFluid = false;
			waterParticle.setSize(0.0325F, 0.0325F);

			waterParticle.flowMovementScale = 0.05D;
			waterParticle.particleTweaks$setCanBurn(true);

			final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.5F);
			entrance.setToZero();
			final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.FADE, 0.5F);
			waterParticle.particleTweaks$setScaleHandler(new ParticleScaleHandler(false, entrance, exit));

			return waterParticle;
		}
	}

	@Environment(EnvType.CLIENT)
	public record SplashFactory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
		@Override
		@NotNull
		public Particle createParticle(
			@NotNull SimpleParticleType defaultParticleType,
			@NotNull ClientLevel level,
			double x, double y, double z,
			double xd, double yd, double zd,
			RandomSource random
		) {
			final FluidFlowParticle splashParticle = new FluidFlowParticle(level, x, y, z, xd, yd, zd, this.spriteSet);

			splashParticle.alpha = 0.6F;
			splashParticle.endWhenUnderFluid = true;
			splashParticle.spawnsRipples = true;
			splashParticle.quadSize *= 1.5F;
			splashParticle.lifetime *= 3;

			splashParticle.flowMovementScale = 0.05D;
			splashParticle.particleTweaks$setCanBurn(true);

			final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.75F);
			entrance.setToZero();
			final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.FADE, 0.75F);
			splashParticle.particleTweaks$setScaleHandler(new ParticleScaleHandler(false, entrance, exit));

			return splashParticle;
		}
	}

	@Environment(EnvType.CLIENT)
	public record SmallCascadeFactory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
		@Override
		@NotNull
		public Particle createParticle(
			@NotNull SimpleParticleType defaultParticleType,
			@NotNull ClientLevel level,
			double x, double y, double z,
			double xd, double yd, double zd,
			RandomSource random
		) {
			final FluidFlowParticle smallCascadeParticle = new FluidFlowParticle(level, x, y, z, xd, yd, zd, this.spriteSet);

			final int waterColor = level.getBiome(BlockPos.containing(x, y, z)).value().getWaterColor();
			smallCascadeParticle.rCol = Math.clamp(((ARGB.red(waterColor) / 255F) * (float)random.triangle(1.3D, 0.3D)), 0F, 1F);
			smallCascadeParticle.bCol = Math.clamp(((ARGB.blue(waterColor) / 255F) * (float)random.triangle(1.3D, 0.3D)), 0F, 1F);
			smallCascadeParticle.gCol = Math.clamp(((ARGB.green(waterColor) / 255F) * (float)random.triangle(1.3D, 0.3D)), 0F, 1F);

			smallCascadeParticle.alpha = 0.25F;
			smallCascadeParticle.endWhenUnderFluid = true;
			smallCascadeParticle.quadSize *= 1.5F;

			smallCascadeParticle.flowMovementScale = 0.125D;
			smallCascadeParticle.particleTweaks$setCanBurn(true);

			final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.5F);
			entrance.setToZero();
			final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.FADE, 0.5F);
			smallCascadeParticle.particleTweaks$setScaleHandler(new ParticleScaleHandler(false, entrance, exit));

			return smallCascadeParticle;
		}
	}

	@Environment(EnvType.CLIENT)
	public record CascadeFactory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
		@Override
		@NotNull
		public Particle createParticle(
			@NotNull SimpleParticleType defaultParticleType,
			@NotNull ClientLevel level,
			double x, double y, double z,
			double xd, double yd, double zd,
			RandomSource random
		) {
			final FluidFlowParticle cascadeParticle = new FluidFlowParticle(level, x, y, z, xd, yd, zd, this.spriteSet);

			cascadeParticle.alpha = 0.75F;
			cascadeParticle.endWhenUnderFluid = false;
			cascadeParticle.quadSize *= 2.5F;

			cascadeParticle.flowMovementScale = 0.125D;
			cascadeParticle.particleTweaks$setCanBurn(true);

			final ParticleScaler entrance = new ParticleScaler(ParticleScaler.ScaleMethod.SIZE, 0.5F);
			entrance.setToZero();
			final ParticleScaler exit = new ParticleScaler(ParticleScaler.ScaleMethod.FADE, 0.5F);
			cascadeParticle.particleTweaks$setScaleHandler(new ParticleScaleHandler(false, entrance, exit));

			return cascadeParticle;
		}
	}
}
