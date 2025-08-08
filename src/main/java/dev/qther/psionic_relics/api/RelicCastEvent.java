package dev.qther.psionic_relics.api;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class RelicCastEvent extends PlayerEvent implements ICancellableEvent {
    public RelicCastEvent(Player player) {
        super(player);
    }
}
