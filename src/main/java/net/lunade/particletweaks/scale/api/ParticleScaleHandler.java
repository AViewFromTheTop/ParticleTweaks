package net.lunade.particletweaks.scale.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ParticleScaleHandler {
	final float entranceScaleBuffer;
	final boolean useLifetime;
	final int exitTickThreshold;
	@Nullable
	private final ParticleScaler entrance;
	@Nullable
	private final ParticleScaler exit;

	boolean hasSetMaxLifetime;
	int maxLifetime;
	boolean shouldShrinkStart = false;

	public ParticleScaleHandler(float entranceScaleBuffer, boolean useLifetime, int exitTickThreshold, @Nullable ParticleScaler entrance, @Nullable ParticleScaler exit) {
		this.entranceScaleBuffer = entranceScaleBuffer;
		this.useLifetime = useLifetime;
		this.exitTickThreshold = exitTickThreshold;
		this.entrance = entrance;
		this.exit = exit;
	}

	public ParticleScaleHandler(float entranceScaleBuffer, boolean useLifetime, @Nullable ParticleScaler entrance, @Nullable ParticleScaler exit) {
		this(entranceScaleBuffer, useLifetime, 0, entrance, exit);
	}

	public ParticleScaleHandler(boolean useLifetime, @Nullable ParticleScaler entrance, @Nullable ParticleScaler exit) {
		this(0.85F, useLifetime, 0, entrance, exit);
	}

	public ParticleScaleHandler(boolean useLifetime, int exitTickThreshold, @Nullable ParticleScaler entrance, @Nullable ParticleScaler exit) {
		this(0.85F, useLifetime, exitTickThreshold, entrance, exit);
	}

	@Nullable
	public ParticleScaler entrance() {
		return this.entrance;
	}

	@Nullable
	public ParticleScaler exit() {
		return this.exit;
	}

	public void runScaleTick(Particle particle) {
		if (this.useLifetime && !this.hasSetMaxLifetime) {
			this.hasSetMaxLifetime = true;
			this.maxLifetime = particle.lifetime;
		}

		if (this.entrance != null) this.entrance.calcScale();
		if (this.exit != null) this.exit.calcScale();

		final boolean bufferStartingTime = !this.shouldShrinkStart
			&& this.entrance != null
			&& this.entrance.getScale(0F) <= this.entranceScaleBuffer;
		if (this.useLifetime) {
			particle.lifetime = Math.min(particle.lifetime + 1, this.maxLifetime);
			if (bufferStartingTime) particle.lifetime = Math.min(particle.lifetime + 1, this.maxLifetime);
		} else {
			particle.age = Mth.clamp(particle.age - 1, 0, particle.lifetime);
			if (bufferStartingTime) particle.age = Mth.clamp(particle.age - 1, 0, particle.lifetime);
		}
	}

	public boolean runScaleRemovalTick(Particle particle) {
		if (!this.runScaleRemoval(particle)) return false;
		particle.remove();
		return true;
	}

	private boolean runScaleRemoval(Particle particle) {
		if (this.useLifetime) {
			particle.lifetime -= 1;
		} else {
			particle.age = Mth.clamp(particle.age + 1, 0, particle.lifetime);
		}

		final boolean shouldShrinkStart = this.shouldShrinkStart(particle);
		if (this.exit == null) return shouldShrinkStart;
		return this.exit.runScaleRemoval(shouldShrinkStart);
	}

	private boolean shouldShrinkStart(Particle particle) {
		if (this.shouldShrinkStart) return true;
		if (this.useLifetime ? (particle.lifetime <= this.exitTickThreshold) : (particle.age >= particle.lifetime - this.exitTickThreshold)) this.shouldShrinkStart = true;
		return this.shouldShrinkStart;
	}

}
