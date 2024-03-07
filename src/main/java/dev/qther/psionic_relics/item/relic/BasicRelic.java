package dev.qther.psionic_relics.item.relic;

import dev.qther.psionic_relics.item.base.RelicBase;
import dev.qther.psionic_relics.item.base.IRelic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.common.item.ItemSpellBullet;
import vazkii.psi.common.item.base.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BasicRelic extends Item implements IRelic {
    public BasicRelic(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new RelicBase(stack);
    }

    @Nonnull
    @Override
    public InteractionResult useOn(@Nonnull UseOnContext ctx) {
        return this.relicUseOn(ctx);
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @Nonnull InteractionHand hand) {
        return this.relicUse(worldIn, playerIn, hand, 0, 0, this.getBulletType());
    }

    @Override
    public @Nonnull Component getName(@Nonnull ItemStack stack) {
        return this.getRelicName(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level playerIn, List<Component> tooltip, TooltipFlag advanced) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            tooltip.add(new TranslatableComponent("psimisc.bullet_type", new TranslatableComponent("psi.bullet_type_basic")));
            tooltip.add(new TranslatableComponent("psimisc.bullet_cost", 100));
            tooltip.add(new TextComponent("\u00a7b" + new TranslatableComponent("psi.cadstat.efficiency").getString()).append("\u00a77: \u00a7r100"));
        });
    }

    @Override
    public ItemSpellBullet getBulletType() {
        return (ItemSpellBullet) ModItems.spellBullet;
    }
}
