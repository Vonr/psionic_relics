package dev.qther.psionic_relics.core;

import com.mojang.serialization.Codec;
import dev.qther.psionic_relics.PsionicRelics;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, PsionicRelics.MOD_ID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> RELIC =
            LOOT_MODIFIER_SERIALIZERS.register("relic", RelicLootModifier.CODEC);

    public static void register(IEventBus bus) {
        LOOT_MODIFIER_SERIALIZERS.register(bus);
    }
}
