package dev.qther.psionic_relics.setup;

import com.mojang.serialization.MapCodec;
import dev.qther.psionic_relics.api.RelicLootModifier;
import dev.qther.psionic_relics.item.base.RelicBase;
import dev.qther.psionic_relics.item.relic.*;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static dev.qther.psionic_relics.PsionicRelics.MODID;

public final class PRRegistry {
    public static final DeferredRegister<DataComponentType<?>> DATA = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);

    public static void register(IEventBus bus) {
        Items.register(bus);
        Data.register(bus);
        Tabs.register(bus);
        GlobalLootModifiers.register(bus);
    }

    public static class Items {
        public static final DeferredHolder<Item, BasicRelic> BASIC_RELIC = ITEMS.register("basic_relic", () -> new BasicRelic(new Item.Properties()));
        public static final DeferredHolder<Item, ChargeRelic> CHARGE_RELIC = ITEMS.register("charge_relic", () -> new ChargeRelic(new Item.Properties()));
        public static final DeferredHolder<Item, CircleRelic> CIRCLE_RELIC = ITEMS.register("circle_relic", () -> new CircleRelic(new Item.Properties()));
        public static final DeferredHolder<Item, GrenadeRelic> GRENADE_RELIC = ITEMS.register("grenade_relic", () -> new GrenadeRelic(new Item.Properties()));
        public static final DeferredHolder<Item, LoopcastRelic> LOOPCAST_RELIC = ITEMS.register("loopcast_relic", () -> new LoopcastRelic(new Item.Properties()));
        public static final DeferredHolder<Item, MineRelic> MINE_RELIC = ITEMS.register("mine_relic", () -> new MineRelic(new Item.Properties()));
        public static final DeferredHolder<Item, ProjectileRelic> PROJECTILE_RELIC = ITEMS.register("projectile_relic", () -> new ProjectileRelic(new Item.Properties()));

        public static void register(IEventBus bus) {
            ITEMS.register(bus);
        }
    }

    public static class Tabs {
        public static DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register("general", () -> CreativeModeTab.builder()
                .title(Component.translatable("key.categories.psionic_relics"))
                .icon(() -> Items.BASIC_RELIC.get().getDefaultInstance())
                .displayItems((params, output) -> {
                    for (var entry : ITEMS.getEntries()) {
                        output.accept(entry.get().getDefaultInstance());
                    }
                })
                .build());

        public static void register(IEventBus bus) {
            TABS.register(bus);
        }
    }

    public static class Data {
        public static final DeferredHolder<DataComponentType<?>, DataComponentType<RelicBase.RelicData>> RELIC = DATA.register("remote_data",
                () -> DataComponentType.<RelicBase.RelicData>builder().persistent(RelicBase.RelicData.CODEC).networkSynchronized(RelicBase.RelicData.STREAM_CODEC).build()
        );

        public static void register(IEventBus bus) {
            DATA.register(bus);
        }
    }

    public static class GlobalLootModifiers {
        public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<RelicLootModifier>> RELIC =
                LOOT_MODIFIER_SERIALIZERS.register("relic", () -> RelicLootModifier.CODEC);

        public static void register(IEventBus bus) {
            LOOT_MODIFIER_SERIALIZERS.register(bus);
        }
    }
}
