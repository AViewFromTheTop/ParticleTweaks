/*
 * Copyright 2025 FrozenBlock
 * This file is part of The Copperier Age.
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

package net.lunade.particletweaks.config.gui;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.ParticleTweaksConstants;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import static net.frozenblock.lib.config.clothconfig.FrozenLibClothConfigGuiHelper.booleanEntry;

@Environment(EnvType.CLIENT)
public final class ParticleTweaksConfigGui {

	private static void setupEntries(ConfigCategory category, ConfigEntryBuilder builder) {
		category.addEntry(booleanEntry(builder, "trailer_cave_dust", ParticleTweaksConfig.TRAILER_CAVE_DUST));
		category.addEntry(booleanEntry(builder, "trailer_torches", ParticleTweaksConfig.TRAILER_TORCHES));
		category.addEntry(booleanEntry(builder, "trailer_campfires", ParticleTweaksConfig.TRAILER_CAMPFIRES));
		category.addEntry(booleanEntry(builder, "trailer_bubbles", ParticleTweaksConfig.TRAILER_BUBBLES));
		category.addEntry(booleanEntry(builder, "trailer_water_movement", ParticleTweaksConfig.TRAILER_WATER_MOVEMENT));
		category.addEntry(booleanEntry(builder, "trailer_ambient_water", ParticleTweaksConfig.TRAILER_AMBIENT_WATER));
		category.addEntry(booleanEntry(builder, "trailer_waves", ParticleTweaksConfig.TRAILER_WAVES));
		category.addEntry(booleanEntry(builder, "trailer_splashes", ParticleTweaksConfig.TRAILER_SPLASHES));
		category.addEntry(booleanEntry(builder, "trailer_ripples", ParticleTweaksConfig.TRAILER_RIPPLES));
		category.addEntry(booleanEntry(builder, "trailer_flowing_fluids", ParticleTweaksConfig.TRAILER_FLOWING_FLUIDS));
		category.addEntry(booleanEntry(builder, "trailer_cascades", ParticleTweaksConfig.TRAILER_CASCADES));
		category.addEntry(booleanEntry(builder, "trailer_poof", ParticleTweaksConfig.TRAILER_POOF));
		category.addEntry(booleanEntry(builder, "trailer_bubble_poof", ParticleTweaksConfig.TRAILER_BUBBLE_POOF));
		category.addEntry(booleanEntry(builder, "trailer_spell", ParticleTweaksConfig.TRAILER_SPELL));
		category.addEntry(booleanEntry(builder, "trailer_leaves", ParticleTweaksConfig.TRAILER_LEAVES));
	}

	public static Screen buildScreen(Screen parent) {
		final ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(text("component.title"));
		builder.setSavingRunnable(ParticleTweaksConfig.CONFIG::save);
		final ConfigCategory category = builder.getOrCreateCategory(text("config"));
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		setupEntries(category, entryBuilder);
		return builder.build();
	}

	public static Component text(String key) {
		return Component.translatable("option." + ParticleTweaksConstants.MOD_ID + "." + key);
	}

	public static Component tooltip(String key) {
		return Component.translatable("tooltip." + ParticleTweaksConstants.MOD_ID + "." + key);
	}

	public static Component enumNameProvider(String key) {
		return Component.translatable("enum." + ParticleTweaksConstants.MOD_ID + "." + key);
	}
}
