package dev.qther.psi_addon_template.spell.trick;

import dev.qther.psi_addon_template.annotations.PieceMetadata;
import dev.qther.psi_addon_template.spell.ModPieces;
import net.minecraft.network.chat.TextComponent;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceTrick;

@PieceMetadata(id = "trick_test", group = ModPieces.Group.Example, main = true)
public class PieceTrickTest extends PieceTrick {
    public PieceTrickTest(Spell spell) {
        super(spell);
    }

    @Override
    @SuppressWarnings("deprecation")
    public Object execute(SpellContext context) throws SpellRuntimeException {
        context.caster.sendMessage(new TextComponent("Test"), context.caster.getUUID());
        return null;
    }
}
