package dev.qther.psi_addon_template;

import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Client {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        // Register Entity Rendering Handlers and Tile Entity Renderers
    }

    @SubscribeEvent
    public static void init(RegistryEvent.Register<Item> event) {
        // Register piece textures
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        // Register models
    }

}