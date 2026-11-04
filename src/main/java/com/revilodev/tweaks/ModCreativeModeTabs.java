package com.revilodev.tweaks;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Tweaks.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TWEAKS_TAB = TABS.register("tweaks_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.tweaks"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.IRON_MAGNET.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.WOODEN_HAMMER.get());
                output.accept(ModItems.STONE_HAMMER.get());
                output.accept(ModItems.IRON_HAMMER.get());
                output.accept(ModItems.GOLDEN_HAMMER.get());
                output.accept(ModItems.DIAMOND_HAMMER.get());
                output.accept(ModItems.NETHERITE_HAMMER.get());
                output.accept(ModItems.IRON_MAGNET.get());
                output.accept(ModItems.GOLDEN_MAGNET.get());
                output.accept(ModItems.DIAMOND_MAGNET.get());
                output.accept(ModItems.NETHERITE_MAGNET.get());
            }).build());

    private ModCreativeModeTabs() {
    }
}
