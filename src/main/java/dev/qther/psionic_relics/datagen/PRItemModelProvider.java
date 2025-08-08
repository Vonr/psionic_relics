package dev.qther.psionic_relics.datagen;

import dev.qther.psionic_relics.setup.PRRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class PRItemModelProvider extends ItemModelProvider {
    public PRItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        PRRegistry.ITEMS.getEntries().stream().map(DeferredHolder::get).forEach(this::basicItem);
    }
}
