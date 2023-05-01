package com.example.pathmarker;

import javax.inject.Inject;
import java.awt.event.KeyEvent;

public class KeyListener implements net.runelite.client.input.KeyListener
{
    @Inject
    PathMarkerPlugin plugin;

    @Inject
    PathMarkerConfig config;

    @Override
    public void keyTyped(KeyEvent event)
    {
    }

    @Override
    public void keyPressed(KeyEvent event)
    {
        if (KeyEvent.VK_CONTROL == event.getKeyCode())
        {
            plugin.setCtrlHeld(true);
        }
        if (config.displayKeybindActivePath().matches(event))
        {
            if (config.activePathDisplaySetting() == PathMarkerConfig.PathDisplaySetting.TOGGLE_ON_KEYPRESS)
            {
                plugin.setKeyDisplayActivePath(!plugin.isKeyDisplayActivePath());
            }
            else if (config.activePathDisplaySetting() == PathMarkerConfig.PathDisplaySetting.WHILE_KEY_PRESSED)
            {
                plugin.setKeyDisplayActivePath(true);
            }
        }
        if (config.displayKeybindHoverPath().matches(event))
        {
            if (config.hoverPathDisplaySetting() == PathMarkerConfig.PathDisplaySetting.TOGGLE_ON_KEYPRESS)
            {
                plugin.setKeyDisplayHoverPath(!plugin.isKeyDisplayHoverPath());
            }
            else if (config.hoverPathDisplaySetting() == PathMarkerConfig.PathDisplaySetting.WHILE_KEY_PRESSED)
            {
                plugin.setKeyDisplayHoverPath(true);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent event)
    {
        if (KeyEvent.VK_CONTROL == event.getKeyCode())
        {
            plugin.setCtrlHeld(false);
        }
        if (config.displayKeybindActivePath().matches(event))
        {
            if (config.activePathDisplaySetting() == PathMarkerConfig.PathDisplaySetting.WHILE_KEY_PRESSED)
            {
                plugin.setKeyDisplayActivePath(false);
            }
        }
        if (config.displayKeybindHoverPath().matches(event))
        {
            if (config.hoverPathDisplaySetting() == PathMarkerConfig.PathDisplaySetting.WHILE_KEY_PRESSED)
            {
                plugin.setKeyDisplayHoverPath(false);
            }
        }
    }
}
