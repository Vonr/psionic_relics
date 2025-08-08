package dev.qther.psionic_relics.datagen;

import dev.qther.psionic_relics.PsionicRelics;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static dev.qther.psionic_relics.PsionicRelics.MODID;

public class PRDatagen {
    public static void gatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        var output = event.getGenerator().getPackOutput();
        var provider = event.getLookupProvider();
        var fileHelper = event.getExistingFileHelper();

        gen.addProvider(event.includeClient(), new PRLangProvider(output, MODID, "en_us"));
        gen.addProvider(event.includeClient(), new PRItemModelProvider(output, MODID, fileHelper));

        gen.addProvider(event.includeServer(), new PRGlobalLootModifierProvider(output, provider, MODID));
        gen.addProvider(event.includeServer(), new PRItemTagProvider(output, provider, fileHelper));
        if (PsionicRelics.HAS_CURIOS) {
            gen.addProvider(event.includeServer(), new PRCuriosDataProvider(MODID, output, fileHelper, provider));
        }
    }

}
