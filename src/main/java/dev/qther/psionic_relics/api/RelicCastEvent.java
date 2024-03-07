package dev.qther.psionic_relics.api;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class RelicCastEvent extends PlayerEvent {
    private static boolean posting = false;

    public RelicCastEvent(Player player) {
        super(player);
    }

    public static void post(RelicCastEvent event) {
        if (!posting) {
            posting = true;
            MinecraftForge.EVENT_BUS.post(event);
            posting = false;
        }
    }
}
