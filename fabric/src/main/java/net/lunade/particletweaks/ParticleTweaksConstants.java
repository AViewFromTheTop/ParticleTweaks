package net.lunade.particletweaks;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

public class ParticleTweaksConstants {
	public static final String PROJECT_ID = ParticleTweaksPreLoadConstants.PROJECT_ID;
	public static final String MOD_ID = ParticleTweaksPreLoadConstants.MOD_ID;
	public static final Logger LOGGER = ParticleTweaksPreLoadConstants.LOGGER;

	public static final boolean MAKE_BUBBLES_POP_MOD = FabricLoader.getInstance().isModLoaded("make_bubbles_pop");

	/**
	 * Used for features that may be unstable and crash in public builds.
	 * <p>
	 * It's smart to use this for at least registries.
	 */
	public static boolean UNSTABLE_LOGGING = FabricLoader.getInstance().isDevelopmentEnvironment();

	// LOGGING
	public static void log(String message, boolean shouldLog) {
		if (shouldLog) LOGGER.info(message);
	}

	public static void logWithModId(String message, boolean shouldLog) {
		if (shouldLog) LOGGER.info(message + " " + MOD_ID);
	}

	public static void warn(String message, boolean shouldLog) {
		if (shouldLog) LOGGER.warn(message);
	}

	public static void error(String message, boolean shouldLog) {
		if (shouldLog) LOGGER.error(message);
	}

	public static void printStackTrace(String message, boolean shouldPrint) {
		if (shouldPrint) LOGGER.error(message, new Throwable(message).fillInStackTrace());
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static Identifier vanillaId(String path) {
		return Identifier.withDefaultNamespace(path);
	}

	public static String string(String path) {
		return id(path).toString();
	}

	public static String safeString(String path) {
		return MOD_ID + "_" + path;
	}

	/**
	 * @return A text component for use in a Config GUI
	 */
	public static Component text(String key) {
		return Component.translatable("option." + MOD_ID + "." + key);
	}

	/**
	 * @return A tooltip component for use in a Config GUI
	 */
	public static Component tooltip(String key) {
		return Component.translatable("tooltip." + MOD_ID + "." + key);
	}
}
