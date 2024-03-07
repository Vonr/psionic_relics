package dev.qther.psionic_relics.network.message;

import dev.qther.psionic_relics.PsionicRelics;
import dev.qther.psionic_relics.api.RelicCastEvent;
import dev.qther.psionic_relics.item.base.IRelic;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Supplier;

public class MessageRelicCast {
    public boolean receive(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var e = new RelicCastEvent(ctx.get().getSender());
            ctx.get().enqueueWork(() -> RelicCastEvent.post(e));

            var player = ctx.get().getSender();

            if (e.isCanceled() || player == null || !PsionicRelics.HAS_CURIOS) {
                return;
            }

            var relics = CuriosApi.getCuriosHelper().findCurios(player, "psionic_relic");
            if (relics.size() < 1) {
                return;
            }

            var relic = relics.get(0).stack();
            var relicType = ((IRelic) relic.getItem());
            relicType.cast(relic, player, InteractionHand.MAIN_HAND, 0, 0, relicType.getBulletType());
        });

        return true;
    }
}