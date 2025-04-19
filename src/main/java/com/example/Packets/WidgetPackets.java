package com.example.Packets;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.PacketUtils.PacketDef;
import com.example.PacketUtils.PacketReflection;
import lombok.SneakyThrows;
import net.runelite.api.ItemComposition;
import net.runelite.api.widgets.Widget;
import net.runelite.client.util.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WidgetPackets {
    @SneakyThrows
    public static void queueWidgetActionPacket(int actionFieldNo, int widgetId, int itemId, int childId) {
        switch (actionFieldNo) {
            case 1:
                PacketReflection.sendPacket(PacketDef.getIfButton1(), widgetId, childId, itemId);
                break;
            case 2:
                PacketReflection.sendPacket(PacketDef.getIfButton2(), widgetId, childId, itemId);
                break;
            case 3:
                PacketReflection.sendPacket(PacketDef.getIfButton3(), widgetId, childId, itemId);
                break;
            case 4:
                PacketReflection.sendPacket(PacketDef.getIfButton4(), widgetId, childId, itemId);
                break;
            case 5:
                PacketReflection.sendPacket(PacketDef.getIfButton5(), widgetId, childId, itemId);
                break;
            case 6:
                PacketReflection.sendPacket(PacketDef.getIfButton6(), widgetId, childId, itemId);
                break;
            case 7:
                PacketReflection.sendPacket(PacketDef.getIfButton7(), widgetId, childId, itemId);
                break;
            case 8:
                PacketReflection.sendPacket(PacketDef.getIfButton8(), widgetId, childId, itemId);
                break;
            case 9:
                PacketReflection.sendPacket(PacketDef.getIfButton9(), widgetId, childId, itemId);
                break;
            case 10:
                PacketReflection.sendPacket(PacketDef.getIfButton10(), widgetId, childId, itemId);
                break;
        }
    }

    @SneakyThrows
    public static void queueWidgetSubAction(Widget widget, String... actions) {
        if (widget == null || widget.getItemId() == -1
                || widget.getActions() == null || widget.getActions().length == 0) {
            return;
        }

        ItemComposition composition = EthanApiPlugin.getClient().getItemDefinition(widget.getItemId());

        String[][] subOps = composition.getSubops();
        List<String> actionList = Arrays.stream(widget.getActions()).collect(Collectors.toList());
        List<String> subActionList = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            subActionList.add(null);
        }

        for (String[] subOp : subOps) {
            if (subOp == null) {
                continue;
            }

            int length = subOp.length;

            for (int i = 0; i < length; i++) {
                String subOpAction = subOp[i];
                if (subOpAction == null) {
                    continue;
                }

                subActionList.set(i, Text.removeTags(subOpAction).toLowerCase());
            }
        }

        int subActionFieldNo = -1;

        for (String subOpAction : subActionList) {
            for (String action : actions) {
                if (subOpAction != null && subOpAction.equalsIgnoreCase(action)) {
                    subActionFieldNo = subActionList.lastIndexOf(action.toLowerCase());
                }
            }
        }

        if (subActionFieldNo < 1 || subActionFieldNo > 10) {
            return;
        }

        int menuFieldNo = -1;
        final int finalActionFieldNo = subActionFieldNo;

        if (subActionList.get(finalActionFieldNo) == null) {
            return;
        }

        for (int i = 0; i < subOps.length; i++) {
            if (subOps[i] == null) {
                continue;
            }

            if (Arrays.stream(subOps[i])
                    .anyMatch(s -> subActionList.get(finalActionFieldNo).equalsIgnoreCase(s))) {
                menuFieldNo = i;
                break;
            }
        }

        int pos = 0;

        for (int i = 0; i < actionList.size(); i++) {
            if (actionList.get(i) == null) {
                continue;
            }

            pos++;

            if (pos == menuFieldNo) {
                menuFieldNo = i + 1;
                break;
            }
        }

        if (menuFieldNo == -1) {
            return;
        }

        PacketReflection.sendPacket(PacketDef.getIfSubOp(), widget.getId(), widget.getIndex(),
                widget.getItemId(), menuFieldNo, subActionFieldNo);
    }

    @SneakyThrows
    public static void queueWidgetAction(Widget widget, String... actionlist) {
        if (widget == null) {
            return;
        }
        List<String> actions = Arrays.stream(widget.getActions()).collect(Collectors.toList());
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i) == null)
                continue;
            actions.set(i, actions.get(i).toLowerCase());
        }
        int num = -1;
        for (String action : actions) {
            for (String action2 : actionlist) {
                if (action != null && Text.removeTags(action).equalsIgnoreCase(action2)) {
                    num = actions.indexOf(action.toLowerCase()) + 1;
                }
            }
        }

        if (num < 1 || num > 10) {
            queueWidgetSubAction(widget, actionlist);
            return;
        }
        queueWidgetActionPacket(num, widget.getId(), widget.getItemId(), widget.getIndex());
    }

    public static void queueWidgetOnWidget(Widget srcWidget, Widget destWidget) {
        queueWidgetOnWidget(srcWidget.getId(), srcWidget.getIndex(), srcWidget.getItemId(),
                destWidget.getId(), destWidget.getIndex(), destWidget.getItemId());
    }

    public static void queueWidgetOnWidget(int sourceWidgetId, int sourceSlot, int sourceItemId,
                                           int destinationWidgetId, int destinationSlot, int destinationItemId) {
        PacketReflection.sendPacket(PacketDef.getIfButtonT(), sourceWidgetId, sourceSlot, sourceItemId, destinationWidgetId,
                destinationSlot, destinationItemId);
    }

    public static void queueResumePause(int widgetId, int childId) {
        PacketReflection.sendPacket(PacketDef.getResumePausebutton(), widgetId, childId);
    }

    public static void queueResumeCount(int id) {
        PacketReflection.sendPacket(PacketDef.getResumeCountDialog(), id);
    }

    public static void queueDragAndDrop(Widget src, Widget dest) {
        PacketReflection.sendPacket(PacketDef.getOpHeldd(), src.getId(), src.getIndex(),
                src.getItemId(), dest.getId(), dest.getIndex(), dest.getItemId());
    }
}