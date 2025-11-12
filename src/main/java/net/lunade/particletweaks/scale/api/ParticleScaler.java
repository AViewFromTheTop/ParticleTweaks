package net.lunade.particletweaks.scale.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class ParticleScaler {
	private final float scaler;
	private final ScaleMethod scaleMethod;

	private float prevScale = 1F;
	private float scale = 1F;
	private float targetScale = 1F;

	public ParticleScaler(ScaleMethod scaleMethod, float scaler) {
		this.scaler = scaler;
		this.scaleMethod = scaleMethod;
	}

	public void setToZero() {
		this.prevScale = 0F;
		this.scale = 0F;
	}

	public void calcScale() {
		this.prevScale = this.scale;
		this.scale += (this.targetScale - this.scale) * this.scaler;
	}

	public boolean runScaleRemoval(boolean shouldShrinkStart) {
		if (shouldShrinkStart) {
			this.targetScale = 0F;
			if (this.prevScale <= 0.04F) this.scale = 0F;
			return this.prevScale == 0F;
		}
		this.targetScale = 1F;
		return false;
	}

	public float getScale(float partialTick) {
		return Mth.lerp(partialTick, this.prevScale, this.scale);
	}

	public boolean isSize() {
		return this.scaleMethod.isSize();
	}

	public boolean isFade() {
		return this.scaleMethod.isFade();
	}

	public enum ScaleMethod {
		SIZE(true, false),
		FADE(false, true),
		SIZE_AND_FADE(true, true);

		private final boolean isSize;
		private final boolean isFade;

		ScaleMethod(boolean isSize, boolean isFade) {
			this.isSize = isSize;
			this.isFade = isFade;
		}

		public boolean isSize() {
			return this.isSize;
		}

		public boolean isFade() {
			return this.isFade;
		}
	}
}
