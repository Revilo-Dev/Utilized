package com.revilodev.tweaks;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

public class PaxelItem extends PickaxeItem {
    public PaxelItem(Tier tier, Item.Properties properties) {
        super(tier, properties.attributes(PickaxeItem.createAttributes(tier, 2.0F, -2.8F)));
    }
}
