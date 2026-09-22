package net.lunade.particletweaks.trailer.mixin.falling_leaves;

import com.llamalad7.mixinextras.sugar.Local;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import net.lunade.particletweaks.scale.api.ParticleScaleHandler;
import net.lunade.particletweaks.scale.impl.ParticleScaleInterface;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FallingLeavesParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ClientOnly
@Mixin(value = FallingLeavesParticle.class, priority = 1002)
public abstract class FallingLeavesParticleMixin extends SingleQuadParticle implements ParticleFluidMovementInterface {

	@Shadow
	@Final
	@Mutable
	private float windBig;
	@Shadow
	@Final
	@Mutable
	private double zaFlowScale;
	@Shadow
	@Final
	@Mutable
	private double xaFlowScale;

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
	private boolean particleTweaks$wasEverInFluid = false;

	protected FallingLeavesParticleMixin(ClientLevel level, double d, double e, double f, TextureAtlasSprite sprite) {
		super(level, d, e, f, sprite);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(
		CallbackInfo info,
		@Local(name = "particleRandom") float particleRandom
	) {
		if (!ParticleTweaksConfig.TRAILER_LEAVES.get()) return;
		this.particleTweaks$yRotPerTick = ((this.random.nextFloat() * particleRandom)) * 0.05F * (this.random.nextBoolean() ? -1F : 1F);
		this.particleTweaks$yRot = ((this.random.nextFloat())) * (this.random.nextBoolean() ? -0.5F : 0.5F) * Mth.TWO_PI;
		this.particleTweaks$prevYRot = this.particleTweaks$yRot;

		this.particleTweaks$xRotPerTick = ((this.random.nextFloat() * particleRandom)) * 0.005F * -this.random.nextFloat() * (this.random.nextBoolean() ? -1F : 1F);
		this.particleTweaks$xRot = ((this.random.nextFloat())) * (this.random.nextBoolean() ? -1F : 1F);
		this.particleTweaks$prevXRot = this.particleTweaks$xRot;
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void particleTweaks$tick(CallbackInfo info) {
		if (!ParticleTweaksConfig.TRAILER_LEAVES.get()) return;
		this.oRoll = this.roll;

		this.particleTweaks$prevYRot = this.particleTweaks$yRot;
		this.particleTweaks$yRot += this.particleTweaks$yRotPerTick;

		this.particleTweaks$prevXRot = this.particleTweaks$xRot;
		this.particleTweaks$xRot += this.particleTweaks$xRotPerTick;
	}

	@Override
	public void particleTweaks$onTouchingFluidSet(boolean touchingFluid) {
		final boolean wasEverInFluid = this.particleTweaks$wasEverInFluid;
		this.particleTweaks$wasEverInFluid = wasEverInFluid || touchingFluid;
		if (wasEverInFluid || !touchingFluid) return;
		this.gravity = 0.02F;
		this.xaFlowScale = 0F;
		this.zaFlowScale = 0F;
		this.windBig = 0F;
	}

	@Override
	public void extract(QuadParticleRenderState renderState, Camera camera, float partialTickTime) {
		if (!ParticleTweaksConfig.TRAILER_LEAVES.get()) {
			super.extract(renderState, camera, partialTickTime);
			return;
		}

		final Vec3 pos = camera.position();
		final float x = (float)(Mth.lerp(partialTickTime, this.xo, this.x) - pos.x());
		final float y = (float)(Mth.lerp(partialTickTime, this.yo, this.y) - pos.y());
		final float z = (float)(Mth.lerp(partialTickTime, this.zo, this.z) - pos.z());

		float roll = Mth.lerp(partialTickTime, this.oRoll, this.roll);
		float xRot = (Mth.lerp(partialTickTime, this.particleTweaks$prevXRot, this.particleTweaks$xRot)) + Mth.HALF_PI;
		float yRot = Mth.lerp(partialTickTime, this.particleTweaks$prevYRot, this.particleTweaks$yRot);
		final Quaternionf rotation = new Quaternionf()
			.rotateY(yRot)
			.rotateZ(roll)
			.rotateX(xRot);
		final Quaternionf flippedRotation = new Quaternionf()
			.rotateY(-Mth.PI + yRot)
			.rotateZ(-roll)
			.rotateX(-xRot);

		final ParticleScaleHandler scaleHandler = this instanceof ParticleScaleInterface scaleInterface
			? scaleInterface.particleTweaks$getScaleHandler()
			: null;

		final Layer layer = scaleHandler != null
			? scaleHandler.tryModifyParticleLayer(this.getLayer(), partialTickTime)
			: this.getLayer();
		final float quadSize = scaleHandler != null
			? scaleHandler.tryModifyQuadSize(this.getQuadSize(partialTickTime), partialTickTime)
			: this.getQuadSize(partialTickTime);
		final float UA = this.getU0();
		final float UB = this.getU1();
		final float V0 = this.getV0();
		final float V1 = this.getV1();
		final float alpha = scaleHandler != null
			? scaleHandler.tryModifyAlpha(this.alpha, partialTickTime)
			: this.alpha;
		final int color = ARGB.colorFromFloat(alpha, this.rCol, this.gCol, this.bCol);
		final int lightColor = this.getLightCoords(partialTickTime);

		renderState.add(
			layer,
			x, y, z,
			rotation.x, rotation.y, rotation.z, rotation.w,
			quadSize,
			UA, UB, V0, V1,
			color,
			lightColor
		);

		renderState.add(
			layer,
			x, y, z,
			flippedRotation.x, flippedRotation.y, flippedRotation.z, flippedRotation.w,
			quadSize,
			UB, UA, V0, V1,
			color,
			lightColor
		);
	}
}
