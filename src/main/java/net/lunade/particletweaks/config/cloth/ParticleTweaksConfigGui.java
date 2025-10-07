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

package net.lunade.particletweaks.config.cloth;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.ParticleTweaksConstants;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public final class ParticleTweaksConfigGui {

	private static void setupEntries(@NotNull ConfigCategory category, @NotNull ConfigEntryBuilder entryBuilder) {
		var config = ParticleTweaksConfig.get(true);

		category.addEntry(
			entryBuilder.startBooleanToggle(ParticleTweaksConstants.text("trailer_cave_dust"), config.trailerCaveDust)
				.setDefaultValue(false)
				.setSaveConsumer(newValue -> config.trailerCaveDust = newValue)
				.setTooltip(ParticleTweaksConstants.tooltip("trailer_cave_dust"))
				.setYesNoTextSupplier(value -> ParticleTweaksConstants.text(value ? "true" : "false"))
				.build()
		);

		category.addEntry(
			entryBuilder.startBooleanToggle(ParticleTweaksConstants.text("trailer_torches"), config.trailerTorches)
				.setDefaultValue(false)
				.setSaveConsumer(newValue -> config.trailerTorches = newValue)
				.setTooltip(ParticleTweaksConstants.tooltip("trailer_torches"))
				.setYesNoTextSupplier(value -> ParticleTweaksConstants.text(value ? "true" : "false"))
				.build()
		);

		category.addEntry(
			entryBuilder.startBooleanToggle(ParticleTweaksConstants.text("trailer_campfires"), config.trailerCampfires)
				.setDefaultValue(false)
				.setSaveConsumer(newValue -> config.trailerCampfires = newValue)
				.setTooltip(ParticleTweaksConstants.tooltip("trailer_campfires"))
				.setYesNoTextSupplier(value -> ParticleTweaksConstants.text(value ? "true" : "false"))
				.build()
		);

		category.addEntry(
			entryBuilder.startBooleanToggle(ParticleTweaksConstants.text("trailer_bubbles"), config.trailerBubbles)
				.setDefaultValue(false)
				.setSaveConsumer(newValue -> config.trailerBubbles = newValue)
				.setTooltip(ParticleTweaksConstants.tooltip("trailer_bubbles"))
				.setYesNoTextSupplier(value -> ParticleTweaksConstants.text(value ? "true" : "false"))
				.build()
		);

		category.addEntry(
			entryBuilder.startBooleanToggle(ParticleTweaksConstants.text("trailer_water_movement"), config.trailerWaterMovement)
				.setDefaultValue(false)
				.setSaveConsumer(newValue -> config.trailerWaterMovement = newValue)
				.setTooltip(ParticleTweaksConstants.tooltip("trailer_water_movement"))
				.setYesNoTextSupplier(value -> ParticleTweaksConstants.text(value ? "true" : "false"))
				.build()
		);

		category.addEntry(
			entryBuilder.startBooleanToggle(ParticleTweaksConstants.text("trailer_ambient_water"), config.trailerAmbientWater)
				.setDefaultValue(false)
				.setSaveConsumer(newValue -> config.trailerAmbientWater = newValue)
				.setTooltip(ParticleTweaksConstants.tooltip("trailer_ambient_water"))
				.setYesNoTextSupplier(value -> ParticleTweaksConstants.text(value ? "true" : "false"))
				.build()
		);

		category.addEntry(
			entryBuilder.startBooleanToggle(ParticleTweaksConstants.text("trailer_splashes"), config.trailerSplashes)
				.setDefaultValue(false)
				.setSaveConsumer(newValue -> config.trailerSplashes = newValue)
				.setTooltip(ParticleTweaksConstants.tooltip("trailer_splashes"))
				.setYesNoTextSupplier(value -> ParticleTweaksConstants.text(value ? "true" : "false"))
				.build()
		);

		category.addEntry(
			entryBuilder.startBooleanToggle(ParticleTweaksConstants.text("trailer_flowing_fluids"), config.trailerFlowingFluids)
				.setDefaultValue(false)
				.setSaveConsumer(newValue -> config.trailerFlowingFluids = newValue)
				.setTooltip(ParticleTweaksConstants.tooltip("trailer_flowing_fluids"))
				.setYesNoTextSupplier(value -> ParticleTweaksConstants.text(value ? "true" : "false"))
				.build()
		);

		category.addEntry(
			entryBuilder.startBooleanToggle(ParticleTweaksConstants.text("trailer_cascades"), config.trailerCascades)
				.setDefaultValue(false)
				.setSaveConsumer(newValue -> config.trailerCascades = newValue)
				.setTooltip(ParticleTweaksConstants.tooltip("trailer_cascades"))
				.setYesNoTextSupplier(value -> ParticleTweaksConstants.text(value ? "true" : "false"))
				.build()
		);

		category.addEntry(
			entryBuilder.startBooleanToggle(ParticleTweaksConstants.text("trailer_poof"), config.trailerPoof)
				.setDefaultValue(false)
				.setSaveConsumer(newValue -> config.trailerPoof = newValue)
				.setTooltip(ParticleTweaksConstants.tooltip("trailer_poof"))
				.setYesNoTextSupplier(value -> ParticleTweaksConstants.text(value ? "true" : "false"))
				.build()
		);

		category.addEntry(
			entryBuilder.startBooleanToggle(ParticleTweaksConstants.text("trailer_spell"), config.trailerSpell)
				.setDefaultValue(false)
				.setSaveConsumer(newValue -> config.trailerSpell = newValue)
				.setTooltip(ParticleTweaksConstants.tooltip("trailer_spell"))
				.setYesNoTextSupplier(value -> ParticleTweaksConstants.text(value ? "true" : "false"))
				.build()
		);

		category.addEntry(
			entryBuilder.startBooleanToggle(ParticleTweaksConstants.text("trailer_leaves"), config.trailerLeaves)
				.setDefaultValue(false)
				.setSaveConsumer(newValue -> config.trailerLeaves = newValue)
				.setTooltip(ParticleTweaksConstants.tooltip("trailer_leaves"))
				.setYesNoTextSupplier(value -> ParticleTweaksConstants.text(value ? "true" : "false"))
				.build()
		);
	}

	public static Screen buildScreen(Screen parent) {
		var configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(text("component.title"));
		configBuilder.setSavingRunnable(ParticleTweaksConfig.INSTANCE::save);
		var config = configBuilder.getOrCreateCategory(text("config"));
		ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();
		setupEntries(config, entryBuilder);
		return configBuilder.build();
	}

	@Contract(value = "_ -> new", pure = true)
	public static @NotNull Component text(String key) {
		return Component.translatable("option." + ParticleTweaksConstants.MOD_ID + "." + key);
	}

	@Contract(value = "_ -> new", pure = true)
	public static @NotNull Component tooltip(String key) {
		return Component.translatable("tooltip." + ParticleTweaksConstants.MOD_ID + "." + key);
	}

	@Contract(value = "_ -> new", pure = true)
	public static @NotNull Component enumNameProvider(String key) {
		return Component.translatable("enum." + ParticleTweaksConstants.MOD_ID + "." + key);
	}
}
