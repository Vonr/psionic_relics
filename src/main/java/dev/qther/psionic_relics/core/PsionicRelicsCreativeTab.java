package dev.qther.psionic_relics.core;

import dev.qther.psionic_relics.PsionicRelics;
import dev.qther.psionic_relics.item.base.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class PsionicRelicsCreativeTab extends CreativeModeTab {
    public static final PsionicRelicsCreativeTab INSTANCE = new PsionicRelicsCreativeTab();

    public PsionicRelicsCreativeTab() {
        super(PsionicRelics.MOD_ID);
    }

    @Nonnull
    public ItemStack makeIcon() {
        return new ItemStack(ModItems.basicRelic.get());
    }

    public boolean hasSearchBar() {
        return true;
    }
}
