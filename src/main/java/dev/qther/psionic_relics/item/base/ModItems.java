package dev.qther.psionic_relics.item.base;

import dev.qther.psionic_relics.PsionicRelics;
import dev.qther.psionic_relics.core.PsionicRelicsCreativeTab;
import dev.qther.psionic_relics.item.relic.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = PsionicRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PsionicRelics.MOD_ID);

    public static RegistryObject<Item> basicRelic = ITEMS.register("basic_relic", () -> new BasicRelic(defaultBuilder()));
    public static RegistryObject<Item> chargeRelic = ITEMS.register("charge_relic", () -> new ChargeRelic(defaultBuilder()));
    public static RegistryObject<Item> circleRelic = ITEMS.register("circle_relic", () -> new CircleRelic(defaultBuilder()));
    public static RegistryObject<Item> grenadeRelic = ITEMS.register("grenade_relic", () -> new GrenadeRelic(defaultBuilder()));
    public static RegistryObject<Item> loopcastRelic = ITEMS.register("loopcast_relic", () -> new LoopcastRelic(defaultBuilder()));
    public static RegistryObject<Item> mineRelic = ITEMS.register("mine_relic", () -> new MineRelic(defaultBuilder()));
    public static RegistryObject<Item> projectileRelic = ITEMS.register("projectile_relic", () -> new ProjectileRelic(defaultBuilder()));

    public static Item.Properties defaultBuilder() {
        return new Item.Properties().tab(PsionicRelicsCreativeTab.INSTANCE);
    }

    public static void register() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}