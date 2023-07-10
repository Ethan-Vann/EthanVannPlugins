package com.example.RuneBotApi.Items;

import com.example.EthanApiPlugin.Collections.Inventory;
import com.example.InteractionApi.InventoryInteraction;
import net.runelite.api.ItemID;

public class Potions {

    private static final int[] brewIds = {ItemID.SARADOMIN_BREW1, ItemID.SARADOMIN_BREW2, ItemID.SARADOMIN_BREW3, ItemID.SARADOMIN_BREW4};
    private static final int[] restoreIds = {ItemID.SUPER_RESTORE1, ItemID.SUPER_RESTORE2, ItemID.SUPER_RESTORE3, ItemID.SUPER_RESTORE4};
    private static final int[] prayerIds = {ItemID.PRAYER_POTION1, ItemID.PRAYER_POTION2, ItemID.PRAYER_POTION3, ItemID.PRAYER_POTION4};
    private static final int[] superAttackIds = {ItemID.SUPER_ATTACK1, ItemID.SUPER_ATTACK2, ItemID.SUPER_ATTACK3, ItemID.SUPER_ATTACK4};
    private static final int[] superStrengthIds = {ItemID.SUPER_STRENGTH1, ItemID.SUPER_STRENGTH2, ItemID.SUPER_STRENGTH3, ItemID.SUPER_STRENGTH4};
    private static final int[] superCombatIds = {ItemID.SUPER_COMBAT_POTION1, ItemID.SUPER_COMBAT_POTION2, ItemID.SUPER_COMBAT_POTION3, ItemID.SUPER_COMBAT_POTION4};
    private static final int[] divineScpIds = {ItemID.DIVINE_SUPER_COMBAT_POTION1, ItemID.DIVINE_SUPER_COMBAT_POTION2, ItemID.DIVINE_SUPER_COMBAT_POTION3, ItemID.DIVINE_SUPER_COMBAT_POTION4};
    private static final int[] rangedIds = {ItemID.RANGING_POTION1, ItemID.RANGING_POTION2, ItemID.RANGING_POTION3, ItemID.RANGING_POTION4};
    private static final int[] divineRangedIds = {ItemID.DIVINE_RANGING_POTION1, ItemID.DIVINE_RANGING_POTION1, ItemID.DIVINE_RANGING_POTION1, ItemID.DIVINE_RANGING_POTION1};
    private static final int[] bastionIds = {ItemID.BASTION_POTION1, ItemID.BASTION_POTION2, ItemID.BASTION_POTION3, ItemID.BASTION_POTION4};
    private static final int[] divineBastionIds = {ItemID.DIVINE_BASTION_POTION1, ItemID.DIVINE_BASTION_POTION2, ItemID.DIVINE_BASTION_POTION3, ItemID.DIVINE_BASTION_POTION4};
    private static final int[] staminaIds = {ItemID.STAMINA_POTION1, ItemID.STAMINA_POTION2, ItemID.STAMINA_POTION3, ItemID.STAMINA_POTION4};

    public static boolean drinkPotion(PotionType type)
    {
        switch (type)
        {
            case SARA_BREW:             return consume(brewIds);
            case SUPER_RESTORE:         return consume(restoreIds);
            case PRAYER:                return consume(prayerIds);
            case SUPER_ATTACK:          return consume(superAttackIds);
            case SUPER_STRENGTH:        return consume(superStrengthIds);
            case SUPER_COMBAT:          return consume(superCombatIds);
            case DIVINE_SUPER_COMBAT:   return consume(divineScpIds);
            case RANGING:               return consume(rangedIds);
            case DIVINE_RANGING:        return consume(divineRangedIds);
            case BASTION:               return consume(bastionIds);
            case DIVINE_BASTION:        return consume(divineBastionIds);
            case STAMINA:               return consume(staminaIds);
        }

        return false;
    }

    private static boolean consume(int[] potionIds)
    {
        for (int i : potionIds)
        {
            if (!Inventory.search().withId(i).result().isEmpty())
            {
                InventoryInteraction.useItem(i, "Drink");
                return true;
            }
        }
        return false;
    }

    /**
     * drinks brews with the lowest dose having the highest priority
     * returns true if successful
     * @deprecated use drinkPotion(PotionType type)
     */
    public static boolean drinkBrew()
    {
        for (int i : brewIds)
        {
            if (!Inventory.search().withId(i).result().isEmpty())
            {
                InventoryInteraction.useItem(i, "Drink");
                return true;
            }
        }
        return false;
    }

    /**
     * drinks restores with the lowest dose having the highest priority
     * returns true if successful
     * @deprecated use drinkPotion(PotionType type)
     */
    public static boolean drinkRestore()
    {
        for (int i : restoreIds)
        {
            if (!Inventory.search().withId(i).result().isEmpty())
            {
                InventoryInteraction.useItem(i, "Drink");
                return true;
            }
        }
        return false;
    }
}
