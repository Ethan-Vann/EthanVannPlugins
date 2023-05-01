//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.prayer;

import com.example.WidgetInfoExtended;

public enum Prayer {
    THICK_SKIN(4104, 5.0, WidgetInfoExtended.PRAYER_THICK_SKIN),
    BURST_OF_STRENGTH(4105, 5.0, WidgetInfoExtended.PRAYER_BURST_OF_STRENGTH),
    CLARITY_OF_THOUGHT(4106, 5.0, WidgetInfoExtended.PRAYER_CLARITY_OF_THOUGHT),
    SHARP_EYE(4122, 5.0, WidgetInfoExtended.PRAYER_SHARP_EYE),
    MYSTIC_WILL(4123, 5.0, WidgetInfoExtended.PRAYER_MYSTIC_WILL),
    ROCK_SKIN(4107, 10.0, WidgetInfoExtended.PRAYER_ROCK_SKIN),
    SUPERHUMAN_STRENGTH(4108, 10.0, WidgetInfoExtended.PRAYER_SUPERHUMAN_STRENGTH),
    IMPROVED_REFLEXES(4109, 10.0, WidgetInfoExtended.PRAYER_IMPROVED_REFLEXES),
    RAPID_RESTORE(4110, 1.6666666666666667, WidgetInfoExtended.PRAYER_RAPID_RESTORE),
    RAPID_HEAL(4111, 3.3333333333333335, WidgetInfoExtended.PRAYER_RAPID_HEAL),
    PROTECT_ITEM(4112, 3.3333333333333335, WidgetInfoExtended.PRAYER_PROTECT_ITEM),
    HAWK_EYE(4124, 10.0, WidgetInfoExtended.PRAYER_HAWK_EYE),
    MYSTIC_LORE(4125, 10.0, WidgetInfoExtended.PRAYER_MYSTIC_LORE),
    STEEL_SKIN(4113, 20.0, WidgetInfoExtended.PRAYER_STEEL_SKIN),
    ULTIMATE_STRENGTH(4114, 20.0, WidgetInfoExtended.PRAYER_ULTIMATE_STRENGTH),
    INCREDIBLE_REFLEXES(4115, 20.0, WidgetInfoExtended.PRAYER_INCREDIBLE_REFLEXES),
    PROTECT_FROM_MAGIC(4116, 20.0, WidgetInfoExtended.PRAYER_PROTECT_FROM_MAGIC),
    PROTECT_FROM_MISSILES(4117, 20.0, WidgetInfoExtended.PRAYER_PROTECT_FROM_MISSILES),
    PROTECT_FROM_MELEE(4118, 20.0, WidgetInfoExtended.PRAYER_PROTECT_FROM_MELEE),
    EAGLE_EYE(4126, 20.0, WidgetInfoExtended.PRAYER_EAGLE_EYE),
    MYSTIC_MIGHT(4127, 20.0, WidgetInfoExtended.PRAYER_MYSTIC_MIGHT),
    RETRIBUTION(4119, 5.0, WidgetInfoExtended.PRAYER_RETRIBUTION),
    REDEMPTION(4120, 10.0, WidgetInfoExtended.PRAYER_REDEMPTION),
    SMITE(4121, 30.0, WidgetInfoExtended.PRAYER_SMITE),
    CHIVALRY(4128, 40.0, WidgetInfoExtended.PRAYER_CHIVALRY),
    PIETY(4129, 40.0, WidgetInfoExtended.PRAYER_PIETY),
    PRESERVE(5466, 3.3333333333333335, WidgetInfoExtended.PRAYER_PRESERVE),
    RIGOUR(5464, 40.0, WidgetInfoExtended.PRAYER_RIGOUR),
    AUGURY(5465, 40.0, WidgetInfoExtended.PRAYER_AUGURY);

    private final int varbit;
    private final double drainRate;
    private final WidgetInfoExtended widgetInfo;

    private Prayer(int varbit, double drainRate, WidgetInfoExtended widgetInfo) {
        this.varbit = varbit;
        this.drainRate = drainRate;
        this.widgetInfo = widgetInfo;
    }

    public int getVarbit() {
        return this.varbit;
    }

    public double getDrainRate() {
        return this.drainRate;
    }

    public WidgetInfoExtended getWidgetInfoExtended() {
        return this.widgetInfo;
    }
}
