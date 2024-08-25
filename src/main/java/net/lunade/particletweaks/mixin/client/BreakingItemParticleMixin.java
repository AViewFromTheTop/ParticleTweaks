package net.lunade.particletweaks.mixin.client;

import net.lunade.particletweaks.impl.ParticleTweakInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BreakingItemParticle.class, priority = 999)
public abstract class BreakingItemParticleMixin implements ParticleTweakInterface {

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void particleTweaks$init(
		ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, ItemStack stack,
		CallbackInfo info
	) {
		this.particleTweaks$setNewSystem(true);
		this.particleTweaks$setScaler(0.35F);
		this.particleTweaks$setFadeInsteadOfScale(true);
		this.particleTweaks$setSlowsInFluid(true);
		this.particleTweaks$setMovesWithFluid(true);
		this.particleTweaks$setCanBurn(!stack.has(DataComponents.FIRE_RESISTANT));
	}

}
