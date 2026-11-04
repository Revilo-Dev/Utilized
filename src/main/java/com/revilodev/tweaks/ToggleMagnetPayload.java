package com.revilodev.tweaks;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ToggleMagnetPayload() implements CustomPacketPayload {
    public static final Type<ToggleMagnetPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Tweaks.MODID, "toggle_magnet"));
    public static final ToggleMagnetPayload INSTANCE = new ToggleMagnetPayload();
    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleMagnetPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
