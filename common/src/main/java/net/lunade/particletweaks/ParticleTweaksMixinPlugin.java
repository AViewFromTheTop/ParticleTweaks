package net.lunade.particletweaks;

import java.util.List;
import java.util.Set;
import net.frozenblock.lib.FrozenLibEarlyConstants;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class ParticleTweaksMixinPlugin implements IMixinConfigPlugin {

	@Override
	public void onLoad(String mixinPackage) {}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (mixinClassName.contains("wilderwild.")) return FrozenLibEarlyConstants.HAS_WILDER_WILD;
		if (mixinClassName.contains("trailiertales.")) return FrozenLibEarlyConstants.HAS_TRAILIER_TALES;
		if (mixinClassName.contains("thecopperierage.")) return FrozenLibEarlyConstants.HAS_THE_COPPERIER_AGE;
		if (mixinClassName.contains("netheriernether.")) return FrozenLibEarlyConstants.HAS_NETHERIER_NETHER;
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
