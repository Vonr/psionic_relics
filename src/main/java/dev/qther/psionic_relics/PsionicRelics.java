package dev.qther.psionic_relics;

import dev.qther.psionic_relics.item.base.ModItems;
import net.minecraftforge.fml.common.Mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PsionicRelics.MOD_ID)
public class PsionicRelics {
    public static final String MOD_ID = "psionic_relics";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public PsionicRelics() {
        ModItems.register();
    }
}
