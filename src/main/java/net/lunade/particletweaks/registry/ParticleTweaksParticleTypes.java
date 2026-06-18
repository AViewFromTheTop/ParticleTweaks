package net.lunade.particletweaks.registry;

import com.mojang.serialization.MapCodec;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.lunade.particletweaks.ParticleTweaksConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class ParticleTweaksParticleTypes {
	public static final SimpleParticleType FLOWING_LAVA = register("flowing_lava");
	public static final SimpleParticleType FLOWING_WATER = register("flowing_water");
	public static final SimpleParticleType SMALL_BUBBLE = register("small_bubble");
	public static final SimpleParticleType SMALL_CASCADE = register("small_cascade");
	public static final SimpleParticleType CASCADE_A = register("cascade_a");
	public static final SimpleParticleType CASCADE_B = register("cascade_b");
	public static final SimpleParticleType SPLASH = register("splash");
	public static final SimpleParticleType RIPPLE = register("ripple");
	public static final SimpleParticleType WAVE_OUTLINE = register("wave_outline");
	public static final SimpleParticleType WAVE = register("wave");
	public static final SimpleParticleType WAVE_SEED = register("wave_seed");
	public static final SimpleParticleType CAVE_DUST = register("cave_dust");
	public static final SimpleParticleType POOF = register("poof");
	public static final SimpleParticleType FLARE = register("flare");
	public static final SimpleParticleType SOUL_FLARE = register("soul_flare");
	public static final SimpleParticleType COPPER_FLARE = register("copper_flare");
	public static final SimpleParticleType CAMPFIRE_FLARE = register("campfire_flare");
	public static final SimpleParticleType SOUL_CAMPFIRE_FLARE = register("soul_campfire_flare");
	public static final SimpleParticleType COPPER_CAMPFIRE_FLARE = register("copper_campfire_flare");
	public static final SimpleParticleType COMFY_SMOKE_A = register("comfy_smoke_a");
	public static final SimpleParticleType COMFY_SMOKE_B = register("comfy_smoke_b");
	public static final SimpleParticleType SULFUR_BUBBLE_POP = register("sulfur_bubble_pop");

	public static void init() {}

	private static SimpleParticleType register(String name, boolean alwaysShow) {
		return Registry.register(BuiltInRegistries.PARTICLE_TYPE, ParticleTweaksConstants.id(name), FabricParticleTypes.simple(alwaysShow));
	}

	private static SimpleParticleType register(String name) {
		return register(name, false);
	}

	private static <T extends ParticleOptions> ParticleType<T> register(
		String name,
		boolean alwaysShow,
		Function<ParticleType<T>, MapCodec<T>> codec,
		Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamodec
	) {
		return register(ParticleTweaksConstants.id(name), alwaysShow, codec, streamodec);
	}

	@NotNull
	private static <T extends ParticleOptions> ParticleType<T> register(
		Identifier id,
		boolean alwaysShow,
		Function<ParticleType<T>, MapCodec<T>> codec,
		Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodec
	) {
		return Registry.register(BuiltInRegistries.PARTICLE_TYPE, id, new ParticleType<T>(alwaysShow) {
			@Override
			public MapCodec<T> codec() {
				return codec.apply(this);
			}

			@Override
			public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
				return streamCodec.apply(this);
			}
		});
	}
}
