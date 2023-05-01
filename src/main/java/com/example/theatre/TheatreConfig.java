//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("Theatre")
public interface TheatreConfig extends Config {
    @ConfigSection(
            name = "General",
            description = "General Configurartion",
            position = 0
    )
    String generalSection = "General";
    @ConfigSection(
            name = "Prayer",
            description = "Prayer Configuration",
            position = 100
    )
    String prayerSection = "Prayer";
    @ConfigSection(
            name = "Maiden",
            description = "Maiden's Configuration",
            position = 200,
            closedByDefault = true
    )
    String maidenSection = "Maiden";
    @ConfigSection(
            name = "Bloat",
            description = "Bloat's Configuration",
            position = 300,
            closedByDefault = true
    )
    String bloatSection = "Bloat";
    @ConfigSection(
            name = "Nylocas",
            description = "Nylocas' Configuration",
            position = 400,
            closedByDefault = true
    )
    String nylocasSection = "Nylocas";
    @ConfigSection(
            name = "Sotetseg",
            description = "Sotetseg's Configuration",
            position = 500,
            closedByDefault = true
    )
    String sotetsegSection = "Sotetseg";
    @ConfigSection(
            name = "Xarpus",
            description = "Xarpus's Configuration",
            position = 600,
            closedByDefault = true
    )
    String xarpusSection = "Xarpus";
    @ConfigSection(
            name = "Verzik",
            description = "Verzik's Configuration",
            position = 700,
            closedByDefault = true
    )
    String verzikSection = "Verzik";
    @ConfigSection(
            name = "Misc",
            description = "Misc Configuration",
            position = 800
    )
    String miscSection = "Misc";

    @Range(
            max = 20
    )
    @ConfigItem(
            position = 1,
            keyName = "theatreFontSize",
            name = "Theatre Overlay Font Size",
            description = "Sets the font size for all theatre text overlays.",
            section = "General"
    )
    default int theatreFontSize() {
        return 12;
    }

    @ConfigItem(
            keyName = "fontStyle",
            name = "Font Style",
            description = "Bold/Italics/Plain.",
            position = 2,
            section = "General"
    )
    default FontStyle fontStyle() {
        return FontStyle.BOLD;
    }

    @ConfigItem(
            position = 101,
            keyName = "prayerHelper",
            name = "Prayer Helper",
            description = "Display prayer indicator in the prayer tab or in the bottom right corner of the screen",
            section = "Prayer"
    )
    default boolean prayerHelper() {
        return true;
    }

    @ConfigItem(
            position = 102,
            keyName = "descendingBoxes",
            name = "Prayer Descending Boxes",
            description = "Draws timing boxes above the prayer icons, as if you were playing Guitar Hero",
            section = "Prayer"
    )
    default boolean descendingBoxes() {
        return false;
    }

    @ConfigItem(
            position = 103,
            keyName = "indicateNonPriorityDescendingBoxes",
            name = "Indicate Non-Priority Boxes",
            description = "Render descending boxes for prayers that are not the priority prayer for that tick",
            section = "Prayer"
    )
    default boolean indicateNonPriorityDescendingBoxes() {
        return true;
    }

    @ConfigItem(
            position = 104,
            keyName = "alwaysShowPrayerHelper",
            name = "Always Show Prayer Helper",
            description = "Render prayer helper at all time, even when other inventory tabs are open.",
            section = "Prayer"
    )
    default boolean alwaysShowPrayerHelper() {
        return true;
    }

    @Alpha
    @ConfigItem(
            position = 105,
            keyName = "prayerColor",
            name = "Box Color",
            description = "Color for descending box normal",
            section = "Prayer"
    )
    default Color prayerColor() {
        return Color.ORANGE;
    }

    @Alpha
    @ConfigItem(
            position = 106,
            keyName = "prayerColorDanger",
            name = "Box Color Danger",
            description = "Color for descending box one tick before damage",
            section = "Prayer"
    )
    default Color prayerColorDanger() {
        return Color.RED;
    }

    @ConfigItem(
            position = 107,
            keyName = "verzikPrayerHelper",
            name = "Verzik",
            description = "Render prayers during the verzik fight",
            section = "Prayer"
    )
    default boolean verzikPrayerHelper() {
        return true;
    }

