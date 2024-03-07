package dev.qther.psionic_relics.item.base;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.internal.VanillaPacketDispatcher;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.item.ItemSpellBullet;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static vazkii.psi.common.item.ItemCAD.isTruePlayer;

public interface IRelic {
    String TAG_SPELL = "spell";

    @Nonnull
    default InteractionResult relicUseOn(@Nonnull UseOnContext ctx) {
        var playerIn = ctx.getPlayer();
        var hand = ctx.getHand();
        var worldIn = ctx.getLevel();
        var pos = ctx.getClickedPos();
        var stack = playerIn.getItemInHand(hand);
        var tile = worldIn.getBlockEntity(pos);

        if (!(tile instanceof TileProgrammer programmer)) {
            return InteractionResult.PASS;
        }

        var acceptor = ISpellAcceptor.acceptor(stack);
        var spell = ISpellAcceptor.acceptor(stack).getSpell();
        if (!acceptor.containsSpell()) {
            return InteractionResult.PASS;
        }

        var enabled = programmer.isEnabled();
        if (enabled && !programmer.playerLock.isEmpty()) {
            if (!programmer.playerLock.equals(playerIn.getName().getString())) {
                if (!worldIn.isClientSide) {
                    playerIn.sendMessage(new TranslatableComponent("psimisc.not_your_programmer").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), UUID.randomUUID());
                }
                return InteractionResult.FAIL;
            }
        } else {
            programmer.playerLock = playerIn.getName().getString();
        }

        programmer.spell = spell;
        programmer.onSpellChanged();
        if (!worldIn.isClientSide) {
            worldIn.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, PsiSoundHandler.bulletCreate, SoundSource.PLAYERS, 0.5F, 1F);
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(programmer);
        }

        return InteractionResult.SUCCESS;
    }

    @Nonnull
    default InteractionResultHolder<ItemStack> relicUse(Level worldIn, Player playerIn, @Nonnull InteractionHand hand, int reservoir, int cd, ItemSpellBullet bullet) {
        var stack = playerIn.getItemInHand(hand);
        var did = cast(stack, playerIn, hand, reservoir, cd, bullet).isPresent();

        return new InteractionResultHolder<>(did ? InteractionResult.CONSUME : InteractionResult.PASS, stack);
    }

    @Nonnull
    default Component getRelicName(@Nonnull ItemStack stack) {
        var spell = ISpellAcceptor.acceptor(stack).getSpell();
        if (spell == null) {
            return new TranslatableComponent(stack.getDescriptionId());
        }

        return new TextComponent("\u00a7b" + spell.name).append("\u00a7r (" + new TranslatableComponent(stack.getDescriptionId()).getString() + "\u00a7r)");
    }

    default int getRealCost(ItemStack bullet, int cost) {
        return (int) ((double) cost * ((ItemSpellBullet) bullet.getItem()).getCostModifier(bullet));
    }

    default Optional<ArrayList<Entity>> cast(ItemStack stack, Player player, InteractionHand hand, int reservoir, int cd, ItemSpellBullet bulletType) {
        var data = PlayerDataHandler.get(player);
        var cad = PsiAPI.getPlayerCAD(player);

        if (cad.isEmpty()) {
            return Optional.empty();
        }

        var world = player.getCommandSenderWorld();
        Consumer<SpellContext> predicate = (SpellContext ctx) -> ctx.castFrom = hand;

        if (data.overflowed || data.getAvailablePsi() <= 0 || cad.isEmpty() || !ISpellAcceptor.hasSpell(stack) || !isTruePlayer(player)) {
            return Optional.empty();
        }

        var acceptor = ISpellAcceptor.acceptor(stack);
        var spell = acceptor.getSpell();
        var ctx = new SpellContext().setPlayer(player).setSpell(spell);
        predicate.accept(ctx);

        if (!ctx.isValid()) {
            return Optional.empty();
        }

        if (!ctx.cspell.metadata.evaluateAgainst(cad)) {
            if (!world.isClientSide) {
                player.sendMessage((new TranslatableComponent("psimisc.weak_cad")).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), Util.NIL_UUID);
            }

            return Optional.empty();
        }

        var bullet = new ItemStack(bulletType);
        ISpellAcceptor.acceptor(bullet).setSpell(ctx.caster, spell);
        var cost = Math.max(getRealCost(bullet, ctx.cspell.metadata.getStat(EnumSpellStat.COST)) - reservoir, 0);

        var particles = 10;
        var sound = 0.05F;
        var event = new PreSpellCastEvent(cost, sound, particles, cd, spell, ctx, player, data, cad, bullet);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            var cancelMessage = event.getCancellationMessage();
            if (cancelMessage != null && !cancelMessage.isEmpty()) {
                player.sendMessage((new TranslatableComponent(cancelMessage)).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), Util.NIL_UUID);
            }

            return Optional.empty();
        }

        cd = event.getCooldown();
        particles = event.getParticles();
        sound = event.getSound();
        cost = event.getCost();
        spell = event.getSpell();
        ctx = event.getContext();
        if (cost > 0) {
            data.deductPsi(cost, cd, true);
        }

        if (cost != 0 && sound > 0.0F) {
            if (!world.isClientSide) {
                world.playSound(null, player.getX(), player.getY(), player.getZ(), PsiSoundHandler.cadShoot, SoundSource.PLAYERS, sound, (float) (0.5 + Math.random() * 0.5));
            } else {
                var color = Psi.proxy.getColorForCAD(cad);
                var r = (float) PsiRenderHelper.r(color) / 255.0F;
                var g = (float) PsiRenderHelper.g(color) / 255.0F;
                var b = (float) PsiRenderHelper.b(color) / 255.0F;

                for (var i = 0; i < particles; ++i) {
                    var x = player.getX() + (Math.random() - 0.5) * 2.1 * (double) player.getBbWidth();
                    var y = player.getY() - player.getMyRidingOffset();
                    var z = player.getZ() + (Math.random() - 0.5) * 2.1 * (double) player.getBbWidth();
                    var grav = -0.15F - (float) Math.random() * 0.03F;
                    Psi.proxy.sparkleFX(x, y, z, r, g, b, grav, 0.25F, 15);
                }

                var x = player.getX();
                var y = player.getY() + (double) player.getEyeHeight() - 0.1;
                var z = player.getZ();
                var lookOrig = new Vector3(player.getLookAngle());

                for (int i = 0; i < 25; ++i) {
                    var look = lookOrig.copy();
                    var spread = 0.25;
                    look.x += (Math.random() - 0.5) * spread;
                    look.y += (Math.random() - 0.5) * spread;
                    look.z += (Math.random() - 0.5) * spread;
                    look.normalize().multiply(0.15);
                    Psi.proxy.sparkleFX(x, y, z, r, g, b, (float) look.x, (float) look.y, (float) look.z, 0.3F, 5);
                }
            }
        }

        var spellEntities = new ArrayList<Entity>();
        if (!world.isClientSide) {
            spellEntities = acceptor.castSpell(ctx);
        }

        MinecraftForge.EVENT_BUS.post(new SpellCastEvent(spell, ctx, player, data, cad, bullet));
        return Optional.of(spellEntities);
    }

}
