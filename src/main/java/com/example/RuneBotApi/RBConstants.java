package com.example.RuneBotApi;

import net.runelite.api.ItemID;

public class RBConstants {

    // ~~~~Npcs~~~~
    public static final int ardyKnightId = 3297;

    // ~~~~Misc~~~~
    public static final int coinPouchId = 22531;


    // ~~~~Food~~~~

    /**
     * doesn't support blighted foods because that'd be cumbersome, and
     * if you use blighted foods you're a broke bitch anyways
     */
    public static final int[] foodIds = {
            ItemID.ANGLERFISH,
            ItemID.DARK_CRAB,
            ItemID.TUNA_POTATO,
            ItemID.MANTA_RAY,
            ItemID.HALF_A_WILD_PIE,
            ItemID.WILD_PIE,
            ItemID.HALF_A_SUMMER_PIE,
            ItemID.SUMMER_PIE,
            2303, // 1/2 pineapple pizza
            ItemID.PINEAPPLE_PIZZA,
            ItemID.SEA_TURTLE,
            ItemID.SHARK,
            ItemID.CURRY,
            ItemID.COOKED_KARAMBWAN,
            2299, // 1/2 anchovy pizza
            ItemID.ANCHOVY_PIZZA,
            ItemID.MONKFISH,
            2295, // 1/2 meat pizza
            ItemID.MEAT_PIZZA,
            ItemID.POTATO_WITH_CHEESE,
            ItemID.CHOCOLATE_BOMB,
            ItemID.TANGLED_TOADS_LEGS,
            ItemID.CHOCOLATE_CAKE,
            1899, // 1/3 chocolate cake
            1901, // 2/3 chocolate cake
            ItemID.APPLE_PIE,
            ItemID.HALF_AN_APPLE_PIE,
            ItemID.POTATO_WITH_BUTTER,
            ItemID.SWORDFISH,
            2291, // 1/2 plain pizza
            ItemID.PLAIN_PIZZA,
            ItemID.BASS,
            ItemID.LOBSTER,
            ItemID.HALF_A_MEAT_PIE,
            ItemID.MEAT_PIE,
            ItemID.SLICE_OF_CAKE,
            1893, // 2/3 cake
            ItemID.CAKE,
            ItemID.STEW,
            ItemID.TUNA,
            ItemID.SALMON,
            ItemID.TROUT,
            ItemID.GUTHIX_REST1,
            ItemID.GUTHIX_REST2,
            ItemID.GUTHIX_REST3,
            ItemID.GUTHIX_REST4,
            ItemID.SARADOMIN_BREW1,
            ItemID.SARADOMIN_BREW2,
            ItemID.SARADOMIN_BREW3,
            ItemID.SARADOMIN_BREW4
    };

    // ~~~~Potions~~~~
    public static final int[] brewIds = {ItemID.SARADOMIN_BREW1, ItemID.SARADOMIN_BREW2, ItemID.SARADOMIN_BREW3, ItemID.SARADOMIN_BREW4};
    public static final int[] restoreIds = {ItemID.SUPER_RESTORE1, ItemID.SUPER_RESTORE2, ItemID.SUPER_RESTORE3, ItemID.SUPER_RESTORE4};

}
