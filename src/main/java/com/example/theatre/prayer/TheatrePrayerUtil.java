//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.prayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class TheatrePrayerUtil {
    public TheatrePrayerUtil() {
    }

    public static void updateNextPrayerQueue(Queue<TheatreUpcomingAttack> queue) {
        queue.forEach(TheatreUpcomingAttack::decrementTicks);
        queue.removeIf(TheatreUpcomingAttack::shouldRemove);
    }

    public static Map<Integer, TheatreUpcomingAttack> getTickPriorityMap(Queue<TheatreUpcomingAttack> queue) {
        Map<Integer, TheatreUpcomingAttack> map = new HashMap();
        queue.forEach((attack) -> {
            if (!map.containsKey(attack.getTicksUntil())) {
                map.put(attack.getTicksUntil(), attack);
            }

            if (attack.getPriority() < ((TheatreUpcomingAttack)map.get(attack.getTicksUntil())).getPriority()) {
                map.put(attack.getTicksUntil(), attack);
            }

        });
        return map;
    }
}
