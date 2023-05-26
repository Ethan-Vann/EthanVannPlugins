package com.example.EthanApiPlugin;

import com.example.EthanApiPlugin.Collections.*;
import com.example.EthanApiPlugin.Collections.query.QuickPrayer;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import lombok.SneakyThrows;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginInstantiationException;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.util.Text;
import net.runelite.client.util.WildcardMatcher;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static net.runelite.api.Varbits.QUICK_PRAYER;

@PluginDescriptor(
        name = "EthanApiPlugin",
        description = "",
        tags = {"ethan"},
        hidden = false
)
public class EthanApiPlugin extends Plugin {

    static Client client = RuneLite.getInjector().getInstance(Client.class);
    static PluginManager pluginManager = RuneLite.getInjector().getInstance(PluginManager.class);
    static ItemManager itemManager = RuneLite.getInjector().getInstance(ItemManager.class);
    @Inject
    EventBus eventBus;
    public static LoadingCache<Integer, ItemComposition> itemDefs = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<Integer, ItemComposition>() {
                        @Override
                        public ItemComposition load(Integer itemId) {
                            return itemManager.getItemComposition(itemId);
                        }
                    });


    public static SkullIcon getSkullIcon(Player player) {
        Field skullField = null;
        try {
            skullField = player.getClass().getDeclaredField("ac");
            skullField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
        int var1 = -1;
        try {
            var1 = skullField.getInt(player) * -1875167049;
            skullField.setAccessible(false);
        } catch (IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }
        switch (var1) {
            case 0:
                return SkullIcon.SKULL;
            case 1:
                return SkullIcon.SKULL_FIGHT_PIT;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            default:
                return null;
            case 8:
                return SkullIcon.DEAD_MAN_FIVE;
            case 9:
                return SkullIcon.DEAD_MAN_FOUR;
            case 10:
                return SkullIcon.DEAD_MAN_THREE;
            case 11:
                return SkullIcon.DEAD_MAN_TWO;
            case 12:
                return SkullIcon.DEAD_MAN_ONE;
        }
    }

    public static boolean isQuickPrayerActive(QuickPrayer prayer) {
        return (client.getVarbitValue(4102) & (int) Math.pow(2, prayer.getIndex())) == Math.pow(2, prayer.getIndex());
    }

    public static boolean isQuickPrayerEnabled() {
        return client.getVarbitValue(QUICK_PRAYER) == 1;
    }

    @SneakyThrows
    public static HeadIcon getHeadIcon(NPC npc) {
        Method getHeadIconArrayMethod = null;
        for (Method declaredMethod : npc.getComposition().getClass().getDeclaredMethods()) {
            if (declaredMethod.getReturnType() == short[].class && declaredMethod.getParameterTypes().length == 0) {
                getHeadIconArrayMethod = declaredMethod;
            }
        }
        if (getHeadIconArrayMethod == null) {
            return null;
        }
        getHeadIconArrayMethod.setAccessible(true);
        short[] headIconArray = (short[]) getHeadIconArrayMethod.invoke(npc.getComposition());
        if (headIconArray == null || headIconArray.length == 0) {
            return null;
        }
        return HeadIcon.values()[headIconArray[0]];
    }

    @Deprecated
    public int countItem(String str, WidgetInfo container) {
        Widget[] items = client.getWidget(container).getDynamicChildren();
        int count = 0;
        for (int i = 0; i < items.length; i++) {
            if (WildcardMatcher.matches(str.toLowerCase(), Text.removeTags(items[i].getName()).toLowerCase())) {
                count++;
            }
        }
        return count;
    }

    @Deprecated
    public static Widget getItem(String str) {
        Widget[] items = client.getWidget(WidgetInfo.INVENTORY).getDynamicChildren();
        for (int i = 0; i < items.length; i++) {
            if (WildcardMatcher.matches(str.toLowerCase(), Text.removeTags(items[i].getName()).toLowerCase())) {
                return items[i];
            }
        }
        return null;
    }

    public static List<WorldPoint> sceneWorldPoints() {
        List<WorldPoint> allWorldPoints = new ArrayList<>();
        Scene scene = client.getScene();
        Tile[][][] tiles = scene.getTiles();
        int z = client.getPlane();
        for (int x = 0; x < 104; ++x) {
            for (int y = 0; y < 104; ++y) {
                Tile tile = tiles[z][x][y];
                if (tile == null) {
                    continue;
                }
                allWorldPoints.add(tile.getWorldLocation());
            }
        }
        return allWorldPoints;
    }

    public static List<WorldPoint> reachableTiles() {
        HashSet<Tile> retPoints = new HashSet<>();
        Tile[][] tiles = client.getScene().getTiles()[client.getPlane()];
        Tile firstPoint = tiles[client.getLocalPlayer().getWorldLocation().getX() - client.getBaseX()][client.getLocalPlayer().getWorldLocation().getY() - client.getBaseY()];
        Queue<Tile> queue = new LinkedList<>();
        int[][] flags = client.getCollisionMaps()[client.getPlane()].getFlags();
        queue.add(firstPoint);
        while (!queue.isEmpty()) {
            Tile tile = queue.poll();
            int x = tile.getSceneLocation().getX();
            int y = tile.getSceneLocation().getY();

            if (y > 0 && canMoveSouth(flags[x][y]) && canMoveTo(flags[x][y - 1]) && !retPoints.contains(tiles[x][y - 1])) {
                queue.add(tiles[x][y - 1]);
                retPoints.add(tiles[x][y - 1]);
            }
            if (y < 127 && canMoveNorth(flags[x][y]) && canMoveTo(flags[x][y + 1]) && !retPoints.contains(tiles[x][y + 1])) {
                queue.add(tiles[x][y + 1]);
                retPoints.add(tiles[x][y + 1]);
            }
            if (x > 0 && canMoveWest(flags[x][y]) && canMoveTo(flags[x - 1][y]) && !retPoints.contains(tiles[x - 1][y])) {
                queue.add(tiles[x - 1][y]);
                retPoints.add(tiles[x - 1][y]);
            }
            if (x < 127 && canMoveEast(flags[x][y]) && canMoveTo(flags[x + 1][y]) && !retPoints.contains(tiles[x + 1][y])) {
                queue.add(tiles[x + 1][y]);
                retPoints.add(tiles[x + 1][y]);
            }
        }
        return retPoints.stream().map(Tile::getWorldLocation).collect(Collectors.toList());
    }

//    public static List<WorldPoint> reachableTiles() {
//        return new ArrayList<>(Arrays.stream(client.getScene().getTiles()).flatMap(Arrays::stream).flatMap(Arrays::stream).filter(Objects::nonNull).filter(x -> canPathToTile(x.getWorldLocation()).isReachable()).map(Tile::getWorldLocation).filter(Objects::nonNull).collect(Collectors.toList()));
//    }

    static boolean canMoveWest(int flag) {
        return (flag & CollisionDataFlag.BLOCK_MOVEMENT_WEST) != CollisionDataFlag.BLOCK_MOVEMENT_WEST;
    }

    static boolean canMoveEast(int flag) {
        return (flag & CollisionDataFlag.BLOCK_MOVEMENT_EAST) != CollisionDataFlag.BLOCK_MOVEMENT_EAST;
    }

    static boolean canMoveNorth(int flag) {
        return (flag & CollisionDataFlag.BLOCK_MOVEMENT_NORTH) != CollisionDataFlag.BLOCK_MOVEMENT_NORTH;
    }

    static boolean canMoveSouth(int flag) {
        return (flag & CollisionDataFlag.BLOCK_MOVEMENT_SOUTH) != CollisionDataFlag.BLOCK_MOVEMENT_SOUTH;
    }

    static boolean canMoveTo(int flag) {
        if ((flag & CollisionDataFlag.BLOCK_MOVEMENT_FULL) == CollisionDataFlag.BLOCK_MOVEMENT_FULL) {
            return false;
        }
        if ((flag & CollisionDataFlag.BLOCK_MOVEMENT_FLOOR) == CollisionDataFlag.BLOCK_MOVEMENT_FLOOR) {
            return false;
        }
        if ((flag & CollisionDataFlag.BLOCK_MOVEMENT_OBJECT) == CollisionDataFlag.BLOCK_MOVEMENT_OBJECT) {
            return false;
        }
        return (flag & CollisionDataFlag.BLOCK_MOVEMENT_FLOOR_DECORATION) != CollisionDataFlag.BLOCK_MOVEMENT_FLOOR_DECORATION;
    }


    @Deprecated
    public static Widget getItem(int id, WidgetInfo container) {
        if (client.getWidget(container) == null) {
            return null;
        }
        Widget[] items = client.getWidget(container).getDynamicChildren();
        for (int i = 0; i < items.length; i++) {
            if (items[i].getItemId() == id) {
                return items[i];
            }
        }
        return null;
    }

    public int getFirstFreeSlot(WidgetInfo container) {
        Widget[] items = client.getWidget(container).getDynamicChildren();
        for (int i = 0; i < items.length; i++) {
            if (items[i].getItemId() == 6512) {
                return i;
            }
        }
        return -1;
    }

    @Deprecated
    public int getEmptySlots(WidgetInfo widgetInfo) {
        List<Widget> inventoryItems = Arrays.asList(client.getWidget(widgetInfo.getId()).getDynamicChildren());
        return (int) inventoryItems.stream().filter(item -> item.getItemId() == 6512).count();
    }

    public static boolean isMoving() {
        return client.getLocalPlayer().getPoseAnimation()
                != client.getLocalPlayer().getIdlePoseAnimation();
    }

    @Deprecated
    public TileObject findObject(String objectName) {
        ArrayList<TileObject> validObjects = new ArrayList<>();
        for (Tile[][] tile : client.getScene().getTiles()) {
            for (Tile[] tiles : tile) {
                for (Tile tile1 : tiles) {
                    if (tile1 == null) {
                        continue;
                    }
                    if (tile1.getGameObjects() == null) {
                        continue;
                    }
                    if (tile1.getGameObjects().length != 0) {
                        GameObject returnVal =
                                Arrays.stream(tile1.getGameObjects()).filter(gameObject -> gameObject != null && client.getObjectDefinition(gameObject.getId()).getName().toLowerCase().contains(objectName.toLowerCase())).findFirst().orElse(null);
                        if (returnVal != null) {
                            validObjects.add(returnVal);
                        }
                    }
                }
            }
        }
        if (validObjects.size() > 0) {
            return validObjects.stream().sorted(Comparator.comparingInt(x -> x.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation()))).findFirst().orElse(null);
        }
        return null;
    }

    @SneakyThrows
    public static void invoke(int var0, int var1, int var2, int var3, int var4, String var5, String var6, int var7,
                              int var8) {
        Class invokeClass = client.getClass().getClassLoader().loadClass("ar");
        Method invoke = invokeClass.getDeclaredMethod("ke", int.class, int.class, int.class, int.class, int.class,
                String.class, String.class, int.class, int.class, int.class);
        invoke.setAccessible(true);
        invoke.invoke(null, var0, var1, var2, var3, var4, var5, var6, var7, var8, 1849187210);
        invoke.setAccessible(false);
    }

    @Deprecated
    public TileObject findObject(int id) {
        ArrayList<TileObject> validObjects = new ArrayList<>();
        Arrays.stream(client.getScene().getTiles()).flatMap(Arrays::stream).flatMap(Arrays::stream).filter(Objects::nonNull).filter(tile -> tile.getGameObjects() != null && tile.getGameObjects().length != 0).forEach(tile ->
        {
            GameObject returnVal =
                    Arrays.stream(tile.getGameObjects()).filter(gameObject -> gameObject != null && gameObject.getId() == id).findFirst().orElse(null);
            if (returnVal != null) {
                validObjects.add(returnVal);
            }
        });
        return validObjects.stream().min(Comparator.comparingInt(x -> x.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation()))).orElse(null);
    }

    @Deprecated
    public Widget getItemFromList(int[] list, WidgetInfo container) {
        for (int i : list) {
            Widget item = getItem(i, container);
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    @Deprecated
    public int checkIfWearing(int[] ids) {

        if (client.getItemContainer(InventoryID.EQUIPMENT) != null) {
            Item[] equipment = client.getItemContainer(InventoryID.EQUIPMENT).getItems();
            for (Item item : equipment) {
                for (int id : ids) {
                    if (id == item.getId()) {
                        return item.getId();
                    }
                }
            }
        }
        return -1;
    }

    public static PathResult canPathToTile(WorldPoint destinationTile) {
        int z = client.getPlane();
        if (z != destinationTile.getPlane()) {
            return new PathResult(false, Integer.MAX_VALUE);
        }

        CollisionData[] collisionData = client.getCollisionMaps();
        if (collisionData == null) {
            return new PathResult(false, Integer.MAX_VALUE);
        }

        int[][] directions = new int[128][128];
        int[][] distances = new int[128][128];
        int[] bufferX = new int[4096];
        int[] bufferY = new int[4096];

        // Initialise directions and distances
        for (int i = 0; i < 128; ++i) {
            for (int j = 0; j < 128; ++j) {
                directions[i][j] = 0;
                distances[i][j] = Integer.MAX_VALUE;
            }
        }

        int pSX = client.getLocalPlayer().getLocalLocation().getSceneX();

        int pSY = client.getLocalPlayer().getLocalLocation().getSceneY();
        Point p1 = client.getScene().getTiles()[client.getPlane()][pSX][pSY].getSceneLocation();
        Point p2 = new Point(LocalPoint.fromWorld(client, destinationTile).getSceneX(), LocalPoint.fromWorld(client, destinationTile).getSceneY());

        int middleX = p1.getX();
        int middleY = p1.getY();
        int currentX = middleX;
        int currentY = middleY;
        int offsetX = 64;
        int offsetY = 64;
        // Initialise directions and distances for starting tile
        directions[offsetX][offsetY] = 99;
        distances[offsetX][offsetY] = 0;
        int index1 = 0;
        bufferX[0] = currentX;
        int index2 = 1;
        bufferY[0] = currentY;
        int[][] collisionDataFlags = collisionData[z].getFlags();

        int currentDistance = Integer.MAX_VALUE;
        boolean isReachable = false;

        while (index1 != index2) {
            currentX = bufferX[index1];
            currentY = bufferY[index1];
            index1 = index1 + 1 & 4095;
            // currentX is for the local coordinate while currentMapX is for the index in the directions and distances arrays
            int currentMapX = currentX - middleX + offsetX;
            int currentMapY = currentY - middleY + offsetY;
            if ((currentX == p2.getX()) && (currentY == p2.getY())) {
                isReachable = true;
                break;
            }

            currentDistance = distances[currentMapX][currentMapY] + 1;
            if (currentMapX > 0 && directions[currentMapX - 1][currentMapY] == 0 && (collisionDataFlags[currentX - 1][currentY] & 19136776) == 0) {
                // Able to move 1 tile west
                bufferX[index2] = currentX - 1;
                bufferY[index2] = currentY;
                index2 = index2 + 1 & 4095;
                directions[currentMapX - 1][currentMapY] = 2;
                distances[currentMapX - 1][currentMapY] = currentDistance;
            }

            if (currentMapX < 127 && directions[currentMapX + 1][currentMapY] == 0 && (collisionDataFlags[currentX + 1][currentY] & 19136896) == 0) {
                // Able to move 1 tile east
                bufferX[index2] = currentX + 1;
                bufferY[index2] = currentY;
                index2 = index2 + 1 & 4095;
                directions[currentMapX + 1][currentMapY] = 8;
                distances[currentMapX + 1][currentMapY] = currentDistance;
            }

            if (currentMapY > 0 && directions[currentMapX][currentMapY - 1] == 0 && (collisionDataFlags[currentX][currentY - 1] & 19136770) == 0) {
                // Able to move 1 tile south
                bufferX[index2] = currentX;
                bufferY[index2] = currentY - 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX][currentMapY - 1] = 1;
                distances[currentMapX][currentMapY - 1] = currentDistance;
            }

            if (currentMapY < 127 && directions[currentMapX][currentMapY + 1] == 0 && (collisionDataFlags[currentX][currentY + 1] & 19136800) == 0) {
                // Able to move 1 tile north
                bufferX[index2] = currentX;
                bufferY[index2] = currentY + 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX][currentMapY + 1] = 4;
                distances[currentMapX][currentMapY + 1] = currentDistance;
            }

            if (currentMapX > 0 && currentMapY > 0 && directions[currentMapX - 1][currentMapY - 1] == 0 && (collisionDataFlags[currentX - 1][currentY - 1] & 19136782) == 0 && (collisionDataFlags[currentX - 1][currentY] & 19136776) == 0 && (collisionDataFlags[currentX][currentY - 1] & 19136770) == 0) {
                // Able to move 1 tile south-west
                bufferX[index2] = currentX - 1;
                bufferY[index2] = currentY - 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX - 1][currentMapY - 1] = 3;
                distances[currentMapX - 1][currentMapY - 1] = currentDistance;
            }

            if (currentMapX < 127 && currentMapY > 0 && directions[currentMapX + 1][currentMapY - 1] == 0 && (collisionDataFlags[currentX + 1][currentY - 1] & 19136899) == 0 && (collisionDataFlags[currentX + 1][currentY] & 19136896) == 0 && (collisionDataFlags[currentX][currentY - 1] & 19136770) == 0) {
                // Able to move 1 tile north-west
                bufferX[index2] = currentX + 1;
                bufferY[index2] = currentY - 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX + 1][currentMapY - 1] = 9;
                distances[currentMapX + 1][currentMapY - 1] = currentDistance;
            }

            if (currentMapX > 0 && currentMapY < 127 && directions[currentMapX - 1][currentMapY + 1] == 0 && (collisionDataFlags[currentX - 1][currentY + 1] & 19136824) == 0 && (collisionDataFlags[currentX - 1][currentY] & 19136776) == 0 && (collisionDataFlags[currentX][currentY + 1] & 19136800) == 0) {
                // Able to move 1 tile south-east
                bufferX[index2] = currentX - 1;
                bufferY[index2] = currentY + 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX - 1][currentMapY + 1] = 6;
                distances[currentMapX - 1][currentMapY + 1] = currentDistance;
            }

            if (currentMapX < 127 && currentMapY < 127 && directions[currentMapX + 1][currentMapY + 1] == 0 && (collisionDataFlags[currentX + 1][currentY + 1] & 19136992) == 0 && (collisionDataFlags[currentX + 1][currentY] & 19136896) == 0 && (collisionDataFlags[currentX][currentY + 1] & 19136800) == 0) {
                // Able to move 1 tile north-east
                bufferX[index2] = currentX + 1;
                bufferY[index2] = currentY + 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX + 1][currentMapY + 1] = 12;
                distances[currentMapX + 1][currentMapY + 1] = currentDistance;
            }
        }
        return new PathResult(isReachable, currentDistance);
    }

    public static class PathResult {
        private final boolean reachable;
        private final int distance;

        public PathResult(boolean reachable, int distance) {
            this.reachable = reachable;
            this.distance = distance;
        }

        public boolean isReachable() {
            return reachable;
        }

        public int getDistance() {
            return distance;
        }
    }

    @SneakyThrows
    public static void stopPlugin(Plugin plugin) {

        SwingUtilities.invokeAndWait(() ->
        {
            try {
                pluginManager.stopPlugin(plugin);
                pluginManager.setPluginEnabled(plugin, false);
            } catch (PluginInstantiationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Client getClient() {
        return client;
    }

    public static ArrayList<WorldPoint> pathToGoal(WorldPoint goal, HashSet<WorldPoint> dangerous) {

        HashMap<WorldPoint, List<WorldPoint>> paths = new HashMap<>();
        HashSet<WorldPoint> walkableTiles = new HashSet<>(reachableTiles());
        HashSet<WorldPoint> impassibleTiles = new HashSet<>(EthanApiPlugin.sceneWorldPoints());
        System.out.println("Completed impassible tiles 1");
        impassibleTiles.removeIf(walkableTiles::contains);
        paths.put(client.getLocalPlayer().getWorldLocation(), List.of(client.getLocalPlayer().getWorldLocation()));
        return pathToGoal(goal, paths, impassibleTiles, dangerous, new HashSet<>(reachableTiles()), new HashSet<>());
    }

    public static ArrayList<WorldPoint> pathToGoal(WorldPoint goal, HashSet<WorldPoint> dangerous, HashSet<WorldPoint> impassible) {

        HashMap<WorldPoint, List<WorldPoint>> paths = new HashMap<>();
        paths.put(client.getLocalPlayer().getWorldLocation(), List.of(client.getLocalPlayer().getWorldLocation()));
        return pathToGoal(goal, paths, impassible, dangerous, new HashSet<>(reachableTiles()), new HashSet<>());
    }

    public static ArrayList<WorldPoint> pathToGoal(WorldPoint goal, HashSet<WorldPoint> walkable, HashSet<WorldPoint> dangerous, HashSet<WorldPoint> impassible) {
        HashMap<WorldPoint, List<WorldPoint>> paths = new HashMap<>();
        paths.put(client.getLocalPlayer().getWorldLocation(), List.of(client.getLocalPlayer().getWorldLocation()));
        return pathToGoal(goal, paths, impassible, dangerous, walkable, new HashSet<>());
    }


    //this method paths locally aka within the current scene. It is not a fully fledged worldwalker
    @SneakyThrows
    public static ArrayList<WorldPoint> pathToGoal(WorldPoint goal, HashMap<WorldPoint, List<WorldPoint>> paths,
                                                   HashSet<WorldPoint> impassible, HashSet<WorldPoint> dangerous,
                                                   HashSet<WorldPoint> walkable, HashSet<WorldPoint> walked) {
        HashMap<WorldPoint, List<WorldPoint>> paths2 = new HashMap<>(paths);
        if (!walkable.contains(goal)) {
            return null;
        }
        for (Map.Entry<WorldPoint, List<WorldPoint>> worldPointListEntry : paths.entrySet()) {
            //			int counter = 1;
            for (int x = -2; x < 3; x++) {
                b:
                for (int y = -2; y < 3; y++) {
                    //Our original tile
                    if (x == 0 && y == 0) {
                        continue;
                    }

                    // ORIGINAL LINE (DY USES X, DX USES Y?
                    // WorldPoint point = worldPointListEntry.getKey().dy(x).dx(y);
                    WorldPoint point = worldPointListEntry.getKey().dy(y).dx(x);
                    if (!walkable.contains(point) || impassible.contains(point) || dangerous.contains(point)) {
//                        						System.out.println("rejecting 1");
                        continue;
                    }
                    if (walked.contains(point)) {
                        continue b;
                    }

                    //far movements
                    //Far West
                    if (x == -2 && y == 0) {
                        if (farWObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                            continue;
                        }
                    }
                    //Far East
                    if (x == 2 && y == 0) {
                        if (farEObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                            continue;
                        }
                    }
                    //Far South
                    if (x == 0 && y == -2) {
                        if (farSObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                            continue;
                        }
                    }
                    //Far North
                    if (x == 0 && y == 2) {
                        if (farNObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                            continue;
                        }
                    }
                    //far movements
                    //L movement in here so i dont get lost in the saauce down there
                    if (Math.abs(x) + Math.abs(y) == 3) {
                        //North east
                        if (x == 1 && y == 2) {
                            if (northEastLObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                                continue;
                            }
                        }
                        //East north
                        if (x == 2 && y == 1) {
                            if (eastNorthLObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                                continue;
                            }
                        }
                        //East south
                        if (x == 2 && y == -1) {
                            if (eastSouthLObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                                continue;
                            }
                        }
                        //South east
                        if (x == 1 && y == -2) {
                            if (southEastLObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                                continue;
                            }
                        }
                        //South west
                        if (x == -1 && y == -2) {
                            if (southWestLObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                                continue;
                            }
                        }
                        //West south
                        if (x == -2 && y == -1) {
                            if (westSouthLObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                                continue;
                            }
                        }
                        //West north
                        if (x == -2 && y == 1) {
                            if (westNorthLObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                                continue;
                            }
                        }
                        //North west
                        if (x == -1 && y == 2) {
                            if (northWestLObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                                continue;
                            }
                        }
                    } else {
                        //One tile movement

                        //diagonal SE
                        if (x == 1 && y == -1) {
                            if (seObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                                continue;
                            }

                        }
                        //diagonal NE
                        if (x == 1 && y == 1) {
                            if (neObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                                continue;
                            }
                        }
                        //diagonal NW
                        if (x == -1 && y == 1) {
                            if (nwObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                                continue;
                            }
                        }
                        //diagonal SW
                        if (x == -1 && y == -1) {
                            if (swObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                                continue;
                            }
                        }

                        //Two tile movement

                        //Diagonal SW
                        if (x == -2 && y == -2) {
                            if (farSWObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                                continue;
                            }
                        }
                        //Diagonal NW
                        if (x == -2 && y == 2) {
                            if (farNWObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                                continue;
                            }
                        }
                        //Diagonal SE
                        if (x == 2 && y == -2) {
                            if (farSEObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                                continue;
                            }
                        }
                        //Diagonal NE
                        if (x == 2 && y == 2) {
                            if (farNEObstructed(worldPointListEntry.getKey(), impassible, walkable)) {
                                continue;
                            }
                        }
                    }
                    ArrayList<WorldPoint> newPath = new ArrayList<>(worldPointListEntry.getValue());
                    //					System.out.println("adding: "+counter);
                    //					counter++;
                    newPath.add(point);
                    walked.add(point);
                    if (point.getX() == goal.getX() && point.getY() == goal.getY()) {
                        return newPath;
                    }
                    paths2.put(point, newPath);
                }
            }
            paths2.put(worldPointListEntry.getKey(), null);
        }
        paths2.entrySet().removeIf(x -> x.getValue() == null);
        if (paths2.isEmpty()) {
            System.out.println("path not possible");
            return null;
        }
        return pathToGoal(goal, paths2, impassible, dangerous, walkable, walked);
    }

//    	@SneakyThrows
//    	public static List<WorldPoint> pathToGoal(WorldPoint goal, HashMap<WorldPoint, List<WorldPoint>> paths,
//    											  HashSet<WorldPoint> impassible, HashSet<WorldPoint> dangerous,
//    											  HashSet<WorldPoint> walkable)
//    	{
//    		HashMap<WorldPoint, List<WorldPoint>> paths2 = new HashMap<>(paths);
//    		if (!walkable.contains(goal))
//    		{
//    			return null;
//    		}
//    		for (Map.Entry<WorldPoint, List<WorldPoint>> worldPointListEntry : paths.entrySet())
//    		{
//    			//			int counter = 1;
//    			for (int x = -2; x < 3; x++)
//    			{
//    				b:
//    				for (int y = -2; y < 3; y++)
//    				{
//    					if (x == 0 && y == 0)
//    					{
//    						continue;
//    					}
//
//    					//L movement banned
//    					if (Math.abs(x) + Math.abs(y) == 3)
//    					{
//    						continue;
//    					}
//    					//L movement banned
//    					WorldPoint point = worldPointListEntry.getKey().dy(x).dx(y);
//    					if (!walkable.contains(point) || impassible.contains(point) || dangerous.contains(point))
//    					{
//    						//						System.out.println("rejecting 1");
//    						continue;
//    					}
//
//    					if (x == -2 && y == -2)
//    					{
//    						if (farSWObstructed(worldPointListEntry.getKey(), impassible, walkable))
//    						{
//    							continue;
//    						}
//    					}
//    					if (x == -2 && y == 2)
//    					{
//    						if (farNWObstructed(worldPointListEntry.getKey(), impassible, walkable))
//    						{
//    							continue;
//    						}
//    					}
//    					if (x == 2 && y == -2)
//    					{
//    						if (farSEObstructed(worldPointListEntry.getKey(), impassible, walkable))
//    						{
//    							continue;
//    						}
//    					}
//    					if (x == 2 && y == 2)
//    					{
//    						if (farNEObstructed(worldPointListEntry.getKey(), impassible, walkable))
//    						{
//    							continue;
//    						}
//    					}
//    					if (x == -2 && y == 0)
//    					{
//    						if (farWObstructed(worldPointListEntry.getKey(), impassible, walkable))
//    						{
//    							continue;
//    						}
//    					}
//    					if (x == 2 && y == 0)
//    					{
//    						if (farEObstructed(worldPointListEntry.getKey(), impassible, walkable))
//    						{
//    							continue;
//    						}
//    					}
//    					if (x == 0 && y == -2)
//    					{
//    						if (farSObstructed(worldPointListEntry.getKey(), impassible, walkable))
//    						{
//    							continue;
//    						}
//    					}
//    					if (x == 0 && y == 2)
//    					{
//    						if (farNObstructed(worldPointListEntry.getKey(), impassible, walkable))
//    						{
//    							continue;
//    						}
//    					}
//    					if (x == -1 && y == -1)
//    					{
//    						if (swObstructed(worldPointListEntry.getKey(), impassible, walkable))
//    						{
//    							continue;
//    						}
//    					}
//    					if (x == -1 && y == 1)
//    					{
//    						if (nwObstructed(worldPointListEntry.getKey(), impassible, walkable))
//    						{
//    							continue;
//    						}
//    					}
//    					if (x == 1 && y == -1)
//    					{
//    						if (seObstructed(worldPointListEntry.getKey(), impassible, walkable))
//    						{
//    							continue;
//    						}
//    					}
//    					if (x == 1 && y == 1)
//    					{
//    						if (neObstructed(worldPointListEntry.getKey(), impassible, walkable))
//    						{
//    							continue;
//    						}
//    					}
//    					for (Map.Entry<WorldPoint, List<WorldPoint>> worldPointListEntry2 : paths.entrySet())
//    					{
//    						if (worldPointListEntry2.getValue().contains(point))
//    						{
//    							continue b;
//    						}
//    					}
//    					List<WorldPoint> newPath = new ArrayList<>(worldPointListEntry.getValue());
//    					//					System.out.println("adding: "+counter);
//    					//					counter++;
//    					newPath.add(point);
//    					if (point.getX() == goal.getX() && point.getY() == goal.getY())
//    					{
//    						return newPath;
//    					}
//    					paths2.put(point, newPath);
//    				}
//    			}
//    			paths2.put(worldPointListEntry.getKey(), null);
//    		}
//    		paths2.entrySet().removeIf(x -> x.getValue() == null);
//    		if (paths2.isEmpty())
//    		{
//    			System.out.println("path not possible");
//    			return null;
//    		}
//    		return pathToGoal(goal, paths2, impassible, dangerous, walkable);
//    	}
//
//    static boolean nwObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
//        if (impassible.contains(starting.dx(-1).dy(0)) || !walkable.contains(starting.dx(-1).dy(0))) {
//            return true;
//        }
//        if (impassible.contains(starting.dx(0).dy(1)) || !walkable.contains(starting.dx(0).dy(1))) {
//            return true;
//        }
//        return false;
//    }


    static boolean nwObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        if (impassible.contains(starting.dx(-1).dy(0)) || !walkable.contains(starting.dx(-1).dy(0))) {
            return true;
        }
        return impassible.contains(starting.dx(0).dy(1)) || !walkable.contains(starting.dx(0).dy(1));
    }

    public static void sendClientMessage(String message) {
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", message, null);
    }

    static boolean neObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        if (impassible.contains(starting.dx(1).dy(0)) || !walkable.contains(starting.dx(1).dy(0))) {
            return true;
        }
        return impassible.contains(starting.dx(0).dy(1)) || !walkable.contains(starting.dx(0).dy(1));
    }

    //L movement of 2 north one east, as opposed to 2 east one north (what should we call this difference?)
    static boolean northEastLObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        if (impassible.contains(starting.dx(1).dy(1)) || !walkable.contains(starting.dx(1).dy(1))) {
            return true;
        }
        if (impassible.contains(starting.dx(0).dy(1)) || !walkable.contains(starting.dx(0).dy(1))) {
            return true;
        }
        return impassible.contains(starting.dx(0).dy(2)) || !walkable.contains(starting.dx(0).dy(2));
    }

    //L movement of 2 east one North, as opposed to 2 north one east
    static boolean eastNorthLObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        if (impassible.contains(starting.dx(1).dy(1)) || !walkable.contains(starting.dx(1).dy(1))) {
            return true;
        }
        if (impassible.contains(starting.dx(1).dy(0)) || !walkable.contains(starting.dx(1).dy(0))) {
            return true;
        }
        return impassible.contains(starting.dx(2).dy(0)) || !walkable.contains(starting.dx(2).dy(0));
    }

    static boolean eastSouthLObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        if (impassible.contains(starting.dx(1).dy(-1)) || !walkable.contains(starting.dx(1).dy(-1))) {
            return true;
        }
        if (impassible.contains(starting.dx(1).dy(0)) || !walkable.contains(starting.dx(1).dy(0))) {
            return true;
        }
        return impassible.contains(starting.dx(2).dy(0)) || !walkable.contains(starting.dx(2).dy(0));
    }

    static boolean southEastLObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        if (impassible.contains(starting.dx(1).dy(-1)) || !walkable.contains(starting.dx(1).dy(-1))) {
            return true;
        }
        if (impassible.contains(starting.dx(0).dy(-1)) || !walkable.contains(starting.dx(0).dy(-1))) {
            return true;
        }
        return impassible.contains(starting.dx(0).dy(-2)) || !walkable.contains(starting.dx(0).dy(-2));
    }

    static boolean southWestLObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        if (impassible.contains(starting.dx(-1).dy(-1)) || !walkable.contains(starting.dx(-1).dy(-1))) {
            return true;
        }
        if (impassible.contains(starting.dx(0).dy(-1)) || !walkable.contains(starting.dx(0).dy(-1))) {
            return true;
        }
        return impassible.contains(starting.dx(0).dy(-2)) || !walkable.contains(starting.dx(0).dy(-2));
    }

    static boolean westSouthLObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        if (impassible.contains(starting.dx(-1).dy(-1)) || !walkable.contains(starting.dx(-1).dy(-1))) {
            return true;
        }
        if (impassible.contains(starting.dx(-1).dy(0)) || !walkable.contains(starting.dx(-1).dy(0))) {
            return true;
        }
        return impassible.contains(starting.dx(-2).dy(0)) || !walkable.contains(starting.dx(-2).dy(0));
    }

    static boolean westNorthLObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        if (impassible.contains(starting.dx(-1).dy(1)) || !walkable.contains(starting.dx(-1).dy(1))) {
            return true;
        }
        if (impassible.contains(starting.dx(-1).dy(0)) || !walkable.contains(starting.dx(-1).dy(0))) {
            return true;
        }
        return impassible.contains(starting.dx(-2).dy(0)) || !walkable.contains(starting.dx(-2).dy(0));
    }

    static boolean northWestLObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        if (impassible.contains(starting.dx(-1).dy(1)) || !walkable.contains(starting.dx(-1).dy(1))) {
            return true;
        }
        if (impassible.contains(starting.dx(0).dy(1)) || !walkable.contains(starting.dx(0).dy(1))) {
            return true;
        }
        return impassible.contains(starting.dx(0).dy(2)) || !walkable.contains(starting.dx(0).dy(2));
    }

    static boolean seObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        if (impassible.contains(starting.dx(1).dy(0)) || !walkable.contains(starting.dx(1).dy(0))) {
            return true;
        }
        return impassible.contains(starting.dx(0).dy(-1)) || !walkable.contains(starting.dx(0).dy(-1));
    }

    static boolean swObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        if (impassible.contains(starting.dx(-1).dy(0)) || !walkable.contains(starting.dx(-1).dy(0))) {
            return true;
        }
        return impassible.contains(starting.dx(0).dy(-1)) || !walkable.contains(starting.dx(0).dy(-1));
    }

    static boolean farNObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        return impassible.contains(starting.dx(0).dy(1)) || !walkable.contains(starting.dx(0).dy(1));
    }

    static boolean farSObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        return impassible.contains(starting.dx(0).dy(-1)) || !walkable.contains(starting.dx(0).dy(-1));
    }

    static boolean farEObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        return impassible.contains(starting.dx(1).dy(0)) || !walkable.contains(starting.dx(1).dy(0));
    }

    static boolean farWObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        return impassible.contains(starting.dx(-1).dy(0)) || !walkable.contains(starting.dx(-1).dy(0));
    }

    static boolean farSWObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        if (impassible.contains(starting.dx(-1).dy(-2)) || !walkable.contains(starting.dx(-1).dy(-2))) {
            return true;
        }
        if (impassible.contains(starting.dx(-2).dy(-1)) || !walkable.contains(starting.dx(-2).dy(-1))) {
            return true;
        }
        if (impassible.contains(starting.dx(0).dy(-1)) || !walkable.contains(starting.dx(0).dy(-1))) {
            return true;
        }
        if (impassible.contains(starting.dx(-1).dy(0)) || !walkable.contains(starting.dx(-1).dy(0))) {
            return true;
        }
        return impassible.contains(starting.dx(-1).dy(-1)) || !walkable.contains(starting.dx(-1).dy(-1));
    }

    static boolean farNWObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        if (impassible.contains(starting.dx(-1).dy(2)) || !walkable.contains(starting.dx(-1).dy(2))) {
            return true;
        }
        if (impassible.contains(starting.dx(-2).dy(1)) || !walkable.contains(starting.dx(-2).dy(1))) {
            return true;
        }
        if (impassible.contains(starting.dx(0).dy(1)) || !walkable.contains(starting.dx(0).dy(1))) {
            return true;
        }
        if (impassible.contains(starting.dx(-1).dy(0)) || !walkable.contains(starting.dx(-1).dy(0))) {
            return true;
        }
        return impassible.contains(starting.dx(-1).dy(1)) || !walkable.contains(starting.dx(-1).dy(1));
    }

    static boolean farNEObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        if (impassible.contains(starting.dx(1).dy(2)) || !walkable.contains(starting.dx(1).dy(2))) {
            return true;
        }
        if (impassible.contains(starting.dx(2).dy(1)) || !walkable.contains(starting.dx(2).dy(1))) {
            return true;
        }
        if (impassible.contains(starting.dx(0).dy(1)) || !walkable.contains(starting.dx(0).dy(1))) {
            return true;
        }
        if (impassible.contains(starting.dx(1).dy(0)) || !walkable.contains(starting.dx(1).dy(0))) {
            return true;
        }
        return impassible.contains(starting.dx(1).dy(1)) || !walkable.contains(starting.dx(1).dy(1));
    }

    static boolean farSEObstructed(WorldPoint starting, HashSet<WorldPoint> impassible, HashSet<WorldPoint> walkable) {
        if (impassible.contains(starting.dx(1).dy(-2)) || !walkable.contains(starting.dx(1).dy(-2))) {
            return true;
        }
        if (impassible.contains(starting.dx(2).dy(-1)) || !walkable.contains(starting.dx(2).dy(-1))) {
            return true;
        }
        if (impassible.contains(starting.dx(0).dy(-1)) || !walkable.contains(starting.dx(0).dy(-1))) {
            return true;
        }
        if (impassible.contains(starting.dx(1).dy(0)) || !walkable.contains(starting.dx(1).dy(0))) {
            return true;
        }
        return impassible.contains(starting.dx(1).dy(-1)) || !walkable.contains(starting.dx(1).dy(-1));
    }

    @Override
    public void startUp() throws Exception {
        eventBus.register(RuneLite.getInjector().getInstance(Inventory.class));
        eventBus.register(RuneLite.getInjector().getInstance(Bank.class));
        eventBus.register(RuneLite.getInjector().getInstance(BankInventory.class));
        eventBus.register(RuneLite.getInjector().getInstance(NPCs.class));
        eventBus.register(RuneLite.getInjector().getInstance(TileObjects.class));
        eventBus.register(RuneLite.getInjector().getInstance(Players.class));
        eventBus.register(RuneLite.getInjector().getInstance(Equipment.class));
        eventBus.register(RuneLite.getInjector().getInstance(DepositBox.class));
    }
}
