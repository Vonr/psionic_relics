package dev.qther.psionic_relics.item.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.qther.psionic_relics.setup.PRRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

import java.util.ArrayList;

public class RelicBase implements ICapabilityProvider<Object, Void, Object>, IPsiBarDisplay, ISpellAcceptor {
    protected ItemStack relic;

    public RelicBase(ItemStack relic) {
        this.relic = relic;
    }

    @Override
    public boolean shouldShow(IPlayerData data) {
        return true;
    }

    @Override
    public void setSpell(Player player, Spell spell) {
        if (this.containsSpell()) {
            return;
        }

        relic.set(PRRegistry.Data.RELIC, new RelicData(spell));
    }

    @Nullable
    @Override
    public Spell getSpell() {
        var cmp = relic.get(PRRegistry.Data.RELIC);
        if (cmp == null) {
            return null;
        }

        return cmp.spell();
    }

    @Override
    public boolean containsSpell() {
        return relic.has(PRRegistry.Data.RELIC);
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

    @Override
    public @Nullable Object getCapability(@NotNull Object cap, Void context) {
        if (cap == PsiAPI.SPELL_ACCEPTOR_CAPABILITY || cap == PsiAPI.PSI_BAR_DISPLAY_CAPABILITY) {
            return this;
        }

        return null;
    }

    public record RelicData(Spell spell) {
        public static final Codec<RelicData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Spell.CODEC.fieldOf("spell").forGetter(RelicData::spell)
        ).apply(instance, RelicData::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, RelicData> STREAM_CODEC = StreamCodec.composite(
                Spell.STREAM_CODEC, RelicData::spell,
                RelicData::new
        );
    }
}
