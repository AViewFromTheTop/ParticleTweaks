package net.lunade.particletweaks.movement.mixin.fluid.tweak.wilderwild.mesoglea;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.wilderwild.particle.MesogleaBubbleColumnUpParticle;
import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(MesogleaBubbleColumnUpParticle.class)
public class MesogleaBubbleColumnUpParticleMixin implements ParticleFluidMovementInterface {

	@Override
	public boolean particleTweaks$canBurn() {
		return true;
	}

	@Override
	public boolean particleTweaks$movesWithFluid() {
		return true;
	}
}
