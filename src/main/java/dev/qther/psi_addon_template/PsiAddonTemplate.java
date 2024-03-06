package dev.qther.psi_addon_template;

import net.minecraftforge.fml.common.Mod;

import java.util.logging.LogManager;
import java.util.logging.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PsiAddonTemplate.modId)
public class PsiAddonTemplate {
    public static final String modId = "psi_addon_template";
    public static final Logger LOGGER = LogManager.getLogManager().getLogger(modId);
}
