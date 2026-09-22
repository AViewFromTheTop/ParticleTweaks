package net.lunade.particletweaks.particle.api;

import net.frozenblock.lib.block.api.waterlike.WaterLikeBlock;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@ClientOnly
public final class WaterColorGetter {

	public static int getWaterColor(Level level, double x, double y, double z) {
		if (y < level.getMinY()) y = level.getMinY();
		if (y > level.getMaxY()) y = level.getMaxY();
		final BlockPos pos = BlockPos.containing(x, y, z);
		final BlockState blockState = level.getBlockState(pos);
		return blockState.getBlock() instanceof WaterLikeBlock waterLike
			? waterLike.waterLikeColor().rgba()
			: level.getBiome(pos).value().getWaterColor();
	}

	private WaterColorGetter() {}
}
