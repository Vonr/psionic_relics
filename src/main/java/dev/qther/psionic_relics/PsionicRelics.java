package dev.qther.psionic_relics;

import dev.qther.psionic_relics.item.base.ModItems;
import dev.qther.psionic_relics.network.MessageRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PsionicRelics.MOD_ID)
public class PsionicRelics {
    public static final String MOD_ID = "psionic_relics";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static boolean HAS_CURIOS = false;

    public PsionicRelics() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        ModItems.register();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        HAS_CURIOS = ModList.get().isLoaded("curios");

        MessageRegistry.register();
    }
}
