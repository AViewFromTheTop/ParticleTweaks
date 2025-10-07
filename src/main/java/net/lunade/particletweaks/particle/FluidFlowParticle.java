package net.lunade.particletweaks.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class FluidFlowParticle extends SingleQuadParticle {
	private static final int LAVA_COLOR = 16743195;
	private final SpriteSet spriteSet;
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

		final BlockPos blockPos = BlockPos.containing(this.x, this.y, this.z);
		final BlockState blockState = this.level.getBlockState(blockPos);
		final FluidState fluidState = blockState.getFluidState();
		final float fluidHeight = fluidState.getHeight(this.level, blockPos);
		final float worldFluidHeight = fluidHeight + (float) blockPos.getY();
		final boolean isFluidHighEnough = !fluidState.isEmpty() && worldFluidHeight >= this.y;
		if (isFluidHighEnough) {
			if (fluidState.getFlow(this.level, blockPos).horizontalDistance() == 0D) this.age = Math.clamp(this.age + 3, 0, this.lifetime);
			if (this.floatOnFluid) {
				if (!fluidState.hasProperty(FlowingFluid.FALLING) || !fluidState.getValue(FlowingFluid.FALLING)) {
					if (this.yd < 0.01D) this.yd += 0.05D;
					this.yd += (0F - this.yd) * 0.4D;
					this.y += ((blockPos.getY() + fluidHeight) - this.y) * 0.5D;
				}
			}

			if (this.endWhenUnderFluid || this.spawnsRipples) this.age = this.lifetime;
			if (this.spawnsRipples
				&& !this.hasSpawnedRipple
				&& worldFluidHeight < this.yo
				&& this.level.getFluidState(blockPos.above()).isEmpty()
			) {
				this.hasSpawnedRipple = true;
				this.level.addParticle(
					ParticleTweaksParticleTypes.RIPPLE,
					this.x,
					(blockPos.getY() + fluidHeight),
					this.z,
					0D,
					0D,
					0D
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
			FluidFlowParticle lavaParticle = new FluidFlowParticle(level, x, y, z, xd, yd, zd, this.spriteSet);

			lavaParticle.rCol = ARGB.red(LAVA_COLOR) / 255F;
			lavaParticle.bCol = ARGB.blue(LAVA_COLOR) / 255F;
			lavaParticle.gCol = ARGB.green(LAVA_COLOR) / 255F;

			lavaParticle.isLava = true;
			lavaParticle.quadSize *= 0.75F;
			lavaParticle.floatOnFluid = false;
			lavaParticle.endWhenUnderFluid = false;
			lavaParticle.setSize(0.078125F, 0.078125F);

			if (lavaParticle instanceof ParticleTweakInterface particleTweakInterface) {
				particleTweakInterface.particleTweaks$setNewSystem(true);
				particleTweakInterface.particleTweaks$setMovesWithFluid(true);
				particleTweakInterface.particleTweaks$setCanBurn(true);
				particleTweakInterface.particleTweaks$setScalesToZero();
				particleTweakInterface.particleTweaks$setSwitchesExit(true);
				particleTweakInterface.particleTweaks$setFluidMovementScale(0.05D);
				particleTweakInterface.particleTweaks$setScaler(0.5F);
			}

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
			FluidFlowParticle waterParticle = new FluidFlowParticle(level, x, y, z, xd, yd, zd, this.spriteSet);

			int waterColor = level.getBiome(BlockPos.containing(x, y, z)).value().getWaterColor();
			waterParticle.rCol = Math.clamp(((ARGB.red(waterColor) / 255F) * (float)random.triangle(1.3D, 0.3D)), 0F, 1F);
			waterParticle.bCol = Math.clamp(((ARGB.blue(waterColor) / 255F) * (float)random.triangle(1.3D, 0.3D)), 0F, 1F);
			waterParticle.gCol = Math.clamp(((ARGB.green(waterColor) / 255F) * (float)random.triangle(1.3D, 0.3D)), 0F, 1F);

			waterParticle.alpha = 0.6F;
			waterParticle.quadSize *= 0.5F;
			waterParticle.floatOnFluid = true;
			waterParticle.endWhenUnderFluid = false;
			waterParticle.setSize(0.0325F, 0.0325F);

			if (waterParticle instanceof ParticleTweakInterface particleTweakInterface) {
				particleTweakInterface.particleTweaks$setNewSystem(true);
				particleTweakInterface.particleTweaks$setMovesWithFluid(true);
				particleTweakInterface.particleTweaks$setCanBurn(true);
				particleTweakInterface.particleTweaks$setScalesToZero();
				particleTweakInterface.particleTweaks$setSwitchesExit(true);
				particleTweakInterface.particleTweaks$setFluidMovementScale(0.05D);
				particleTweakInterface.particleTweaks$setScaler(0.5F);
				particleTweakInterface.particleTweaks$setMaxAlpha(0.6F);
			}

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
			FluidFlowParticle splashParticle = new FluidFlowParticle(level, x, y, z, xd, yd, zd, this.spriteSet);

			splashParticle.alpha = 0.6F;
			splashParticle.endWhenUnderFluid = true;
			splashParticle.spawnsRipples = true;
			splashParticle.quadSize *= 1.5F;
			splashParticle.lifetime *= 3;

			if (splashParticle instanceof ParticleTweakInterface particleTweakInterface) {
				particleTweakInterface.particleTweaks$setNewSystem(true);
				particleTweakInterface.particleTweaks$setMovesWithFluid(true);
				particleTweakInterface.particleTweaks$setCanBurn(true);
				particleTweakInterface.particleTweaks$setScalesToZero();
				particleTweakInterface.particleTweaks$setSwitchesExit(true);
				particleTweakInterface.particleTweaks$setFluidMovementScale(0.05D);
				particleTweakInterface.particleTweaks$setScaler(0.75F);
				particleTweakInterface.particleTweaks$setMaxAlpha(0.6F);
			}

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
			FluidFlowParticle smallCascadeParticle = new FluidFlowParticle(level, x, y, z, xd, yd, zd, this.spriteSet);

			int waterColor = level.getBiome(BlockPos.containing(x, y, z)).value().getWaterColor();
			smallCascadeParticle.rCol = Math.clamp(((ARGB.red(waterColor) / 255F) * (float)random.triangle(1.3D, 0.3D)), 0F, 1F);
			smallCascadeParticle.bCol = Math.clamp(((ARGB.blue(waterColor) / 255F) * (float)random.triangle(1.3D, 0.3D)), 0F, 1F);
			smallCascadeParticle.gCol = Math.clamp(((ARGB.green(waterColor) / 255F) * (float)random.triangle(1.3D, 0.3D)), 0F, 1F);

			smallCascadeParticle.alpha = 0.25F;
			smallCascadeParticle.endWhenUnderFluid = true;
			smallCascadeParticle.quadSize *= 1.5F;

			if (smallCascadeParticle instanceof ParticleTweakInterface particleTweakInterface) {
				particleTweakInterface.particleTweaks$setNewSystem(true);
				particleTweakInterface.particleTweaks$setMovesWithFluid(true);
				particleTweakInterface.particleTweaks$setCanBurn(true);
				particleTweakInterface.particleTweaks$setScalesToZero();
				particleTweakInterface.particleTweaks$setSwitchesExit(true);
				particleTweakInterface.particleTweaks$setFluidMovementScale(0.125D);
				particleTweakInterface.particleTweaks$setScaler(0.5F);
				particleTweakInterface.particleTweaks$setMaxAlpha(0.25F);
			}

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
			FluidFlowParticle cascadeParticle = new FluidFlowParticle(level, x, y, z, xd, yd, zd, this.spriteSet);

			cascadeParticle.alpha = 0.75F;
			cascadeParticle.endWhenUnderFluid = false;
			cascadeParticle.quadSize *= 2.5F;

			if (cascadeParticle instanceof ParticleTweakInterface particleTweakInterface) {
				particleTweakInterface.particleTweaks$setNewSystem(true);
				particleTweakInterface.particleTweaks$setMovesWithFluid(true);
				particleTweakInterface.particleTweaks$setCanBurn(true);
				particleTweakInterface.particleTweaks$setScalesToZero();
				particleTweakInterface.particleTweaks$setSwitchesExit(true);
				particleTweakInterface.particleTweaks$setFluidMovementScale(0.125D);
				particleTweakInterface.particleTweaks$setScaler(0.5F);
				particleTweakInterface.particleTweaks$setMaxAlpha(0.75F);
			}

			return cascadeParticle;
		}
	}
}
