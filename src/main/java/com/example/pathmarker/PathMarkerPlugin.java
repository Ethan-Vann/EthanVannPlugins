package com.example.pathmarker;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Point;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.Varbits;
import net.runelite.api.WallObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@PluginDescriptor(
        name = "Path Marker",
        description = "Highlights your character's path to its target tile and/or to the hovered tile",
        tags = {"route","pathfinder","hover","highlight","tiles"}
)

@Slf4j
public class PathMarkerPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private PathMarkerConfig config;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private PathMarkerOverlay overlay;

    @Inject
    private PathMinimapMarkerOverlay minimapOverlay;

    public Pathfinder pathfinder;

    private Tile lastSelectedSceneTile;

    @Setter
    private boolean ctrlHeld;

    private WorldPoint lastTickWorldLocation;

    private MenuEntry lastSelectedMenuEntry;

    @Getter
    private List<WorldPoint> activeCheckpointWPs;

    private Tile oldSelectedSceneTile;

    private boolean isRunning;

    private boolean activePathFound;

    private boolean hoverPathFound;

    private boolean activePathStartedLastTick;

    private boolean activePathMismatchLastTick;

    private boolean calcTilePathOnNextClientTick;

    @Setter
    @Getter
    private boolean keyDisplayActivePath;

    @Setter
    @Getter
    private boolean keyDisplayHoverPath;

    @Setter
    private boolean leftClicked;

    @Getter
    private boolean pathActive;

    @Getter
    private List<WorldPoint> hoverPathTiles;

    @Getter
    private List<WorldPoint> hoverMiddlePathTiles;

    @Getter
    private List<WorldPoint> activePathTiles;

    @Getter
    private List<WorldPoint> activeMiddlePathTiles;

    private List<WorldPoint> hoverCheckpointWPs;

    private List<WorldArea> npcBlockWAs;

    @Setter
    private Point lastMouseCanvasPosition;

    private MenuEntry[] oldMenuEntries;

    @Inject
    private KeyManager keyManager;

    @Inject
    private MouseManager mouseManager;

    @Inject
    private KeyListener keyListener;

    @Inject
    private MouseListener mouseListener;

    private static Map<Integer, Integer> objectBlocking;

    private static Map<Integer, Integer> npcBlocking;

	/*private final int[][] directions = new int[128][128];

	private final int[][] distances = new int[128][128];

	private final int[] bufferX = new int[4096];

	private final int[] bufferY = new int[4096];*/

    static class PathDestination
    {
        private WorldPoint worldPoint;
        private int sizeX;
        private int sizeY;
        private int objConfig;
        private int objID;
        private Actor actor;

        public PathDestination(WorldPoint worldPoint, int sizeX, int sizeY, int objConfig, int objID)
        {
            this.worldPoint = worldPoint;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            this.objConfig = objConfig;
            this.objID = objID;
            this.actor = null;
        }
        public PathDestination(WorldPoint worldPoint, int sizeX, int sizeY, int objConfig, int objID, Actor actor)
        {
            this.worldPoint = worldPoint;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            this.objConfig = objConfig;
            this.objID = objID;
            this.actor = actor;
        }
    }

    private PathDestination activePathDestination;

    @Override
    protected void startUp() throws Exception
    {
        hoverPathTiles = new ArrayList<>();
        hoverMiddlePathTiles = new ArrayList<>();
        hoverCheckpointWPs = new ArrayList<>();
        activePathTiles = new ArrayList<>();
        activeMiddlePathTiles = new ArrayList<>();
        activeCheckpointWPs = new ArrayList<>();
        npcBlockWAs = new ArrayList<>();
        ctrlHeld = false;
        pathActive = false;
        activePathStartedLastTick = false;
        activePathMismatchLastTick = false;
        leftClicked = false;
        keyDisplayActivePath = false;
        isRunning = willRunOnClick();
        objectBlocking = readFile("loc_blocking.txt");
        npcBlocking = readFile("npc_blocking.txt");
        overlayManager.add(overlay);
        overlayManager.add(minimapOverlay);
        keyManager.registerKeyListener(keyListener);
        mouseManager.registerMouseListener(mouseListener);
        pathfinder = new Pathfinder(client, config, this);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
        overlayManager.remove(minimapOverlay);
        keyManager.unregisterKeyListener(keyListener);
        mouseManager.unregisterMouseListener(mouseListener);
    }

    @Provides
    PathMarkerConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(PathMarkerConfig.class);
    }

    private Pair<List<WorldPoint>, Boolean> pathToHover()
    {
        MenuEntry[] menuEntries = client.getMenuEntries();
        if (menuEntries.length == 0)
        {
            return null;
        }
        MenuEntry menuEntry;
        if (!client.isMenuOpen())
        {
            int i = 1;
            menuEntry = menuEntries[menuEntries.length - 1];
            MenuAction type = menuEntry.getType();
            while (type == MenuAction.EXAMINE_ITEM_GROUND
                    || type == MenuAction.EXAMINE_NPC
                    || type == MenuAction.EXAMINE_OBJECT
                    || type == MenuAction.RUNELITE
                    || type == MenuAction.RUNELITE_HIGH_PRIORITY
                    || type == MenuAction.RUNELITE_INFOBOX
                    || type == MenuAction.RUNELITE_OVERLAY
                    || type == MenuAction.RUNELITE_PLAYER
                    || type == MenuAction.RUNELITE_OVERLAY_CONFIG)
            {
                // For some reason, RuneLite considers the "Examine" options to be the first menuEntryOptions when no right-click menu is open.
                // It's impossible to have "Examine" as left-click option, a far as I'm aware.
                // The first non-Examine option is the real left-click option.
                // Oh, and apparently the issue is also there with RuneLite menu entries, so It'll be assumed those are never left-click.
                i += 1;
                menuEntry = menuEntries[menuEntries.length - i];
                type = menuEntry.getType();
            }
        }
        else
        {
            menuEntry = hoveredMenuEntry(menuEntries);
        }
        switch (menuEntry.getType())
        {
            case EXAMINE_ITEM_GROUND:
            case EXAMINE_NPC:
            case EXAMINE_OBJECT:
            case CANCEL:
            case CC_OP:
            case CC_OP_LOW_PRIORITY:
            case PLAYER_EIGHTH_OPTION:
            case WIDGET_CLOSE:
            case WIDGET_CONTINUE:
            case WIDGET_FIRST_OPTION:
            case WIDGET_SECOND_OPTION:
            case WIDGET_THIRD_OPTION:
            case WIDGET_FOURTH_OPTION:
            case WIDGET_FIFTH_OPTION:
            case WIDGET_TARGET:
            case WIDGET_TARGET_ON_WIDGET:
            case WIDGET_TYPE_1:
            case WIDGET_TYPE_4:
            case WIDGET_TYPE_5:
            case WIDGET_USE_ON_ITEM:
            case RUNELITE:
            case RUNELITE_HIGH_PRIORITY:
            case RUNELITE_INFOBOX:
            case RUNELITE_OVERLAY:
            case RUNELITE_OVERLAY_CONFIG:
            case RUNELITE_PLAYER:
            {
                hoverCheckpointWPs.clear();
                return null;
            }
            case GAME_OBJECT_FIRST_OPTION:
            case GAME_OBJECT_SECOND_OPTION:
            case GAME_OBJECT_THIRD_OPTION:
            case GAME_OBJECT_FOURTH_OPTION:
            case GAME_OBJECT_FIFTH_OPTION:
            case WIDGET_TARGET_ON_GAME_OBJECT:
            case GROUND_ITEM_FIRST_OPTION:
            case GROUND_ITEM_SECOND_OPTION:
            case GROUND_ITEM_THIRD_OPTION:
            case GROUND_ITEM_FOURTH_OPTION:
            case GROUND_ITEM_FIFTH_OPTION:
            case WIDGET_TARGET_ON_GROUND_ITEM:
            {
                int x = menuEntry.getParam0();
                int y = menuEntry.getParam1();
                int id = menuEntry.getIdentifier();
                int objConfig = -1;
                int sizeX = 1;
                int sizeY = 1;
                TileObject tileObject = findTileObject(x, y, id);
                TileItem tileItem = findTileItem(x, y, id);
                if (tileObject == null && tileItem == null)
                {
                    return null;
                }
                if (tileObject != null)
                {
                    if (tileObject instanceof GameObject)
                    {
                        GameObject gameObject = (GameObject) tileObject;
                        objConfig = gameObject.getConfig();
                        sizeX = gameObject.sizeX();
                        sizeY = gameObject.sizeY();
                    }
                    if (tileObject instanceof WallObject)
                    {
                        WallObject wallObject = (WallObject) tileObject;
                        objConfig = wallObject.getConfig();
                    }
                    if (tileObject instanceof DecorativeObject)
                    {
                        DecorativeObject decorativeObject = (DecorativeObject) tileObject;
                        objConfig = decorativeObject.getConfig();
                    }
                    if (tileObject instanceof GroundObject)
                    {
                        GroundObject groundObject = (GroundObject) tileObject;
                        objConfig = groundObject.getConfig();
                    }
                }
                return pathfinder.pathTo(x, y, sizeX, sizeY, objConfig, id);
            }
            case NPC_FIRST_OPTION:
            case NPC_SECOND_OPTION:
            case NPC_THIRD_OPTION:
            case NPC_FOURTH_OPTION:
            case NPC_FIFTH_OPTION:
            case WIDGET_TARGET_ON_NPC:
            case PLAYER_FIRST_OPTION:
            case PLAYER_SECOND_OPTION:
            case PLAYER_THIRD_OPTION:
            case PLAYER_FOURTH_OPTION:
            case PLAYER_FIFTH_OPTION:
            case PLAYER_SIXTH_OPTION:
            case PLAYER_SEVENTH_OPTION:
            case WIDGET_TARGET_ON_PLAYER:
            {
                Actor actor = menuEntry.getActor();
                if (actor == null)
                {
                    return null;
                }
                int x = actor.getLocalLocation().getSceneX();
                int y = actor.getLocalLocation().getSceneY();
                int size = 1;
                if (actor instanceof NPC)
                {
                    size = ((NPC) actor).getComposition().getSize();
                }
                return pathfinder.pathTo(x, y, size, size, -2, -1);
            }
            case WALK:
            default:
            {
                Tile selectedSceneTile = client.getSelectedSceneTile();
                if (selectedSceneTile == null)
                {
                    return null;
                }
                return pathfinder.pathTo(client.getSelectedSceneTile());
            }
        }
    }

    private void pathFromCheckpointTiles(List<WorldPoint> checkpointWPs, boolean running, List<WorldPoint> middlePathTiles, List<WorldPoint> pathTiles, boolean pathFound)
    {
        pathTiles.clear();
        middlePathTiles.clear();
        WorldArea currentWA = client.getLocalPlayer().getWorldArea();
        if (currentWA == null || checkpointWPs == null || checkpointWPs.size() == 0)
        {
            return;
        }
        if ((currentWA.getPlane() != checkpointWPs.get(0).getPlane()) && pathFound)
        {
            return;
        }
        boolean runSkip = true;
        int cpTileIndex = 0;
        while (currentWA.toWorldPoint().getX() != checkpointWPs.get(checkpointWPs.size() - 1).getX()
                || currentWA.toWorldPoint().getY() != checkpointWPs.get(checkpointWPs.size() - 1).getY())
        {
            WorldPoint cpTileWP = checkpointWPs.get(cpTileIndex);
            if (currentWA.toWorldPoint().equals(cpTileWP))
            {
                cpTileIndex += 1;
                cpTileWP = checkpointWPs.get(cpTileIndex);
            }
            int dx = Integer.signum(cpTileWP.getX() - currentWA.getX());
            int dy = Integer.signum(cpTileWP.getY() - currentWA.getY());
            WorldArea finalCurrentWA = currentWA;
            boolean movementCheck = currentWA.canTravelInDirection(client, dx, dy, (worldPoint -> {
                WorldPoint worldPoint1 = new WorldPoint(finalCurrentWA.getX() + dx, finalCurrentWA.getY(), client.getPlane());
                WorldPoint worldPoint2 = new WorldPoint(finalCurrentWA.getX(), finalCurrentWA.getY() + dy, client.getPlane());
                WorldPoint worldPoint3 = new WorldPoint(finalCurrentWA.getX() + dx, finalCurrentWA.getY() + dy, client.getPlane());
                for (WorldArea worldArea : npcBlockWAs)
                {
                    if (worldArea.contains(worldPoint1) || worldArea.contains(worldPoint2) || worldArea.contains(worldPoint3))
                    {
                        return false;
                    }
                }
                return true;
            }));
            if (movementCheck)
            {
                currentWA = new WorldArea(currentWA.getX() + dx, currentWA.getY() + dy, 1, 1, client.getPlane());
                if (currentWA.toWorldPoint().equals(checkpointWPs.get(checkpointWPs.size() - 1)) || !pathFound)
                {
                    pathTiles.add(currentWA.toWorldPoint());
                }
                else if (runSkip && running)
                {
                    middlePathTiles.add(currentWA.toWorldPoint());
                }
                else
                {
                    pathTiles.add(currentWA.toWorldPoint());
                }
                runSkip = !runSkip;
                continue;
            }
            movementCheck = currentWA.canTravelInDirection(client, dx, 0, (worldPoint -> {
                for (WorldArea worldArea : npcBlockWAs)
                {
                    WorldPoint worldPoint1 = new WorldPoint(finalCurrentWA.getX() + dx, finalCurrentWA.getY(), client.getPlane());
                    if (worldArea.contains(worldPoint1))
                    {
                        return false;
                    }
                }
                return true;
            }));
            if (dx != 0 && movementCheck)
            {
                currentWA = new WorldArea(currentWA.getX() + dx, currentWA.getY(), 1, 1, client.getPlane());
                if (currentWA.toWorldPoint().equals(checkpointWPs.get(checkpointWPs.size() - 1)) || !pathFound)
                {
                    pathTiles.add(currentWA.toWorldPoint());
                }
                else if (runSkip && running)
                {
                    middlePathTiles.add(currentWA.toWorldPoint());
                }
                else
                {
                    pathTiles.add(currentWA.toWorldPoint());
                }
                runSkip = !runSkip;
                continue;
            }
            movementCheck = currentWA.canTravelInDirection(client, 0, dy, (worldPoint -> {
                for (WorldArea worldArea : npcBlockWAs)
                {
                    WorldPoint worldPoint1 = new WorldPoint(finalCurrentWA.getX(), finalCurrentWA.getY() + dy, client.getPlane());
                    if (worldArea.contains(worldPoint1))
                    {
                        return false;
                    }
                }
                return true;
            }));
            if (dy != 0 && movementCheck)
            {
                currentWA = new WorldArea(currentWA.getX(), currentWA.getY() + dy, 1, 1, client.getPlane());
                if (currentWA.toWorldPoint().equals(checkpointWPs.get(checkpointWPs.size() - 1)) || !pathFound)
                {
                    pathTiles.add(currentWA.toWorldPoint());
                }
                else if (runSkip && running)
                {
                    middlePathTiles.add(currentWA.toWorldPoint());
                }
                else
                {
                    pathTiles.add(currentWA.toWorldPoint());
                }
                runSkip = !runSkip;
                continue;
            }
            return;
        }
    }

    private void updateCheckpointTiles()
    {
        if (lastTickWorldLocation == null)
        {
            return;
        }
        WorldArea currentWA = new WorldArea(lastTickWorldLocation.getX(), lastTickWorldLocation.getY(), 1,1, client.getPlane());
        if (activeCheckpointWPs == null)
        {
            return;
        }
        if ((lastTickWorldLocation.getPlane() != activeCheckpointWPs.get(0).getPlane()) && activePathFound)
        {
            WorldPoint lastActiveCPTile = activeCheckpointWPs.get(0);
            activeCheckpointWPs.clear();
            activeCheckpointWPs.add(lastActiveCPTile);
            pathActive = false;
            return;
        }
        int cpTileIndex = 0;
        int steps = 0;
        while (currentWA.toWorldPoint().getX() != activeCheckpointWPs.get(activeCheckpointWPs.size() - 1).getX()
                || currentWA.toWorldPoint().getY() != activeCheckpointWPs.get(activeCheckpointWPs.size() - 1).getY())
        {
            WorldPoint cpTileWP = activeCheckpointWPs.get(cpTileIndex);
            if (currentWA.toWorldPoint().equals(cpTileWP))
            {
                cpTileIndex += 1;
                cpTileWP = activeCheckpointWPs.get(cpTileIndex);
            }
            int dx = Integer.signum(cpTileWP.getX() - currentWA.getX());
            int dy = Integer.signum(cpTileWP.getY() - currentWA.getY());
            WorldArea finalCurrentWA = currentWA;
            boolean movementCheck = currentWA.canTravelInDirection(client, dx, dy, (worldPoint -> {
                WorldPoint worldPoint1 = new WorldPoint(finalCurrentWA.getX() + dx, finalCurrentWA.getY(), client.getPlane());
                WorldPoint worldPoint2 = new WorldPoint(finalCurrentWA.getX(), finalCurrentWA.getY() + dy, client.getPlane());
                WorldPoint worldPoint3 = new WorldPoint(finalCurrentWA.getX() + dx, finalCurrentWA.getY() + dy, client.getPlane());
                for (WorldArea worldArea : npcBlockWAs)
                {
                    if (worldArea.contains(worldPoint1) || worldArea.contains(worldPoint2) || worldArea.contains(worldPoint3))
                    {
                        return false;
                    }
                }
                return true;
            }));
            if (movementCheck)
            {
                currentWA = new WorldArea(currentWA.getX() + dx, currentWA.getY() + dy, 1, 1, client.getPlane());
            }
            else
            {
                movementCheck = currentWA.canTravelInDirection(client, dx, 0, (worldPoint -> {
                    WorldPoint worldPoint1 = new WorldPoint(finalCurrentWA.getX() + dx, finalCurrentWA.getY(), client.getPlane());
                    for (WorldArea worldArea : npcBlockWAs)
                    {
                        if (worldArea.contains(worldPoint1))
                        {
                            return false;
                        }
                    }
                    return true;
                }));
                if (dx != 0 && movementCheck)
                {
                    currentWA = new WorldArea(currentWA.getX() + dx, currentWA.getY(), 1, 1, client.getPlane());
                }
                else
                {
                    movementCheck = currentWA.canTravelInDirection(client, 0, dy, (worldPoint -> {
                        WorldPoint worldPoint1 = new WorldPoint(finalCurrentWA.getX(), finalCurrentWA.getY() + dy, client.getPlane());
                        for (WorldArea worldArea : npcBlockWAs)
                        {
                            if (worldArea.contains(worldPoint1))
                            {
                                return false;
                            }
                        }
                        return true;
                    }));
                    if (dy != 0 && movementCheck)
                    {
                        currentWA = new WorldArea(currentWA.getX(), currentWA.getY() + dy, 1, 1, client.getPlane());
                    }
                }
            }
            steps += 1;
            if (steps == 2 || !isRunning || !activePathFound)
            {
                break;
            }
        }
        if (steps == 0 && pathActive)
        {
            WorldPoint lastActiveCPTile = activeCheckpointWPs.get(0);
            activeCheckpointWPs.clear();
            activeCheckpointWPs.add(lastActiveCPTile);
            pathActive = false;
            activePathDestination.objConfig = -1;
            activePathMismatchLastTick = true;
            return;
        }
        if (!currentWA.toWorldPoint().equals(client.getLocalPlayer().getWorldLocation()) && pathActive)
        {
            if (activePathStartedLastTick)
            {
                LocalPoint localPoint = LocalPoint.fromWorld(client, activePathDestination.worldPoint);
                if (localPoint != null)
                {
                    Pair<List<WorldPoint>, Boolean> pathResult = pathfinder.pathTo(localPoint.getSceneX(), localPoint.getSceneY(), activePathDestination.sizeX, activePathDestination.sizeY, activePathDestination.objConfig, activePathDestination.objID);
                    if (pathResult == null)
                    {
                        return;
                    }
                    lastTickWorldLocation = client.getLocalPlayer().getWorldLocation();
                    pathActive = true;
                    activeCheckpointWPs = pathResult.getLeft();
                    activePathFound = pathResult.getRight();
                    pathFromCheckpointTiles(activeCheckpointWPs, isRunning, activeMiddlePathTiles, activePathTiles, activePathFound);
                    activePathStartedLastTick = false;
                }
            }
            else if (activePathMismatchLastTick)
            {
                WorldPoint lastActiveCPTile = activeCheckpointWPs.get(0);
                activeCheckpointWPs.clear();
                activeCheckpointWPs.add(lastActiveCPTile);
                pathActive = false;
                activePathStartedLastTick = false;
            }
            activePathMismatchLastTick = true;
        }
        else
        {
            activePathMismatchLastTick = false;
        }
        for (int i = 0; i < cpTileIndex; i++)
        {
            if (activeCheckpointWPs.size()>1)
            {
                activeCheckpointWPs.remove(0);
            }
        }
    }

    private void updateNpcBlockings()
    {
        List<NPC> npcs = client.getNpcs();
        npcBlockWAs.clear();
        for (NPC npc : npcs)
        {
            NPCComposition npcComposition = npc.getTransformedComposition();
            if (npcComposition == null)
            {
                continue;
            }
            if (getNpcBlocking(npcComposition.getId()))
            {
                npcBlockWAs.add(npc.getWorldArea());
            }
        }
    }

    private boolean willRunOnClick()
    {
        boolean willRun = (client.getVarpValue(173) == 1); //run toggled on
        if (!ctrlHeld)
        {
            return willRun;
        }
        int ctrlSetting = client.getVarbitValue(13132);
        switch (ctrlSetting)
        {
            case 0: //never
                return willRun;
            case 1: //walk --> run only
                return true;
            case 2: //Run --> walk only
                return false;
            case 3: //Always
                return !willRun;
            default:
                return willRun;
        }
    }

    TileItem findTileItem(int x, int y, int id)
    {
        Scene scene = client.getScene();
        Tile[][][] tiles = scene.getTiles();
        Tile tile = tiles[client.getPlane()][x][y];
        if (tile == null)
        {
            return null;
        }
        List<TileItem> tileItems = tile.getGroundItems();
        if (tileItems == null)
        {
            return null;
        }
        for (TileItem tileItem : tileItems)
        {
            if (tileItem != null && tileItem.getId() == id)
            {
                return tileItem;
            }
        }
        return null;
    }

    TileObject findTileObject(int x, int y, int id)
    {
        Scene scene = client.getScene();
        Tile[][][] tiles = scene.getTiles();
        Tile tile = tiles[client.getPlane()][x][y];
        if (tile != null)
        {
            for (GameObject gameObject : tile.getGameObjects())
            {
                if (gameObject != null && gameObject.getId() == id)
                {
                    return gameObject;
                }
            }

            WallObject wallObject = tile.getWallObject();
            if (wallObject != null && wallObject.getId() == id)
            {
                return wallObject;
            }

            DecorativeObject decorativeObject = tile.getDecorativeObject();
            if (decorativeObject != null && decorativeObject.getId() == id)
            {
                return decorativeObject;
            }

            GroundObject groundObject = tile.getGroundObject();
            if (groundObject != null && groundObject.getId() == id)
            {
                return groundObject;
            }
        }
        return null;
    }

    public static int getObjectBlocking(final int objectId, final int rotation)
    {
        if (objectBlocking == null)
        {
            return 0;
        }
        int blockingValue = objectBlocking.getOrDefault(objectId, 0);
        return rotation == 0 ? blockingValue : (((blockingValue << rotation) & 0xF) + (blockingValue >> (4 - rotation)));
    }

    public static boolean getNpcBlocking(final int npcCompId)
    {
        if (npcBlocking == null)
        {
            return false;
        }
        return npcBlocking.getOrDefault(npcCompId, 0) == 1;
    }

    private static Map<Integer, Integer> readFile(String name) {
        try {
            InputStream inputStream = PathMarkerPlugin.class.getResourceAsStream(name);
            if (inputStream == null)
            {
                return null;
            }
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            final Map<Integer, Integer> map = new LinkedHashMap<>();
            for (String line; (line = reader.readLine()) != null;) {
                String[] split = line.split("=");
                int id = Integer.parseInt(split[0]);
                int blocking = Integer.parseInt(split[1].split(" ")[0]);
                map.put(id, blocking);
            }
            reader.close();
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Point minimapToScenePoint()
    {
        if (client.getMenuEntries().length != 1 || lastMouseCanvasPosition == null)
        {
            // Minimap hovering doesn't add menu entries other than the default "cancel"
            return null;
        }
        Widget minimapDrawWidget;
        if (client.isResized())
        {
            if (client.getVarbitValue(Varbits.SIDE_PANELS) == 1)
            {
                minimapDrawWidget = client.getWidget(WidgetInfo.RESIZABLE_MINIMAP_DRAW_AREA);
            }
            else
            {
                minimapDrawWidget = client.getWidget(WidgetInfo.RESIZABLE_MINIMAP_STONES_DRAW_AREA);
            }
        }
        else
        {
            minimapDrawWidget = client.getWidget(WidgetInfo.FIXED_VIEWPORT_MINIMAP_DRAW_AREA);
        }

        if (minimapDrawWidget == null || minimapDrawWidget.isHidden())
        {
            return null;
        }
        if (!minimapDrawWidget.contains(lastMouseCanvasPosition))
        {
            return null;
        }
        int widgetX = lastMouseCanvasPosition.getX() - minimapDrawWidget.getCanvasLocation().getX() - minimapDrawWidget.getWidth()/2;
        int widgetY = lastMouseCanvasPosition.getY() - minimapDrawWidget.getCanvasLocation().getY() - minimapDrawWidget.getHeight()/2;
        int angle = client.getMapAngle() & 0x7FF;
        int sine = (int) (65536.0D * Math.sin((double) angle * 0.0030679615D));
        int cosine = (int) (65536.0D * Math.cos((double) angle * 0.0030679615D));
        int xx = cosine * widgetX + widgetY * sine >> 11;
        int yy = widgetY * cosine - widgetX * sine >> 11;
        int deltaX = xx + client.getLocalPlayer().getLocalLocation().getSceneX() >> 7;
        int deltaY = client.getLocalPlayer().getLocalLocation().getSceneY() - yy >> 7;
        LocalPoint localPoint = client.getLocalPlayer().getLocalLocation();
        if (localPoint == null)
        {
            return null;
        }
        return new Point(localPoint.getSceneX() + deltaX, localPoint.getSceneY() + deltaY);
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event)
    {
        if (event.getVarpId() == 173)
        {
            // Run toggled
            int[] varps = client.getVarps();
            isRunning = varps[173] == 1;
        }
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event)
    {
        switch (event.getMenuAction())
        {
            case EXAMINE_ITEM_GROUND:
            case EXAMINE_NPC:
            case EXAMINE_OBJECT:
            case CANCEL:
            case CC_OP:
            case CC_OP_LOW_PRIORITY:
            case PLAYER_EIGHTH_OPTION:
            case WIDGET_CLOSE:
            case WIDGET_CONTINUE:
            case WIDGET_FIRST_OPTION:
            case WIDGET_SECOND_OPTION:
            case WIDGET_THIRD_OPTION:
            case WIDGET_FOURTH_OPTION:
            case WIDGET_FIFTH_OPTION:
            case WIDGET_TARGET:
            case WIDGET_TARGET_ON_WIDGET:
            case WIDGET_TYPE_1:
            case WIDGET_TYPE_4:
            case WIDGET_TYPE_5:
            case WIDGET_USE_ON_ITEM:
            case RUNELITE:
            case RUNELITE_HIGH_PRIORITY:
            case RUNELITE_INFOBOX:
            case RUNELITE_OVERLAY:
            case RUNELITE_OVERLAY_CONFIG:
            case RUNELITE_PLAYER:
                return;
            case GAME_OBJECT_FIRST_OPTION:
            case GAME_OBJECT_SECOND_OPTION:
            case GAME_OBJECT_THIRD_OPTION:
            case GAME_OBJECT_FOURTH_OPTION:
            case GAME_OBJECT_FIFTH_OPTION:
            case WIDGET_TARGET_ON_GAME_OBJECT:
            case GROUND_ITEM_FIRST_OPTION:
            case GROUND_ITEM_SECOND_OPTION:
            case GROUND_ITEM_THIRD_OPTION:
            case GROUND_ITEM_FOURTH_OPTION:
            case GROUND_ITEM_FIFTH_OPTION:
            case WIDGET_TARGET_ON_GROUND_ITEM:
            {
                int x = event.getParam0();
                int y = event.getParam1();
                int id = event.getId();
                int config = -1;
                int sizeX = 1;
                int sizeY = 1;
                TileObject tileObject = findTileObject(x, y, id);
                TileItem tileItem = findTileItem(x, y, id);
                if (tileObject == null && tileItem == null)
                {
                    return;
                }
                isRunning = willRunOnClick();
                if (tileObject != null)
                {
                    if (tileObject instanceof GameObject)
                    {
                        GameObject gameObject = (GameObject) tileObject;
                        config = gameObject.getConfig();
                        sizeX = gameObject.sizeX();
                        sizeY = gameObject.sizeY();
                    }
                    if (tileObject instanceof WallObject)
                    {
                        WallObject wallObject = (WallObject) tileObject;
                        config = wallObject.getConfig();
                    }
                    if (tileObject instanceof DecorativeObject)
                    {
                        DecorativeObject decorativeObject = (DecorativeObject) tileObject;
                        config = decorativeObject.getConfig();
                    }
                    if (tileObject instanceof GroundObject)
                    {
                        GroundObject groundObject = (GroundObject) tileObject;
                        config = groundObject.getConfig();
                    }
                }
                WorldPoint worldPoint = WorldPoint.fromScene(client, x, y, client.getPlane());
                Pair<List<WorldPoint>, Boolean> pathResult = pathfinder.pathTo(x, y, sizeX, sizeY, config, id);
                activePathDestination = new PathDestination(worldPoint, sizeX, sizeY, config, id);
                if (pathResult == null)
                {
                    return;
                }
                lastTickWorldLocation = client.getLocalPlayer().getWorldLocation();
                pathActive = true;
                activeCheckpointWPs = pathResult.getLeft();
                activePathFound = pathResult.getRight();
                pathFromCheckpointTiles(activeCheckpointWPs, isRunning, activeMiddlePathTiles, activePathTiles, activePathFound);
                activePathStartedLastTick = true;
                calcTilePathOnNextClientTick = false;
                return;
            }
            case NPC_FIRST_OPTION:
            case NPC_SECOND_OPTION:
            case NPC_THIRD_OPTION:
            case NPC_FOURTH_OPTION:
            case NPC_FIFTH_OPTION:
            case WIDGET_TARGET_ON_NPC:
            case PLAYER_FIRST_OPTION:
            case PLAYER_SECOND_OPTION:
            case PLAYER_THIRD_OPTION:
            case PLAYER_FOURTH_OPTION:
            case PLAYER_FIFTH_OPTION:
            case PLAYER_SIXTH_OPTION:
            case PLAYER_SEVENTH_OPTION:
            case WIDGET_TARGET_ON_PLAYER:
            {
                Actor actor = event.getMenuEntry().getActor();
                if (actor == null)
                {
                    return;
                }
                isRunning = willRunOnClick();
                LocalPoint localPoint = LocalPoint.fromWorld(client, actor.getWorldLocation());
                if (localPoint == null)
                {
                    return;
                }
                int x = localPoint.getSceneX();
                int y = localPoint.getSceneY();
                int size = 1;
                if (actor instanceof NPC)
                {
                    size = ((NPC) actor).getComposition().getSize();
                }
                WorldPoint worldPoint = WorldPoint.fromScene(client, x, y, client.getPlane());
                Pair<List<WorldPoint>, Boolean> pathResult = pathfinder.pathTo(x, y, size, size, -2, -1);
                activePathDestination = new PathDestination(worldPoint, size, size, -2, -1, actor);
                if (pathResult == null)
                {
                    return;
                }
                lastTickWorldLocation = client.getLocalPlayer().getWorldLocation();
                pathActive = true;
                activeCheckpointWPs = pathResult.getLeft();
                activePathFound = pathResult.getRight();
                pathFromCheckpointTiles(activeCheckpointWPs, isRunning, activeMiddlePathTiles, activePathTiles, activePathFound);
                activePathStartedLastTick = true;
                calcTilePathOnNextClientTick = false;
                return;
            }
            case WALK:
            default:
            {
                if (!client.isMenuOpen())
                {
                    calcTilePathOnNextClientTick = true;
                    return;
                }
                if (oldSelectedSceneTile == null)
                {
                    return;
                }
                isRunning = willRunOnClick();
                WorldPoint worldPoint = WorldPoint.fromScene(client, oldSelectedSceneTile.getSceneLocation().getX(), oldSelectedSceneTile.getSceneLocation().getY(), client.getPlane());
                Pair<List<WorldPoint>, Boolean> pathResult = pathfinder.pathTo(oldSelectedSceneTile);
                activePathDestination = new PathDestination(worldPoint, 1, 1, -1, -1);
                if (pathResult == null)
                {
                    return;
                }
                lastTickWorldLocation = client.getLocalPlayer().getWorldLocation();
                pathActive = true;
                activeCheckpointWPs = pathResult.getLeft();
                activePathFound = pathResult.getRight();
                pathFromCheckpointTiles(activeCheckpointWPs, isRunning, activeMiddlePathTiles, activePathTiles, activePathFound);
                activePathStartedLastTick = true;
                calcTilePathOnNextClientTick = (event.getMenuAction() == MenuAction.WALK && !client.isMenuOpen());
            }
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        switch (event.getGameState())
        {
            case HOPPING:
            case LOGGING_IN:
            {
                activeCheckpointWPs.clear();
                activeCheckpointWPs.add(new WorldPoint(0,0,client.getPlane()));
                pathActive = false;
            }
        }
    }

    @Subscribe
    public void onClientTick(ClientTick event)
    {
        if (calcTilePathOnNextClientTick)
        {
            Tile selectedSceneTile = client.getSelectedSceneTile();
            if (selectedSceneTile != null)
            {
                isRunning = willRunOnClick();
                WorldPoint worldPoint = WorldPoint.fromScene(client, selectedSceneTile.getSceneLocation().getX(), selectedSceneTile.getSceneLocation().getY(), client.getPlane());
                Pair<List<WorldPoint>, Boolean> pathResult = pathfinder.pathTo(selectedSceneTile);
                activePathDestination = new PathDestination(worldPoint, 1, 1, -1, -1);
                if (pathResult != null)
                {
                    lastTickWorldLocation = client.getLocalPlayer().getWorldLocation();
                    pathActive = true;
                    activeCheckpointWPs = pathResult.getLeft();
                    activePathFound = pathResult.getRight();
                    pathFromCheckpointTiles(activeCheckpointWPs, isRunning, activeMiddlePathTiles, activePathTiles, activePathFound);
                    activePathStartedLastTick = true;
                    calcTilePathOnNextClientTick = false;
                }
            }
        }
        Tile selectedSceneTile = client.getSelectedSceneTile();
        MenuEntry[] menuEntries = client.getMenuEntries();
        if (menuEntries.length == 1 && !client.isMenuOpen()
            && (leftClicked || (config.hoverPathDisplaySetting() != PathMarkerConfig.PathDisplaySetting.NEVER)))
        {
            // Potential minimap hover/click
            Point point = minimapToScenePoint();
            if (point != null)
            {
                Pair<List<WorldPoint>, Boolean> pathResult = pathfinder.pathTo(point.getX(), point.getY(), 1,1,-1,-1);
                if (pathResult != null)
                {
                    hoverCheckpointWPs = pathResult.getLeft();
                    hoverPathFound = pathResult.getRight();
                    if (hoverCheckpointWPs != null)
                    {
                        lastSelectedSceneTile = selectedSceneTile;
                    }
                    if (leftClicked)
                    {
                        isRunning = willRunOnClick();
                        activePathDestination = new PathDestination(WorldPoint.fromScene(client, point.getX(), point.getY(), client.getPlane()), 1, 1, -1, -1);
                        lastTickWorldLocation = client.getLocalPlayer().getWorldLocation();
                        pathActive = true;
                        activeCheckpointWPs = new ArrayList<>(pathResult.getLeft());
                        activePathFound = pathResult.getRight();
                        pathFromCheckpointTiles(activeCheckpointWPs, isRunning, activeMiddlePathTiles, activePathTiles, activePathFound);
                        activePathStartedLastTick = true;
                    }
                }
            }
        }
        if (lastSelectedSceneTile==null || lastSelectedSceneTile!=selectedSceneTile
                || (client.isMenuOpen() && hoveredMenuEntry(menuEntries) != lastSelectedMenuEntry)
                || oldMenuEntries.length != menuEntries.length)
        {
            if (client.isMenuOpen())
            {
                lastSelectedMenuEntry = hoveredMenuEntry(menuEntries);
            }
            if (selectedSceneTile != null)
            {
                Pair<List<WorldPoint>, Boolean> pathResult = pathToHover();
                if (pathResult != null)
                {
                    hoverCheckpointWPs = pathResult.getLeft();
                    hoverPathFound = pathResult.getRight();
                }
            }
        }
        oldSelectedSceneTile = client.getSelectedSceneTile();
        oldMenuEntries = menuEntries;
        leftClicked = false;
        lastMouseCanvasPosition=client.getMouseCanvasPosition();
        lastSelectedSceneTile = selectedSceneTile;
        pathFromCheckpointTiles(hoverCheckpointWPs, willRunOnClick(), hoverMiddlePathTiles, hoverPathTiles, hoverPathFound);
    }

    private MenuEntry hoveredMenuEntry(final MenuEntry[] menuEntries)
    {
        final int menuX = client.getMenuX();
        final int menuY = client.getMenuY();
        final int menuWidth = client.getMenuWidth();
        final Point mousePosition = client.getMouseCanvasPosition();

        int dy = mousePosition.getY() - menuY;
        dy -= 19; // Height of Choose Option
        if (dy < 0)
        {
            return menuEntries[0];
        }

        int idx = dy / 15; // Height of each menu option
        idx = menuEntries.length - 1 - idx;

        if (mousePosition.getX() > menuX && mousePosition.getX() < menuX + menuWidth
                && idx >= 0 && idx < menuEntries.length)
        {
            return menuEntries[idx];
        }
        return menuEntries[0];
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        LocalPoint localDestinationLocation = client.getLocalDestinationLocation();
        if (localDestinationLocation != null && pathActive && activePathFound)
        {
            WorldPoint worldDestinationLocation = WorldPoint.fromLocal(client, localDestinationLocation);
            if (!worldDestinationLocation.equals(activeCheckpointWPs.get(activeCheckpointWPs.size() - 1)))
            {
                Pair<List<WorldPoint>, Boolean> pathResult = pathfinder.pathTo(localDestinationLocation.getSceneX(), localDestinationLocation.getSceneY(), activePathDestination.sizeX, activePathDestination.sizeY, activePathDestination.objConfig, activePathDestination.objID);
                if (pathResult != null)
                {
                    activeCheckpointWPs = pathResult.getLeft();
                    activePathFound = pathResult.getRight();
                    activePathStartedLastTick = false;
                }
            }
        }
        updateNpcBlockings();
        WorldPoint currentWorldLocation = client.getLocalPlayer().getWorldLocation();
        if (lastTickWorldLocation == null || lastTickWorldLocation != currentWorldLocation)
        {
            Pair<List<WorldPoint>, Boolean> pathResult = pathToHover();
            if (pathResult != null)
            {
                hoverCheckpointWPs = pathResult.getLeft();
                hoverPathFound = pathResult.getRight();
            }
        }
        if (hoverCheckpointWPs !=null && hoverCheckpointWPs.size()>0)
        {
            pathFromCheckpointTiles(hoverCheckpointWPs, willRunOnClick(), hoverMiddlePathTiles, hoverPathTiles, hoverPathFound);
        }
        if (currentWorldLocation.equals(activeCheckpointWPs.get(activeCheckpointWPs.size() - 1))
                || (lastTickWorldLocation != null && currentWorldLocation.distanceTo(lastTickWorldLocation) > 2))
        {
            pathActive = false;
        }
        updateCheckpointTiles();
        if (pathActive && activePathDestination.objConfig == -2 && activeCheckpointWPs.size()<2)
        {
            // Path is recalculated when there's <2 checkpoint tiles remaining when pathing to a NPC/player
            LocalPoint localPoint = LocalPoint.fromWorld(client, activePathDestination.actor.getWorldLocation());
            if (localPoint != null)
            {
                Pair<List<WorldPoint>, Boolean> pathResult = pathfinder.pathTo(localPoint.getSceneX(), localPoint.getSceneY(), activePathDestination.sizeX, activePathDestination.sizeY, activePathDestination.objConfig, activePathDestination.objID);
                if (pathResult != null)
                {
                    pathActive = true;
                    activeCheckpointWPs = pathResult.getLeft();
                    activePathFound = pathResult.getRight();
                    activePathStartedLastTick = false;
                }
            }
        }
        pathFromCheckpointTiles(activeCheckpointWPs, isRunning, activeMiddlePathTiles, activePathTiles, activePathFound);
        lastTickWorldLocation = currentWorldLocation;
    }
}
