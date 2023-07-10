package com.example.RuneBotApi;

import com.example.PacketUtils.PacketDef;
import com.example.PacketUtils.PacketReflection;
import com.example.Packets.MousePackets;
import com.example.RuneBotApi.LocalPlayer.LocationInformation;
import net.runelite.api.Client;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

public class Movement {
    public static void move(int worldPointX, int worldPointY, boolean ctrlDown)
    {
        int ctrl = ctrlDown ? 2 : 0;
        MousePackets.queueClickPacket();
        PacketReflection.sendPacket(PacketDef.MOVE_GAMECLICK, worldPointX, worldPointY, ctrl, 5);
    }

    public static void move(WorldPoint location)
    {
        move(location.getX(), location.getY(), false);
    }

    /**
     * sends a movement packet at the specified location offset in your map region
     * This offset can be viewed in the RL dev tools under "Location", and is the
     * X and Y offset of the bottom-leftmost loaded tile
     */
    public static void regionMove(int offsetX, int offsetY)
    {
        Client client = RBApi.getClient();
        WorldPoint worldPoint = WorldPoint.fromRegion(
                client.getLocalPlayer().getWorldLocation().getRegionID(),
                offsetX, offsetY, client.getPlane()
        );

        move(worldPoint);
    }

    public static void moveRelative(int offsetX, int offsetY)
    {
        WorldPoint loc = LocationInformation.getTile();

        loc = loc.dx(offsetX);
        loc = loc.dy(offsetY);

        move(loc);
    }
}
