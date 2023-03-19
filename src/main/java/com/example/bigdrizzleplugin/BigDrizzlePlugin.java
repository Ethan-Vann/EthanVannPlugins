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

import com.example.EthanApiPlugin.Bank;
import com.example.EthanApiPlugin.BankInventory;
import com.example.EthanApiPlugin.Inventory;
import com.example.EthanApiPlugin.TileObjects;
import com.google.inject.Provides;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

@PluginDescriptor(
	name = "BigDrizzle",
	description = "BD Desc tbd",
	tags = {"external"},
	loadWhenOutdated = true
)
@Slf4j
public class BigDrizzlePlugin extends Plugin
{
	private static int depositBoxWidgetID = 12582914;
	private boolean fishBarrelFull = false;

	private int globalTimeout = 0;

	boolean usedChisel = false;
	@Inject
	private Client client;
	private Class classWithInvokeMenuAction;
	private Method invokeMenuAction;

	private Queue<MenuEntryMirror> actionQueue;

	@Inject
	private BigDrizzleConfig config;

	@Provides
	BigDrizzleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BigDrizzleConfig.class);
	}

	@Override
	protected void startUp() throws Exception {
		loadInvokeMethod();
		actionQueue = new LinkedList<MenuEntryMirror>();
	}

	@Override
	protected void shutDown() {
		log.info("ActionQueue cleared");
		actionQueue.clear();
	}

	@Subscribe
	public void onGameTick(GameTick event){
		globalTimeout = Math.max(--globalTimeout, 0);
		if(config.debugLog()){
			logMenuEntries();
		}
	}
	@SneakyThrows
	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event){
		if (globalTimeout > 0){
			if (config.queueLog()){
				log.info("Ignoring a click. Timeout = " + globalTimeout);
			}
			return;
		}
		if(actionQueue.isEmpty()){
			if (config.activityType() == BigDrizzleConfig.ActivityType.NORMALCOOKING){
				enqueue(NormalCooking.buildActions());
			}
			if (config.activityType() == BigDrizzleConfig.ActivityType.FISHING){
				enqueue(KarambwanFishing.buildActions());
			}
		}

		if (!actionQueue.isEmpty()){
			globalTimeout = actionQueue.peek().getPostActionTickDelay();
			event.consume();
			invokeMenuAction(actionQueue.poll());
		}
	}



	@Subscribe
	public void onClientTick(ClientTick event) throws InvocationTargetException, IllegalAccessException {
		ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
		if (config.activityType() == BigDrizzleConfig.ActivityType.TICKCOOKING){
			if (client.getSelectedWidget() == null && inventory.count(3142) > 1){
				log.info("Selecting raw karambwan");
				invokeMenuAction(0, MenuAction.WIDGET_TARGET.getId(), 27, 9764864, 3142, -1, -1);
				return;
			}
		}

		if (config.activityType() == BigDrizzleConfig.ActivityType.FISHING){

		}
	}

	private void setRuneLiteEntry(MenuEntryMirror mirror) {
		MenuEntry entry = client.getMenuEntries()[client.getMenuEntries().length - 1];
		entry.setOption(mirror.getOption());
		entry.setTarget("");
		entry.setIdentifier(mirror.getIdentifier());
		entry.setType(mirror.getMenuAction());
		entry.setParam0(mirror.getParam0());
		entry.setParam1(mirror.getParam1());
		entry.setForceLeftClick(false);
		entry.setDeprioritized(false);
		setItemID(entry, mirror.getItemID());
	}

	private void enqueue(MenuEntryMirror mirror){
		if (mirror == null){
			log.info("Tried to enqueue a null action");
			return;
		}
		if (config.queueLog()){
			log.info("Added: " + mirror.toString());
		}
		actionQueue.add(mirror);
	}

	private void enqueue(LinkedList<MenuEntryMirror> actionList) {
		while(!actionList.isEmpty()){
			enqueue(actionList.poll());
		}
	}


	private void logMenuEntries() {
		MenuEntry[] menuEntries = client.getMenuEntries();
		for (int i = menuEntries.length - 1; i >= 0; i--) {
			String entryOption = menuEntries[i].getOption();
			MenuEntry entry = menuEntries[i];
//			if (entryOption.contains("All")){
//				log.info("i=" + i + " Guess=" + (menuEntries.length-i+1) + " id=" + entry.getIdentifier());
//				log.info(entry.toString());
//			}
			if(i == menuEntries.length-1){
				log.info("Top entry: " + entry.toString());
			}else{
				//log.info(entry.toString());
			}
		}
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

	private void invokeMenuAction(MenuEntryMirror mirror) throws InvocationTargetException, IllegalAccessException {
		if(config.queueLog()){
			log.info("Invoking: " + mirror.toString());
		}
		//string values are option, target, irrelevant
		int garbageValue = 1849187210;
		int convertedGarbage = garbageValue; //garbageValue.byteValue();
		invokeMenuAction.invoke(null,mirror.getParam0(),mirror.getParam1(),mirror.getMenuAction().getId(),mirror.getIdentifier(), mirror.getItemID(), "","",-1,-1,convertedGarbage);
	}
	private void maxCapePOHTele() {
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