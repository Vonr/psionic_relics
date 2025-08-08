package dev.qther.psionic_relics.datagen;

import dev.qther.psionic_relics.PsionicRelics;
import dev.qther.psionic_relics.item.base.IRelic;
import dev.qther.psionic_relics.setup.PRRegistry;
import dev.qther.psionic_relics.setup.PRTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class PRItemTagProvider extends IntrinsicHolderTagsProvider<Item> {
    public PRItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, Registries.ITEM, future, item -> BuiltInRegistries.ITEM.getResourceKey(item).get(), PsionicRelics.MODID, helper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        //noinspection SuspiciousToArrayCall
        this.tag(PRTags.Items.PSIONIC_RELIC).add(
                PRRegistry.ITEMS.getEntries().stream().map(DeferredHolder::get).filter(i -> i instanceof IRelic).toArray(Item[]::new)
        );

        this.tag(PRTags.Items.PSIONIC_RELIC_CURIOS).addTag(PRTags.Items.PSIONIC_RELIC);
    }
}
