package com.example.bigdrizzleplugin.builders;

import com.example.EthanApiPlugin.Bank;
import com.example.bigdrizzleplugin.MenuEntryMirror;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.widgets.Widget;
import net.runelite.client.RuneLite;

import java.util.Optional;

public class WidgetBuilder {
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    public static MenuEntryMirror widgetAction(Widget widget, String action){
        if (widget == null || action == null){
            return null;
        }
        int identifier = 0;
        for (int i = widget.getActions().length - 1; i >= 0; i--) {
            if (widget.getActions()[i] != null && widget.getActions()[i].equalsIgnoreCase(action)){
                identifier = i+1;
                break;
            }
        }
        MenuAction menuAction = MenuAction.CC_OP;
        return new MenuEntryMirror("WidgetAction: " + action + " " + widget.getName(), identifier, menuAction, widget.getIndex(), widget.getId(), widget.getItemId());
    }

    static MenuEntryMirror widgetActionMenuOverride(Widget widget, MenuAction menuAction){
        if (widget == null){
            return null;
        }
        return new MenuEntryMirror("WidgetAction: " + widget.getName(), 0, menuAction, widget.getIndex(), widget.getId(), widget.getItemId());
    }
}
