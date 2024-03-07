package dev.qther.psionic_relics.item.base;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class RelicBase implements ICapabilityProvider, IPsiBarDisplay, ISpellAcceptor {
    protected ItemStack relic;
    protected LazyOptional<?> capOptional;

    public RelicBase(ItemStack relic) {
        this.relic = relic;
        this.capOptional = LazyOptional.of(() -> this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PsiAPI.PSI_BAR_DISPLAY_CAPABILITY
                || cap == PsiAPI.SPELL_ACCEPTOR_CAPABILITY) {
            return capOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public boolean shouldShow(IPlayerData data) {
        return false;
    }

    @Override
    public void setSpell(Player player, Spell spell) {
        if (this.containsSpell()) {
            return;
        }

        var name = IRelic.TAG_SPELL;
        var cmp = new CompoundTag();

        spell.writeToNBT(cmp);

        relic.getOrCreateTag().put(name, cmp);
    }

    @Nullable
    @Override
    public Spell getSpell() {
        var name = IRelic.TAG_SPELL;
        var cmp = relic.getOrCreateTag().getCompound(name);

        if (cmp.isEmpty()) {
            return null;
        }

        return Spell.createFromNBT(cmp);
    }

    @Override
    public boolean containsSpell() {
        var name = IRelic.TAG_SPELL;
        var cmp = relic.getOrCreateTag().getCompound(name);

        return !cmp.isEmpty();
    }

    @Override
    public ArrayList<Entity> castSpell(SpellContext context) {
        context.cspell.safeExecute(context);
        return new ArrayList<>();
    }

    @Override
    public boolean castableFromSocket() {
        return false;
    }
}
