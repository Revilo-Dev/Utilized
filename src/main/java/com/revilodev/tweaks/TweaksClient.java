package com.revilodev.tweaks;

import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@Mod(value = Tweaks.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Tweaks.MODID, value = Dist.CLIENT)
public class TweaksClient {
    private static final KeyMapping TOGGLE_MAGNET = new KeyMapping(
            "key.tweaks.toggle_magnet", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F6, "key.categories.tweaks");

    public TweaksClient(ModContainer container) {
    }

    @SubscribeEvent
    static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_MAGNET);
    }

    @SubscribeEvent
    static void onClientTick(ClientTickEvent.Post event) {
        while (TOGGLE_MAGNET.consumeClick()) {
            PacketDistributor.sendToServer(ToggleMagnetPayload.INSTANCE);
        }
    }
}
