//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.prayer;

import lombok.NonNull;

import java.util.Comparator;

public class TheatreUpcomingAttack implements Comparable<TheatreUpcomingAttack> {
    private int ticksUntil;
    private final Prayer prayer;
    private final int priority;

    public TheatreUpcomingAttack(int ticksUntil, Prayer prayer, int priority) {
        this.ticksUntil = ticksUntil;
        this.prayer = prayer;
        this.priority = priority;
    }

    public TheatreUpcomingAttack(int ticksUntil, Prayer prayer) {
        this(ticksUntil, prayer, 0);
    }

    public void decrementTicks() {
        if (this.ticksUntil > 0) {
            --this.ticksUntil;
        }

    }

    public boolean shouldRemove() {
        return this.ticksUntil == 0;
    }

    public int compareTo(@NonNull TheatreUpcomingAttack o) {
        if (o == null) {
            throw new NullPointerException("o is marked non-null but is null");
        } else {
            return Comparator.comparing(TheatreUpcomingAttack::getTicksUntil).thenComparing(TheatreUpcomingAttack::getPriority).compare(this, o);
        }
    }

    public int getTicksUntil() {
        return this.ticksUntil;
    }

    public Prayer getPrayer() {
        return this.prayer;
    }

    public int getPriority() {
        return this.priority;
    }
}
