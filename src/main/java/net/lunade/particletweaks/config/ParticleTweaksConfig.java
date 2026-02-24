package net.lunade.particletweaks.config;

import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.instance.json.JsonType;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.frozenblock.lib.config.api.sync.SyncBehavior;
import net.lunade.particletweaks.ParticleTweaksPreLoadConstants;

// UNSYNCABLE
public final class ParticleTweaksConfig {
	public static final Config<ParticleTweaksConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ParticleTweaksPreLoadConstants.MOD_ID,
			ParticleTweaksConfig.class,
			JsonType.JSON5_UNQUOTED_KEYS,
			true
		) {
			@Override
			public void onSave() throws Exception {
				super.onSave();
				this.onSync(null);
			}

			// doesn't actually sync
			private void onSync(ParticleTweaksConfig syncInstance) {
				var config = this.config();
				TRAILER_CAVE_DUST = config.trailerCaveDust;
				TRAILER_TORCHES = config.trailerTorches;
				TRAILER_CAMPFIRES = config.trailerCampfires;
				TRAILER_BUBBLES = config.trailerBubbles;
				TRAILER_WATER_MOVEMENT = config.trailerWaterMovement;
				TRAILER_AMBIENT_WATER = config.trailerAmbientWater;
				TRAILER_WAVES = config.trailerWaves;
				TRAILER_SPLASHES = config.trailerSplashes;
				TRAILER_RIPPLES = config.trailerRipples;
				TRAILER_FLOWING_FLUIDS = config.trailerFlowingFluids;
				TRAILER_CASCADES = config.trailerCascades;
				TRAILER_POOF = config.trailerPoof;
				TRAILER_BUBBLE_POOF = config.trailerBubblePoof;
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
	public static volatile boolean TRAILER_WAVES = false;
	public static volatile boolean TRAILER_SPLASHES = false;
	public static volatile boolean TRAILER_RIPPLES = false;
	public static volatile boolean TRAILER_FLOWING_FLUIDS = false;
	public static volatile boolean TRAILER_CASCADES = false;
	public static volatile boolean TRAILER_POOF = false;
	public static volatile boolean TRAILER_BUBBLE_POOF = false;
	public static volatile boolean TRAILER_SPELL = false;
	public static volatile boolean TRAILER_LEAVES = false;

	public boolean trailerCaveDust;

	public boolean trailerTorches;
	public boolean trailerCampfires;

	public boolean trailerBubbles;
	public boolean trailerWaterMovement;
	public boolean trailerAmbientWater;
	public boolean trailerWaves;
	public boolean trailerSplashes;
	public boolean trailerRipples;

	public boolean trailerFlowingFluids;
	public boolean trailerCascades;

	public boolean trailerPoof;
	public boolean trailerBubblePoof;
	public boolean trailerSpell;

	public boolean trailerLeaves;

	public static ParticleTweaksConfig get(boolean real) {
		if (real) return INSTANCE.instance();
		return INSTANCE.config();
	}

	public static ParticleTweaksConfig get() {
		return get(false);
	}
}