    @ConfigItem(
            position = 108,
            keyName = "sotetsegPrayerHelper",
            name = "Sotetseg",
            description = "Render prayers during the sotetseg fight",
            section = "Prayer"
    )
    default boolean sotetsegPrayerHelper() {
        return false;
    }

    @ConfigItem(
            position = 201,
            keyName = "maidenBlood",
            name = "Maiden Blood Attack Marker",
            description = "Highlights Maiden's Blood Pools.",
            section = "Maiden"
    )
    default boolean maidenBlood() {
        return true;
    }

    @ConfigItem(
            position = 202,
            keyName = "maidenSpawns",
            name = "Maiden Blood Spawns Marker",
            description = "Highlights Maiden Blood Spawns (Tomatoes).",
            section = "Maiden"
    )
    default boolean maidenSpawns() {
        return true;
    }

    @ConfigItem(
            position = 203,
            keyName = "maidenReds",
            name = "Maiden Reds Health Overlay",
            description = "Displays the health of each red crab.",
            section = "Maiden"
    )
    default boolean maidenRedsHealth() {
        return true;
    }

    @ConfigItem(
            position = 204,
            keyName = "maidenRedsDistance",
            name = "Maiden Reds Distance Overlay",
            description = "Displays the distance of each red crab to reach Maiden.",
            section = "Maiden"
    )
    default boolean maidenRedsDistance() {
        return true;
    }

    @ConfigItem(
            position = 205,
            keyName = "MaidenTickCounter",
            name = "Maiden Tank Tick Counter",
            description = "Displays the tick counter for when she decides who to choose for tanking.",
            section = "Maiden"
    )
    default boolean maidenTickCounter() {
        return true;
    }

    @ConfigItem(
            position = 301,
            keyName = "bloatIndicator",
            name = "Bloat Tile Indicator",
            description = "Highlights Bloat's Tile.",
            section = "Bloat"
    )
    default boolean bloatIndicator() {
        return true;
    }

    @Alpha
    @ConfigItem(
            position = 302,
            keyName = "bloatIndicatorColorUP",
            name = "Bloat Indicator Color - UP",
            description = "Select a color for when Bloat is UP.",
            section = "Bloat"
    )
    default Color bloatIndicatorColorUP() {
        return Color.CYAN;
    }

    @Alpha
    @ConfigItem(
            position = 303,
            keyName = "bloatIndicatorColorTHRESH",
            name = "Bloat Indicator Color - THRESHOLD",
            description = "Select a color for when Bloat UP and goes over 37 ticks, which allows you to know when he can go down.",
            section = "Bloat"
    )
    default Color bloatIndicatorColorTHRESH() {
        return Color.ORANGE;
    }

    @Alpha
    @ConfigItem(
            position = 304,
            keyName = "bloatIndicatorColorDOWN",
            name = "Bloat Indicator Color - DOWN",
            description = "Select a color for when Bloat is DOWN.",
            section = "Bloat"
    )
    default Color bloatIndicatorColorDOWN() {
        return Color.WHITE;
    }

    @Alpha
    @ConfigItem(
            position = 305,
            keyName = "bloatIndicatorColorWARN",
            name = "Bloat Indicator Color - WARN",
            description = "Select a color for when Bloat is DOWN and about to get UP.",
            section = "Bloat"
    )
    default Color bloatIndicatorColorWARN() {
        return Color.RED;
    }

    @ConfigItem(
            position = 306,
            keyName = "bloatTickCounter",
            name = "Bloat Tick Counter",
            description = "Displays the tick counter for how long Bloat has been DOWN or UP.",
            section = "Bloat"
    )
    default boolean bloatTickCounter() {
        return true;
    }

    @ConfigItem(
            position = 307,
            keyName = "BloatTickCountStyle",
            name = "Bloat Tick Time Style",
            description = "Count up or Count down options on bloat downed state",
            section = "Bloat"
    )
    default BLOATTIMEDOWN BloatTickCountStyle() {
        return BLOATTIMEDOWN.COUNTDOWN;
    }

    @ConfigItem(
            position = 308,
            keyName = "bloatHands",
            name = "Bloat Hands Overlay",
            description = "Highlights the tiles where Bloat's hands will fall.",
            section = "Bloat"
    )
    default boolean bloatHands() {
        return false;
    }

