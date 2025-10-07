package net.lunade.particletweaks.config;

import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.instance.json.JsonType;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.frozenblock.lib.config.api.sync.SyncBehavior;
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData;
import net.lunade.particletweaks.ParticleTweaksConstants;

public class ParticleTweaksConfig {

	public static final Config<ParticleTweaksConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ParticleTweaksConstants.MOD_ID,
			ParticleTweaksConfig.class,
			JsonType.JSON5_UNQUOTED_KEYS,
			true
		) {
			@Override
			public void onSave() throws Exception {
				super.onSave();
				this.onSync(null);
			}

			@Override
			public void onSync(ParticleTweaksConfig syncInstance) {
				var config = this.config();
				TRAILER_CAVE_DUST = config.trailerCaveDust;
				TRAILER_TORCHES = config.trailerTorches;
				TRAILER_CAMPFIRES = config.trailerCampfires;
				TRAILER_BUBBLES = config.trailerBubbles;
				TRAILER_WATER_MOVEMENT = config.trailerWaterMovement;
				TRAILER_AMBIENT_WATER = config.trailerAmbientWater;
				TRAILER_SPLASHES = config.trailerSplashes;
				TRAILER_FLOWING_FLUIDS = config.trailerFlowingFluids;
				TRAILER_CASCADES = config.trailerCascades;
				TRAILER_POOF = config.trailerPoof;
				TRAILER_SPELL = config.trailerSpell;
				TRAILER_LEAVES = config.trailerLeaves;
			}
		}
	);

	public static volatile boolean TRAILER_CAVE_DUST = false;
	public static volatile boolean TRAILER_TORCHES = false;
	public static volatile boolean TRAILER_CAMPFIRES = false;
	public static volatile boolean TRAILER_BUBBLES = false;
	public static volatile boolean TRAILER_WATER_MOVEMENT = false;
	public static volatile boolean TRAILER_AMBIENT_WATER = false;
	public static volatile boolean TRAILER_SPLASHES = false;
	public static volatile boolean TRAILER_FLOWING_FLUIDS = false;
	public static volatile boolean TRAILER_CASCADES = false;
	public static volatile boolean TRAILER_POOF = false;
	public static volatile boolean TRAILER_SPELL = false;
	public static volatile boolean TRAILER_LEAVES = false;

	@EntrySyncData(value = "trailerCaveDust", behavior = SyncBehavior.UNSYNCABLE)
	public boolean trailerCaveDust;

	@EntrySyncData(value = "trailerTorches", behavior = SyncBehavior.UNSYNCABLE)
	public boolean trailerTorches;
	@EntrySyncData(value = "trailerCampfires", behavior = SyncBehavior.UNSYNCABLE)
	public boolean trailerCampfires;

	@EntrySyncData(value = "trailerBubbles", behavior = SyncBehavior.UNSYNCABLE)
	public boolean trailerBubbles;
	@EntrySyncData(value = "trailerWaterMovement", behavior = SyncBehavior.UNSYNCABLE)
	public boolean trailerWaterMovement;
	@EntrySyncData(value = "trailerAmbientWater", behavior = SyncBehavior.UNSYNCABLE)
	public boolean trailerAmbientWater;
	@EntrySyncData(value = "trailerSplashes", behavior = SyncBehavior.UNSYNCABLE)
	public boolean trailerSplashes;

	@EntrySyncData(value = "trailerFlowingFluids", behavior = SyncBehavior.UNSYNCABLE)
	public boolean trailerFlowingFluids;
	@EntrySyncData(value = "trailerCascades", behavior = SyncBehavior.UNSYNCABLE)
	public boolean trailerCascades;

	@EntrySyncData(value = "trailerPoof", behavior = SyncBehavior.UNSYNCABLE)
	public boolean trailerPoof;
	@EntrySyncData(value = "trailerSpell", behavior = SyncBehavior.UNSYNCABLE)
	public boolean trailerSpell;

	@EntrySyncData(value = "trailerLeaves", behavior = SyncBehavior.UNSYNCABLE)
	public boolean trailerLeaves;

	public static ParticleTweaksConfig get(boolean real) {
		if (real) return INSTANCE.instance();
		return INSTANCE.config();
	}

	public static ParticleTweaksConfig get() {
		return get(false);
	}

	public static ParticleTweaksConfig getWithSync() {
		return INSTANCE.configWithSync();
	}
}
