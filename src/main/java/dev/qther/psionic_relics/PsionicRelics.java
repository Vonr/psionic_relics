package dev.qther.psionic_relics;

import dev.qther.psionic_relics.datagen.PRDatagen;
import dev.qther.psionic_relics.network.PRNetworking;
import dev.qther.psionic_relics.setup.CuriosIntegration;
import dev.qther.psionic_relics.setup.PRCapabilities;
import dev.qther.psionic_relics.setup.PRRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(PsionicRelics.MODID)
public class PsionicRelics {
    public static final String MODID = "psionic_relics";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static boolean HAS_CURIOS = false;

    public PsionicRelics(IEventBus bus, ModContainer container) {
        HAS_CURIOS = ModList.get().isLoaded("curios");
        if (HAS_CURIOS) {
            if (FMLEnvironment.dist.isClient()) {
                bus.addListener(CuriosIntegration.Keybinds.Registrar::keyRegistration);
                NeoForge.EVENT_BUS.addListener(CuriosIntegration.Keybinds.Handler::keyHandler);
            }
        }

        bus.addListener(PRDatagen::gatherData);
        bus.addListener(PRCapabilities::register);
        bus.addListener(PRNetworking::register);
        PRRegistry.register(bus);
    }

    public static ResourceLocation prefix(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
