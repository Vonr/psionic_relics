package dev.qther.psionic_relics.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.TreeMap;

public class PRLangProvider extends LanguageProvider {
    private final Map<String, String> data = new TreeMap<>();

    public PRLangProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        add("key.categories.psionic_relics", "Psionic Relics");
        add("itemGroup.psionic_relics", "Psionic Relics");
        add("curios.identifier.psionic_relic", "Psionic Relic");
        add("key.psionic_relics.cast", "Cast Relic");

        add("item.psionic_relics.basic_relic", "Sorcerer's Relic");
        add("item.psionic_relics.charge_relic", "Trickster's Relic");
        add("item.psionic_relics.circle_relic", "Scribe's Relic");
        add("item.psionic_relics.grenade_relic", "Unstable Relic");
        add("item.psionic_relics.loopcast_relic", "Timekeeper's Relic");
        add("item.psionic_relics.mine_relic", "Strategist's Relic");
        add("item.psionic_relics.projectile_relic", "Archer's Relic");
    }

    @Override
    public void add(@NotNull Item key, @NotNull String name) {
        super.add(key, name);
    }

    @Override
    public void add(@NotNull String key, @NotNull String value) {
        super.add(key, value);
        data.put(key, value);
    }

}
