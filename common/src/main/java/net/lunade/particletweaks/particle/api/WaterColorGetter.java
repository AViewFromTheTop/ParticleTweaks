/*
 * Copyright 2026 Lunade Music/AViewFromTheTop
 * This file is part of Particle Tweaks.
 *
 * This program is free software; you can modify it under
 * the terms of version 1 of the FrozenBlock Modding Oasis License
 * as published by FrozenBlock Modding Oasis.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * FrozenBlock Modding Oasis License for more details.
 *
 * You should have received a copy of the FrozenBlock Modding Oasis License
 * along with this program; if not, see <https://github.com/FrozenBlock/Licenses>.
 */

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
