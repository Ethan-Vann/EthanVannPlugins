package com.example.faldita;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("nightmareOfAshihama")
public interface FaldaConfig extends Config {
	@ConfigSection(name = "General", description = "Configure general settings.", position = 0)
	public static final String generalSection = "General";

	@ConfigSection(name = "Totems", description = "Configure Totems settings.", position = 1)
	public static final String totemsSection = "Totems";

	@ConfigSection(name = "Shadows", description = "Configure Shadows settings.", position = 2)
	public static final String shadowsSection = "Shadows";

	@ConfigSection(name = "Spores", description = "Configure Spores settings.", position = 3)
	public static final String sporesSection = "Spores";

	@ConfigSection(name = "Parasites", description = "Configure Parasites settings.", position = 4)
	public static final String parasitesSection = "Parasites";

	@ConfigSection(name = "Husk", description = "Configure Husk settings.", position = 5)
	public static final String huskSection = "Husk";

	@ConfigSection(name = "Charge", description = "Configure Charge settings.", position = 6)
	public static final String chargeSection = "Charge";

	@ConfigItem(
		keyName = "prayerHelper",
		name = "Prayer helper",
		description = "Displays the correct prayer to use at various points in the fight.",
		position = 0,
		section = "General"
	)
	default PrayerDisplay prayerHelper() {
		return PrayerDisplay.BOTH;
	}

	@ConfigItem(
			keyName = "tickCounter",
			name = "Show Ticks",
			description = "Displays the number of ticks until next attack",
			position = 1,
			section = "General"
	)
	default boolean ticksCounter() {
		return true;
	}

	@ConfigItem(keyName = "hideAttackNightmareTotems", name = "Hide Attack during Totems", description = "Remove the attack option on Nightmare during Totems", position = 2, section = "General")
	default boolean hideAttackNightmareTotems() {
		return false;
	}

	@ConfigItem(keyName = "hideAttackNightmareParasites", name = "Hide Attack during Parasites", description = "Remove the attack option on Nightmare during Parasites", position = 3, section = "General")
	default boolean hideAttackNightmareParasites() {
		return false;
	}

	@ConfigItem(keyName = "hideAttackNightmareHusk", name = "Hide Attack during Husks", description = "Remove the attack option on Nightmare during Husks", position = 4, section = "General")
	default boolean hideAttackNightmareHusk() {
		return false;
	}

	@ConfigItem(keyName = "hideAttackNightmareSleepwalkers", name = "Hide Attack during Sleepwalkers", description = "Remove the attack option on Nightmare during Sleepwalkers (not on last phase of Phosanis)", position = 5, section = "General")
	default boolean hideAttackNightmareSleepwalkers() {
		return false;
	}

	@ConfigItem(keyName = "hideAttackSleepwalkers", name = "Hide Attack Sleepwalkers Last Phase", description = "Remove the attack option on Sleepwalkers during the last phase of Phosanis", position = 6, section = "General")
	default boolean hideAttackSleepwalkers() {
		return false;
	}

	@ConfigItem(keyName = "highlightTotems", name = "Highlight Totems", description = "Highlights Totems based on their status", position = 0, section = "Totems")
	default boolean highlightTotems() {
		return true;
	}

	@Range(min = 1, max = 10)
	@ConfigItem(
		name = "Totem Outline Size",
		description = "Change the size of the totem outline.",
		position = 1, keyName = "totemOutlineSize",
		section = "Totems",
		hidden = true)
	@Units("PIXELS")
	default int totemOutlineSize() {
		return 3;
	}

	@ConfigItem(keyName = "highlightShadows", name = "Highlight Shadows", description = "Highlights the Shadow Attacks", position = 0, section = "Shadows")
	default boolean highlightShadows() {
		return true;
	}

	@ConfigItem(keyName = "shadowsTickCounter", name = "Shadows Tick Counter", description = "Displays the number of ticks until shadows do damage", position = 1, section = "Shadows")
	default boolean shadowsTickCounter() {
		return true;
	}

	@Range(max = 20, min = 1)
	@ConfigItem(keyName = "shadowsRenderDistance", name = "Render Distance", description = "Render shadows distance in tiles from your player", position = 2, section = "Shadows")
	@Units("tiles")
	default int shadowsRenderDistance() {
		return 5;
	}

	@Alpha
	@ConfigItem(keyName = "shadowsBorderColour", name = "Shadows border colour", description = "Colour the edges of the area highlighted by shadows", position = 3, section = "Shadows")
	default Color shadowsBorderColour() {
		return new Color(0, 255, 255, 100);
	}

	@Alpha
	@ConfigItem(keyName = "shadowsColour", name = "Shadows colour", description = "Colour for shadows highlight", position = 4, section = "Shadows")
	default Color shadowsColour() {
		return new Color(0, 255, 255, 50);
	}

	@ConfigItem(keyName = "highlightSpores", name = "Highlight Spores", description = "Highlights spores that will make you yawn", position = 0, section = "Spores")
	default boolean highlightSpores() {
		return true;
	}

	@Alpha
	@ConfigItem(keyName = "poisonBorderCol", name = "Poison border colour", description = "Colour the edges of the area highlighted by poison special will be", position = 1, section = "Spores")
	default Color poisonBorderCol() {
		return new Color(255, 0, 0, 100);
	}

