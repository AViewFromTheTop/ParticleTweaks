package net.lunade.particletweaks.config.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.frozenblock.lib.FrozenBools;
import net.mehvahdjukaar.candlelight.api.ClientOnly;
import net.minecraft.client.gui.screens.Screen;

@ClientOnly
public final class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
		if (FrozenBools.HAS_CLOTH_CONFIG) return ParticleTweaksConfigGui::buildScreen;
        return screen -> null;
    }
}
