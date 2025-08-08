package dev.qther.psionic_relics.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.qther.psionic_relics.item.base.IRelic;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class RelicLootModifier extends LootModifier {
    public static final MapCodec<RelicLootModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LootItemCondition.TYPED_CODEC.listOf().fieldOf("conditions").forGetter((RelicLootModifier m) -> Arrays.stream(m.conditions).toList()),
            ResourceKey.codec(Registries.ITEM).fieldOf("relic").forGetter((RelicLootModifier m) -> m.relic.builtInRegistryHolder().key()),
            Codec.DOUBLE.fieldOf("chance").forGetter((RelicLootModifier m) -> m.chance)
    ).apply(instance, (conditions, relic, chance) -> new RelicLootModifier(conditions.toArray(new LootItemCondition[]{}), BuiltInRegistries.ITEM.get(relic), chance)));

    public final Item relic;
    public final double chance;

    public RelicLootModifier(LootItemCondition[] cnd, Item relic, double chance) {
        super(cnd);
        this.relic = relic;
        this.chance = chance;
    }

    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> loot, LootContext ctx) {
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
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
