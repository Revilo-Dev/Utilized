package com.example.tweaks;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

// A pickaxe that mines a 3x3 area on the face being mined
@EventBusSubscriber(modid = Tweaks.MODID)
public class HammerItem extends PickaxeItem {
    private static final Map<UUID, Direction> LAST_HIT_FACES = new ConcurrentHashMap<>();

    public HammerItem(Tier tier, Item.Properties properties) {
        super(tier, properties.attributes(PickaxeItem.createAttributes(tier, 1.0F, -2.8F)));
    }

    @SubscribeEvent
    public static void rememberHitFace(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getItemStack().getItem() instanceof HammerItem && event.getFace() != null) {
            LAST_HIT_FACES.put(event.getEntity().getUUID(), event.getFace());
        }
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        boolean mined = super.mineBlock(stack, level, state, pos, miningEntity);

        if (level.isClientSide || !(miningEntity instanceof Player player)) {
            return mined;
        }

        Direction face = LAST_HIT_FACES.remove(player.getUUID());
        if (face == null) {
            face = Direction.UP;
        }

        for (int first = -1; first <= 1; first++) {
            for (int second = -1; second <= 1; second++) {
                if (first == 0 && second == 0) {
                    continue;
                }

                BlockPos target = offset(pos, face, first, second);
                BlockState targetState = level.getBlockState(target);
                if (targetState.isAir() || targetState.getDestroySpeed(level, target) < 0.0F
                        || !isCorrectToolForDrops(stack, targetState)) {
                    continue;
                }

                if (level.destroyBlock(target, true, player)) {
                    stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                }
            }
        }

        return mined;
    }

    private static BlockPos offset(BlockPos center, Direction face, int first, int second) {
        return switch (face.getAxis()) {
            case X -> center.offset(0, first, second);
            case Y -> center.offset(first, 0, second);
            case Z -> center.offset(first, second, 0);
        };
    }
}
