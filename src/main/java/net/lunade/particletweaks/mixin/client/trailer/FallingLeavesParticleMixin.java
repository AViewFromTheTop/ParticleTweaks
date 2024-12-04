package net.lunade.particletweaks.mixin.client.trailer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.config.ParticleTweaksConfigGetter;
import net.lunade.particletweaks.impl.FallingLeavesParticleInterface;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FallingLeavesParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;
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
public abstract class FallingLeavesParticleMixin extends TextureSheetParticle implements FallingLeavesParticleInterface {

	@Shadow
	@Final
	private float particleRandom;

	@Shadow
	private double xaFlowScale;
	@Shadow
	private double zaFlowScale;
	@Mutable
	@Shadow
	@Final
	private float windBig;
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

	protected FallingLeavesParticleMixin(ClientLevel world, double d, double e, double f) {
		super(world, d, e, f);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		if (ParticleTweaksConfigGetter.trailerLeaves()) {
			RandomSource random = RandomSource.createNewThreadLocalInstance();
			this.particleTweaks$yRotPerTick = ((random.nextFloat() * this.particleRandom)) * 0.05F * (random.nextBoolean() ? -1F : 1F);
			this.particleTweaks$yRot = ((random.nextFloat())) * (random.nextBoolean() ? -0.5F : 0.5F) * Mth.TWO_PI;
			this.particleTweaks$prevYRot = this.particleTweaks$yRot;

			this.particleTweaks$xRotPerTick = ((random.nextFloat() * this.particleRandom)) * 0.005F * -random.nextFloat() * (random.nextBoolean() ? -1F : 1F);
			this.particleTweaks$xRot = ((random.nextFloat())) * (random.nextBoolean() ? -1F : 1F);
			this.particleTweaks$prevXRot = this.particleTweaks$xRot;
		}
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void particleTweaks$tick(CallbackInfo info) {
		if (ParticleTweaksConfigGetter.trailerLeaves()) {
			this.oRoll = this.roll;

			this.particleTweaks$prevYRot = this.particleTweaks$yRot;
			this.particleTweaks$yRot += this.particleTweaks$yRotPerTick;

			this.particleTweaks$prevXRot = this.particleTweaks$xRot;
			this.particleTweaks$xRot += this.particleTweaks$xRotPerTick;
		}
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
	public void render(VertexConsumer buffer, @NotNull Camera camera, float partialTicks) {
		if (ParticleTweaksConfigGetter.trailerLeaves()) {
			float roll = Mth.lerp(partialTicks, this.oRoll, this.roll);
			float xRot = (Mth.lerp(partialTicks, this.particleTweaks$prevXRot, this.particleTweaks$xRot)) + Mth.HALF_PI;
			float yRot = Mth.lerp(partialTicks, this.particleTweaks$prevYRot, this.particleTweaks$yRot);

			this.trailierTales$render3DParticle(
				buffer, camera, partialTicks, false, transforms -> transforms.rotateY(yRot)
					.rotateZ(roll)
					.rotateX(xRot)
			);
			this.trailierTales$render3DParticle(
				buffer, camera, partialTicks, true, transforms -> transforms.rotateY((float) -Math.PI + yRot)
					.rotateZ(-roll)
					.rotateX(-xRot)
			);
		} else {
			super.render(buffer, camera, partialTicks);
		}
	}

	@Unique
	private static final Vector3f TRAILIERTALES$NORMALIZED_QUAT_VECTOR = new Vector3f(0.5F, 0.5F, 0.5F).normalize();

	@Unique
	private void trailierTales$render3DParticle(
		VertexConsumer buffer,
		@NotNull Camera renderInfo,
		float partialTicks,
		boolean flipped,
		@NotNull Consumer<Quaternionf> quaternionConsumer
	) {
		Vec3 vec3 = renderInfo.getPosition();
		float f = (float)(Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
		float g = (float)(Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
		float h = (float)(Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
		Quaternionf quaternionf = new Quaternionf().setAngleAxis(
			0F,
			TRAILIERTALES$NORMALIZED_QUAT_VECTOR.x(),
			TRAILIERTALES$NORMALIZED_QUAT_VECTOR.y(),
			TRAILIERTALES$NORMALIZED_QUAT_VECTOR.z()
		);
		quaternionConsumer.accept(quaternionf);
		Vector3f[] vector3fs = new Vector3f[]{
			new Vector3f(-1F, -1F, 0F),
			new Vector3f(-1F, 1F, 0F),
			new Vector3f(1F, 1F, 0F),
			new Vector3f(1F, -1F, 0F)
		};
		float i = this.getQuadSize(partialTicks);

		for (int j = 0; j < 4; ++j) {
			Vector3f vector3f2 = vector3fs[j];
			vector3f2.rotate(quaternionf);
			vector3f2.mul(i);
			vector3f2.add(f, g, h);
		}

		float k = !flipped ? this.getU0() : this.getU1();
		float l = !flipped ? this.getU1() : this.getU0();
		float m = this.getV0();
		float n = this.getV1();
		int light = this.getLightColor(partialTicks);
		buffer.addVertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z())
			.setUv(l, n)
			.setColor(this.rCol, this.gCol, this.bCol, this.alpha)
			.setLight(light);
		buffer.addVertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z())
			.setUv(l, m)
			.setColor(this.rCol, this.gCol, this.bCol, this.alpha)
			.setLight(light);
		buffer.addVertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z())
			.setUv(k, m)
			.setColor(this.rCol, this.gCol, this.bCol, this.alpha)
			.setLight(light);
		buffer.addVertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z())
			.setUv(k, n)
			.setColor(this.rCol, this.gCol, this.bCol, this.alpha)
			.setLight(light);
	}
}
