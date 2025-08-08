package dev.qther.psionic_relics.network;

import dev.qther.psionic_relics.PsionicRelics;
import dev.qther.psionic_relics.network.message.AbstractPacket;
import dev.qther.psionic_relics.network.message.PacketCastRelic;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PRNetworking {
    public static PayloadRegistrar INSTANCE;

    public static void register(final RegisterPayloadHandlersEvent event) {
        INSTANCE = event.registrar("1");
        var reg = INSTANCE;

        if (PsionicRelics.HAS_CURIOS) {
            reg.playToServer(PacketCastRelic.TYPE, PacketCastRelic.CODEC, PRNetworking::handle);
        }
    }

    private static <T extends AbstractPacket> void handle(T message, IPayloadContext ctx) {
        if (ctx.flow().getReceptionSide() == LogicalSide.SERVER) {
            handleServer(message, ctx);
        } else {
            ClientMessageHandler.handleClient(message, ctx);
        }
    }

    private static <T extends AbstractPacket> void handleServer(T message, IPayloadContext ctx) {
        MinecraftServer server = ctx.player().getServer();
        message.handleServer(server, (ServerPlayer) ctx.player());
    }

    private static class ClientMessageHandler {

        public static <T extends AbstractPacket> void handleClient(T message, IPayloadContext ctx) {
            Minecraft minecraft = Minecraft.getInstance();
            message.handleClient(minecraft, minecraft.player);
        }
    }

    public static void sendToNearbyClient(Level world, BlockPos pos, AbstractPacket toSend) {
        if (world instanceof ServerLevel ws) {
            ws.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).stream()
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64)
                    .forEach(p -> sendToPlayerClient(toSend, p));
        }
    }

    public static void sendToNearbyClient(Level world, Entity e, AbstractPacket toSend) {
        sendToNearbyClient(world, e.blockPosition(), toSend);
    }

    public static void sendToPlayerClient(AbstractPacket msg, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, msg);
    }

    public static void sendToServer(AbstractPacket msg) {
        PacketDistributor.sendToServer(msg);
    }
}