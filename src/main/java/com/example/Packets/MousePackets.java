package com.example.Packets;

import com.example.ObfuscatedNames;
import com.example.PacketDef;
import com.example.PacketReflection;
import lombok.SneakyThrows;
import net.runelite.api.Client;

import javax.inject.Inject;
import java.awt.event.KeyEvent;
import java.math.BigInteger;
import java.util.Random;

public class MousePackets {
    @Inject
    Client client;
    @Inject
    PacketReflection packetReflection;
    private static Random random = new Random();
    private static long randomDelay =randomDelay();

    public static BigInteger modInverse(BigInteger val, int bits)
    {
        try
        {
            BigInteger shift = BigInteger.ONE.shiftLeft(bits);
            return val.modInverse(shift);
        }
        catch (ArithmeticException e)
        {
            return val;
        }
    }

    public static long modInverse(long val)
    {
        return modInverse(BigInteger.valueOf(val), 64).longValue();
    }
    @SneakyThrows
    public void queueClickPacket(int x, int y) {
        PacketReflection.mouseHandlerLastPressedTime.setAccessible(true);
        PacketReflection.clientMouseLastLastPressedTimeMillis.setAccessible(true);
        long currentTime = System.currentTimeMillis();
        PacketReflection.mouseHandlerLastPressedTime.setLong(null, currentTime * modInverse(Long.parseLong(ObfuscatedNames.MouseHandlerGarbage)));
        long clientMs = Long.parseLong(ObfuscatedNames.ClientMouseHandlerGarbage)*(long)PacketReflection.clientMouseLastLastPressedTimeMillis.get(null);
        long mouseMs =System.currentTimeMillis();
        long deltaMs = mouseMs - clientMs;
        if (deltaMs < 0) {
            deltaMs = 0L;
        }
        if (deltaMs > 32767) {
            deltaMs = 32767L;
        }
        PacketReflection.clientMouseLastLastPressedTimeMillis.set(client,Long.parseLong(ObfuscatedNames.ClientMouseSetterGarbage)*(long)PacketReflection.mouseHandlerLastPressedTime.get(null));
        int mouseInfo = ((int)deltaMs << 1) + 1;
        packetReflection.sendPacket(PacketDef.EVENT_MOUSE_CLICK, mouseInfo, x, y);
        PacketReflection.mouseHandlerLastPressedTime.setAccessible(false);
        PacketReflection.clientMouseLastLastPressedTimeMillis.setAccessible(false);
        if(checkIdleLogout()){
            randomDelay = randomDelay();
            pressKey();
        }
    }

    public void queueClickPacket() {
        queueClickPacket(0, 0);
    }
    private boolean checkIdleLogout()
    {
        int idleClientTicks = client.getKeyboardIdleTicks();

        if (client.getMouseIdleTicks() < idleClientTicks)
        {
            idleClientTicks = client.getMouseIdleTicks();
        }

        return idleClientTicks >= randomDelay;
    }

    private static long randomDelay()
    {
        return (long) clamp(
                Math.round(random.nextGaussian() * 8000)
        );
    }
    private static double clamp(double val)
    {
        return Math.max(1, Math.min(13000, val));
    }
    private void pressKey()
    {
        this.client.getCanvas().dispatchEvent(new KeyEvent(this.client.getCanvas(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE));
        this.client.getCanvas().dispatchEvent(new KeyEvent(this.client.getCanvas(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE));
        this.client.getCanvas().dispatchEvent(new KeyEvent(this.client.getCanvas(), KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE));
    }
}