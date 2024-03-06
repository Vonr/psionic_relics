package dev.qther.psi_addon_template.spell;

import dev.qther.psi_addon_template.PsiAddonTemplate;
import dev.qther.psi_addon_template.annotations.PieceMetadata;
import dev.qther.psi_addon_template.spell.trick.PieceTrickTest;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellPiece;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModPieces {
    public enum Group {
        Example("example");

        public final String id;

        Group(String id) {
            this.id = id;
        }
    }

    @SubscribeEvent
    public static void init(RegistryEvent.Register<Item> event) {
        List<Class<? extends SpellPiece>> pieces = Arrays.asList(PieceTrickTest.class);
        for (Class<? extends SpellPiece> piece : pieces) {
            PieceMetadata pm = piece.getAnnotation(PieceMetadata.class);
            if (pm == null) {
                PsiAddonTemplate.LOGGER.log(Level.WARNING, "SpellPiece " + piece.getCanonicalName() + " does not implement PieceMetadata annotation.");
                continue;
            }

            PsiAPI.registerSpellPieceAndTexture(new ResourceLocation(PsiAddonTemplate.modId, pm.id()), piece);
            PsiAPI.addPieceToGroup(piece, new ResourceLocation(PsiAddonTemplate.modId, pm.group().id), pm.main());
        }
    }
}
