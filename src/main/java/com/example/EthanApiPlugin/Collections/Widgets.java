package com.example.EthanApiPlugin.Collections;

import com.example.EthanApiPlugin.Collections.query.WidgetQuery;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.RuneLite;

import java.util.*;

public class Widgets {
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    static int lastSearchIdleTicks = -10;
    static HashSet<Widget> cachedWidgets = new HashSet<>();
    //It is important to note that this method will return all widgets, including hidden ones.
    //Some widgets are not updated while hidden so there is a chance that the widgets returned contain outdated
    // information.
    //for update critical information make sure the widget is not hidden or use the other query types like inventory,
    // equipment ect as they will only return up-to-date information.
    public static WidgetQuery search() {
        if(lastSearchIdleTicks==client.getKeyboardIdleTicks()){
            return new WidgetQuery(cachedWidgets);
        }
        HashSet<Widget> returnList = new HashSet<>();
        Widget[] currentQueue;
        ArrayList<Widget> buffer = new ArrayList<>();
        currentQueue = client.getWidgetRoots();
        while(currentQueue.length!=0) {
            for (Widget widget : currentQueue) {
                if (widget == null) {
                    continue;
                }
                returnList.add(widget);
                if (widget.getDynamicChildren() != null) {
                    for (Widget dynamicChild : widget.getDynamicChildren()) {
                        if (dynamicChild == null) {
                            continue;
                        }
                        buffer.add(dynamicChild);
                        returnList.add(dynamicChild);
                    }
                }
                if (widget.getNestedChildren() != null) {
                    for (Widget nestedChild : widget.getNestedChildren()) {
                        if (nestedChild == null) {
                            continue;
                        }
                        buffer.add(nestedChild);
                        returnList.add(nestedChild);
                    }
                }
                Widget[] staticChildren;
                try {
                    staticChildren = widget.getStaticChildren();
                } catch (NullPointerException e) {
                    continue;
                }
                if (staticChildren != null) {
                    for (Widget staticChild : staticChildren) {
                        if (staticChild == null) {
                            continue;
                        }
                        buffer.add(staticChild);
                        returnList.add(staticChild);
                    }
                }
            }
            currentQueue = buffer.toArray(new Widget[]{});
            buffer.clear();
        }
        lastSearchIdleTicks = client.getKeyboardIdleTicks();
        cachedWidgets = returnList;
        return new WidgetQuery(returnList);
    }
}