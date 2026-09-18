package net.lunade.particletweaks.config.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.FrozenLibEarlyConstants;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Contract;

@Environment(EnvType.CLIENT)
public final class ModMenuIntegration implements ModMenuApi {

    @Contract(pure = true)
    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
		if (FrozenLibEarlyConstants.HAS_CLOTH_CONFIG) return ParticleTweaksConfigGui::buildScreen;
        return screen -> null;
    }

}