	@Alpha
	@ConfigItem(keyName = "poisonCol", name = "Poison colour", description = "Colour the fill of the area highlighted by poison special will be", position = 2, section = "Spores")
	default Color poisonCol() {
		return new Color(255, 0, 0, 50);
	}

	@ConfigItem(keyName = "yawnInfoBox", name = "Yawn InfoBox", description = "InfoBox telling you the time until your yawning ends", position = 3, section = "Spores")
	default boolean yawnInfoBox() {
		return true;
	}

	@ConfigItem(keyName = "showTicksUntilParasite", name = "Indicate Parasites", description = "Displays a red tick timer on any impregnated players", position = 0, section = "Parasites")
	default boolean showTicksUntilParasite() {
		return true;
	}

	@ConfigItem(keyName = "parasitesInfoBox", name = "Parasites InfoBox", description = "InfoBox telling you the time until parasites", position = 1, section = "Parasites")
	default boolean parasitesInfoBox() {
		return true;
	}

	@ConfigItem(keyName = "sanfewReminder", name = "Sanfew Reminder", description = "Overlay that reminds you to drink a sanfew when impregnated", position = 2, section = "Parasites")
	default boolean sanfewReminder() {
		return true;
	}

	@ConfigItem(keyName = "flash", name = "Flash screen when impregnated", description = "Your Screen flashes when the nightmare infects you with her parasite", position = 3, section = "Parasites")
	default boolean flash() {
		return false;
	}

	@ConfigItem(keyName = "highlightHusk", name = "Highlight Husk", description = "Highlights the mage and range husk", position = 0, section = "Husk")
	default boolean huskHighlight() {
		return true;
	}

	@ConfigItem(keyName = "highlightHuskTarget", name = "Highlight Husk Target(s)", description = "Highlights whoever the husks will spawn on", position = 1, section = "Husk")
	default boolean highlightHuskTarget() {
		return true;
	}

	@Alpha
	@ConfigItem(keyName = "huskBorderCol", name = "Husk Target Border Color", description = "Colour the edges of the area highlighted by poison special will be", position = 2, section = "Husk")
	default Color huskBorderCol() {
		return new Color(255, 0, 0, 100);
	}

	@ConfigItem(keyName = "highlightNightmareHitboxOnCharge", name = "Highlight Nightmare's Hitbox On Charge", description = "Highlights the hitbox of the Nightmare when she charges", position = 0, section = "Charge")
	default boolean highlightNightmareHitboxOnCharge() {
		return true;
	}

	@ConfigItem(keyName = "highlightNightmareChargeRange", name = "Highlight Nightmare's Charge Range", description = "Highlights the range the Nightmare will damage you with her charge attack", position = 1, section = "Charge")
	default boolean highlightNightmareChargeRange() {
		return true;
	}

	@Alpha
	@ConfigItem(keyName = "nightmareChargeBorderCol", name = "Nightmare Charge Border Color", description = "Color the edges of the area highlighted by the nightmare's charge attack", position = 2, section = "Charge")
	default Color nightmareChargeBorderCol() {
		return new Color(255, 0, 0, 100);
	}

	@Alpha
	@ConfigItem(keyName = "nightmareChargeCol", name = "Nightmare charge fill color", description = "Color the fill of the area highlighted by the nightmare's charge attack", position = 3, section = "Charge")
	default Color nightmareChargeCol() {
		return new Color(255, 0, 0, 50);
	}

	public enum PrayerDisplay {
		PRAYER_TAB, BOTTOM_RIGHT, BOTH;

		public boolean showInfoBox() {
			return true;

			// Byte code:
			//   0: getstatic net/runelite/client/plugins/nightmare/NightmareConfig$1.$SwitchMap$net$runelite$client$plugins$nightmare$NightmareConfig$PrayerDisplay : [I
			//   3: aload_0
			//   4: invokevirtual ordinal : ()I
			//   7: iaload
			//   8: lookupswitch default -> 38, 1 -> 36, 2 -> 36
			//   36: iconst_1
			//   37: ireturn
			//   38: iconst_0
			//   39: ireturn
			// Line number table:
			//   Java source line number -> byte code offset
			//   #479	-> 0
			//   #483	-> 36
			//   #485	-> 38
			// Local variable table:
			//   start	length	slot	name	descriptor
			//   0	40	0	this	Lnet/runelite/client/plugins/nightmare/NightmareConfig$PrayerDisplay;
		}

		public boolean showWidgetHelper() {
			return true;
			// Byte code:
			//   0: getstatic net/runelite/client/plugins/nightmare/NightmareConfig$1.$SwitchMap$net$runelite$client$plugins$nightmare$NightmareConfig$PrayerDisplay : [I
			//   3: aload_0
			//   4: invokevirtual ordinal : ()I
			//   7: iaload
			//   8: lookupswitch default -> 38, 2 -> 36, 3 -> 36
			//   36: iconst_1
			//   37: ireturn
			//   38: iconst_0
			//   39: ireturn
			// Line number table:
			//   Java source line number -> byte code offset
			//   #491	-> 0
			//   #495	-> 36
			//   #497	-> 38
			// Local variable table:
			//   start	length	slot	name	descriptor
			//   0	40	0	this	Lnet/runelite/client/plugins/nightmare/NightmareConfig$PrayerDisplay;
		}
	}
}

