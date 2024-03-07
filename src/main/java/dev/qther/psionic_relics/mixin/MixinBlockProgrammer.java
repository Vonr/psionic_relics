package dev.qther.psionic_relics.mixin;

import dev.qther.psionic_relics.item.base.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.core.handler.PsiSoundHandler;

@Mixin(BlockProgrammer.class)
public class MixinBlockProgrammer {
    @Inject(
            method = "setSpell(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/InteractionResult;",
            remap = false,
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void checkSetSpell(Level worldIn, BlockPos pos, Player playerIn, ItemStack heldItem, CallbackInfoReturnable<InteractionResult> cir) {
        if (heldItem.getItem().getClass().getPackageName().equals("dev.qther.psionic_relics.item.relic") && ISpellAcceptor.acceptor(heldItem).containsSpell()) {
            if (!worldIn.isClientSide) {
                worldIn.playSound((Player) null, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, PsiSoundHandler.compileError, SoundSource.BLOCKS, 0.5F, 1.0F);
            }
            cir.setReturnValue(InteractionResult.FAIL);
            cir.cancel();
        }
    }
}
