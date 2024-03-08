package dev.qther.psionic_relics.core;

import com.google.common.base.Suppliers;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.qther.psionic_relics.item.base.IRelic;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;


public class RelicLootModifier extends LootModifier {
    public static final Supplier<Codec<RelicLootModifier>> CODEC = Suppliers.memoize(
            () -> RecordCodecBuilder.create(inst -> codecStart(inst)
                    .and(ForgeRegistries.ITEMS.getCodec().fieldOf("relic").forGetter(m -> m.relic))
                    .and(Codec.DOUBLE.fieldOf("chance").forGetter(m -> m.chance))
                    .apply(inst, RelicLootModifier::new)));

    Item relic;
    double chance;

    public RelicLootModifier(LootItemCondition[] cnd, Item relic, double chance) {
        super(cnd);
        this.relic = relic;
        this.chance = chance;
    }

    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> loot, LootContext ctx) {
        if (ctx.getLevel().getRandom().nextDouble() > this.chance) {
            return loot;
        }

        var hasRelic = false;
        for (var stack : loot) {
            if (stack.getItem() instanceof IRelic) {
                hasRelic = true;
                break;
            }
        }

        if (!hasRelic) {
            loot.add(new ItemStack(this.relic));
        }

        return loot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
