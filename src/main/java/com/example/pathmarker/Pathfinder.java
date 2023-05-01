package com.example.pathmarker;

import net.runelite.api.Client;
import net.runelite.api.CollisionData;
import net.runelite.api.Player;
import net.runelite.api.Tile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pathfinder
{
    private final Client client;

    private final PathMarkerConfig config;

    private final PathMarkerPlugin plugin;

    private final int[][] directions = new int[128][128];

    private final int[][] distances = new int[128][128];

    private final int[] bufferX = new int[4096];

    private final int[] bufferY = new int[4096];

    public Pathfinder(Client client, PathMarkerConfig config, PathMarkerPlugin plugin)
    {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
    }

    public Pair<List<WorldPoint>, Boolean> pathTo(int approxDestinationX, int approxDestinationY, int sizeX, int sizeY, int objConfig, int objID)
    {
        Player player = client.getLocalPlayer();
        if (player == null)
        {
            return null;
        }
        int z = client.getPlane();

        CollisionData[] collisionData = client.getCollisionMaps();
        if (collisionData == null)
        {
            return null;
        }

        // Initialise directions and distances
        for (int i = 0; i < 128; ++i)
        {
            for (int j = 0; j < 128; ++j)
            {
                directions[i][j] = 0;
                distances[i][j] = Integer.MAX_VALUE;
            }
        }
        LocalPoint playerTrueTileLocalPoint = LocalPoint.fromWorld(client, player.getWorldLocation());
        if (playerTrueTileLocalPoint == null)
        {
            return null;
        }
        int middleX = playerTrueTileLocalPoint.getSceneX();
        int middleY = playerTrueTileLocalPoint.getSceneY();
        int currentX = middleX;
        int currentY = middleY;
        int offsetX = 64;
        int offsetY = 64;
        // Initialise directions and dist
        directions[offsetX][offsetY] = 99;
        distances[offsetX][offsetY] = 0;
        int index1 = 0;
        bufferX[0] = currentX;
        int index2 = 1;
        bufferY[0] = currentY;
        int[][] collisionDataFlags = collisionData[z].getFlags();

        boolean isReachable = false;

        while (index1 != index2)
        {
            currentX = bufferX[index1];
            currentY = bufferY[index1];
            index1 = index1 + 1 & 4095;
            // currentX is for the local coordinate while currentMapX is for the index in the directions and distances arrays
            int currentMapX = currentX - middleX + offsetX;
            int currentMapY = currentY - middleY + offsetY;

            if (hasArrived(currentX, currentY, approxDestinationX, approxDestinationY, sizeX, sizeY, objConfig, objID, collisionDataFlags))
            {
                isReachable = true;
                break;
            }

            int currentDistance = distances[currentMapX][currentMapY] + 1;
            if (currentMapX > 0 && directions[currentMapX - 1][currentMapY] == 0 && (collisionDataFlags[currentX - 1][currentY] & 19136776) == 0)
            {
                // Able to move 1 tile west
                bufferX[index2] = currentX - 1;
                bufferY[index2] = currentY;
                index2 = index2 + 1 & 4095;
                directions[currentMapX - 1][currentMapY] = 2;
                distances[currentMapX - 1][currentMapY] = currentDistance;
            }

            if (currentMapX < 127 && directions[currentMapX + 1][currentMapY] == 0 && (collisionDataFlags[currentX + 1][currentY] & 19136896) == 0)
            {
                // Able to move 1 tile east
                bufferX[index2] = currentX + 1;
                bufferY[index2] = currentY;
                index2 = index2 + 1 & 4095;
                directions[currentMapX + 1][currentMapY] = 8;
                distances[currentMapX + 1][currentMapY] = currentDistance;
            }

            if (currentMapY > 0 && directions[currentMapX][currentMapY - 1] == 0 && (collisionDataFlags[currentX][currentY - 1] & 19136770) == 0)
            {
                // Able to move 1 tile south
                bufferX[index2] = currentX;
                bufferY[index2] = currentY - 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX][currentMapY - 1] = 1;
                distances[currentMapX][currentMapY - 1] = currentDistance;
            }

            if (currentMapY < 127 && directions[currentMapX][currentMapY + 1] == 0 && (collisionDataFlags[currentX][currentY + 1] & 19136800) == 0)
            {
                // Able to move 1 tile north
                bufferX[index2] = currentX;
                bufferY[index2] = currentY + 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX][currentMapY + 1] = 4;
                distances[currentMapX][currentMapY + 1] = currentDistance;
            }

            if (currentMapX > 0 && currentMapY > 0 && directions[currentMapX - 1][currentMapY - 1] == 0 && (collisionDataFlags[currentX - 1][currentY - 1] & 19136782) == 0 && (collisionDataFlags[currentX - 1][currentY] & 19136776) == 0 && (collisionDataFlags[currentX][currentY - 1] & 19136770) == 0)
            {
                // Able to move 1 tile south-west
                bufferX[index2] = currentX - 1;
                bufferY[index2] = currentY - 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX - 1][currentMapY - 1] = 3;
                distances[currentMapX - 1][currentMapY - 1] = currentDistance;
            }

            if (currentMapX < 127 && currentMapY > 0 && directions[currentMapX + 1][currentMapY - 1] == 0 && (collisionDataFlags[currentX + 1][currentY - 1] & 19136899) == 0 && (collisionDataFlags[currentX + 1][currentY] & 19136896) == 0 && (collisionDataFlags[currentX][currentY - 1] & 19136770) == 0)
            {
                // Able to move 1 tile south-east
                bufferX[index2] = currentX + 1;
                bufferY[index2] = currentY - 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX + 1][currentMapY - 1] = 9;
                distances[currentMapX + 1][currentMapY - 1] = currentDistance;
            }

            if (currentMapX > 0 && currentMapY < 127 && directions[currentMapX - 1][currentMapY + 1] == 0 && (collisionDataFlags[currentX - 1][currentY + 1] & 19136824) == 0 && (collisionDataFlags[currentX - 1][currentY] & 19136776) == 0 && (collisionDataFlags[currentX][currentY + 1] & 19136800) == 0)
            {
                // Able to move 1 tile north-west
                bufferX[index2] = currentX - 1;
                bufferY[index2] = currentY + 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX - 1][currentMapY + 1] = 6;
                distances[currentMapX - 1][currentMapY + 1] = currentDistance;
            }

            if (currentMapX < 127 && currentMapY < 127 && directions[currentMapX + 1][currentMapY + 1] == 0 && (collisionDataFlags[currentX + 1][currentY + 1] & 19136992) == 0 && (collisionDataFlags[currentX + 1][currentY] & 19136896) == 0 && (collisionDataFlags[currentX][currentY + 1] & 19136800) == 0)
            {
                // Able to move 1 tile north-east
                bufferX[index2] = currentX + 1;
                bufferY[index2] = currentY + 1;
                index2 = index2 + 1 & 4095;
                directions[currentMapX + 1][currentMapY + 1] = 12;
                distances[currentMapX + 1][currentMapY + 1] = currentDistance;
            }
        }
        if (!isReachable)
        {
            // Try find a different reachable tile in the 21x21 area around the target tile, as close as possible to the target tile
            int upperboundDistance = Integer.MAX_VALUE;
            int pathLength = Integer.MAX_VALUE;
            int checkRange = 10;
            for (int i = approxDestinationX - checkRange; i <= checkRange + approxDestinationX; ++i)
            {
                for (int j = approxDestinationY - checkRange; j <= checkRange + approxDestinationY; ++j)
                {
                    int currentMapX = i - middleX + offsetX;
                    int currentMapY = j - middleY + offsetY;
                    if (currentMapX >= 0 && currentMapY >= 0 && currentMapX < 128 && currentMapY < 128 && distances[currentMapX][currentMapY] < 100)
                    {
                        int deltaX = 0;
                        if (i < approxDestinationX)
                        {
                            deltaX = approxDestinationX - i;
                        }
                        else if (i > approxDestinationX + sizeX - 1)
                        {
                            deltaX = i - (approxDestinationX + sizeX - 1);
                        }

                        int deltaY = 0;
                        if (j < approxDestinationY)
                        {
                            deltaY = approxDestinationY - j;
                        }
                        else if (j > approxDestinationY + sizeY - 1)
                        {
                            deltaY = j - (approxDestinationY + sizeY - 1);
                        }

                        int distanceSquared = deltaX * deltaX + deltaY * deltaY;
                        if (distanceSquared < upperboundDistance || distanceSquared == upperboundDistance && distances[currentMapX][currentMapY] < pathLength)
                        {
                            upperboundDistance = distanceSquared;
                            pathLength = distances[currentMapX][currentMapY];
                            currentX = i;
                            currentY = j;
                        }
                    }
                }
            }
            if (upperboundDistance == Integer.MAX_VALUE)
            {
                // No path found
                List<WorldPoint> checkpointWPs = new ArrayList<>();
                checkpointWPs.add(plugin.getActiveCheckpointWPs().get(0));
                return Pair.of(checkpointWPs,false);
            }
        }

        // Getting path from directions and distances
        bufferX[0] = currentX;
        bufferY[0] = currentY;
        int index = 1;
        int directionNew;
        int directionOld;
        for (directionNew = directionOld = directions[currentX - middleX + offsetX][currentY - middleY + offsetY]; middleX != currentX || middleY != currentY; directionNew = directions[currentX - middleX + offsetX][currentY - middleY + offsetY])
        {
            if (directionNew != directionOld)
            {
                // "Corner" of the path --> new checkpoint tile
                directionOld = directionNew;
                bufferX[index] = currentX;
                bufferY[index++] = currentY;
            }

            if ((directionNew & 2) != 0)
            {
                ++currentX;
            }
            else if ((directionNew & 8) != 0)
            {
                --currentX;
            }

            if ((directionNew & 1) != 0)
            {
                ++currentY;
            }
            else if ((directionNew & 4) != 0)
            {
                --currentY;
            }
        }

        int checkpointTileNumber = 1;
        Tile[][][] tiles = client.getScene().getTiles();
        List<WorldPoint> checkpointWPs = new ArrayList<>();
        while (index-- > 0)
        {
            checkpointWPs.add(tiles[z][bufferX[index]][bufferY[index]].getWorldLocation());
            if (checkpointTileNumber == 25)
            {
                // Pathfinding only supports up to the 25 first checkpoint tiles
                break;
            }
            checkpointTileNumber++;
        }
        if (checkpointWPs.size() == 0)
        {
            checkpointWPs.add(player.getWorldLocation());
            return Pair.of(checkpointWPs,true);
        }
        return Pair.of(checkpointWPs,true);
    }

    public Pair<List<WorldPoint>, Boolean> pathTo(Tile other)
    {
        return pathTo(other.getSceneLocation().getX(), other.getSceneLocation().getY(), 1, 1, -1, -1);
    }

    private boolean hasArrived(int baseX, int baseY, int targetX, int targetY, int sizeX, int sizeY, int objConfig, int objID, int[][] flags)
    {
        int objShape = -1;
        int objRot = 0;
        switch (objConfig)
        {
            case -2:
                objShape = -2;
                break;
            case -1:
                break;
            default:
            {
                objShape = objConfig & 0x1F;
                objRot = objConfig >>> 6 & 3;
            }
        }
        if (objShape != -2)
        {
            // Not pathing to an Actor
            if (targetX <= baseX && baseX <= targetX + sizeX - 1 && targetY <= baseY && baseY <= targetY + sizeY - 1)
            {
                // Inside the object or on the target tile
                return true;
            }
        }
        switch (objShape)
        {
            case 0: // Pathing to straight wall
                return reachStraightWall(flags, baseX, baseY, targetX, targetY, objRot);
            case 2:	// Pathing to L wall
                return reachLWall(flags, baseX, baseY, targetX, targetY, objRot);
            case 6: // Diagonal wall decoration, diagonal offset
                return reachDiagonalWallDecoration(flags, baseX, baseY, targetX, targetY, objRot);
            case 7: // Diagonal wall decoration, no offset
                return reachDiagonalWallDecoration(flags, baseX, baseY, targetX, targetY, objRot + 2 & 0x3);
            case -2: // Pathing to Actor, not an official flag
            case 8: // Diagonal wall decoration, both sides of the wall
            case 9: // Pathing to diagonal wall
            case 10: // Pathing to straight centrepiece
            case 11: // Pathing to diagonal centrepiece
            case 22: // Pathing to ground decor
            {
                int objFlags = 0;
                if (Arrays.asList(10,11,22).contains(objShape))
                {
                    objFlags = PathMarkerPlugin.getObjectBlocking(objID, objRot);
                }
                return reachRectangularBoundary(flags, baseX, baseY, targetX, targetY, sizeX, sizeY, objFlags);
            }
        }
        return false;
    }

    private boolean reachRectangularBoundary(int[][] flags, int x, int y, int destX, int destY, int destWidth, int destHeight, int objectflags)
    {
        int east = destX + destWidth - 1;
        int north = destY + destHeight - 1;
        if (x == destX - 1 && y >= destY && y <= north &&
                (flags[x][y] & 0x8) == 0 &&
                (objectflags & 0x8) == 0)
        {
            //Valid destination tile to the west of the rectangularBoundary
            return true;
        }
        if (x == east + 1 && y >= destY && y <= north &&
                (flags[x][y] & 0x80) == 0 &&
                (objectflags & 0x2) == 0)
        {
            //Valid destination tile to the east of the rectangularBoundary
            return true;
        }
        if (y + 1 == destY && x >= destX && x <= east &&
                (flags[x][y] & 0x2) == 0 &&
                (objectflags & 0x4) == 0)
        {
            //Valid destination tile to the south of the rectangularBoundary
            return true;
        }
        return y == north + 1 && x >= destX && x <= east &&
                (flags[x][y] & 0x20) == 0 &&
                (objectflags & 0x1) == 0;
        //Test for valid destination tile to the north of the rectangularBoundary
    }

    private boolean reachStraightWall(int[][] flags, int x, int y, int destX, int destY, int rot)
    {
        switch (rot)
        {
            case 0:
            {
                if (x == destX - 1 && y == destY)
                    return true;
                if (x == destX && y == destY + 1 && (flags[x][y] & 0x12c0120) == 0)
                    return true;
                if (x == destX && y == destY - 1 && (flags[x][y] & 0x12c0102) == 0)
                    return true;
                break;
            }
            case 1:
            {
                if (x == destX && y == destY + 1)
                    return true;
                if (x == destX - 1 && y == destY && (flags[x][y] & 0x12c0108) == 0)
                    return true;
                if (x == destX + 1 && y == destY && (flags[x][y] & 0x12c0180) == 0)
                    return true;
                break;
            }
            case 2:
            {
                if (x == destX + 1 && y == destY)
                    return true;
                if (x == destX && y == destY + 1 && (flags[x][y] & 0x12c0120) == 0)
                    return true;
                if (x == destX && y == destY - 1 && (flags[x][y] & 0x12c0102) == 0)
                    return true;
                break;
            }
            case 3:
            {
                if (x == destX && y == destY - 1)
                    return true;
                if (x == destX - 1 && y == destY && (flags[x][y] & 0x12c0108) == 0)
                    return true;
                if (x == destX + 1 && y == destY && (flags[x][y] & 0x12c0180) == 0)
                    return true;
            }
        }
        return false;
    }

    private boolean reachLWall(int[][] flags, int x, int y, int destX, int destY, int rot)
    {
        int WESTWALLFLAGS = 0x12c0108;
        int NORTHWALLFLAGS = 0x12c0120;
        int EASTWALLFLAGS = 0x12c0180;
        int SOUTHWALLFLAGS = 0x12c0102;
        switch (rot)
        {
            case 0:
            {
                WESTWALLFLAGS = 0;
                NORTHWALLFLAGS = 0;
                break;
            }
            case 1:
            {
                NORTHWALLFLAGS = 0;
                EASTWALLFLAGS = 0;
                break;
            }
            case 2:
            {
                EASTWALLFLAGS = 0;
                SOUTHWALLFLAGS = 0;
                break;
            }
            case 3:
            {
                SOUTHWALLFLAGS = 0;
                WESTWALLFLAGS = 0;
            }
        }
        if (x == destX - 1 && y == destY && (flags[x][y] & WESTWALLFLAGS) == 0)
            return true;
        if (x == destX && y == destY + 1 && (flags[x][y] & NORTHWALLFLAGS) == 0)
            return true;
        if (x == destX + 1 && y == destY && (flags[x][y] & EASTWALLFLAGS) == 0)
            return true;
        return x == destX && y == destY - 1 && (flags[x][y] & SOUTHWALLFLAGS) == 0;
    }

    private boolean reachDiagonalWallDecoration(int[][] flags, int x, int y, int destX, int destY, int rot)
    {
        switch (rot)
        {
            case 0:
            {
                if (x == destX + 1 && y == destY && (flags[x][y] & 0x80) == 0)
                    return true;
                if (x == destX && y == destY - 1 && (flags[x][y] & 0x2) == 0)
                    return true;
                break;
            }
            case 1:
            {
                if (x == destX - 1 && y == destY && (flags[x][y] & 0x4) == 0)
                    return true;
                if (x == destX && y == destY - 1 && (flags[x][y] & 0x2) == 0)
                    return true;
                break;
            }
            case 2:
            {
                if (x == destX - 1 && y == destY && (flags[x][y] & 0x4) == 0)
                    return true;
                if (x == destX && y == destY + 1 && (flags[x][y] & 0x20) == 0)
                    return true;
                break;
            }
            case 3:
            {
                if (x == destX + 1 && y == destY && (flags[x][y] & 0x80) == 0)
                    return true;
                if (x == destX && y == destY + 1 && (flags[x][y] & 0x20) == 0)
                    return true;
            }
        }
        return false;
    }
}
