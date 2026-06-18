package net.lunade.particletweaks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
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
import net.minecraft.client.particle.BubblePopParticle;

@Environment(EnvType.CLIENT)
public class ParticleTweaksClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientChunkEvents.CHUNK_UNLOAD.register((level, chunk) -> {
			TrailerFluidParticleSpawner.clearCascadesInChunk(chunk.getPos());
			TrailerTorchParticleSpawner.clearTorchesInChunk(chunk.getPos());
		});
		ClientLifecycleEvents.CLIENT_STOPPING.register((minecraft) -> {
			TrailerFluidParticleSpawner.clearCascades();
			TrailerTorchParticleSpawner.clearTorches();
		});
		ClientPlayConnectionEvents.DISCONNECT.register((handler, minecraft) -> {
			TrailerFluidParticleSpawner.clearCascades();
			TrailerTorchParticleSpawner.clearTorches();
		});

		ClientTickEvents.START_LEVEL_TICK.register((level) -> {
			TrailerFluidParticleSpawner.tickCascades(level);
			TrailerTorchParticleSpawner.tickTorches(level);
			TrailerCaveDustSpawner.tick(level);
		});

		ParticleTweaksParticleTypes.init();

		final ParticleProviderRegistry particleRegistry = ParticleProviderRegistry.getInstance();
		particleRegistry.register(ParticleTweaksParticleTypes.FLOWING_LAVA, FluidFlowParticle.LavaProvider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.FLOWING_WATER, FluidFlowParticle.WaterProvider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.SMALL_BUBBLE, SmallBubbleParticle.Provider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.SMALL_CASCADE, FluidFlowParticle.SmallCascadeProvider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.CASCADE_A, FluidFlowParticle.CascadeProvider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.CASCADE_B, FluidFlowParticle.CascadeProvider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.SPLASH, FluidFlowParticle.SplashProvider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.RIPPLE, RippleParticle.Provider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.WAVE_OUTLINE, WaveParticle.OutlineProvider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.WAVE, WaveParticle.Provider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.WAVE_SEED, WaveSeedParticle.Provider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.CAVE_DUST, CaveDustParticle.Factory::new);
		particleRegistry.register(ParticleTweaksParticleTypes.POOF, PoofParticle.Provider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.FLARE, FlareParticle.Provider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.SOUL_FLARE, FlareParticle.SoulProvider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.COPPER_FLARE, FlareParticle.CopperProvider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.CAMPFIRE_FLARE, CampfireFlareParticle.Provider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.SOUL_CAMPFIRE_FLARE, CampfireFlareParticle.SoulProvider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.COPPER_CAMPFIRE_FLARE, CampfireFlareParticle.CopperProvider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.COMFY_SMOKE_A, ComfySmokeParticle.Provider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.COMFY_SMOKE_B, ComfySmokeParticle.Provider::new);
		particleRegistry.register(ParticleTweaksParticleTypes.SULFUR_BUBBLE_POP, BubblePopParticle.Provider::new);

		ParticleTweaksConfig.CONFIG.load(true);
	}
}
