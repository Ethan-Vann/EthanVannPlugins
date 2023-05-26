package com.example.Packets;

import com.example.PacketUtils.ObfuscatedNames;
import com.example.PacketUtils.PacketDef;
import com.example.PacketUtils.PacketReflection;
import lombok.SneakyThrows;
import net.runelite.api.Client;
import net.runelite.client.RuneLite;

import java.awt.event.KeyEvent;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.Executors;

import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;

public class MousePackets {

    static Client client = RuneLite.getInjector().getInstance(Client.class);
    private static final Random random = new Random();
    private static long randomDelay = randomDelay();

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
        PacketReflection.mouseHandlerLastPressedTime.setAccessible(true);
        PacketReflection.clientMouseLastLastPressedTimeMillis.setAccessible(true);
        long currentTime = System.currentTimeMillis();
        PacketReflection.mouseHandlerLastPressedTime.setLong(null, currentTime * modInverse(Long.parseLong(ObfuscatedNames.MouseHandlerGarbage)));
        long clientMs = Long.parseLong(ObfuscatedNames.ClientMouseHandlerGarbage) * (long) PacketReflection.clientMouseLastLastPressedTimeMillis.get(null);
        long mouseMs = System.currentTimeMillis();
        long deltaMs = mouseMs - clientMs;
        if (deltaMs < 0) {
            deltaMs = 0L;
        }
        if (deltaMs > 32767) {
            deltaMs = 32767L;
        }
        PacketReflection.clientMouseLastLastPressedTimeMillis.set(client, Long.parseLong(ObfuscatedNames.ClientMouseSetterGarbage) * (long) PacketReflection.mouseHandlerLastPressedTime.get(null));
        int mouseInfo = ((int) deltaMs << 1) + 1;
        PacketReflection.sendPacket(PacketDef.EVENT_MOUSE_CLICK, mouseInfo, x, y);
        PacketReflection.mouseHandlerLastPressedTime.setAccessible(false);
        PacketReflection.clientMouseLastLastPressedTimeMillis.setAccessible(false);
        if (checkIdleLogout()) {
            randomDelay = randomDelay();
            Executors.newSingleThreadExecutor()
                    .submit(MousePackets::pressKey);
        }
    }

    public static void queueClickPacket() {
        queueClickPacket(0, 0);
    }

    private static boolean checkIdleLogout() {
        int idleClientTicks = client.getKeyboardIdleTicks();

        if (client.getMouseIdleTicks() < idleClientTicks) {
            idleClientTicks = client.getMouseIdleTicks();
        }

        return idleClientTicks >= randomDelay;
    }

    private static long randomDelay() {
        return (long) clamp(
                Math.round(random.nextGaussian() * 8000)
        );
    }

    private static double clamp(double val) {
        return Math.max(1, Math.min(13000, val));
    }

    private static void pressKey() {
        KeyEvent keyPress = new KeyEvent(client.getCanvas(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), BUTTON1_DOWN_MASK, KeyEvent.VK_BACK_SPACE);
        client.getCanvas().dispatchEvent(keyPress);
        KeyEvent keyRelease = new KeyEvent(client.getCanvas(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE);
        client.getCanvas().dispatchEvent(keyRelease);
        KeyEvent keyTyped = new KeyEvent(client.getCanvas(), KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE);
        client.getCanvas().dispatchEvent(keyTyped);
    }
}