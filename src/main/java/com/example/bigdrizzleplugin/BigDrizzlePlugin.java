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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.RuneLite;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;


@PluginDescriptor(
		name = "BigDrizzle",
		description = "BD Desc tbd",
		tags = {"external"},
		loadWhenOutdated = true
)
@Slf4j
public class BigDrizzlePlugin extends Plugin {
	@Inject private Client client;
	@Inject private BigDrizzleConfig config;
	@Inject EventBus eventBus;
	@Provides BigDrizzleConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(BigDrizzleConfig.class);
	}

	@Override
	protected void startUp() throws Exception {
		eventBus.register(RuneLite.getInjector().getInstance(NormalCooking.class));
	}

	@Override
	protected void shutDown() {
	}

	@Subscribe
	public void onGameTick(GameTick event){
		if(config.debugLog()){
			logMenuEntries();
		}
	}

	@SneakyThrows
	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event){
		boolean ranAction = false;
		BigDrizzleConfig.ActivityType type = config.activityType();
		switch (type){
			case NORMALCOOKING: ranAction = NormalCooking.processClick(); 				break;
//				case FISHING:		enqueue(KarambwanFishing.buildActions()); 				break;
//				case ZMI:			enqueue(ZMI.buildActions());							break;
//				case TICKCOOKING:	enqueue(TickCooking.buildActions());					break;
//				case CHAOSALTAR: 	enqueue(ChaosAltar.buildActions());						break;
//				case LAVARC:		enqueue(LavaRC.buildActions());							break;
//				case DAEYALTMINING:	enqueue(DaeyaltMining.buildActions());					break;
		}
		if (ranAction || config.consumeClicks()){
			config.consumeClicks();
		}
	}

	@Subscribe
	public void onClientTick(ClientTick event) throws InvocationTargetException, IllegalAccessException {
	}

	private void logMenuEntries() {
		MenuEntry[] menuEntries = client.getMenuEntries();
		for (int i = menuEntries.length - 1; i >= 0; i--) {
			String entryOption = menuEntries[i].getOption();
			MenuEntry entry = menuEntries[i];
			if(i == menuEntries.length-1){
				log.info("Top entry: " + entry.toString());
			}else if (config.extraDebugLog()){
				log.info(entry.toString());
			}
		}
	}
}