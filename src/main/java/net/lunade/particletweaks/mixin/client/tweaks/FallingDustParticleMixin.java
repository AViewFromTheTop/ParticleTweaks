package net.lunade.particletweaks.mixin.client.tweaks;

import net.lunade.particletweaks.impl.FlowingFluidParticleUtil;
import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FallingDustParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FallingDustParticle.class, priority = 1001)
public abstract class FallingDustParticleMixin extends SingleQuadParticle implements ParticleTweakInterface {

	protected FallingDustParticleMixin(ClientLevel clientLevel, double d, double e, double f, TextureAtlasSprite textureAtlasSprite) {
		super(clientLevel, d, e, f, textureAtlasSprite);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(CallbackInfo info) {
		this.particleTweaks$setNewSystem(true);
		this.particleTweaks$setScaler(0.25F);
		this.particleTweaks$setScalesToZero();
		this.particleTweaks$setSwitchesExit(true);
		this.particleTweaks$setSlowsInFluid(true);
		this.particleTweaks$setMovesWithFluid(true);
		this.particleTweaks$setCanBurn(true);
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$runScaling(CallbackInfo info) {
		if (!this.particleTweaks$usesNewSystem()) return;
		this.particleTweaks$calcScale();
		this.age = Mth.clamp(age - 1, 0, this.lifetime);
		if (this.particleTweaks$getScale(0F) <= 0.85F && !this.particleTweaks$hasSwitchedToShrinking()) {
			this.age = Mth.clamp(age - 1, 0, this.lifetime);
		}

		final Vec3 fluidMovement = FlowingFluidParticleUtil.handleFluidInteraction(
			this.level,
			new Vec3(this.x, this.y, this.z),
			new Vec3(this.xd, this.yd, this.zd),
			this,
			!this.particleTweaks$canBurn(),
			this.particleTweaks$slowsInFluid(),
			this.particleTweaks$movesWithFluid(),
			this.particleTweaks$getFluidMovementScale()
		);

		if (fluidMovement != null) {
			this.xd = fluidMovement.x;
			this.yd = fluidMovement.y;
			this.zd = fluidMovement.z;
		} else {
			info.cancel();
		}
	}

	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	public void particleTweaks$removeOnceSmall(CallbackInfo info) {
		if (this.particleTweaks$usesNewSystem() && this.particleTweaks$runScaleRemoval()) {
			this.remove();
			info.cancel();
		}
	}

	@Inject(method = "getLayer", at = @At("HEAD"), cancellable = true)
	public void particleTweaks$getRenderType(CallbackInfoReturnable<SingleQuadParticle.Layer> info) {
		info.setReturnValue(SingleQuadParticle.Layer.TRANSLUCENT);
	}
}