    @Alpha
    @ConfigItem(
            position = 309,
            keyName = "bloatHandsColor",
            name = "Bloat Hands Overlay Color",
            description = "Select a color for the Bloat Hands Overlay to be.",
            section = "Bloat"
    )
    default Color bloatHandsColor() {
        return Color.CYAN;
    }

    @Range(
            max = 10
    )
    @ConfigItem(
            position = 310,
            keyName = "bloatHandsWidth",
            name = "Bloat Hands Overlay Thickness",
            description = "Sets the stroke width of the tile overlay where the hands fall. (BIGGER = THICKER).",
            section = "Bloat"
    )
    default int bloatHandsWidth() {
        return 2;
    }

    @ConfigItem(
            name = "Hide Bloat Tank",
            keyName = "hideBloatTank",
            description = "Hides the entire Bloat tank in the center of the room",
            position = 311,
            section = "Bloat"
    )
    default boolean hideBloatTank() {
        return false;
    }

    @ConfigItem(
            name = "Hide Ceiling Chains",
            keyName = "hideCeilingChains",
            description = "Hides the chains hanging from the ceiling in the Bloat room",
            position = 312,
            section = "Bloat"
    )
    default boolean hideCeilingChains() {
        return false;
    }

    @ConfigItem(
            position = 401,
            keyName = "nyloPillars",
            name = "Nylocas Pillar Health Overlay",
            description = "Displays the health percentage of the pillars.",
            section = "Nylocas"
    )
    default boolean nyloPillars() {
        return true;
    }

    @ConfigItem(
            position = 402,
            keyName = "nyloExplosions",
            name = "Nylocas Explosion Warning",
            description = "Highlights a Nylocas that is about to explode.",
            section = "Nylocas"
    )
    default boolean nyloExplosions() {
        return true;
    }

    @Range(
            max = 52
    )
    @ConfigItem(
            position = 403,
            keyName = "nyloExplosionDisplayTicks",
            name = "Nylocas Display Last Ticks",
            description = "Displays the last 'x' amount of ticks for a Nylocas. (ex: to see the last 10 ticks, you set it to 10).",
            section = "Nylocas"
    )
    default int nyloExplosionDisplayTicks() {
        return 46;
    }

    @ConfigItem(
            position = 404,
            keyName = "nyloExplosionDisplayStyle",
            name = "Nylocas Display Explosion Style",
            description = "How to display when a nylocas is about to explode.",
            section = "Nylocas"
    )
    default EXPLOSIVENYLORENDERSTYLE nyloExplosionOverlayStyle() {
        return EXPLOSIVENYLORENDERSTYLE.RECOLOR_TICK;
    }

    @ConfigItem(
            position = 405,
            keyName = "nyloTimeAlive",
            name = "Nylocas Tick Time Alive",
            description = "Displays the tick counter of each nylocas spawn (Explodes on 52).",
            section = "Nylocas"
    )
    default boolean nyloTimeAlive() {
        return false;
    }

    @ConfigItem(
            position = 406,
            keyName = "nyloTimeAliveCountStyle",
            name = "Nylocas Tick Time Alive Style",
            description = "Count up or Count down options on the tick time alive.",
            section = "Nylocas"
    )
    default NYLOTIMEALIVE nyloTimeAliveCountStyle() {
        return NYLOTIMEALIVE.COUNTDOWN;
    }

    @ConfigItem(
            position = 407,
            keyName = "nyloRecolorMenu",
            name = "Nylocas Recolor Menu Options",
            description = "Recolors the menu options of each Nylocas to it's respective attack style.",
            section = "Nylocas"
    )
    default boolean nyloRecolorMenu() {
        return false;
    }

    @ConfigItem(
            position = 408,
            keyName = "nyloHighlightOverlay",
            name = "Nylocas Highlight Overlay",
            description = "Select your role to highlight respective Nylocas to attack.",
            section = "Nylocas"
    )
    default boolean nyloHighlightOverlay() {
        return false;
    }

    @ConfigItem(
            position = 409,
            keyName = "nyloAliveCounter",
            name = "Nylocas Alive Counter Panel",
            description = "Displays how many Nylocas are currently alive.",
            section = "Nylocas"
    )
    default boolean nyloAlivePanel() {
        return false;
    }

