package dev.qther.psionic_relics.network;

import dev.qther.psionic_relics.PsionicRelics;
import dev.qther.psionic_relics.network.message.MessageRelicCast;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class MessageRegistry {
    private static final String VERSION = "1";

    public static final SimpleChannel HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(PsionicRelics.MOD_ID, "main"),
            () -> VERSION,
            VERSION::equals,
            VERSION::equals);

    public static void register() {
        HANDLER.messageBuilder(MessageRelicCast.class, 0)
                .encoder((msg, buf) -> {})
                .decoder((msg) -> new MessageRelicCast())
                .consumer(MessageRelicCast::receive).add();
    }
}