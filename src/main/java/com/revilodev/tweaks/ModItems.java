package com.revilodev.tweaks;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Tweaks.MODID);

    public static final DeferredItem<HammerItem> WOODEN_HAMMER = registerHammer("wooden_hammer", Tiers.WOOD);
    public static final DeferredItem<HammerItem> STONE_HAMMER = registerHammer("stone_hammer", Tiers.STONE);
    public static final DeferredItem<HammerItem> IRON_HAMMER = registerHammer("iron_hammer", Tiers.IRON);
    public static final DeferredItem<HammerItem> GOLDEN_HAMMER = registerHammer("golden_hammer", Tiers.GOLD);
    public static final DeferredItem<HammerItem> DIAMOND_HAMMER = registerHammer("diamond_hammer", Tiers.DIAMOND);
    public static final DeferredItem<HammerItem> NETHERITE_HAMMER = ITEMS.register("netherite_hammer",
            () -> new HammerItem(Tiers.NETHERITE, new Item.Properties().fireResistant()));

    public static final DeferredItem<MagnetItem> IRON_MAGNET = registerMagnet("iron_magnet", ChatFormatting.WHITE, 1, 5, 2, null);
    public static final DeferredItem<MagnetItem> GOLDEN_MAGNET = registerMagnet("golden_magnet", ChatFormatting.GREEN, 2, 7, 2, Rarity.UNCOMMON);
    public static final DeferredItem<MagnetItem> DIAMOND_MAGNET = registerMagnet("diamond_magnet", ChatFormatting.AQUA, 3, 9, 2, Rarity.RARE);
    public static final DeferredItem<MagnetItem> NETHERITE_MAGNET = registerMagnet("netherite_magnet", ChatFormatting.LIGHT_PURPLE, 4, 11, 2, Rarity.EPIC);

    private ModItems() {
    }

    private static DeferredItem<HammerItem> registerHammer(String name, net.minecraft.world.item.Tier tier) {
        return ITEMS.register(name, () -> new HammerItem(tier, new Item.Properties()));
    }

    private static DeferredItem<MagnetItem> registerMagnet(String name, ChatFormatting color, int range, int speed,
                                                            int runeSlots, Rarity rarity) {
        return ITEMS.register(name, () -> {
            Item.Properties properties = new Item.Properties();
            if (rarity != null) {
                properties = properties.rarity(rarity);
            }
            return new MagnetItem(color, range, speed, runeSlots, properties);
        });
    }
}