    @ConfigItem(
            position = 410,
            keyName = "nyloAggressiveOverlay",
            name = "Highlight Aggressive Nylocas",
            description = "Highlights aggressive Nylocas after they spawn.",
            section = "Nylocas"
    )
    default boolean nyloAggressiveOverlay() {
        return true;
    }

    @ConfigItem(
            position = 411,
            keyName = "nyloAggressiveOverlayStyle",
            name = "Highlight Aggressive Nylocas Style",
            description = "Highlight style for aggressive Nylocas after they spawn.",
            section = "Nylocas"
    )
    default AGGRESSIVENYLORENDERSTYLE nyloAggressiveOverlayStyle() {
        return AGGRESSIVENYLORENDERSTYLE.TILE;
    }

    @ConfigItem(
            position = 412,
            keyName = "removeNyloEntries",
            name = "Remove Attack Options",
            description = "Removes the attack options for Nylocas immune to your current attack style.",
            section = "Nylocas"
    )
    default boolean removeNyloEntries() {
        return true;
    }

    @ConfigItem(
            position = 413,
            keyName = "nylocasWavesHelper",
            name = "Nylocas Waves Helper",
            description = "Overlay's squares with wave numbers on nylo entry bridges for upcoming nylos",
            section = "Nylocas"
    )
    default boolean nyloWavesHelper() {
        return false;
    }

    @ConfigItem(
            position = 414,
            keyName = "nylocasTicksUntilWave",
            name = "Nylocas Ticks Until Wave",
            description = "Prints how many ticks until the next wave could spawn",
            section = "Nylocas"
    )
    default boolean nyloTicksUntilWaves() {
        return false;
    }

    @ConfigItem(
            position = 415,
            keyName = "nyloInstanceTimer",
            name = "Nylocas Instance Timer",
            description = "Displays an instance timer when the next set will potentially spawn - ENTER ON ZERO.",
            section = "Nylocas"
    )
    default boolean nyloInstanceTimer() {
        return false;
    }

    @ConfigItem(
            position = 416,
            keyName = "nyloStallMessage",
            name = "Nylocas Stall Wave Messages",
            description = "Sends a chat message when you have stalled the next wave of Nylocas to spawn due to being capped.",
            section = "Nylocas"
    )
    default boolean nyloStallMessage() {
        return false;
    }

    @ConfigItem(
            position = 417,
            keyName = "nylocasBigSplitsHelper",
            name = "Nylocas Big Splits",
            description = "Tells you when bigs will spawn little nylos",
            section = "Nylocas"
    )
    default boolean bigSplits() {
        return false;
    }

    @ConfigItem(
            position = 418,
            keyName = "nylocasBigSplitsHighlightColor",
            name = "Highlight Color",
            description = "Color of the NPC highlight",
            section = "Nylocas"
    )
    @Alpha
    default Color getBigSplitsHighlightColor() {
        return Color.YELLOW;
    }

    @ConfigItem(
            position = 419,
            keyName = "nylocasBigSplitsTileColor2",
            name = "Highlight Color Tick 2",
            description = "Color of the NPC highlight on tick 1",
            section = "Nylocas"
    )
    @Alpha
    default Color getBigSplitsTileColor2() {
        return Color.ORANGE;
    }

    @ConfigItem(
            position = 420,
            keyName = "nylocasBigSplitsTileColor1",
            name = "Highlight Color Tick 1",
            description = "Color of the NPC highlight on tick 0",
            section = "Nylocas"
    )
    @Alpha
    default Color getBigSplitsTileColor1() {
        return Color.RED;
    }

    @ConfigItem(
            position = 421,
            keyName = "nylocasBigSplitsTextColor2",
            name = "Text Color Tick 2",
            description = "Color of the baby tick counter on tick 2",
            section = "Nylocas"
    )
    @Alpha
    default Color getBigSplitsTextColor2() {
        return Color.ORANGE;
    }

    @ConfigItem(
            position = 422,
            keyName = "nylocasBigSplitsTextColor1",
            name = "Text Color Tick 1",
            description = "Color of the baby tick counter on tick 1",
            section = "Nylocas"
    )
    @Alpha
    default Color getBigSplitsTextColor1() {
        return Color.RED;
    }

