package com.revilodev.tweaks;

import java.util.Comparator;
import java.util.List;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Tweaks.MODID)
public final class MagnetHandler {
    private static final String ENABLED_KEY = Tweaks.MODID + ":magnet_enabled";

    private MagnetHandler() {}

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || !isEnabled(player)) {
            return;
        }
        ItemStack magnet = strongest(player.getMainHandItem(), player.getOffhandItem());
        if (magnet.isEmpty()) {
            return;
        }
        MagnetItem item = (MagnetItem) magnet.getItem();
        pullItems(player, item, magnet);
    }

    public static boolean isEnabled(Player player) {
        return !player.getPersistentData().contains(ENABLED_KEY) || player.getPersistentData().getBoolean(ENABLED_KEY);
    }

    public static void toggle(Player player) {
        boolean enabled = !isEnabled(player);
        player.getPersistentData().putBoolean(ENABLED_KEY, enabled);
        player.displayClientMessage(net.minecraft.network.chat.Component.translatable(
                enabled ? "message.tweaks.magnet_enabled" : "message.tweaks.magnet_disabled")
                .withStyle(enabled ? net.minecraft.ChatFormatting.GREEN : net.minecraft.ChatFormatting.RED), true);
    }

    private static ItemStack strongest(ItemStack first, ItemStack second) {
        if (!(first.getItem() instanceof MagnetItem firstMagnet)) {
            return second.getItem() instanceof MagnetItem ? second : ItemStack.EMPTY;
        }
        if (!(second.getItem() instanceof MagnetItem secondMagnet)) {
            return first;
        }
        int rangeCompare = Integer.compare(firstMagnet.bonusRange(first), secondMagnet.bonusRange(second));
        if (rangeCompare != 0) {
            return rangeCompare > 0 ? first : second;
        }
        return firstMagnet.pullSpeed(first) >= secondMagnet.pullSpeed(second) ? first : second;
    }

    private static void pullItems(ServerPlayer player, MagnetItem magnet, ItemStack stack) {
        double range = magnet.attractionRange(stack);
        double force = magnet.attractionForce(stack);
        AABB area = player.getBoundingBox().inflate(range);
        Vec3 target = player.position().add(0.0D, player.getEyeHeight() * 0.4D, 0.0D);
        List<ItemEntity> items = player.level().getEntitiesOfClass(ItemEntity.class, area, Entity -> Entity.isAlive());
        for (ItemEntity item : items) {
            Vec3 direction = target.subtract(item.position());
            double distance = direction.length();
            if (distance <= 0.001D || distance > range) {
                continue;
            }
            Vec3 pullDirection = direction.scale(1.0D / distance);
            double normalizedDistance = Math.min(distance / range, 1.0D);
            double pullStrength = force * (0.8D + ((1.0D - normalizedDistance) * 1.6D));
            double maxSpeed = 0.18D + (force * 1.8D);
            Vec3 desiredVelocity = pullDirection.scale(Math.min(pullStrength, maxSpeed));
            Vec3 velocity = item.getDeltaMovement().scale(0.45D).add(desiredVelocity.scale(0.55D));
            if (distance <= 0.85D) {
                velocity = velocity.scale(0.6D);
            }
            if (velocity.lengthSqr() > maxSpeed * maxSpeed) {
                velocity = velocity.normalize().scale(maxSpeed);
            }
            item.setDeltaMovement(velocity);
            item.hasImpulse = true;
            item.hurtMarked = true;
        }
    }
}
