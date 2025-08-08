package dev.qther.psionic_relics.network.message;

import dev.qther.psionic_relics.PsionicRelics;
import dev.qther.psionic_relics.api.RelicCastEvent;
import dev.qther.psionic_relics.item.base.IRelic;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;

public class PacketCastRelic extends AbstractPacket {
    public static final PacketCastRelic INSTANCE = new PacketCastRelic();
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketCastRelic> CODEC = StreamCodec.unit(INSTANCE);
    public static final Type<PacketCastRelic> TYPE = new Type<>(PsionicRelics.prefix("clear_remote"));

    private PacketCastRelic() {}

    @Override
    public void handleServer(@NotNull MinecraftServer server, @NotNull ServerPlayer player) {
        var e = new RelicCastEvent(player);
        NeoForge.EVENT_BUS.post(e);
        if (e.isCanceled()) {
            return;
        }

        var maybeInv = CuriosApi.getCuriosInventory(player);
        if (maybeInv.isEmpty()) {
            return;
        }

        var relics = maybeInv.get().findCurios("psionic_relic");
        if (relics.isEmpty()) {
            return;
        }

        var relic = relics.getFirst().stack();
        if (relic.getItem() instanceof IRelic relicType) {
            relicType.cast(relic, player, InteractionHand.MAIN_HAND, 0, 0, relicType.getBulletType());
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}