    @ConfigItem(
            position = 423,
            keyName = "nyloBossAttackTickCount",
            name = "Nylocas Boss Attack Tick Counter",
            description = "Displays the ticks left until the Nylocas Boss will attack next (LEFT-MOST).",
            section = "Nylocas"
    )
    default boolean nyloBossAttackTickCount() {
        return false;
    }

    @ConfigItem(
            position = 424,
            keyName = "nyloBossSwitchTickCount",
            name = "Nylocas Boss Switch Tick Counter",
            description = "Displays the ticks left until the Nylocas Boss will switch next (MIDDLE).",
            section = "Nylocas"
    )
    default boolean nyloBossSwitchTickCount() {
        return true;
    }

    @ConfigItem(
            position = 425,
            keyName = "nyloBossTotalTickCount",
            name = "Nylocas Boss Total Tick Counter",
            description = "Displays the total ticks since the Nylocas Boss has spawned (RIGHT-MOST).",
            section = "Nylocas"
    )
    default boolean nyloBossTotalTickCount() {
        return false;
    }

    @ConfigItem(
            position = 426,
            keyName = "removeNyloBossEntries",
            name = "Nylocas Boss Remove Attack Options",
            description = "Removes the attack options for Nylocas Boss when immune to your current attack style.",
            section = "Nylocas"
    )
    default boolean removeNyloBossEntries() {
        return true;
    }

    @ConfigItem(
            position = 501,
            keyName = "sotetsegMaze",
            name = "Sotetseg Maze",
            description = "Memorizes Solo Mazes and displays tiles of other chosen players.",
            section = "Sotetseg"
    )
    default boolean sotetsegMaze() {
        return true;
    }

    @ConfigItem(
            position = 502,
            keyName = "sotetsegOrbAttacksTicks",
            name = "Sotetseg Small Attack Orb Ticks",
            description = "Displays the amount of ticks until it will hit you (change prayers when you see 1).",
            section = "Sotetseg"
    )
    default boolean sotetsegOrbAttacksTicks() {
        return true;
    }

    @ConfigItem(
            position = 503,
            keyName = "sotetsegAutoAttacksTicks",
            name = "Sotetseg Auto Attack Ticks",
            description = "Displays a tick counter for when Sotetseg will attack next.",
            section = "Sotetseg"
    )
    default boolean sotetsegAutoAttacksTicks() {
        return true;
    }

    @ConfigItem(
            position = 504,
            keyName = "sotetsegAttackCounter",
            name = "Sotetseg Attack Counter",
            description = "Countdown until death ball.",
            section = "Sotetseg"
    )
    default boolean sotetsegAttackCounter() {
        return true;
    }

    @ConfigItem(
            position = 505,
            keyName = "sotetsegBigOrbTicks",
            name = "Sotetseg Big Ball Tick Overlay",
            description = "Displays how many ticks until the ball will explode (eat when you see 0).",
            section = "Sotetseg"
    )
    default boolean sotetsegBigOrbTicks() {
        return true;
    }

    @Alpha
    @ConfigItem(
            position = 506,
            keyName = "sotetsegBigOrbTickColor",
            name = "Sotetseg Big Ball Tick Color",
            description = "Select a color for the Sotetseg Big Ball tick countdown text.",
            section = "Sotetseg"
    )
    default Color sotetsegBigOrbTickColor() {
        return Color.WHITE;
    }

    @Alpha
    @ConfigItem(
            position = 507,
            keyName = "sotetsegBigOrbTileColor",
            name = "Sotetseg Big Ball Tile Color",
            description = "Select a color for the Sotetseg Big Ball tile color.",
            section = "Sotetseg"
    )
    default Color sotetsegBigOrbTileColor() {
        return new Color(188, 74, 74, 255);
    }

    @ConfigItem(
            position = 601,
            keyName = "xarpusInstanceTimer",
            name = "Xarpus Instance Timer",
            description = "Displays the Xarpus Instance timer to be tick efficient with the first spawn of an exhumed - ENTER ON ZERO.",
            section = "Xarpus"
    )
    default boolean xarpusInstanceTimer() {
        return true;
    }

    @ConfigItem(
            position = 602,
            keyName = "xarpusExhumed",
            name = "Xarpus Exhumed Markers",
            description = "Highlights the tiles of exhumed spawns.",
            section = "Xarpus"
    )
    default boolean xarpusExhumed() {
        return true;
    }

