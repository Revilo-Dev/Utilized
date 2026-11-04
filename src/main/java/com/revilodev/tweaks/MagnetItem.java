package com.revilodev.tweaks;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.CustomData;

public class MagnetItem extends Item {
    private static final String ROOT = Tweaks.MODID;
    private static final String RANGE_UPGRADES = "magnet_range_upgrades";
    private static final String SPEED_UPGRADES = "magnet_speed_upgrades";

    private final ChatFormatting nameColor;
    private final int bonusRange;
    private final int pullSpeed;
    private final int runeSlots;

    public MagnetItem(ChatFormatting nameColor, int bonusRange, int pullSpeed, int runeSlots, Properties properties) {
        super(properties.stacksTo(1));
        this.nameColor = nameColor;
        this.bonusRange = bonusRange;
        this.pullSpeed = pullSpeed;
        this.runeSlots = runeSlots;
    }

    public ChatFormatting nameColor() {
        return nameColor;
    }

    public int bonusRange() {
        return bonusRange;
    }

    public int bonusRange(ItemStack stack) {
        return bonusRange + upgradeValue(stack, RANGE_UPGRADES);
    }

    public int pullSpeed() {
        return pullSpeed;
    }

    public int pullSpeed(ItemStack stack) {
        return pullSpeed + upgradeValue(stack, SPEED_UPGRADES);
    }

    public int runeSlots() {
        return runeSlots;
    }

    public double attractionRange(ItemStack stack) {
        return 3.0D + bonusRange(stack);
    }

    public double attractionForce(ItemStack stack) {
        return 0.025D + (pullSpeed(stack) * 0.03D);
    }

    @Override
    public Component getName(ItemStack stack) {
        return super.getName(stack).copy().withStyle(nameColor);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!stack.has(DataComponents.CUSTOM_DATA)) {
            CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.getCompound(ROOT));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("+" + (bonusRange(stack) + 2) + " block magnet range").withStyle(ChatFormatting.AQUA));
        if (pullSpeed(stack) > 0) {
            tooltip.add(Component.literal("+" + pullSpeed(stack) + " pull speed").withStyle(ChatFormatting.GREEN));
        }
    }

    public static void upgradeMagnet(ItemStack stack) {
        if (!(stack.getItem() instanceof MagnetItem)) {
            return;
        }
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            CompoundTag root = tag.getCompound(ROOT);
            root.putInt(RANGE_UPGRADES, Math.max(0, root.getInt(RANGE_UPGRADES)) + 1);
            root.putInt(SPEED_UPGRADES, Math.max(0, root.getInt(SPEED_UPGRADES)) + 1);
            tag.put(ROOT, root);
        });
    }

    private static int upgradeValue(ItemStack stack, String key) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            return 0;
        }
        CompoundTag root = data.copyTag().getCompound(ROOT);
        return Math.max(0, root.getInt(key));
    }
}
