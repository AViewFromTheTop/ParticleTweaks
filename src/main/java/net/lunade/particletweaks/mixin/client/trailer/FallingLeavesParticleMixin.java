package net.lunade.particletweaks.mixin.client.trailer;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.impl.FallingLeavesParticleInterface;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FallingLeavesParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = FallingLeavesParticle.class, priority = 1002)
public abstract class FallingLeavesParticleMixin extends SingleQuadParticle implements FallingLeavesParticleInterface {

	@Final
	@Shadow
	@Mutable
	private double xaFlowScale;
	@Final
	@Shadow
	@Mutable
	private double zaFlowScale;
	@Mutable
	@Shadow
	@Final
	private float windBig;

	@Shadow
	public abstract Layer getLayer();

	@Unique
	private float particleTweaks$yRotPerTick;
	@Unique
	private float particleTweaks$yRot;
	@Unique
	private float particleTweaks$prevYRot;

	@Unique
	private float particleTweaks$xRotPerTick;
	@Unique
	private float particleTweaks$xRot;
	@Unique
	private float particleTweaks$prevXRot;

	@Unique
	private boolean particleTweaks$inWater;
	@Unique
	private boolean particleTweaks$wasEverInWater;

	protected FallingLeavesParticleMixin(ClientLevel clientLevel, double d, double e, double f, TextureAtlasSprite textureAtlasSprite) {
		super(clientLevel, d, e, f, textureAtlasSprite);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(
		CallbackInfo info,
		@Local(ordinal = 5) float particleRandom
	) {
		if (!ParticleTweaksConfig.TRAILER_LEAVES) return;
		this.particleTweaks$yRotPerTick = ((this.random.nextFloat() * particleRandom)) * 0.05F * (this.random.nextBoolean() ? -1F : 1F);
		this.particleTweaks$yRot = ((this.random.nextFloat())) * (random.nextBoolean() ? -0.5F : 0.5F) * Mth.TWO_PI;
		this.particleTweaks$prevYRot = this.particleTweaks$yRot;

		this.particleTweaks$xRotPerTick = ((this.random.nextFloat() * particleRandom)) * 0.005F * -this.random.nextFloat() * (this.random.nextBoolean() ? -1F : 1F);
		this.particleTweaks$xRot = ((this.random.nextFloat())) * (this.random.nextBoolean() ? -1F : 1F);
		this.particleTweaks$prevXRot = this.particleTweaks$xRot;
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void particleTweaks$tick(CallbackInfo info) {
		if (!ParticleTweaksConfig.TRAILER_LEAVES) return;
		this.oRoll = this.roll;

		this.particleTweaks$prevYRot = this.particleTweaks$yRot;
		this.particleTweaks$yRot += this.particleTweaks$yRotPerTick;

		this.particleTweaks$prevXRot = this.particleTweaks$xRot;
		this.particleTweaks$xRot += this.particleTweaks$xRotPerTick;
	}

	@Unique
	@Override
	public void particleTweaks$setInWater(boolean inWater) {
		this.particleTweaks$inWater = inWater;

		boolean hasEverBeenInTheWaterBefore = this.particleTweaks$wasEverInWater;
		this.particleTweaks$wasEverInWater = hasEverBeenInTheWaterBefore || inWater;
		if (!hasEverBeenInTheWaterBefore && inWater) {
			this.gravity = 0.02F;
			this.xaFlowScale = 0F;
			this.zaFlowScale = 0F;
			this.windBig = 0F;
		}
	}

	@Unique
	@Override
	public boolean particleTweaks$inWater() {
		return this.particleTweaks$inWater;
	}

	@Override
	public void extract(QuadParticleRenderState quadParticleRenderState, Camera camera, float partialTick) {
		if (!ParticleTweaksConfig.TRAILER_LEAVES) {
			super.extract(quadParticleRenderState, camera, partialTick);
			return;
		}

		float roll = Mth.lerp(partialTick, this.oRoll, this.roll);
		float xRot = (Mth.lerp(partialTick, this.particleTweaks$prevXRot, this.particleTweaks$xRot)) + Mth.HALF_PI;
		float yRot = Mth.lerp(partialTick, this.particleTweaks$prevYRot, this.particleTweaks$yRot);
		final Quaternionf rotation = new Quaternionf()
			.rotateY(yRot)
			.rotateZ(roll)
			.rotateX(xRot);

		this.extractRotatedQuad(quadParticleRenderState, camera, rotation, partialTick);
	}

	@Override
	protected void extractRotatedQuad(
		@NotNull QuadParticleRenderState quadParticleRenderState,
		@NotNull Quaternionf quaternionf,
		float x, float y, float z,
		float partialTick
	) {
		final Layer layer = this.getLayer();
		final float quadSize = this.getQuadSize(partialTick);
		final float UA = this.getU0();
		final float UB = this.getU1();
		final float V0 = this.getV0();
		final float V1 = this.getV1();
		final int color = ARGB.colorFromFloat(this.alpha, this.rCol, this.gCol, this.bCol);
		final int lightColor = this.getLightColor(partialTick);

		quadParticleRenderState.add(
			layer,
			x, y, z,
			quaternionf.x, quaternionf.y, quaternionf.z, quaternionf.w,
			quadSize,
			UA, UB, V0, V1,
			color,
			lightColor
		);

		final Quaternionf oppositeRot = quaternionf.conjugate().rotateY(-Mth.PI);
		quadParticleRenderState.add(
			layer,
			x, y, z,
			oppositeRot.x, oppositeRot.y, oppositeRot.z, oppositeRot.w,
			quadSize,
			UB, UA, V0, V1,
			color,
			lightColor
		);
	}
}
