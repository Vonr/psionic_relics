package dev.qther.psionic_relics.core;

import com.google.gson.JsonObject;
import dev.qther.psionic_relics.item.base.IRelic;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RelicLootModifier extends LootModifier {
    Item relic;
    double chance;

    public RelicLootModifier(LootItemCondition[] cnd, Item relic, double chance) {
        super(cnd);
        this.relic = relic;
        this.chance = chance;
    }

    @NotNull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> loot, LootContext ctx) {
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

    public static class Serializer extends GlobalLootModifierSerializer<RelicLootModifier> {

        @Override
        public RelicLootModifier read(ResourceLocation loc, JsonObject obj, LootItemCondition[] cnd) {
            Item relic = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(obj, "relic")));
            double chance = GsonHelper.getAsDouble(obj, "chance");
            return new RelicLootModifier(cnd, relic, chance);
        }

        @Override
        public JsonObject write(RelicLootModifier mod) {
            JsonObject json = makeConditions(mod.conditions);
            json.addProperty("relic", ForgeRegistries.ITEMS.getKey(mod.relic).toString());
            json.addProperty("chance", mod.chance);
            return json;
        }
    }
}
