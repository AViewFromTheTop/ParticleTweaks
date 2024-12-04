package net.lunade.particletweaks.config.cloth;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lunade.particletweaks.ParticleTweaksConstants;
import org.jetbrains.annotations.NotNull;

@Config(name = "config")
public class ParticleTweaksParticleConfig implements ConfigData {

	public boolean trailerCaveDust;

	public boolean trailerTorches;
	public boolean trailerCampfires;

	public boolean trailerBubbles;
	public boolean trailerWaterMovement;
	public boolean trailerAmbientWater;
	public boolean trailerSplashes;

	public boolean trailerFlowingFluids;
	public boolean trailerCascades;

	public boolean trailerPoof;
	public boolean trailerSpell;

	public boolean trailerLeaves;

	@Environment(EnvType.CLIENT)
	static void setupEntries(@NotNull ConfigCategory category, @NotNull ConfigEntryBuilder entryBuilder) {
		var config = ParticleTweaksConfig.get().config;

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
}
