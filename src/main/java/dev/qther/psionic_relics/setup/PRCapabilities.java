package dev.qther.psionic_relics.setup;

import dev.qther.psionic_relics.item.base.IRelic;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import vazkii.psi.api.PsiAPI;

public class PRCapabilities {
    public static void register(RegisterCapabilitiesEvent event) {
        var relics = new ItemLike[]{
                PRRegistry.Items.BASIC_RELIC.get(),
                PRRegistry.Items.CHARGE_RELIC.get(),
                PRRegistry.Items.CIRCLE_RELIC.get(),
                PRRegistry.Items.GRENADE_RELIC.get(),
                PRRegistry.Items.LOOPCAST_RELIC.get(),
                PRRegistry.Items.MINE_RELIC.get(),
                PRRegistry.Items.PROJECTILE_RELIC.get()
        };

        event.registerItem(PsiAPI.SPELL_ACCEPTOR_CAPABILITY, (stack, ctx) -> ((IRelic) stack.getItem()).getRelicBase(stack), relics);
        event.registerItem(PsiAPI.PSI_BAR_DISPLAY_CAPABILITY, (stack, ctx) -> ((IRelic) stack.getItem()).getRelicBase(stack), relics);
    }
}