    @ConfigItem(
            position = 603,
            keyName = "xarpusExhumedTick",
            name = "Xarpus Exhumed Ticks",
            description = "Displays how many ticks until the exhumeds will despawn.",
            section = "Xarpus"
    )
    default boolean xarpusExhumedTick() {
        return true;
    }

    @ConfigItem(
            position = 604,
            keyName = "xarpusExhumedCount",
            name = "Xarpus Exhumed Count",
            description = "Count the amount of exhumeds.",
            section = "Xarpus"
    )
    default XARPUS_EXHUMED_COUNT xarpusExhumedCount() {
        return XARPUS_EXHUMED_COUNT.DOWN;
    }

    @ConfigItem(
            position = 605,
            keyName = "xarpusTickP2",
            name = "Xarpus Attack Tick - P2",
            description = "Displays a tick counter for when Xarpus faces a new target to spit at.",
            section = "Xarpus"
    )
    default boolean xarpusTickP2() {
        return true;
    }

    @ConfigItem(
            position = 606,
            keyName = "xarpusTickP3",
            name = "Xarpus Attack Tick - P3",
            description = "Displays a tick counter for when Xarpus will rotate.",
            section = "Xarpus"
    )
    default boolean xarpusTickP3() {
        return true;
    }

    @ConfigItem(
            position = 607,
            name = "Line of Sight",
            keyName = "xarpusLineOfSight",
            description = "Displays Xarpus's Line of Sight on P3<br>Melee Tiles: Displays only the melee tiles that Xarpus can see<br>Square: Displays the whole region that Xarpus can see",
            section = "Xarpus"
    )
    default XARPUS_LINE_OF_SIGHT xarpusLineOfSight() {
        return XARPUS_LINE_OF_SIGHT.OFF;
    }

    @Alpha
    @ConfigItem(
            position = 608,
            name = "Line of Sight Color",
            keyName = "xarpusLineOfSightColor",
            description = "Customize the color for Xarpus's Line of Sight",
            section = "Xarpus"
    )
    default Color xarpusLineOfSightColor() {
        return Color.RED;
    }

    @ConfigItem(
            position = 701,
            keyName = "verzikTileOverlay",
            name = "Verzik Tile Indicator",
            description = "Highlights Verzik's tile - If you are next to or inside of the indicator, you can be meleed.",
            section = "Verzik"
    )
    default boolean verzikTileOverlay() {
        return true;
    }

    @ConfigItem(
            position = 702,
            keyName = "verzikProjectiles",
            name = "Verzik Range Tile Markers",
            description = "Highlights the tiles of Verzik's range projectiles.",
            section = "Verzik"
    )
    default boolean verzikProjectiles() {
        return true;
    }

    @Alpha
    @ConfigItem(
            position = 703,
            keyName = "verzikProjectilesColor",
            name = "Verzik Range Tile Markers Color",
            description = "Select a color for the Verzik's Range Projectile Tile Overlay to be.",
            section = "Verzik"
    )
    default Color verzikProjectilesColor() {
        return new Color(255, 0, 0, 50);
    }

    @ConfigItem(
            position = 704,
            keyName = "VerzikRedHP",
            name = "Verzik Reds Health Overlay",
            description = "Displays the health of red crabs during Verzik.",
            section = "Verzik"
    )
    default boolean verzikReds() {
        return true;
    }

    @ConfigItem(
            position = 705,
            keyName = "verzikAutosTick",
            name = "Verzik Attack Tick Counter",
            description = "Displays the ticks until Verzik will attack next.",
            section = "Verzik"
    )
    default boolean verzikAutosTick() {
        return true;
    }

    @ConfigItem(
            position = 706,
            keyName = "verzikAttackCounter",
            name = "Verzik Attack Counter",
            description = "Displays Verzik's Attack Count (useful for when P2 reds as they despawn after the 7th attack).",
            section = "Verzik"
    )
    default boolean verzikAttackCounter() {
        return true;
    }

    @ConfigItem(
            position = 707,
            keyName = "verzikTotalTickCounter",
            name = "Verzik Total Tick Counter",
            description = "Displays the total amount of ticks Verzik has been alive for.",
            section = "Verzik"
    )
    default boolean verzikTotalTickCounter() {
        return true;
    }

