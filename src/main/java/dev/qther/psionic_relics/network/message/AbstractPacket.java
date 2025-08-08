package dev.qther.psionic_relics.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractPacket implements CustomPacketPayload {
    public void handleClient(@NotNull Minecraft mc, @NotNull LocalPlayer player) {
    }

    public void handleServer(@NotNull MinecraftServer server, @NotNull ServerPlayer player) {
    }
}

