package com.example.UpkeepPlugin;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("UpkeepPlugin")
public interface UpkeepPluginConfig extends Config
{
	@ConfigSection(
			name = "Vida",
			description = "",
			position = -10,
			closedByDefault = false
	)
	String Vidita = "HP";

	@ConfigItem(
			keyName = "HealthLowAmount",
			name = "HP critica",
			description = "Vida critica",
			position = -2,
			section = Vidita
	)
	default int HealthLowAmount()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "HealthLowActions",
			name = "Vida accion",
			description = "Que hacer al bajar de la vida critica",
			position = -3,
			section = Vidita
	)
	default String HealthActions()
	{
		return "";
	}

	@ConfigSection(
			name = "Run Energy",
			description = "",
			position = 9
	) String runEnergy = "RunEnergy";

	@ConfigItem(
			keyName = "StaminaLowAmount",
			name = "Run Energy",
			description = "Run critico",
			position = -6,
			section = runEnergy
	)
	default int StaminaLowAmount()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "StaminaLowActions",
			name = "Run Energy Accion",
			description = "Que hacer al bajar del Run critico",
			position = -7,
			section = runEnergy
	)
	default String StaminaActions()
	{
		return "";
	}

	@ConfigItem(
			keyName = "RangeLowAmount",
			name = "Ranged critico",
			description = "Ranged Critico"
	)
	default int RangeLowAmount()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "RangeLowActions",
			name = "Range critico acciones",
			description = "Acciones Ranged critico"
	)
	default String RangeActions()
	{
		return "";
	}

	@ConfigSection(
			name = "Prayer",
			description = "",
			position = -8
	) String Prayercito = "Prayercito";

	@ConfigItem(
			keyName = "PrayerLowAmount",
			name = "Prayer critico",
			description = "Prayer critico",
			position = 3,
			section = Prayercito
	)
	default int PrayerLowAmount()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "PrayerLowActions",
			name = "Prayer critico Acciones",
			description = "Prayer critico Acciones",
			position = 4,
			section = Prayercito
	)
	default String PrayerActions()
	{
		return "";
	}

	@ConfigItem(
			keyName = "MagicLowAmount",
			name = "Magic critico",
			description = "Magic critico"
	)
	default int MagicLowAmount()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "MagicLowActions",
			name = "Magic critico acciones",
			description = "Magic critico acciones"
	)
	default String MagicActions()
	{
		return "";
	}

	@ConfigItem(
			keyName = "StrengthLowAmount",
			name = "Strength critico",
			description = "Strength critico"
	)
	default int StrengthLowAmount()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "StrengthLowActions",
			name = "Strength critico Aciones",
			description = "Strength critico Aciones"
	)
	default String StrengthActions()
	{
		return "";
	}
}