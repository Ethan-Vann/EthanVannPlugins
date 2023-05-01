package com.example.Toacito;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("toacito")
public interface ToacitoConfig extends Config {

	@ConfigSection(
			name = "Akkha Path",
			description = "Settings for Akkha Path",
			position = 0
	)
	String akkhaPathSection = "akkhaPath";

	@ConfigSection(
			name = "Ba-ba",
			description = "Setting for Ba-ba",
			position = 8
	)
	String babaSection = "baba";

	@ConfigSection(
			name = "Warden P1 and P2",
			description = "Settings for Watdens P1 and P2",
			position = 9
	)
	String wardenP12Section = "wardenP12";

	@ConfigSection(
			name = "Warden P3 and P4",
			description = "Settings for wareden P3 and P4",
			position = 10
	)
	String wardenP34Section = "wardenP34";

	@ConfigSection(
			name = "Lobby 1",
			description = "Settings for first Lobby",
			position = 11
	)
	String lobby1Section = "lobby1";

	@ConfigSection(
			name = "Lobby 2",
			description = "Settings for second Lobby (just before warden fight)",
			position = 12
	)
	String lobby2Section = "lobby2";


	@ConfigItem(
			position = 1,
			keyName = "obelisko",
			name = "Obelisko",
			description = "Un contador pal obelisko",
			section = akkhaPathSection
	)
	default boolean obeliskConfig() { return true; }

	@ConfigItem(
			position = 1,
			keyName = "babita",
			name = "Baba contador",
			description = "cuenta los atakes del baba",
			section = babaSection
	)
	default boolean babaConfig() { return true; }

	@ConfigItem(
			position = 1,
			keyName = "pikachus",
			name = "Pikachus",
			description = "las calaveras de trueno",
			section = wardenP12Section
	)
	default boolean pikachusConfig() { return true; }

	//--------------------------
	@ConfigItem(
			position = 1,
			keyName = "life1",
			name = "Quitar Life",
			description = "Quitar life en primer Supplies Pack",
			section = lobby1Section
	)
	default boolean life1Config() { return true;}
	@ConfigItem(
			position = 2,
			keyName = "chaos1",
			name = "Quitar Chaos",
			description = "Quitar Chaos en primer Supplies Pack",
			section = lobby1Section
	)
	default boolean chaos1Config() { return false;}
	@ConfigItem(
			position = 3,
			keyName = "power1",
			name = "Quitar Power",
			description = "Quitar Power en primer Supplies Pack",
			section = lobby1Section
	)
	default boolean power1Config() { return false;}

	@ConfigItem(
			position = 1,
			keyName = "life2",
			name = "Quitar Life",
			description = "Quitar life en primer Supplies Pack",
			section = lobby2Section
	)
	default boolean life2Config() { return false;}
	@ConfigItem(
			position = 2,
			keyName = "chaos2",
			name = "Quitar Chaos",
			description = "Quitar Chaos en primer Supplies Pack",
			section = lobby2Section
	)
	default boolean chaos2Config() { return false;}
	@ConfigItem(
			position = 3,
			keyName = "power2",
			name = "Quitar Power",
			description = "Quitar Power en primer Supplies Pack",
			section = lobby2Section
	)
	default boolean power2Config() { return true;}

	//--------------------------


	@ConfigItem(
			position = 1,
			keyName = "pisoPosicion",
			name = "Piso posicion",
			description = "Remember floor attack after skulls",
			section = wardenP34Section
	)
	default boolean floorPosition() {return true;}

	@ConfigItem(
			position = 2,
			keyName = "pantheon",
			name = "Pantheon",
			description = "Shows Akkha ghost attack",
			section = wardenP34Section
	)
	default boolean pantheonConfig() {return true;}
}
