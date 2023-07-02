package com.example.EthanApiPlugin.Utility;

import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

import java.util.function.Predicate;

public class WorldAreaExtended
{
	public static Point getComparisonPoint(WorldArea area1, WorldArea area2)
	{
		int x1 = area1.getX();
		int y1 = area1.getY();
		int x2 = area2.getX();
		int y2 = area2.getY();
		int w1 = area1.getWidth();
		int h1 = area1.getHeight();
		int x, y;
		if (x2 <= x1)
		{
			x = x1;
		}
		else
		{
			x = Math.min(x2, x1 + w1 - 1);
		}
		if (y2 <= y1)
		{
			y = y1;
		}
		else
		{
			y = Math.min(y2, y1 + h1 - 1);
		}
		return new Point(x, y);
	}
	
	public static Point getAxisDistances(WorldArea area1, WorldArea area2)
	{
		Point p1 = getComparisonPoint(area1, area2);
		Point p2 = getComparisonPoint(area2, area1);
		return new Point(Math.abs(p1.getX() - p2.getX()), Math.abs(p1.getY() - p2.getY()));
	}
	
	public static WorldArea calculateNextTravellingPoint(Client client, WorldArea original, WorldArea target, boolean stopAtMeleeDistance,
												   Predicate<? super WorldPoint> extraCondition)
	{
		int z1 = original.getPlane();
		int z2 = target.getPlane();
		
		if (z1 != z2)
		{
			return null;
		}
		
		if (original.intersectsWith(target))
		{
			if (stopAtMeleeDistance)
			{
				// Movement is unpredictable when the NPC and actor stand on top of each other
				return null;
			}
			else
			{
				return original;
			}
		}
		
		int x1 = original.getX();
		int y1 = original.getY();
		int x2 = target.getX();
		int y2 = target.getY();
		
		int dx = x2 - x1;
		int dy = y2 - y1;
		
		Point axisDistances = getAxisDistances(original, target);
		int axisX = axisDistances.getX();
		int axisY = axisDistances.getY();
		
		if (stopAtMeleeDistance && axisX + axisY == 1)
		{
			// NPC is in melee distance of target, so no movement is done
			return original;
		}
		
		LocalPoint lp = LocalPoint.fromWorld(client, x1, y1);
		if (lp == null)
		{
			return null;
		}
		int lpSceneX = lp.getSceneX();
		int lpSceneY = lp.getSceneY();
		if (lpSceneX + dx < 0 || lpSceneX + dy >= Constants.SCENE_SIZE
				|| lpSceneY + dx < 0 || lpSceneY + dy >= Constants.SCENE_SIZE)
		{
			// NPC is travelling out of the scene, so collision data isn't available
			return null;
		}
		
		int w1 = original.getWidth();
		int h1 = original.getHeight();
		
		int dxSig = Integer.signum(dx);
		int dySig = Integer.signum(dy);
		if (stopAtMeleeDistance && axisX == 1 && axisY == 1)
		{
			// When it needs to stop at melee distance, it will only attempt
			// to travel along the x-axis when it is standing diagonally
			// from the target
			if (original.canTravelInDirection(client, dxSig, 0, extraCondition))
			{
				return new WorldArea(x1 + dxSig, y1, w1, h1, z1);
			}
		}
		else
		{
			if (original.canTravelInDirection(client, dxSig, dySig, extraCondition))
			{
				return new WorldArea(x1 + dxSig, y1 + dySig, w1, h1, z1);
			}
			else if (dx != 0 && original.canTravelInDirection(client, dxSig, 0, extraCondition))
			{
				return new WorldArea(x1 + dxSig, y1, w1, h1, z1);
			}
			else if (dy != 0 && Math.max(Math.abs(dx), Math.abs(dy)) > 1 && original.canTravelInDirection(client, 0, dy, extraCondition))
			{
				// Note that NPCs don't attempt to travel along the y-axis
				// if the target is <= 1 tile distance away
				return new WorldArea(x1, y1 + dySig, w1, h1, z1);
			}
		}
		
		// The NPC is stuck
		return original;
	}
	
	public static WorldArea calculateNextTravellingPoint(Client client, WorldArea original, WorldArea target, boolean stopAtMeleeDistance)
	{
		return calculateNextTravellingPoint(client, original, target, stopAtMeleeDistance, x -> true);
	}
}
