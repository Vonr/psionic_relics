package dev.qther.psionic_relics.item.relic;

import dev.qther.psionic_relics.item.base.IRelic;
import dev.qther.psionic_relics.item.base.RelicBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.entity.EntitySpellCircle;
import vazkii.psi.common.item.ItemCircleSpellBullet;
import vazkii.psi.common.item.ItemSpellBullet;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorRaycast;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CircleRelic extends Item implements IRelic {
    public CircleRelic(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new CircleRelicBase(stack);
    }

    @Nonnull
    @Override
    public InteractionResult useOn(@Nonnull UseOnContext ctx) {
        return this.relicUseOn(ctx);
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @Nonnull InteractionHand hand) {
        return this.relicUse(worldIn, playerIn, hand, 0, 0, (ItemSpellBullet) ModItems.circleSpellBullet);
    }

    @Override
    public @Nonnull Component getName(@Nonnull ItemStack stack) {
        return this.getRelicName(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level playerIn, List<Component> tooltip, TooltipFlag advanced) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            tooltip.add(new TranslatableComponent("psimisc.bullet_type", new TranslatableComponent("psi.bullet_type_circle")));
            tooltip.add(new TranslatableComponent("psimisc.bullet_cost", (int) (this.getCostModifier() * 100)));
            tooltip.add(new TextComponent("\u00a7b" + new TranslatableComponent("psi.cadstat.efficiency").getString()).append("\u00a77: \u00a7r100"));
        });
    }

    @Override
    public ItemSpellBullet getBulletType() {
        return (ItemSpellBullet) ModItems.circleSpellBullet;
    }

    public class CircleRelicBase extends RelicBase {
        public CircleRelicBase(ItemStack relic) {
            super(relic);
            this.capOptional = LazyOptional.of(() -> this);
        }

        @Override
        public ArrayList<Entity> castSpell(SpellContext context) {
            var cad = PsiAPI.getPlayerCAD(context.caster);
            var colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
            var pos = PieceOperatorVectorRaycast.raycast(context.caster, 32);
            var spellEntities = new ArrayList<Entity>();
            if(pos != null) {
                var circle = new EntitySpellCircle(EntitySpellCircle.TYPE, context.caster.getCommandSenderWorld());
                var bullet = new ItemStack(ModItems.circleSpellBullet);
                ISpellAcceptor.acceptor(bullet).setSpell(context.caster, ISpellAcceptor.acceptor(this.relic).getSpell());

                circle.setInfo(context.caster, colorizer, bullet);
                circle.setPos(pos.getLocation().x, pos.getLocation().y, pos.getLocation().z);
                circle.getCommandSenderWorld().addFreshEntity(circle);
                spellEntities.add(circle);
            }
            return spellEntities;
        }
    }
}
