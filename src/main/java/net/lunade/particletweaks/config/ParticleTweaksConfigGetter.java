package net.lunade.particletweaks.config;

import net.lunade.particletweaks.ParticleTweaksConstants;
import net.lunade.particletweaks.config.cloth.ParticleTweaksConfig;

public class ParticleTweaksConfigGetter {

	public static boolean trailerCaveDust() {
		return ParticleTweaksConstants.CLOTH_CONFIG && ParticleTweaksConfig.get().config.trailerCaveDust;
	}

	public static boolean trailerTorches() {
		return ParticleTweaksConstants.CLOTH_CONFIG && ParticleTweaksConfig.get().config.trailerTorches;
	}

	public static boolean trailerCampfires() {
		return ParticleTweaksConstants.CLOTH_CONFIG && ParticleTweaksConfig.get().config.trailerCampfires;
	}

	public static boolean trailerBubbles() {
		return ParticleTweaksConstants.CLOTH_CONFIG && ParticleTweaksConfig.get().config.trailerBubbles;
	}

	public static boolean trailerWaterMovement() {
		return ParticleTweaksConstants.CLOTH_CONFIG && ParticleTweaksConfig.get().config.trailerWaterMovement;
	}

	public static boolean trailerAmbientWater() {
		return ParticleTweaksConstants.CLOTH_CONFIG && ParticleTweaksConfig.get().config.trailerAmbientWater;
	}

	public static boolean trailerSplashes() {
		return ParticleTweaksConstants.CLOTH_CONFIG && ParticleTweaksConfig.get().config.trailerSplashes;
	}

	public static boolean trailerCascades() {
		return ParticleTweaksConstants.CLOTH_CONFIG && ParticleTweaksConfig.get().config.trailerCascades;
	}

	public static boolean trailerFlowingFluids() {
		return ParticleTweaksConstants.CLOTH_CONFIG && ParticleTweaksConfig.get().config.trailerFlowingFluids;
	}

	public static boolean trailerPoof() {
		return ParticleTweaksConstants.CLOTH_CONFIG && ParticleTweaksConfig.get().config.trailerPoof;
	}

	public static boolean trailerSpell() {
		return ParticleTweaksConstants.CLOTH_CONFIG && ParticleTweaksConfig.get().config.trailerSpell;
	}

	public static boolean trailerLeaves() {
		return ParticleTweaksConstants.CLOTH_CONFIG && ParticleTweaksConfig.get().config.trailerLeaves;
	}
}
