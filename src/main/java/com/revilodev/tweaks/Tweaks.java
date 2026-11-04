package com.revilodev.tweaks;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(Tweaks.MODID)
public class Tweaks {
    public static final String MODID = "tweaks";

    public Tweaks(IEventBus modEventBus) {
        modEventBus.addListener(Tweaks::registerPayloads);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeModeTabs.TABS.register(modEventBus);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(ToggleMagnetPayload.TYPE, ToggleMagnetPayload.STREAM_CODEC,
                (payload, context) -> MagnetHandler.toggle(context.player()));
    }

}