    @ConfigItem(
            position = 708,
            keyName = "verzikNyloPersonalWarning",
            name = "Verzik Nylo Direct Aggro Warning",
            description = "Highlights the Nylocas that are targeting YOU and ONLY you.",
            section = "Verzik"
    )
    default boolean verzikNyloPersonalWarning() {
        return true;
    }

    @ConfigItem(
            position = 709,
            keyName = "verzikNyloOtherWarning",
            name = "Verzik Nylo Indirect Aggro Warnings",
            description = "Highlights the Nylocas that are targeting OTHER players.",
            section = "Verzik"
    )
    default boolean verzikNyloOtherWarning() {
        return true;
    }

    @ConfigItem(
            position = 710,
            keyName = "lightningAttackHelper",
            name = "Lightning Attack Helper",
            description = "Displays the number of attacks before a lightning ball.",
            section = "Verzik"
    )
    default boolean lightningAttackHelper() {
        return false;
    }

    @ConfigItem(
            position = 711,
            keyName = "lightningAttackTick",
            name = "Lightning Attack Tick",
            description = "Displays the number of ticks before a lightning ball hits you.",
            section = "Verzik"
    )
    default boolean lightningAttackTick() {
        return false;
    }

    @ConfigItem(
            position = 712,
            keyName = "verzikAttackPurpleNyloMES",
            name = "Remove Purple Nylo MES",
            description = "Removes the ability to attack the Purple nylo if you cannot poison it",
            section = "Verzik"
    )
    default boolean purpleCrabAttackMES() {
        return false;
    }

    @ConfigItem(
            position = 713,
            keyName = "weaponSet",
            name = "Poison Weapons",
            description = "If a weapon is added to this set, it will NOT deprio attack on Nylocas Athanatos.",
            section = "Verzik"
    )
    default String weaponSet() {
        return "12926, 12006, 22292, 12899";
    }

    @ConfigItem(
            position = 714,
            keyName = "verzikNyloExplodeAOE",
            name = "Verzik Nylo Explosion Area",
            description = "Highlights the area of explosion for the Nylocas (Personal or Indirect Warnings MUST be enabled).",
            section = "Verzik"
    )
    default boolean verzikNyloExplodeAOE() {
        return true;
    }

    @ConfigItem(
            position = 715,
            keyName = "verzikDisplayTank",
            name = "Verzik Display Tank",
            description = "Highlights the tile of the player tanking to help clarify.",
            section = "Verzik"
    )
    default boolean verzikDisplayTank() {
        return true;
    }

    @ConfigItem(
            position = 716,
            keyName = "verzikYellows",
            name = "Verzik Yellows Overlay",
            description = "Highlights the yellow pools and displays the amount of ticks until you can move away or tick eat.",
            section = "Verzik"
    )
    default boolean verzikYellows() {
        return true;
    }

    @ConfigItem(
            position = 717,
            keyName = "verzikGreenBall",
            name = "Verzik Green Ball Tank",
            description = "Displays who the green ball is targeting.",
            section = "Verzik"
    )
    default boolean verzikGreenBall() {
        return true;
    }

    @Alpha
    @ConfigItem(
            position = 718,
            keyName = "verzikGreenBallColor",
            name = "Verzik Green Ball Highlight Color",
            description = "Select a color for the Verzik's Green Ball Tile Overlay to be.",
            section = "Verzik"
    )
    default Color verzikGreenBallColor() {
        return new Color(59, 140, 83, 255);
    }

    @ConfigItem(
            position = 719,
            keyName = "verzikGreenBallMarker",
            name = "Verzik Green Ball Marker",
            description = "Choose between a tile or 3-by-3 area marker.",
            section = "Verzik"
    )
    default VERZIKBALLTILE verzikGreenBallMarker() {
        return VERZIKBALLTILE.TILE;
    }

    @ConfigItem(
            position = 720,
            keyName = "verzikGreenBallTick",
            name = "Verzik Green Ball Tick",
            description = "Displays the number of ticks until the green ball nukes you.",
            section = "Verzik"
    )
    default boolean verzikGreenBallTick() {
        return false;
    }

