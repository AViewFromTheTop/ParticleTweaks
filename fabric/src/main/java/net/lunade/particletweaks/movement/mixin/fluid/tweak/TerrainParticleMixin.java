package net.lunade.particletweaks.movement.mixin.fluid.tweak;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.movement.impl.ParticleFluidMovementInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.component.DamageResistant;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(TerrainParticle.class)
public class TerrainParticleMixin implements ParticleFluidMovementInterface {

	@Unique
	private boolean particleTweaks$isBurnableBlock = true;

	@Inject(
		method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V",
		at = @At("TAIL")
	)
	public void particleTweaks$markAsBurnable(
		ClientLevel level,
		double x, double y, double z,
		double xd, double yd, double zd,
		BlockState state,
		BlockPos pos,
		CallbackInfo info
	) {
		final DamageResistant damageResistant = state.getBlock().asItem().components().get(DataComponents.DAMAGE_RESISTANT);
		final boolean fireResistant = damageResistant != null
			&& damageResistant.isResistantTo(new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypes.IN_FIRE)));
		this.particleTweaks$isBurnableBlock = !fireResistant;
	}

	@Override
	public boolean particleTweaks$slowsInFluid() {
		return true;
	}

	@Override
	public boolean particleTweaks$canBurn() {
		return this.particleTweaks$isBurnableBlock;
	}

	@Override
	public boolean particleTweaks$movesWithFluid() {
		return true;
	}

	@Override
	public double particleTweaks$fluidSlowVerticalScale() {
		return 0.5D;
	}

	@Override
	public double particleTweaks$fluidAdditionalSlowVerticalScaleDownward() {
		return 0.2D;
	}
}
