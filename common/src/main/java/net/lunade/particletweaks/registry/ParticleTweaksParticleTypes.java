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

package net.lunade.particletweaks.registry;

import net.frozenblock.lib.platform.api.registry.DeferredRegister;
import net.frozenblock.lib.platform.api.registry.DeferredSimpleParticleType;
import net.lunade.particletweaks.ParticleTweaksConstants;
import net.mehvahdjukaar.candlelight.api.ClientOnly;

@ClientOnly
public final class ParticleTweaksParticleTypes {
	private static final DeferredRegister.ParticleTypes REGISTER = DeferredRegister.createParticleTypes(ParticleTweaksConstants.MOD_ID);

	public static final DeferredSimpleParticleType FLOWING_LAVA = REGISTER.register("flowing_lava");
	public static final DeferredSimpleParticleType FLOWING_WATER = REGISTER.register("flowing_water");
	public static final DeferredSimpleParticleType SMALL_BUBBLE = REGISTER.register("small_bubble");
	public static final DeferredSimpleParticleType SMALL_CASCADE = REGISTER.register("small_cascade");
	public static final DeferredSimpleParticleType CASCADE_A = REGISTER.register("cascade_a");
	public static final DeferredSimpleParticleType CASCADE_B = REGISTER.register("cascade_b");
	public static final DeferredSimpleParticleType SPLASH = REGISTER.register("splash");
	public static final DeferredSimpleParticleType RIPPLE = REGISTER.register("ripple");
	public static final DeferredSimpleParticleType WAVE_OUTLINE = REGISTER.register("wave_outline");
	public static final DeferredSimpleParticleType WAVE = REGISTER.register("wave");
	public static final DeferredSimpleParticleType WAVE_SEED = REGISTER.register("wave_seed");
	public static final DeferredSimpleParticleType CAVE_DUST = REGISTER.register("cave_dust");
	public static final DeferredSimpleParticleType POOF = REGISTER.register("poof");
	public static final DeferredSimpleParticleType FLARE = REGISTER.register("flare");
	public static final DeferredSimpleParticleType SOUL_FLARE = REGISTER.register("soul_flare");
	public static final DeferredSimpleParticleType COPPER_FLARE = REGISTER.register("copper_flare");
	public static final DeferredSimpleParticleType CAMPFIRE_FLARE = REGISTER.register("campfire_flare");
	public static final DeferredSimpleParticleType SOUL_CAMPFIRE_FLARE = REGISTER.register("soul_campfire_flare");
	public static final DeferredSimpleParticleType COPPER_CAMPFIRE_FLARE = REGISTER.register("copper_campfire_flare");
	public static final DeferredSimpleParticleType COMFY_SMOKE_A = REGISTER.register("comfy_smoke_a");
	public static final DeferredSimpleParticleType COMFY_SMOKE_B = REGISTER.register("comfy_smoke_b");
	public static final DeferredSimpleParticleType SULFUR_BUBBLE_POP = REGISTER.register("sulfur_bubble_pop");

	static {
		REGISTER.register();
	}

	public static void init() {}

	private ParticleTweaksParticleTypes() {}
}
