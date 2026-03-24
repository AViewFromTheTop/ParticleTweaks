package net.lunade.particletweaks.trailer.mixin.poof;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientLevel.class)
public class ClientLevelMixin {

	@Inject(method = "doAddParticle", at = @At("HEAD"))
	public void particleTweaks$trailerPoof(
		CallbackInfo info,
		@Local(argsOnly = true) LocalRef<ParticleOptions> options
	) {
		if (options.get() == ParticleTypes.POOF && ParticleTweaksConfig.TRAILER_POOF.get()) options.set(ParticleTweaksParticleTypes.POOF);
	}
}
