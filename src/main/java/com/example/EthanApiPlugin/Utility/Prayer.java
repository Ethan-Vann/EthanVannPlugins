package com.example.EthanApiPlugin.Utility;

import net.runelite.api.Varbits;
import net.runelite.api.widgets.WidgetInfo;
import com.example.EthanApiPlugin.Utility.WidgetInfoExt;

public enum Prayer {
    THICK_SKIN(Varbits.PRAYER_THICK_SKIN, 5.01, WidgetInfoExt.PRAYER_THICK_SKIN),
    BURST_OF_STRENGTH(Varbits.PRAYER_BURST_OF_STRENGTH, 5.0D, WidgetInfoExt.PRAYER_BURST_OF_STRENGTH),
    CLARITY_OF_THOUGHT(Varbits.PRAYER_CLARITY_OF_THOUGHT, 5.0D, WidgetInfoExt.PRAYER_CLARITY_OF_THOUGHT),
    SHARP_EYE(Varbits.PRAYER_SHARP_EYE, 5.0D, WidgetInfoExt.PRAYER_SHARP_EYE),
    MYSTIC_WILL(Varbits.PRAYER_MYSTIC_WILL, 5.0D, WidgetInfoExt.PRAYER_MYSTIC_WILL),
    ROCK_SKIN(Varbits.PRAYER_ROCK_SKIN, 10.0D, WidgetInfoExt.PRAYER_ROCK_SKIN),
    SUPERHUMAN_STRENGTH(Varbits.PRAYER_SUPERHUMAN_STRENGTH, 10.0D, WidgetInfoExt.PRAYER_SUPERHUMAN_STRENGTH),
    IMPROVED_REFLEXES(Varbits.PRAYER_IMPROVED_REFLEXES, 10.0D, WidgetInfoExt.PRAYER_IMPROVED_REFLEXES),
    RAPID_RESTORE(Varbits.PRAYER_RAPID_RESTORE, 1.6666666666666667D, WidgetInfoExt.PRAYER_RAPID_RESTORE),
    RAPID_HEAL(Varbits.PRAYER_RAPID_HEAL, 3.3333333333333335D, WidgetInfoExt.PRAYER_RAPID_HEAL),
    PROTECT_ITEM(Varbits.PRAYER_PROTECT_ITEM, 3.3333333333333335D, WidgetInfoExt.PRAYER_PROTECT_ITEM),
    HAWK_EYE(Varbits.PRAYER_HAWK_EYE, 10.0D, WidgetInfoExt.PRAYER_HAWK_EYE),
    MYSTIC_LORE(Varbits.PRAYER_MYSTIC_LORE, 10.0D, WidgetInfoExt.PRAYER_MYSTIC_LORE),
    STEEL_SKIN(Varbits.PRAYER_STEEL_SKIN, 20.0D, WidgetInfoExt.PRAYER_STEEL_SKIN),
    ULTIMATE_STRENGTH(Varbits.PRAYER_ULTIMATE_STRENGTH, 20.0D, WidgetInfoExt.PRAYER_ULTIMATE_STRENGTH),
    INCREDIBLE_REFLEXES(Varbits.PRAYER_INCREDIBLE_REFLEXES, 20.0D, WidgetInfoExt.PRAYER_INCREDIBLE_REFLEXES),
    PROTECT_FROM_MAGIC(Varbits.PRAYER_PROTECT_FROM_MAGIC, 20.0D, WidgetInfoExt.PRAYER_PROTECT_FROM_MAGIC),
    PROTECT_FROM_MISSILES(Varbits.PRAYER_PROTECT_FROM_MISSILES, 20.0D, WidgetInfoExt.PRAYER_PROTECT_FROM_MISSILES),
    PROTECT_FROM_MELEE(Varbits.PRAYER_PROTECT_FROM_MELEE, 20.0D, WidgetInfoExt.PRAYER_PROTECT_FROM_MELEE),
    EAGLE_EYE(Varbits.PRAYER_EAGLE_EYE, 20.0D, WidgetInfoExt.PRAYER_EAGLE_EYE),
    MYSTIC_MIGHT(Varbits.PRAYER_MYSTIC_MIGHT, 20.0D, WidgetInfoExt.PRAYER_MYSTIC_MIGHT),
    RETRIBUTION(Varbits.PRAYER_RETRIBUTION, 5.0D, WidgetInfoExt.PRAYER_RETRIBUTION),
    REDEMPTION(Varbits.PRAYER_REDEMPTION, 10.0D, WidgetInfoExt.PRAYER_REDEMPTION),
    SMITE(Varbits.PRAYER_SMITE, 30.0D, WidgetInfoExt.PRAYER_SMITE),
    CHIVALRY(Varbits.PRAYER_CHIVALRY, 40.0D, WidgetInfoExt.PRAYER_CHIVALRY),
    PIETY(Varbits.PRAYER_PIETY, 40.0D, WidgetInfoExt.PRAYER_PIETY),
    PRESERVE(Varbits.PRAYER_PRESERVE, 3.3333333333333335D, WidgetInfoExt.PRAYER_PRESERVE),
    RIGOUR(Varbits.PRAYER_RIGOUR, 40.0D, WidgetInfoExt.PRAYER_RIGOUR),
    AUGURY(Varbits.PRAYER_AUGURY, 40.0D, WidgetInfoExt.PRAYER_AUGURY);

    private final int varbit;
    private final double drainRate;
    private final WidgetInfoExt widgetInfo;

    public int getVarbit() {
        return this.varbit;
    }

    public double getDrainRate() {
        return this.drainRate;
    }

    public WidgetInfoExt getWidgetInfo() {
        return this.widgetInfo;
    }

    public net.runelite.api.Prayer getApiPrayer(){
        for (net.runelite.api.Prayer prayer : net.runelite.api.Prayer.values()) {
            if (prayer.getVarbit() == varbit)
                return prayer;
        }

        return net.runelite.api.Prayer.PROTECT_ITEM;
    }


    Prayer(int varbit, double drainRate, WidgetInfoExt widgetInfo) {
        this.varbit = varbit;
        this.drainRate = drainRate;
        this.widgetInfo = widgetInfo;
    }

	public static int getPrayerWidgetId(net.runelite.api.Prayer prayer)
	{
		return Prayer.valueOf(prayer.name()).getWidgetInfo().getId();
	}

}