package net.lunade.particletweaks;

import net.frozenblock.lib.event.api.events.client.ClientChunkLifecycleEvents;
import net.frozenblock.lib.event.api.events.client.ClientConnectionEvents;
import net.frozenblock.lib.event.api.events.client.ClientLifecycleEvents;
import net.frozenblock.lib.event.api.events.client.ClientTickEvents;
import net.frozenblock.lib.particle.client.api.ParticleProviderRegistry;
import net.lunade.particletweaks.config.ParticleTweaksConfig;
import net.lunade.particletweaks.particle.CampfireFlareParticle;
import net.lunade.particletweaks.particle.CaveDustParticle;
import net.lunade.particletweaks.particle.ComfySmokeParticle;
import net.lunade.particletweaks.particle.FlareParticle;
import net.lunade.particletweaks.particle.FluidFlowParticle;
import net.lunade.particletweaks.particle.PoofParticle;
import net.lunade.particletweaks.particle.RippleParticle;
import net.lunade.particletweaks.particle.SmallBubbleParticle;
import net.lunade.particletweaks.particle.WaveParticle;
import net.lunade.particletweaks.particle.WaveSeedParticle;
import net.lunade.particletweaks.registry.ParticleTweaksParticleTypes;
import net.lunade.particletweaks.trailer.api.TrailerCaveDustSpawner;
import net.lunade.particletweaks.trailer.api.TrailerFluidParticleSpawner;
import net.lunade.particletweaks.trailer.api.TrailerTorchParticleSpawner;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.particle.BubblePopParticle;

@ClientOnly
public final class ParticleTweaksClient {

	public static void init() {
		ClientChunkLifecycleEvents.CHUNK_UNLOAD.register((level, chunk) -> {
			TrailerFluidParticleSpawner.clearCascadesInChunk(chunk.getPos());
			TrailerTorchParticleSpawner.clearTorchesInChunk(chunk.getPos());
		});
		ClientLifecycleEvents.CLIENT_STOPPING.register((minecraft) -> {
			TrailerFluidParticleSpawner.clearCascades();
			TrailerTorchParticleSpawner.clearTorches();
		});
		ClientConnectionEvents.DISCONNECT.register((handler, minecraft) -> {
			TrailerFluidParticleSpawner.clearCascades();
			TrailerTorchParticleSpawner.clearTorches();
		});

		ClientTickEvents.START_LEVEL_TICK.register((level) -> {
			TrailerFluidParticleSpawner.tickCascades(level);
			TrailerTorchParticleSpawner.tickTorches(level);
			TrailerCaveDustSpawner.tick(level);
		});

		ParticleTweaksParticleTypes.init();

		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.FLOWING_LAVA, FluidFlowParticle.LavaProvider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.FLOWING_WATER, FluidFlowParticle.WaterProvider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.SMALL_BUBBLE, SmallBubbleParticle.Provider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.SMALL_CASCADE, FluidFlowParticle.SmallCascadeProvider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.CASCADE_A, FluidFlowParticle.CascadeProvider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.CASCADE_B, FluidFlowParticle.CascadeProvider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.SPLASH, FluidFlowParticle.SplashProvider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.RIPPLE, RippleParticle.Provider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.WAVE_OUTLINE, WaveParticle.OutlineProvider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.WAVE, WaveParticle.Provider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.WAVE_SEED, WaveSeedParticle.Provider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.CAVE_DUST, CaveDustParticle.Factory::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.POOF, PoofParticle.Provider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.FLARE, FlareParticle.Provider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.SOUL_FLARE, FlareParticle.SoulProvider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.COPPER_FLARE, FlareParticle.CopperProvider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.CAMPFIRE_FLARE, CampfireFlareParticle.Provider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.SOUL_CAMPFIRE_FLARE, CampfireFlareParticle.SoulProvider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.COPPER_CAMPFIRE_FLARE, CampfireFlareParticle.CopperProvider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.COMFY_SMOKE_A, ComfySmokeParticle.Provider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.COMFY_SMOKE_B, ComfySmokeParticle.Provider::new);
		ParticleProviderRegistry.register(ParticleTweaksParticleTypes.SULFUR_BUBBLE_POP, BubblePopParticle.Provider::new);

		ParticleTweaksConfig.CONFIG.load(true);
	}

	private ParticleTweaksClient() {}
}
