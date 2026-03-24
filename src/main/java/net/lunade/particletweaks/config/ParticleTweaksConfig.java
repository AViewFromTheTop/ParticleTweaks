package net.lunade.particletweaks.config;

import net.frozenblock.lib.config.v2.config.ConfigData;
import net.frozenblock.lib.config.v2.config.ConfigSettings;
import net.frozenblock.lib.config.v2.entry.ConfigEntry;
import net.frozenblock.lib.config.v2.entry.EntryType;
import net.frozenblock.lib.config.v2.registry.ID;
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonElement;
import net.lunade.particletweaks.ParticleTweaksConstants;

public final class ParticleTweaksConfig {
	public static final ConfigData<JsonElement> CONFIG = ConfigData.createAndRegister(
		ID.of(ParticleTweaksConstants.MOD_ID, "config"),
		ConfigSettings.JSON5_UNQUOTED_KEYS
	);

	public static final ConfigEntry<Boolean> TRAILER_CAVE_DUST = makeEntry("trailerCaveDust");
	public static final ConfigEntry<Boolean> TRAILER_TORCHES = makeEntry("trailerTorches");
	public static final ConfigEntry<Boolean> TRAILER_CAMPFIRES = makeEntry("trailerCampfires");
	public static final ConfigEntry<Boolean> TRAILER_BUBBLES = makeEntry("trailerBubbles");
	public static final ConfigEntry<Boolean> TRAILER_WATER_MOVEMENT = makeEntry("trailerWaterMovement");
	public static final ConfigEntry<Boolean> TRAILER_AMBIENT_WATER = makeEntry("trailerAmbientWater");
	public static final ConfigEntry<Boolean> TRAILER_WAVES = makeEntry("trailerWaves");
	public static final ConfigEntry<Boolean> TRAILER_SPLASHES = makeEntry("trailerSplashes");
	public static final ConfigEntry<Boolean> TRAILER_RIPPLES = makeEntry("trailerRipples");
	public static final ConfigEntry<Boolean> TRAILER_FLOWING_FLUIDS = makeEntry("trailerFlowingFluids");
	public static final ConfigEntry<Boolean> TRAILER_CASCADES = makeEntry("trailerCascades");
	public static final ConfigEntry<Boolean> TRAILER_POOF = makeEntry("trailerPoof");
	public static final ConfigEntry<Boolean> TRAILER_BUBBLE_POOF = makeEntry("trailerBubblePoof");
	public static final ConfigEntry<Boolean> TRAILER_SPELL = makeEntry("trailerSpell");
	public static final ConfigEntry<Boolean> TRAILER_LEAVES = makeEntry("trailerLeaves");

	private static ConfigEntry<Boolean> makeEntry(String id) {
		return CONFIG.unsyncableEntryBuilder(id, EntryType.BOOL, false)
			.textSupplier(value -> ParticleTweaksConstants.text((boolean) value ? "true" : "false"))
			.build();
	}
}
