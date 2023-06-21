package com.example.RuneBotApi;

import com.example.PacketUtils.PacketDef;
import com.example.PacketUtils.PacketReflection;
import com.example.Packets.MousePackets;
import net.runelite.api.coords.WorldPoint;

public class Movement {
    public static void move(int worldPointX, int worldPointY, boolean ctrlDown) {
        int ctrl = ctrlDown ? 2 : 0;
        MousePackets.queueClickPacket();
        PacketReflection.sendPacket(PacketDef.MOVE_GAMECLICK, worldPointX, worldPointY, ctrl, 5);
    }

    public static void move(WorldPoint location) {
        move(location.getX(), location.getY(), false);
    }

}
