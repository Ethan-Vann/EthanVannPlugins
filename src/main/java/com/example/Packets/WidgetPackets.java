package com.example.Packets;

import com.example.PacketDef;
import com.example.PacketReflection;
import lombok.SneakyThrows;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WidgetPackets {
    @Inject
    Client client;
    @Inject
    PacketReflection packetReflection;

    @SneakyThrows
    public void queueWidgetActionPacket(int actionFieldNo, int widgetId, int itemId, int childId) {
        if (actionFieldNo == 1) {
            packetReflection.sendPacket(PacketDef.IF_BUTTON1, widgetId, childId, itemId);
            return;
        }
        switch (actionFieldNo) {
            case 1:
                packetReflection.sendPacket(PacketDef.IF_BUTTON1, widgetId, childId, itemId);
                break;
            case 2:
                packetReflection.sendPacket(PacketDef.IF_BUTTON2, widgetId, childId, itemId);
                break;
            case 3:
                packetReflection.sendPacket(PacketDef.IF_BUTTON3, widgetId, childId, itemId);
                break;
            case 4:
                packetReflection.sendPacket(PacketDef.IF_BUTTON4, widgetId, childId, itemId);
                break;
            case 5:
                packetReflection.sendPacket(PacketDef.IF_BUTTON5, widgetId, childId, itemId);
                break;
            case 6:
                packetReflection.sendPacket(PacketDef.IF_BUTTON6, widgetId, childId, itemId);
                break;
            case 7:
                packetReflection.sendPacket(PacketDef.IF_BUTTON7, widgetId, childId, itemId);
                break;
            case 8:
                packetReflection.sendPacket(PacketDef.IF_BUTTON8, widgetId, childId, itemId);
                break;
            case 9:
                packetReflection.sendPacket(PacketDef.IF_BUTTON9, widgetId, childId, itemId);
                break;
            case 10:
                packetReflection.sendPacket(PacketDef.IF_BUTTON10, widgetId, childId, itemId);
                break;
        }
    }

    @SneakyThrows
    public void queueWidgetAction(Widget widget, String... actionlist) {
        if(widget==null){
            System.out.println("call to queueWidgetAction with null widget");
            return;
        }
        List<String> actions = Arrays.stream(widget.getActions()).collect(Collectors.toList());
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
        queueWidgetActionPacket(num, widget.getId(), widget.getItemId(), widget.getIndex());
    }

    public void queueWidgetOnWidget(Widget srcWidget, Widget destWidget) {
        queueWidgetOnWidget(srcWidget.getId(), srcWidget.getIndex(), srcWidget.getItemId(), destWidget.getId(), destWidget.getIndex(), destWidget.getItemId());
    }
    public void queueWidgetOnWidget(int sourceWidgetId, int sourceSlot, int sourceItemId, int destinationWidgetId, int destinationSlot, int destinationItemId) {
        packetReflection.sendPacket(PacketDef.IF_BUTTONT, sourceWidgetId, sourceSlot, sourceItemId, destinationWidgetId,
                destinationSlot, destinationItemId);
    }
    public void queueResumePause(int widgetId,int childId){
        packetReflection.sendPacket(PacketDef.RESUME_PAUSEBUTTON,widgetId,childId);
    }

}