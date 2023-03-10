package com.example.Packets;

import com.example.PacketDef;
import com.example.PacketReflection;
import lombok.SneakyThrows;
import net.runelite.api.Player;
import net.runelite.api.widgets.Widget;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.PacketReflection.client;

public class PlayerPackets {
    @SneakyThrows
    public void queuePlayerAction(int actionFieldNo, int playerIndex, boolean ctrlDown) {
        int ctrl = ctrlDown ? 1 : 0;
        switch (actionFieldNo) {
            case 1:
                PacketReflection.sendPacket(PacketDef.OPPLAYER1, playerIndex, ctrl);
                break;
            case 2:
                PacketReflection.sendPacket(PacketDef.OPPLAYER2, playerIndex, ctrl);
                break;
            case 3:
                PacketReflection.sendPacket(PacketDef.OPPLAYER3, playerIndex, ctrl);
                break;
            case 4:
                PacketReflection.sendPacket(PacketDef.OPPLAYER4, playerIndex, ctrl);
                break;
            case 5:
                PacketReflection.sendPacket(PacketDef.OPPLAYER5, playerIndex, ctrl);
                break;
            case 6:
                PacketReflection.sendPacket(PacketDef.OPPLAYER6, playerIndex, ctrl);
                break;
            case 7:
                PacketReflection.sendPacket(PacketDef.OPPLAYER7, playerIndex, ctrl);
                break;
            case 8:
                PacketReflection.sendPacket(PacketDef.OPPLAYER8, playerIndex, ctrl);
                break;
        }
    }

    @SneakyThrows
    public void queuePlayerAction(Player player, String... actionlist) {
        List<String> actions = Arrays.stream(client.getPlayerOptions()).collect(Collectors.toList());
        int num = -1;
        for (String action : actions) {
            for (String action2 : actionlist) {
                if (action != null && action.equals(action2)) {
                    num = actions.indexOf(action) + 1;
                }
            }
        }

        if (num < 1 || num > 10) {
            return;
        }
        queuePlayerAction(num, player.getId(), false);
    }

    public void queueWidgetOnPlayer(int playerIndex, int sourceItemId, int sourceSlot, int sourceWidgetId,
                                    boolean ctrlDown) {
        int ctrl = ctrlDown ? 1 : 0;
        PacketReflection.sendPacket(PacketDef.OPPLAYERT, playerIndex, sourceItemId, sourceSlot, sourceWidgetId, ctrl);
    }

    public void queueWidgetOnPlayer(Player player, Widget widget) {
        queueWidgetOnPlayer(player.getId(), widget.getItemId(), widget.getIndex(), widget.getId(), false);
    }
}
