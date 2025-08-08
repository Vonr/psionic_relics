package dev.qther.psionic_relics.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class PRCuriosDataProvider extends CuriosDataProvider {
    public PRCuriosDataProvider(String modId, PackOutput output, ExistingFileHelper fileHelper,
                                CompletableFuture<HolderLookup.Provider> registries) {
        super(modId, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
        this.createSlot("psionic_relic")
                .icon(ResourceLocation.fromNamespaceAndPath("curios", "slot/psionic_relic_slot"))
                .size(1);

        this.createEntities("psionic_relic_entities")
                .replace(false)
                .addPlayer()
                .addSlots("psionic_relic");
    }
}
