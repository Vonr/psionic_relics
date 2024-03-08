package dev.qther.psionic_relics.core;

import dev.qther.psionic_relics.PsionicRelics;
import dev.qther.psionic_relics.network.MessageRegistry;
import dev.qther.psionic_relics.network.message.MessageRelicCast;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

@Mod.EventBusSubscriber(modid = PsionicRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CuriosIntegration {
    @SubscribeEvent
    public static void enqueue(InterModEnqueueEvent e) {
        if (!PsionicRelics.HAS_CURIOS) {
            return;
        }
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("psionic_relic").icon(new ResourceLocation("curios:slot/psionic_relic_slot")).size(1).build());
    }

    @OnlyIn(Dist.CLIENT)
    public static class Keybinds {
        public static KeyMapping cast = new KeyMapping("key.psionic_relics.cast", GLFW_KEY_R, "key.categories.psionic_relics");

        @Mod.EventBusSubscriber(modid = PsionicRelics.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
        @OnlyIn(Dist.CLIENT)
        static class Registrar {
            @SubscribeEvent
            public static void keyRegistration(RegisterKeyMappingsEvent e) {
                if (!PsionicRelics.HAS_CURIOS) {
                    return;
                }
                e.register(cast);
            }
        }

        @Mod.EventBusSubscriber(modid = PsionicRelics.MOD_ID, value = Dist.CLIENT)
        @OnlyIn(Dist.CLIENT)
        public static class Handler {
            protected static boolean castWasDown = false;
            protected static int castDebounce = 0;

            @SubscribeEvent
            public static void keyHandler(TickEvent.ClientTickEvent event) {
                if (event.phase != TickEvent.Phase.START || !PsionicRelics.HAS_CURIOS) {
                    return;
                }

                Minecraft mc = Minecraft.getInstance();

                if (mc.player == null || mc.screen != null) {
                    return;
                }

                if (CuriosIntegration.Keybinds.cast.isDown()) {
                    if (castDebounce > 0) {
                        castDebounce--;
                        return;
                    }

                    var relics = CuriosApi.getCuriosHelper().findCurios(mc.player, "psionic_relic");
                    if (relics.size() < 1) {
                        return;
                    }

                    if (!castWasDown) {
                        castDebounce = 2;
                        castWasDown = true;
                    }

                    MessageRegistry.HANDLER.sendToServer(new MessageRelicCast());
                } else {
                    castWasDown = false;
                    castDebounce = 0;
                }
            }
        }
    }
}
