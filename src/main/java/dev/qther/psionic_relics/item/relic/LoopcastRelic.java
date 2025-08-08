package dev.qther.psionic_relics.item.relic;

import dev.qther.psionic_relics.item.base.IRelic;
import dev.qther.psionic_relics.item.base.RelicBase;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.LoopcastTrackingHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemSpellBullet;
import vazkii.psi.common.item.base.ModItems;

import java.util.ArrayList;
import java.util.List;

public class LoopcastRelic extends Item implements IRelic {
    public LoopcastRelic(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public @NotNull RelicBase getRelicBase(ItemStack stack) {
        return new LoopcastRelicBase(stack);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext ctx) {
        return this.relicUseOn(ctx);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, @NotNull Player playerIn, @NotNull InteractionHand hand) {
        return this.relicUse(worldIn, playerIn, hand, 0, 0, (ItemSpellBullet) ModItems.loopSpellBullet);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return this.getRelicName(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        TooltipHelper.tooltipIfShift(tooltipComponents, () -> {
            tooltipComponents.add(Component.translatable("psimisc.bullet_type", Component.translatable("psi.loopcasting")));
            tooltipComponents.add(Component.translatable("psimisc.bullet_cost", (int) (this.getCostModifier() * 100)));
            tooltipComponents.add(Component.literal("\u00a7b" + Component.translatable("psi.cadstat.efficiency").getString()).append("\u00a77: \u00a7r100"));
        });
    }

    @Override
    public ItemSpellBullet getBulletType() {
        return (ItemSpellBullet) ModItems.loopSpellBullet;
    }

    public static class LoopcastRelicBase extends RelicBase {
        public LoopcastRelicBase(ItemStack relic) {
            super(relic);
        }

        @Override
        public ArrayList<Entity> castSpell(SpellContext context) {
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(context.caster);
            if (!data.loopcasting || context.castFrom != data.loopcastHand) {
                data.loopcasting = true;
                data.loopcastHand = context.castFrom;
                data.lastTickLoopcastStack = null;
                data.loopcastTime = 1;
                data.loopcastAmount = 0;
                context.cspell.safeExecute(context);
                if (context.caster instanceof ServerPlayer) {
                    LoopcastTrackingHandler.syncForTrackersAndSelf((ServerPlayer) context.caster);
                }
            }
            return new ArrayList<>();
        }
    }
}
