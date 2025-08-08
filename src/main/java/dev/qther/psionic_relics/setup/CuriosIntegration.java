package dev.qther.psionic_relics.setup;

import dev.qther.psionic_relics.PsionicRelics;
import dev.qther.psionic_relics.network.PRNetworking;
import dev.qther.psionic_relics.network.message.PacketCastRelic;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import top.theillusivec4.curios.api.CuriosApi;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

public class CuriosIntegration {
    public static class Keybinds {
        public static KeyMapping cast = new KeyMapping("key.psionic_relics.cast", GLFW_KEY_R, "key.categories.psionic_relics");

        public static class Registrar {
            public static void keyRegistration(RegisterKeyMappingsEvent e) {
                if (!PsionicRelics.HAS_CURIOS) {
                    return;
                }
                e.register(cast);
            }
        }

        public static class Handler {
            protected static boolean castWasDown = false;
            protected static int castDebounce = 0;

            public static void keyHandler(ClientTickEvent.Pre event) {
                if (!PsionicRelics.HAS_CURIOS) {
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

                    var maybeInv = CuriosApi.getCuriosInventory(mc.player);
                    if (maybeInv.isEmpty()) {
                        return;
                    }

                    var relics = maybeInv.get().findCurios("psionic_relic");
                    if (relics.isEmpty()) {
                        return;
                    }

                    if (!castWasDown) {
                        castDebounce = 2;
                        castWasDown = true;
                    }

                    PRNetworking.sendToServer(PacketCastRelic.INSTANCE);
                } else {
                    castWasDown = false;
                    castDebounce = 0;
                }
            }
        }
    }
}
