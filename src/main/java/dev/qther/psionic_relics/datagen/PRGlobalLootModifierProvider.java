package dev.qther.psionic_relics.datagen;

import dev.qther.psionic_relics.api.RelicLootModifier;
import dev.qther.psionic_relics.setup.PRRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.concurrent.CompletableFuture;

public class PRGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public PRGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modid) {
        super(output, registries, modid);
    }

    @Override
    protected void start() {
        relic(PRRegistry.Items.BASIC_RELIC, 0.1, "minecraft:chests/abandoned_mineshaft", "minecraft:chests/shipwreck_treasure");
        relic(PRRegistry.Items.BASIC_RELIC, 0.33, "minecraft:chests/buried_treasure");
        relic(PRRegistry.Items.GRENADE_RELIC, 0.1, "minecraft:chests/bastion_other");
        relic(PRRegistry.Items.GRENADE_RELIC, 0.33, "minecraft:chests/bastion_treasure");
        relic(PRRegistry.Items.MINE_RELIC, 0.33, "minecraft:chests/desert_pyramid", "minecraft:chests/jungle_temple");
        relic(PRRegistry.Items.PROJECTILE_RELIC, 0.25, "psionic_relics:chests/woodland_mansion");
        relic(PRRegistry.Items.PROJECTILE_RELIC, 0.33, "minecraft:chests/pillager_outpost");
        relic(PRRegistry.Items.CHARGE_RELIC, 0.1, "minecraft:chests/simple_dungeon");
        relic(PRRegistry.Items.CIRCLE_RELIC, 0.33, "minecraft:chests/stronghold_library");
    }

    private void relic(DeferredHolder<Item, ? extends Item> relic, double chance, String... tableLocs) {
        var relicItem = relic.get();
        for (var tableLoc : tableLocs) {
            this.add(BuiltInRegistries.ITEM.wrapAsHolder(relicItem).getRegisteredName().replaceAll(".*:", "relics/") + "/" + tableLoc.replaceFirst(":", "/"), new RelicLootModifier(new LootItemCondition[]{tableId(tableLoc).build()}, relicItem, chance));
        }
    }

    private LootItemCondition.Builder tableId(String loc) {
        return new LootTableIdCondition.Builder(ResourceLocation.parse(loc));
    }
}
