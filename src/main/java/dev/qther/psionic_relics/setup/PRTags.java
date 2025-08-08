package dev.qther.psionic_relics.setup;

import dev.qther.psionic_relics.PsionicRelics;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class PRTags {
    public static class Items {
        public static final TagKey<Item> PSIONIC_RELIC = TagKey.create(Registries.ITEM, PsionicRelics.prefix("relic"));
        public static final TagKey<Item> PSIONIC_RELIC_CURIOS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("curios", "psionic_relic"));
    }
}
