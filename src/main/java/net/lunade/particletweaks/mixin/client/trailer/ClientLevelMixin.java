package net.lunade.particletweaks.mixin.client.trailer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(ClientLevel.class)
public class ClientLevelMixin {

	@WrapMethod(method = "doAddParticle")
	public void particleTweaks$trailerReplacements(
		ParticleOptions options,
		boolean alwaysSpawn,
		boolean canSpawnOnMinimal,
		double x,
		double y,
		double z,
		double velocityX,
		double velocityY,
		double velocityZ,
		Operation<Particle> original
	) {
		if (options == ParticleTypes.POOF && ParticleTweaksConfig.TRAILER_POOF) options = ParticleTweaksParticleTypes.POOF;
		original.call(options, alwaysSpawn, canSpawnOnMinimal, x, y, z, velocityX, velocityY, velocityZ);
	}
}
