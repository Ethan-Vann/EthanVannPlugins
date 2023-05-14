/*
 * Copyright (c) 2017, Aria <aria@ar1as.space>
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
package com.example.tooltips;

import com.google.inject.Provides;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.mousehighlight.MouseHighlightConfig;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

@PluginDescriptor(
	name = "Tooltips",
	tags = {"null"},
	conflicts = {"Mouse Tooltips"}
)
public class TooltipsPlugin extends Plugin
{
	@Inject
	private OverlayManager overlayManager;

	@Inject
	private TooltipsOverlay tooltipsOverlay;

	@Inject
	private TooltipOverlay tooltipOverlay;

	@Provides
    TooltipsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TooltipsConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(tooltipsOverlay);
		overlayManager.add(tooltipOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(tooltipsOverlay);
		overlayManager.remove(tooltipOverlay);
	}
}
