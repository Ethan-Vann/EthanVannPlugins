package com.example.EthanApiPlugin.PathFinding;

import com.example.EthanApiPlugin.EthanApiPlugin;
import net.runelite.api.coords.WorldPoint;
import org.roaringbitmap.RoaringBitmap;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class GlobalCollisionMap {
    static RoaringBitmap bitmap = init();

    static byte[] load() {
        try {
            InputStream is = GlobalCollisionMap.class.getResourceAsStream("map");
            return new GZIPInputStream(is).readAllBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RoaringBitmap init() {
        RoaringBitmap bitmap = new RoaringBitmap();
        try {
            bitmap.deserialize(ByteBuffer.wrap(load()));
            bitmap.runOptimize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bitmap;
    }

    public static boolean east(WorldPoint wp) {
        return bitmap.contains(packed(wp) | (1 << 30));
    }

    public static boolean north(WorldPoint wp) {
        return bitmap.contains(packed(wp));
    }

    public static boolean south(WorldPoint wp) {
        return north(wp.dy(-1));
    }

    public static boolean west(WorldPoint wp) {
        return east(wp.dx(-1));
    }

    public static int packed(int x, int y, int plane) {
        return (x & 16383) | ((y & 16383) << 14) | (plane << 28);
    }

    public static WorldPoint unpack(int packed) {
        return new WorldPoint(packed & 16383, (packed >> 14) & 16383, packed >> 28);
    }

    public static int packed(WorldPoint wp) {
        return (wp.getX() & 16383) | ((wp.getY() & 16383) << 14) | (wp.getPlane() << 28);
    }

    public static List<WorldPoint> findPath(WorldPoint p) {
        long start = System.currentTimeMillis();
        WorldPoint starting = EthanApiPlugin.getClient().getLocalPlayer().getWorldLocation();
        HashSet<WorldPoint> visited = new HashSet<>();
        ArrayDeque<Node> queue = new ArrayDeque<Node>();
        queue.add(new Node(starting));
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            WorldPoint currentData = current.getData();
            if (currentData.equals(p)) {
                List<WorldPoint> ret = new ArrayList<>();
                while (current != null) {
                    ret.add(current.getData());
                    current = current.getPrevious();
                }
                Collections.reverse(ret);
                ret.remove(0);
                System.out.println("Path took " + (System.currentTimeMillis() - start) + "ms");
                return ret;
            }
            //west
            if (west(currentData) && visited.add(currentData.dx(-1))) {
                queue.add(new Node(currentData.dx(-1), current));
            }
            //east
            if (east(currentData) && visited.add(currentData.dx(1))) {
                queue.add(new Node(currentData.dx(1), current));
            }
            //south
            if (south(currentData) && visited.add(currentData.dy(-1))) {
                queue.add(new Node(currentData.dy(-1), current));
            }
            //north
            if (north(currentData) && visited.add(currentData.dy(1))) {
                queue.add(new Node(currentData.dy(1), current));
            }
        }
        return null;
    }
}
