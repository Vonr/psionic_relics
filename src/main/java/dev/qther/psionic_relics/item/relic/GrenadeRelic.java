package dev.qther.psionic_relics.item.relic;

import dev.qther.psionic_relics.item.base.IRelic;
import dev.qther.psionic_relics.item.base.RelicBase;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.entity.EntitySpellGrenade;
import vazkii.psi.common.item.ItemSpellBullet;
import vazkii.psi.common.item.base.ModItems;

import java.util.ArrayList;
import java.util.List;

public class GrenadeRelic extends Item implements IRelic {
    public GrenadeRelic(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public @NotNull RelicBase getRelicBase(ItemStack stack) {
        return new GrenadeRelicBase(stack);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext ctx) {
        return this.relicUseOn(ctx);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, @NotNull Player playerIn, @NotNull InteractionHand hand) {
        return this.relicUse(worldIn, playerIn, hand, 0, 0, ModItems.grenadeSpellBullet.get());
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return this.getRelicName(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        TooltipHelper.tooltipIfShift(tooltipComponents, () -> {
            tooltipComponents.add(Component.translatable("psimisc.bullet_type", Component.translatable("psi.bullet_type_grenade")));
            tooltipComponents.add(Component.translatable("psimisc.bullet_cost", (int) (this.getCostModifier() * 100)));
            tooltipComponents.add(Component.literal("\u00a7b" + Component.translatable("psi.cadstat.efficiency").getString()).append("\u00a77: \u00a7r100"));
        });
    }

    @Override
    public ItemSpellBullet getBulletType() {
        return ModItems.grenadeSpellBullet.get();
    }

    public static class GrenadeRelicBase extends RelicBase {
        public GrenadeRelicBase(ItemStack relic) {
            super(relic);
        }

        @Override
        public ArrayList<Entity> castSpell(SpellContext context) {
            var cad = PsiAPI.getPlayerCAD(context.caster);
            var colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);

            var projectile = new EntitySpellGrenade(context.caster.getCommandSenderWorld(), context.caster);
            var bullet = new ItemStack(ModItems.grenadeSpellBullet);
            ISpellAcceptor.acceptor(bullet).setSpell(context.caster, ISpellAcceptor.acceptor(this.relic).getSpell());
            projectile.setInfo(context.caster, colorizer, bullet);
            projectile.context = context;
            projectile.getCommandSenderWorld().addFreshEntity(projectile);
            var spellEntities = new ArrayList<Entity>();
            spellEntities.add(projectile);
            return spellEntities;
        }
    }
}
