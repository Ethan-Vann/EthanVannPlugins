package com.example.constru;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.ClientTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(name = "PaJau Constru",enabledByDefault = false)
public class ConstruPlugin extends Plugin {
	@Inject
	private Client client;

	@Subscribe
	void onClientTick(ClientTick event){

		MenuEntry[] menus = this.client.getMenuEntries();
		for(MenuEntry wea: menus){
			if(wea.getTarget()==null) return;
			if(wea.getTarget().contains("Portal")) return;
			if(wea.getOption().contains("Remove") || wea.getOption().contains("Build")){
				MenuEntry[] newEntries = new MenuEntry[1];
				newEntries[0] = wea;
				this.client.setMenuEntries(newEntries);
			}
		}
	}



}
