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
import com.example.Packets.MousePackets;
import com.google.inject.Provides;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
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
import java.util.concurrent.Callable;

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
	private Skill blockingSkill;
	private Callable<Boolean> blockUntil;
	@Inject
	MousePackets mousePackets;

	private LinkedList<MenuEntryMirror> actionQueue;

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
		blockingSkill = null;
		globalTimeout = 0;
		blockUntil = null;
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
		if(runBlockingChecks(event)){
			if (config.consumeClicks()){
				event.consume();
				log.info("Consumed");
			}
			return;
		}
		if(actionQueue.isEmpty()){
			BigDrizzleConfig.ActivityType type = config.activityType();
			switch (type){
				case NORMALCOOKING: enqueue(NormalCooking.buildActions()); 		break;
				case FISHING:		enqueue(KarambwanFishing.buildActions()); 	break;
				case ZMI:			enqueue(ZMI.buildActions());				break;
				case TICKCOOKING:	enqueue(TickCooking.buildActions());		break;
				case CHAOSALTAR: 	enqueue(ChaosAltar.buildActions());			break;
				case LAVARC:		enqueue(LavaRC.buildActions());				break;
			}
		}
		dequeue(event);
	}

	@Subscribe
	public void onStatChanged(StatChanged event){
		if (event.getSkill().equals(blockingSkill)){
			blockingSkill = null;
		}
	}

	@Subscribe
	public void onClientTick(ClientTick event) throws InvocationTargetException, IllegalAccessException {
	}

	@SneakyThrows
	private boolean runBlockingChecks(MenuOptionClicked event){
		if (blockUntil != null){
			if (!blockUntil.call()){
				if (config.queueLog()) {
					log.info("Blocking on callable");
				}
				return true;
			}
		}
		if (globalTimeout > 0){
			if (config.queueLog()){
				log.info("Blocking on timeout = " + globalTimeout);
			}
			return true;
		}
		if (blockingSkill != null){
			if (config.queueLog()){
				log.info("Blocking on xp drop = " + blockingSkill.getName());
			}
			return true;
		}
		return false;
	}

	@SneakyThrows
	private void dequeue(MenuOptionClicked event){
		if (!actionQueue.isEmpty()){
			globalTimeout = actionQueue.peek().getPostActionTickDelay();
			blockingSkill = actionQueue.peek().getBlockUntilXpDrop();
			blockUntil = actionQueue.peek().getBlockUntil();
			while(!actionQueue.peek().getPreActions().isEmpty()){
				mousePackets.queueClickPacket();
				invokeMenuAction(actionQueue.peek().getPreActions().poll());
			}
			event.consume();
			invokeMenuAction(actionQueue.peek());
			while(!actionQueue.peek().getPostActions().isEmpty()){
				mousePackets.queueClickPacket();
				invokeMenuAction(actionQueue.peek().getPostActions().poll());
			}
			actionQueue.poll();
		}
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
			log.info("Successfully loaded invoke menu action method");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Failed to load menuaction class");
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
}