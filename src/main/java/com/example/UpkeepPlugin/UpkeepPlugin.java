package com.example.UpkeepPlugin;

import com.example.PacketUtilsPlugin;
import com.example.Packets.MousePackets;
import com.example.Packets.NPCPackets;
import com.example.Packets.ObjectPackets;
import com.example.Packets.WidgetPackets;
import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginManager;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@PluginDescriptor(
		name = "Upkeep Plugin",
		description = "",
		enabledByDefault = false,
		tags = {"ethan"}
)
@PluginDependency(PacketUtilsPlugin.class)
@Slf4j
public class UpkeepPlugin extends Plugin
{
	public int timeout = 0;
	@Inject
	Client client;
	@Inject
	PluginManager pluginManager;
	@Inject
	WidgetPackets widgetPackets;
	@Inject
	MousePackets mousePackets;
	@Inject
	NPCPackets npcPackets;
	@Inject
	ObjectPackets objectPackets;
	@Inject
	UpkeepPluginConfig config;


	@Provides
	public UpkeepPluginConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(UpkeepPluginConfig.class);
	}
	
	@Override
	@SneakyThrows
	public void startUp()
	{
		timeout = 0;
	}

	@Override
	public void shutDown()
	{

	}

	@Subscribe
	public void onGameTick(GameTick event) throws NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException
	{
		int hitpoints = this.client.getBoostedSkillLevel(Skill.HITPOINTS);
		int prayer = this.client.getBoostedSkillLevel(Skill.PRAYER);
		int magic = this.client.getBoostedSkillLevel(Skill.MAGIC);
		int stamina = client.getEnergy()/100;
		int ranged = this.client.getBoostedSkillLevel(Skill.RANGED);
		int strength = this.client.getBoostedSkillLevel(Skill.STRENGTH);
		if(hitpoints<config.HealthLowAmount()){
			System.out.println("triggered");
			handleAction(config.HealthActions());
		}
		if(prayer<config.PrayerLowAmount()){
			handleAction(config.PrayerActions());
		}
		if(magic<config.MagicLowAmount()){
			handleAction(config.MagicActions());
		}
		if(stamina<config.StaminaLowAmount()){
			handleAction(config.StaminaActions());
		}
		if(ranged<config.RangeLowAmount()){
			handleAction(config.RangeActions());
		}
		if(strength<config.StrengthLowAmount()){
			handleAction(config.StrengthActions());
		}
	}

	public void handleAction(String actionParam){
		String[] Actions = actionParam.split("\n");
		for (String Action : Actions)
		{
			if(!Action.contains(":")){
				continue;
			}
			Widget item = getItems(Action.split(":")[1],WidgetInfo.INVENTORY);
			if(item==null){
				continue;
			}
			System.out.println(Action.split(":")[0]+":"+Action.split(":")[1]);
			mousePackets.queueClickPacket();
			widgetPackets.queueWidgetAction(item,Action.split(":")[0]);
		}
	}
	public Widget getItems(String itemcsv, WidgetInfo container)
	{
		List<Integer> itemsList = Arrays.stream(itemcsv.split(",")).map(Integer::parseInt).collect(Collectors.toList());
		Widget[] items = client.getWidget(container).getDynamicChildren();
		for (Integer s : itemsList)
		{
			Widget returnVal = Arrays.stream(items).filter(i->i.getItemId() == s).findFirst().orElse(null);
			if(returnVal!=null){
				return returnVal;
			}
		}
		return null;
	}
	public Widget getItem(int id, WidgetInfo container)
	{
		Widget[] items = client.getWidget(container).getDynamicChildren();
		for (int i = 0; i < items.length; i++)
		{
			if (items[i].getItemId() == id)
			{
				return items[i];
			}
		}
		return null;
	}

}