    @ConfigItem(
            position = 721,
            keyName = "verzikTornado",
            name = "Verzik Personal Tornado Highlight",
            description = "Displays the tornado that is targeting you.",
            section = "Verzik"
    )
    default boolean verzikTornado() {
        return true;
    }

    @ConfigItem(
            position = 722,
            keyName = "verzikPersonalTornadoOnly",
            name = "Verzik ONLY Highlight Personal",
            description = "Displays the tornado that is targeting you ONLY after it solves which one is targeting you.",
            section = "Verzik"
    )
    default boolean verzikPersonalTornadoOnly() {
        return false;
    }

    @Alpha
    @ConfigItem(
            position = 723,
            keyName = "verzikTornadoColor",
            name = "Verzik Tornado Highlight Color",
            description = "Select a color for the Verzik Tornadoes Overlay to be.",
            section = "Verzik"
    )
    default Color verzikTornadoColor() {
        return Color.RED;
    }

    @ConfigItem(
            position = 724,
            keyName = "verzikPoisonTileHighlight",
            name = "Verzik Poison Tile Highlight",
            description = "Highlight tile with disappearing poison",
            section = "Verzik"
    )
    default boolean verzikPoisonTileHighlight() {
        return true;
    }

    @Alpha
    @ConfigItem(
            position = 725,
            keyName = "verzikPoisonTileHighlightColor",
            name = "Verzik Poison Tile Highlight Color",
            description = "Select a color for the Verzik poison tiles.",
            section = "Verzik"
    )
    default Color verzikPoisonTileHighlightColor() {
        return new Color(184, 246, 196, 152);
    }

    @ConfigItem(
            keyName = "highlightMelee",
            name = "",
            description = "",
            section = "Misc",
            position = 801
    )
    default boolean getHighlightMeleeNylo() {
        return false;
    }

    @ConfigItem(
            keyName = "highlightMelee",
            name = "",
            description = "",
            section = "Misc",
            position = 802
    )
    void setHighlightMeleeNylo(boolean var1);

    @ConfigItem(
            keyName = "highlightMage",
            name = "",
            description = "",
            section = "Misc",
            position = 803
    )
    default boolean getHighlightMageNylo() {
        return false;
    }

    @ConfigItem(
            keyName = "highlightMage",
            name = "",
            description = "",
            section = "Misc",
            position = 804
    )
    void setHighlightMageNylo(boolean var1);

    @ConfigItem(
            keyName = "highlightRange",
            name = "",
            description = "",
            section = "Misc",
            position = 805
    )
    default boolean getHighlightRangeNylo() {
        return false;
    }

    @ConfigItem(
            keyName = "highlightRange",
            name = "",
            description = "",
            section = "Misc",
            position = 806
    )
    void setHighlightRangeNylo(boolean var1);

    public static enum VERZIKBALLTILE {
        TILE,
        AREA;

        private VERZIKBALLTILE() {
        }
    }

    public static enum XARPUS_LINE_OF_SIGHT {
        OFF,
        SQUARE,
        MELEE_TILES;

        private XARPUS_LINE_OF_SIGHT() {
        }
    }

    public static enum XARPUS_EXHUMED_COUNT {
        OFF,
        DOWN,
        UP;

        private XARPUS_EXHUMED_COUNT() {
        }
    }

    public static enum AGGRESSIVENYLORENDERSTYLE {
        TILE,
        HULL;

        private AGGRESSIVENYLORENDERSTYLE() {
        }
    }

    public static enum EXPLOSIVENYLORENDERSTYLE {
        TILE,
        RECOLOR_TICK;

        private EXPLOSIVENYLORENDERSTYLE() {
        }
    }

    public static enum NYLOTIMEALIVE {
        COUNTUP,
        COUNTDOWN;

        private NYLOTIMEALIVE() {
        }
    }

    public static enum BLOATTIMEDOWN {
        COUNTUP,
        COUNTDOWN;

        private BLOATTIMEDOWN() {
        }
    }

    public static enum FontStyle {
        BOLD("Bold", 1),
        ITALIC("Italic", 2),
        PLAIN("Plain", 0);

        private final String name;
        private final int font;

        public String toString() {
            return this.getName();
        }

        String getName() {
            return this.name;
        }

        int getFont() {
            return this.font;
        }

        private FontStyle(String name, int font) {
            this.name = name;
            this.font = font;
        }
    }
}
