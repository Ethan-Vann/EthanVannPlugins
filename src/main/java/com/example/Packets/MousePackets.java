package com.example.Packets;

import com.example.PacketUtils.ObfuscatedNames;
import com.example.PacketUtils.PacketDef;
import com.example.PacketUtils.PacketReflection;
import lombok.SneakyThrows;
import net.runelite.api.Client;
import net.runelite.client.RuneLite;

import java.lang.reflect.Field;
import java.math.BigInteger;

public class MousePackets{
    
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    public static BigInteger modInverse(BigInteger val, int bits) {
        try {
            BigInteger shift = BigInteger.ONE.shiftLeft(bits);
            return val.modInverse(shift);
        } catch (ArithmeticException e) {
            return val;
        }
    }

    public static long modInverse(long val) {
        return modInverse(BigInteger.valueOf(val), 64).longValue();
    }

    @SneakyThrows
    public static void queueClickPacket(int x, int y) {
        long mouseHandlerMS = System.currentTimeMillis();
        setMouseHandlerLastMillis(mouseHandlerMS);
        long clientMS = getClientLastMillis();
        long deltaMs = mouseHandlerMS - clientMS;
        setClientLastMillis(mouseHandlerMS);
        if (deltaMs < 0) {
            deltaMs = 0L;
        }
        if (deltaMs > 32767) {
            deltaMs = 32767L;
        }
        int mouseInfo = ((int) deltaMs << 1);
        PacketReflection.sendPacket(PacketDef.getEventMouseClick(), mouseInfo, x, y);
    }

    public static void queueClickPacket() {
        queueClickPacket(0, 0);
    }

    public void queueRandomClickPacket(int x, int y) {

    }

    @SneakyThrows
    public static long getMouseHandlerLastMillis() {
        Class<?> mouseHandler = client.getClass().getClassLoader().loadClass(ObfuscatedNames.MouseHandler_lastPressedTimeMillisClass);
        Field mouseHandlerLastPressedTime = mouseHandler.getDeclaredField(ObfuscatedNames.MouseHandler_lastPressedTimeMillisField);
        mouseHandlerLastPressedTime.setAccessible(true);
        long retValue = mouseHandlerLastPressedTime.getLong(null) * Long.parseLong(ObfuscatedNames.mouseHandlerMillisMultiplier);
        mouseHandlerLastPressedTime.setAccessible(false);
        return retValue;
    }

    @SneakyThrows
    public static long getClientLastMillis() {
        Field clientLastPressedTimeMillis = client.getClass().getDeclaredField(ObfuscatedNames.clientMillisField);
        clientLastPressedTimeMillis.setAccessible(true);
        long retValue = clientLastPressedTimeMillis.getLong(client) * Long.parseLong(ObfuscatedNames.clientMillisMultiplier);
        clientLastPressedTimeMillis.setAccessible(false);
        return retValue;
    }

    @SneakyThrows
    public static void setMouseHandlerLastMillis(long time) {
        Class<?> mouseHandler = client.getClass().getClassLoader().loadClass(ObfuscatedNames.MouseHandler_lastPressedTimeMillisClass);
        Field mouseHandlerLastPressedTime = mouseHandler.getDeclaredField(ObfuscatedNames.MouseHandler_lastPressedTimeMillisField);
        mouseHandlerLastPressedTime.setAccessible(true);
        mouseHandlerLastPressedTime.setLong(null, time * modInverse(Long.parseLong(ObfuscatedNames.mouseHandlerMillisMultiplier)));
        mouseHandlerLastPressedTime.setAccessible(false);
    }

    @SneakyThrows
    public static void setClientLastMillis(long time) {
        Field clientLastPressedTimeMillis = client.getClass().getDeclaredField(ObfuscatedNames.clientMillisField);
        clientLastPressedTimeMillis.setAccessible(true);
        clientLastPressedTimeMillis.setLong(client, time * modInverse(Long.parseLong(ObfuscatedNames.clientMillisMultiplier)));
        clientLastPressedTimeMillis.setAccessible(false);
    }
}