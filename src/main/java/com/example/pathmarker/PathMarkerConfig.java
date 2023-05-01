package com.example.pathmarker;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Range;

import java.awt.*;

@ConfigGroup("pathmarker")
public interface PathMarkerConfig extends Config
{
    @ConfigSection(
            name = "Active Path",
            description = "All options related to your active path",
            position = 0
    )
    String activePathSection = "activePath";

    @ConfigSection(
            name = "Hover-Path",
            description = "All options related to the path to the hovered location",
            position = 1
    )
    String hoverPathSection = "hoverPath";

    enum drawLocations
    {
        BOTH,
        GAME_WORLD,
        MINIMAP
    }
    @ConfigItem(
            keyName = "activePathDrawLocations",
            name = "Draw location(s)",
            description = "Marks your active path on the game world and/or the minimap",
            section = activePathSection
    )
    default drawLocations activePathDrawLocations()
    {
        return drawLocations.BOTH;
    }

    enum DrawMode
    {
        FULL_PATH,
        TARGET_TILE
    }
    @ConfigItem(
            keyName = "activePathDrawMode",
            name = "Draw mode",
            description = "Determines which tiles are drawn",
            section = activePathSection
    )
    default DrawMode activePathDrawMode()
    {
        return DrawMode.FULL_PATH;
    }

    enum MarkerStyle
    {
        TILE,
        DOT
    }
    @ConfigItem(
            keyName = "activePathMarkerStyle",
            name = "Marker Style",
            description = "Shape of the path markers that are drawn",
            section = activePathSection
    )
    default MarkerStyle activePathMarkerStyle()
    {
        return MarkerStyle.TILE;
    }

    @Alpha
    @ConfigItem(
            keyName = "activePathStroke1",
            name = "Main outline color",
            description = "The main outline color of your active path",
            section = activePathSection
    )
    default Color activePathStroke1()
    {
        return new Color(255,0,0,255);
    }

    @Alpha
    @ConfigItem(
            keyName = "activePathFill1",
            name = "Main fill color",
            description = "The main fill color of your active path",
            section = activePathSection
    )
    default Color activePathFill1()
    {
        return new Color(255,0,0,50);
    }

    @Alpha
    @ConfigItem(
            keyName = "activePathStroke2",
            name = "Secondary outline color",
            description = "The secondary outline color of your active path, indicating the tiles you 'skip' while running.",
            section = activePathSection
    )
    default Color activePathStroke2()
    {
        return new Color(255,255,0,255);
    }

    @Alpha
    @ConfigItem(
            keyName = "activePathFill2",
            name = "Secondary fill color",
            description = "The secondary fill color of your active path, indicating the tiles you 'skip' while running.",
            section = activePathSection
    )
    default Color activePathFill2()
    {
        return new Color(255,255,0,50);
    }

    enum PathDisplaySetting
    {
        ALWAYS,
        WHILE_KEY_PRESSED,
        TOGGLE_ON_KEYPRESS,
        NEVER
    }
    @ConfigItem(
            keyName = "activePathDisplaySetting",
            name = "Display",
            description = "Configures when the active path should be displayed",
            section = activePathSection
    )
    default PathDisplaySetting activePathDisplaySetting()
    {
        return PathDisplaySetting.ALWAYS;
    }

    @ConfigItem(
            keyName = "displayKeybindActivePath",
            name = "Keybind",
            description = "Sets the keybind for the Display setting.",
            section = activePathSection
    )
    default Keybind displayKeybindActivePath()
    {
        return Keybind.NOT_SET;
    }

    @ConfigItem(
            keyName = "hoverPathDrawLocations",
            name = "Draw location(s)",
            description = "Marks your hover-path on the game world and/or the minimap",
            section = hoverPathSection
    )
    default drawLocations hoverPathDrawLocations()
    {
        return drawLocations.MINIMAP;
    }

    @ConfigItem(
            keyName = "hoverPathDrawMode",
            name = "Draw mode",
            description = "Determines which tiles are drawn",
            section = hoverPathSection
    )
    default DrawMode hoverPathDrawMode()
    {
        return DrawMode.FULL_PATH;
    }

    @ConfigItem(
            keyName = "hoverPathMarkerStyle",
            name = "Marker Style",
            description = "Shape of the path markers that are drawn",
            section = hoverPathSection
    )
    default MarkerStyle hoverPathMarkerStyle()
    {
        return MarkerStyle.TILE;
    }

    @Alpha
    @ConfigItem(
            keyName = "hoverPathStroke1",
            name = "Main outline color",
            description = "The main outline color of the hover-path",
            section = hoverPathSection
    )
    default Color hoverPathStroke1()
    {
        return new Color(255,0,255,255);
    }

    @Alpha
    @ConfigItem(
            keyName = "hoverPathFill1",
            name = "Main fill color",
            description = "The main fill color of the hover-path",
            section = hoverPathSection
    )
    default Color hoverPathFill1()
    {
        return new Color(255,0,255,50);
    }

    @Alpha
    @ConfigItem(
            keyName = "hoverPathStroke2",
            name = "Secondary outline color",
            description = "The secondary outline color of the hover-path, indicating the tiles you 'skip' while running.",
            section = hoverPathSection
    )
    default Color hoverPathStroke2()
    {
        return new Color(0,255,0,255);
    }

    @Alpha
    @ConfigItem(
            keyName = "hoverPathFill2",
            name = "Secondary fill color",
            description = "The secondary fill color of the hover-path, indicating the tiles you 'skip' while running.",
            section = hoverPathSection
    )
    default Color hoverPathFill2()
    {
        return new Color(0,255,0,50);
    }

    @ConfigItem(
            keyName = "hoverPathDisplaySetting",
            name = "Display",
            description = "Configures when the hover-path should be displayed",
            section = hoverPathSection
    )
    default PathDisplaySetting hoverPathDisplaySetting()
    {
        return PathDisplaySetting.ALWAYS;
    }

    @ConfigItem(
            keyName = "displayKeybindHoverPath",
            name = "Keybind",
            description = "Sets the keybind for the Display setting.",
            section = hoverPathSection
    )
    default Keybind displayKeybindHoverPath()
    {
        return Keybind.NOT_SET;
    }

    @ConfigItem(
            keyName = "drawOnlyIfNoActivePath",
            name = "Draw only if no active path",
            description = "Marks the path to your hovered location only if you don't have an active path visible",
            position = 0,
            section = hoverPathSection
    )
    default boolean drawOnlyIfNoActivePath()
    {
        return false;
    }
}
