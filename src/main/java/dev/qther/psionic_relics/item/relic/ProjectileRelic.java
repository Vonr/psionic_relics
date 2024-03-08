package dev.qther.psionic_relics.item.relic;

import dev.qther.psionic_relics.item.base.IRelic;
import dev.qther.psionic_relics.item.base.RelicBase;
import net.minecraft.nbt.CompoundTag;
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
import vazkii.psi.common.entity.EntitySpellProjectile;
import vazkii.psi.common.item.ItemSpellBullet;
import vazkii.psi.common.item.base.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ProjectileRelic extends Item implements IRelic {
    public ProjectileRelic(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ProjectileRelicBase(stack);
    }

    @Nonnull
    @Override
    public InteractionResult useOn(@Nonnull UseOnContext ctx) {
        return this.relicUseOn(ctx);
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @Nonnull InteractionHand hand) {
        return this.relicUse(worldIn, playerIn, hand, 0, 0, (ItemSpellBullet) ModItems.projectileSpellBullet);
    }

    @Override
    public @Nonnull Component getName(@Nonnull ItemStack stack) {
        return this.getRelicName(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level playerIn, List<Component> tooltip, TooltipFlag advanced) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            tooltip.add(Component.translatable("psimisc.bullet_type", Component.translatable("psi.bullet_type_projectile")));
            tooltip.add(Component.translatable("psimisc.bullet_cost", (int) (this.getCostModifier() * 100)));
            tooltip.add(Component.literal("\u00a7b" + Component.translatable("psi.cadstat.efficiency").getString()).append("\u00a77: \u00a7r100"));
        });
    }

    @Override
    public ItemSpellBullet getBulletType() {
        return (ItemSpellBullet) ModItems.projectileSpellBullet;
    }

    public class ProjectileRelicBase extends RelicBase {
        public ProjectileRelicBase(ItemStack relic) {
            super(relic);
            this.capOptional = LazyOptional.of(() -> this);
        }

        @Override
        public ArrayList<Entity> castSpell(SpellContext context) {
            var cad = PsiAPI.getPlayerCAD(context.caster);
            var colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
            var projectile = new EntitySpellProjectile(context.caster.getCommandSenderWorld(), context.caster);
            var bullet = new ItemStack(ModItems.projectileSpellBullet);
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
