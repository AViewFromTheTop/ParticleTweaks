package net.lunade.particletweaks.mixin.client.splash;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Arrays;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.particle.WaveParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(QuadParticleRenderState.class)
public abstract class QuadParticleRenderStateMixin {

	@Shadow
	protected abstract void renderVertex(VertexConsumer vertexConsumer, Quaternionf quaternionf, float f, float g, float h, float i, float j, float k, float l, float m, int n, int o);

	@Unique
	private static final Vector3f PARTICLE_TWEAKS$NORMALIZED_QUAT_VECTOR = new Vector3f(0.5F, 0.5F, 0.5F).normalize();

	@WrapOperation(
		method = "prepare",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/state/QuadParticleRenderState$Storage;forEachParticle(Lnet/minecraft/client/renderer/state/QuadParticleRenderState$ParticleConsumer;)V"
		)
	)
	public void particleTweaks$renderWaveParticles(
		QuadParticleRenderState.Storage instance, QuadParticleRenderState.ParticleConsumer particleConsumer, Operation<Void> original,
		@Local Map.Entry<SingleQuadParticle.Layer, QuadParticleRenderState.Storage> entry,
		@Local BufferBuilder bufferBuilder
	) {
		if (entry.getKey() != WaveParticle.WAVE) {
			original.call(instance, particleConsumer);
			return;
		}

		instance.forEachParticle(
			new QuadParticleRenderState.ParticleConsumer() {
				@Override
				public void consume(
					float x, float y, float z,
					float directionOrdinal,
					float width, float height,
					float flippedFloat,
					float quadSize,
					float U0, float U1, float V0, float V1,
					int color,
					int light
				) {
					particleTweaks$renderWaveQuad(bufferBuilder, x, y, z, directionOrdinal, width, height, flippedFloat, quadSize, U0, U1, V0, V1, color, light);
				}
			}
		);
	}

	@Unique
	private void particleTweaks$renderWaveQuad(
		VertexConsumer vertexConsumer,
		float x, float y, float z,
		float directionOrdinal,
		float width, float height,
		float flippedFloat,
		float quadSize,
		float U0, float U1, float V0, float V1,
		int color,
		int light
	) {
		final Direction direction = Arrays.stream(Direction.values())
			.filter(possibleDirection -> possibleDirection.ordinal() == (int)directionOrdinal)
			.findFirst()
			.orElse(null);
		final float yRot = direction.toYRot() * Mth.DEG_TO_RAD;
		final boolean flipped = flippedFloat == 1F;

		final float halfWidth = width * 0.5F;

		Quaternionf rotation = new Quaternionf().setAngleAxis(
			0F,
			PARTICLE_TWEAKS$NORMALIZED_QUAT_VECTOR.x(),
			PARTICLE_TWEAKS$NORMALIZED_QUAT_VECTOR.y(),
			PARTICLE_TWEAKS$NORMALIZED_QUAT_VECTOR.z()
		);
		if (!flipped) {
			rotation = rotation.rotateY(yRot);
		} else {
			rotation = rotation.rotateY(-Mth.PI + yRot);
		}

		final float vertexWidth = halfWidth * 1.07F;
		this.renderVertex(vertexConsumer, rotation, x, y, z, vertexWidth, -height, quadSize, U1, V1, color, light);
		this.renderVertex(vertexConsumer, rotation, x, y, z, vertexWidth, height, quadSize, U1, V0, color, light);
		this.renderVertex(vertexConsumer, rotation, x, y, z, -vertexWidth, height, quadSize, U0, V0, color, light);
		this.renderVertex(vertexConsumer, rotation, x, y, z, -vertexWidth, -height, quadSize, U0, V1, color, light);
	}

}
