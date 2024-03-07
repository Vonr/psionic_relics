package dev.qther.psionic_relics.core;

import dev.qther.psionic_relics.PsionicRelics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = PsionicRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LootEventBus {
    @SubscribeEvent
    public static void registerModifierSerializers(@Nonnull RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().register(new RelicLootModifier.Serializer().setRegistryName(new ResourceLocation(PsionicRelics.MOD_ID, "relic")));
    }
}
