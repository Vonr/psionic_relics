package dev.qther.psionic_relics.item.base;

import dev.qther.psionic_relics.PsionicRelics;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModTextures extends ItemModelProvider {
    public ModTextures(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, PsionicRelics.MOD_ID, existingFileHelper);
    }

    private ItemModelBuilder item(RegistryObject<Item> item) {
        PsionicRelics.LOGGER.info("Registering item/" + item.getId().toString());
        return singleTexture(item.get().getRegistryName().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/" + item.getId().toString()));
    }

    @Override
    protected void registerModels() {
        item(ModItems.basicRelic);
        item(ModItems.chargeRelic);
        item(ModItems.circleRelic);
        item(ModItems.grenadeRelic);
        item(ModItems.loopcastRelic);
        item(ModItems.mineRelic);
        item(ModItems.projectileRelic);
    }
}
