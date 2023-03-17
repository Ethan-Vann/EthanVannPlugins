/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.example.bigdrizzleplugin;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@PluginDescriptor(
	name = "BigDrizzle",
	description = "BD Desc tbd",
	tags = {"external"},
	loadWhenOutdated = true
)
@Slf4j
public class BigDrizzlePlugin extends Plugin
{
	private static WorldArea karambwanFishingArea = new WorldArea(2893, 3110, 14, 13, 0);
	private static WorldArea craftingGuildArea = new WorldArea(2923, 3273, 22, 23, 0);
	private static int depositBoxWidgetID = 12582914;
	private boolean fishBarrelFull = false;

	boolean usedChisel = false;
	boolean karambwanFishing = false;
	boolean karambwanCooking = true;
	@Inject
	private Client client;
	private Class classWithInvokeMenuAction;
	private Method invokeMenuAction;

	@Inject
	private BigDrizzleConfig config;

	@Provides
	BigDrizzleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BigDrizzleConfig.class);
	}

	@Subscribe
	public void onGameTick(GameTick event){
		if(config.debugLog()){
			logMenuEntries();
		}
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event){

	}

	@Subscribe
	public void onClientTick(ClientTick event) throws InvocationTargetException, IllegalAccessException {
		ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
		if (config.activityType() == BigDrizzleConfig.ActivityType.NORMALCOOKING){
			if (client.getWidget(17694735) != null){
				startCooking();
				return;
			}
			if(client.getWidget(786474) != null && inventory.count(3144) > 0){
				depositAllInventoryToBank();
				return;
			}
			if(client.getWidget(786474) != null && inventory.count() == 0){
				withdrallAll(3142);
				return;
			}

			if (inventory.count(3144) > 3){
				openCookingBank();
				return;
			}
			if (inventory.count(3142) > 0){
				clickRange();
				return;
			}
		}
		if (config.activityType() == BigDrizzleConfig.ActivityType.TICKCOOKING){
			if(client.getWidget(786474) != null && inventory.count(3144) > 0){
				depositAllInventoryToBank();
				return;
			}
			if(client.getWidget(786474) != null && inventory.count() == 0){
				withdrallAll(3142);
				return;
			}
			if(client.getWidget(786474) != null && inventory.count(3142) == 28){
				clickRange();
				return;
			}
			if (inventory.count(3144) == 28){
				openCookingBank();
				return;
			}

			if (client.getSelectedWidget() == null && inventory.count(3142) > 1){
				log.info("Selecting raw karambwan");
				invokeMenuAction(0, MenuAction.WIDGET_TARGET.getId(), 27, 9764864, 3142, -1, -1);
				return;
			}
		}

		if (config.activityType() == BigDrizzleConfig.ActivityType.FISHING){
			if (isDepositBoxOpen() && inventory != null && inventory.count()==28 && !fishBarrelFull){
				depositBoxDepositAll(7);
				return;
			}

			if (isDepositBoxOpen() && inventory != null && inventory.count()==28 && fishBarrelFull){
				emptyFishBarrel();
				return;
			}

			if (inArea(karambwanFishingArea) && inventory != null && inventory.count() < 28){
				fishKarambwanSpot();
				return;
			}

			if (inArea(karambwanFishingArea) && inventory != null && inventory.count() == 28){
				fishBarrelFull = true;
				maxCapeCraftingGuildTele();
				return;
			}

			if (inArea(craftingGuildArea) && inventory != null && inventory.count()==28){
				openCraftingGuildDepositBox();
				return;
			}

			if (inArea(craftingGuildArea) && inventory != null && inventory.count() < 28){
				maxCapePOHTele();
				fishBarrelFull = false;
				return;
			}

			if (inPOH()){
				lastDestinationInPOH();
				return;
			}
		}
	}



	private void logMenuEntries() {
		MenuEntry[] menuEntries = client.getMenuEntries();
		for (int i = menuEntries.length - 1; i >= 0; i--) {
			String entryOption = menuEntries[i].getOption();
			MenuEntry entry = menuEntries[i];
			if (entryOption.contains("Crafting")) {
				log.info("default entry: " + entry.toString());
			}
			if(i == menuEntries.length-1){
				log.info("Top entry: " + entry.toString());
			}
		}
	}



	@Override
	protected void startUp() throws Exception {
		loadInvokeMethod();
	}

	private void loadInvokeMethod() {
		try {
//			Field f = ClassLoader.class.getDeclaredField("classes");
//			f.setAccessible(true);
//
//			ClassLoader classLoader = client.getClass().getClassLoader();
//			Vector<Class> classes =  (Vector<Class>) f.get(classLoader);
//			for (Class c : classes){
//				for (Method m : c.getDeclaredMethods()){
//					if (m.getParameterCount() == 10){
//						System.out.println(c.getSimpleName());
//						System.out.println(m.getName());
//						System.out.println(m.getParameterCount());
//						for (Class ca : m.getParameterTypes()){
//							System.out.println(ca.getSimpleName());
//						}
//						System.out.println();
//					}
//
//				}
//			}

			classWithInvokeMenuAction = client.getClass().getClassLoader().loadClass("lk");
			invokeMenuAction = Arrays.stream(classWithInvokeMenuAction.getDeclaredMethods())
					.filter(method -> method.getName().equals("ku")).findAny().orElse(null);
			assert invokeMenuAction != null;
			invokeMenuAction.setAccessible(true);
			//log.info(invokeMenuAction.getParameterTypes().toString());
			System.out.println("successfully loaded invoke menu action method");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to load menuaction class");
		}
	}




	private void invokeMenuAction(int id, int opcode, int param0, int param1, int itemID, int xDraw, int yDraw) throws InvocationTargetException, IllegalAccessException {
		//string values are option, target, irrelevant
		int garbageValue = 1849187210;
		int convertedGarbage = garbageValue; //garbageValue.byteValue();
		invokeMenuAction.invoke(null,param0,param1,opcode,id, itemID, "","",xDraw,yDraw,convertedGarbage);
	}

	private void withdrallAll(int itemID){
		//2023-03-15 14:21:14 [Client] INFO  n.r.c.p.b.BigDrizzlePlugin - Top entry: MenuEntryImpl(getOption=Withdraw-All, getTarget=<col=ff9040>Raw karambwan</col>, getIdentifier=1, getType=CC_OP,
		// getParam0=296, getParam1=786445, getItemId=3142, isForceLeftClick=false, isDeprioritized=false)
		//2023-03-15 14:21:29 [Client] INFO  n.r.c.p.b.BigDrizzlePlugin - Top entry: MenuEntryImpl(getOption=Withdraw-All, getTarget=<col=ff9040>Raw karambwanji</col>, getIdentifier=1, getType=CC_OP,
		//getParam0=294, getParam1=786445, getItemId=3150, isForceLeftClick=false, isDeprioritized=false)
		//2023-03-15 14:22:29 [Client] INFO  n.r.c.p.b.BigDrizzlePlugin - Top entry: MenuEntryImpl(getOption=Withdraw-All, getTarget=<col=ff9040>Raw karambwan</col>, getIdentifier=1, getType=CC_OP,
		//getParam0=264, getParam1=786445, getItemId=3142, isForceLeftClick=false, isDeprioritized=false)
		MenuEntry entry = client.getMenuEntries()[client.getMenuEntries().length - 1];
		entry.setOption("Withdraw All Item: " + itemID);
		entry.setTarget("");
		entry.setIdentifier(1);
		entry.setType(MenuAction.CC_OP);
		entry.setParam0(264);
		entry.setParam1(786445);
		entry.setForceLeftClick(false);
		entry.setDeprioritized(false);
		setItemID(entry, itemID);
	}

	private void clickRange(){
		//2023-03-15 14:10:13 [Client] INFO  n.r.c.p.b.BigDrizzlePlugin - Top entry: MenuEntryImpl(getOption=Cook, getTarget=<col=ffff>Range, getIdentifier=31631, getType=GAME_OBJECT_FIRST_OPTION,
		// getParam0=50, getParam1=48, getItemId=-1, isForceLeftClick=false, isDeprioritized=false)
		MenuEntry entry = client.getMenuEntries()[client.getMenuEntries().length - 1];
		entry.setOption("Cook Range");
		entry.setTarget("");
		entry.setIdentifier(31631);
		entry.setType(MenuAction.GAME_OBJECT_FIRST_OPTION);
		entry.setParam0(50);
		entry.setParam1(48);
		entry.setForceLeftClick(false);
		entry.setDeprioritized(false);
		//setItemID(entry, -1);
	}
	private void openCookingBank(){
		//2023-03-15 14:08:20 [Client] INFO  n.r.c.p.b.BigDrizzlePlugin - Top entry: MenuEntryImpl(getOption=Use, getTarget=<col=ffff>Bank chest, getIdentifier=30087, getType=GAME_OBJECT_FIRST_OPTION,
		// getParam0=49, getParam1=49, getItemId=-1, isForceLeftClick=false, isDeprioritized=false)
		MenuEntry entry = client.getMenuEntries()[client.getMenuEntries().length - 1];
		entry.setOption("Open Mythic Guild Bank");
		entry.setTarget("");
		entry.setIdentifier(30087);
		entry.setType(MenuAction.GAME_OBJECT_FIRST_OPTION);
		entry.setParam0(49);
		entry.setParam1(49);
		entry.setForceLeftClick(false);
		entry.setDeprioritized(false);
		//setItemID(entry, -1);
	}

	private void startCooking(){
		//2023-03-15 14:12:27 [Client] INFO  n.r.c.p.b.BigDrizzlePlugin - Top entry: MenuEntryImpl(getOption=Cook, getTarget=<col=ff9040>Cooked karambwan</col>, getIdentifier=1, getType=CC_OP,
		// getParam0=-1, getParam1=17694735, getItemId=-1, isForceLeftClick=false, isDeprioritized=false)
		MenuEntry entry = client.getMenuEntries()[client.getMenuEntries().length - 1];
		entry.setOption("Cooked Karambwan");
		entry.setTarget("");
		entry.setIdentifier(1);
		entry.setType(MenuAction.CC_OP);
		entry.setParam0(-1);
		entry.setParam1(17694735);
		entry.setForceLeftClick(false);
		entry.setDeprioritized(false);
		//setItemID(entry, -1);
	}

	private void depositAllInventoryToBank(){
		//2023-03-15 14:13:53 [Client] INFO  n.r.c.p.b.BigDrizzlePlugin - Top entry: MenuEntryImpl(getOption=Deposit inventory, getTarget=, getIdentifier=1, getType=CC_OP,
		// getParam0=-1, getParam1=786474, getItemId=-1, isForceLeftClick=false, isDeprioritized=false)
		MenuEntry entry = client.getMenuEntries()[client.getMenuEntries().length - 1];
		entry.setOption("Deposit all inventory");
		entry.setTarget("");
		entry.setIdentifier(1);
		entry.setType(MenuAction.CC_OP);
		entry.setParam0(-1);
		entry.setParam1(786474);
		entry.setForceLeftClick(false);
		entry.setDeprioritized(false);
		//setItemID(entry, -1);
	}
	private void maxCapePOHTele(){
		MenuEntry entry = client.getMenuEntries()[client.getMenuEntries().length - 1];
		entry.setOption("POH Tele");
		entry.setTarget("");
		entry.setIdentifier(5);
		entry.setType(MenuAction.CC_OP);
		entry.setParam0(-1);
		entry.setParam1(25362448);
		entry.setForceLeftClick(false);
		entry.setDeprioritized(false);
		setItemID(entry, -1);
	}
	private void maxCapeCraftingGuildTele(){
		MenuEntry entry = client.getMenuEntries()[client.getMenuEntries().length - 1];
		entry.setOption("Crafting Guild Tele");
		entry.setTarget("");
		entry.setIdentifier(4);
		entry.setType(MenuAction.CC_OP);
		entry.setParam0(-1);
		entry.setParam1(25362448);
		entry.setForceLeftClick(false);
		entry.setDeprioritized(false);
		setItemID(entry, -1);
	}

	private void emptyFishBarrel(){
		MenuEntry entry = client.getMenuEntries()[client.getMenuEntries().length - 1];
		entry.setOption("Empty fish barrel");
		entry.setTarget("");
		entry.setIdentifier(9);
		entry.setType(MenuAction.CC_OP);
		entry.setParam0(0);
		entry.setParam1(12582914);
		entry.setForceLeftClick(false);
		entry.setDeprioritized(false);
		setItemID(entry, 25584);
		entry.onClick(e -> {fishBarrelFull = false;});
	}

	private void openCraftingGuildDepositBox(){
		MenuEntry entry = client.getMenuEntries()[client.getMenuEntries().length - 1];
		entry.setOption("Open Deposit Box");
		entry.setTarget("");
		entry.setIdentifier(29103);
		entry.setType(MenuAction.GAME_OBJECT_FIRST_OPTION);
		entry.setParam0(49);
		entry.setParam1(54);
		entry.setForceLeftClick(false);
		entry.setDeprioritized(false);
		setItemID(entry, -1);
	}

	private void fishKarambwanSpot(){
		MenuEntry entry = client.getMenuEntries()[client.getMenuEntries().length - 1];
		entry.setOption("Fish");
		entry.setTarget("");
		//4712 -> 1347
		int identifier = 0;
		for(NPC npc : client.getNpcs()){
			if (npc.getWorldLocation().equals(new WorldPoint(2899, 3119, 0))){
				//System.out.println("Npc info: " +  npc.getId() + " " + npc.getIndex() + " " + npc.getWorldLocation());
				identifier = npc.getIndex();
			}
		}
		entry.setIdentifier(identifier);
		entry.setType(MenuAction.NPC_FIRST_OPTION);
		entry.setParam0(0);
		entry.setParam1(0);
		entry.setForceLeftClick(false);
		entry.setDeprioritized(false);
		setItemID(entry, -1);
	}
	private boolean inPOH() {
		return client.getMapRegions()[0] == 7769;
	}

	private boolean inArea(WorldArea area){
		return client.getLocalPlayer().getWorldLocation().isInArea2D(area);
	}

	private boolean isDepositBoxOpen(){
		Widget widgetBox = client.getWidget(depositBoxWidgetID);
		return widgetBox != null;
	}

	private void useKarambwan(){
		//2023-03-06 10:11:03 [Client] INFO  n.r.c.p.b.BigDrizzlePlugin - Top entry: MenuEntryImpl(getOption=Use, getTarget=<col=ff9040>Raw karambwan</col>, getIdentifier=0, getType=WIDGET_TARGET,
		// getParam0=27, getParam1=9764864, getItemId=3142, isForceLeftClick=false, isDeprioritized=false)
		MenuEntry entry = client.getMenuEntries()[client.getMenuEntries().length - 1];
		entry.setOption("Use Raw Karambwan");
		entry.setTarget("");
		entry.setIdentifier(0);
		entry.setType(MenuAction.WIDGET_TARGET);
		entry.setParam0(27);
		entry.setParam1(9764864);
		entry.setForceLeftClick(false);
		entry.setDeprioritized(false);
		setItemID(entry, 3142);
		entry.onClick(e -> {
			usedChisel = true;
		});


	}
	private void useKarambwanOnRange(){
		//2023-03-06 10:11:31 [Client] INFO  n.r.c.p.b.BigDrizzlePlugin - Top entry: MenuEntryImpl(getOption=Use, getTarget=<col=ff9040>Raw karambwan</col><col=ffffff> -> <col=ffff>Range,
		// getIdentifier=31631, getType=WIDGET_TARGET_ON_GAME_OBJECT, getParam0=50, getParam1=48, getItemId=-1, isForceLeftClick=false, isDeprioritized=false)
		MenuEntry entry = client.getMenuEntries()[client.getMenuEntries().length - 1];
		entry.setOption("Use Karambwan on Range");
		entry.setTarget("");
		entry.setIdentifier(31631);
		entry.setType(MenuAction.WIDGET_TARGET_ON_GAME_OBJECT);
		entry.setParam0(50);
		entry.setParam1(48);
		entry.setForceLeftClick(false);
		entry.setDeprioritized(false);
		setItemID(entry, -1);
		entry.onClick(e -> {
			usedChisel = false;
		});
	}
	private void lastDestinationInPOH(){
		MenuEntry entry = client.getMenuEntries()[client.getMenuEntries().length - 1];
		entry.setOption("Last Destination");
		entry.setTarget("");
		entry.setIdentifier(29229);
		entry.setType(MenuAction.GAME_OBJECT_FOURTH_OPTION);
		entry.setParam0(51);
		entry.setParam1(51);
		entry.setForceLeftClick(false);
		entry.setDeprioritized(false);
		setItemID(entry, -1);
	}
	private void depositBoxDepositAll(int inventoryIndex) {
		ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
		if (inventory == null || inventory.getItem(inventoryIndex) == null) {
			return;
		}
		int itemID = inventory.getItem(inventoryIndex).getId();

		MenuEntry entry = client.getMenuEntries()[client.getMenuEntries().length - 1];
		entry.setOption("Deposit-All");
		entry.setTarget("");
		entry.setIdentifier(1);
		entry.setType(MenuAction.CC_OP);
		entry.setParam0(inventoryIndex);
		entry.setParam1(12582914);
		entry.setForceLeftClick(false);
		entry.setDeprioritized(false);
		setItemID(entry, itemID);
		//log.info(entry.toString());
	}
	public void setItemID(MenuEntry entry, int itemID){
		try {
//			for (Method m : entry.getClass().getDeclaredMethods()){
//				if (m.getParameterCount() == 1 && m.getParameterTypes()[0].getSimpleName().equals("int")){
//					System.out.println(m.getName());
//				}
//			}
			Method setItemIDMethod = entry.getClass().getDeclaredMethod("tn", int.class);
			setItemIDMethod.invoke(entry, itemID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

//ug
//yd
//kf
//tn