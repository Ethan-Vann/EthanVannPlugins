package com.example.EthanApiPlugin.Collections.query;

import net.runelite.api.Client;
import net.runelite.api.ObjectComposition;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.RuneLite;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TileObjectQuery {
    private List<TileObject> tileObjects;
    static Client client = RuneLite.getInjector().getInstance(Client.class);

    public TileObjectQuery(List<TileObject> tileObjects) {
        this.tileObjects = new ArrayList(tileObjects);
    }

    public TileObjectQuery withName(String name) {
        tileObjects =
                tileObjects.stream().filter(tileObject -> {
                    ObjectComposition objectComposition = getObjectComposition(tileObject);
                    if (objectComposition == null)
                        return false;
                    return objectComposition.getName().equals(name);
                }).collect(Collectors.toList());
        return this;
    }

    public TileObjectQuery withId(int id) {
        tileObjects = tileObjects.stream().filter(tileObject -> tileObject.getId() == id).collect(Collectors.toList());
        return this;
    }

    public TileObjectQuery withAction(String action) {
        tileObjects =
                tileObjects.stream().filter(tileObject ->
                {
                    ObjectComposition objectComposition = getObjectComposition(tileObject);
                    if (objectComposition == null)
                        return false;
                    String[] actions = getObjectComposition(tileObject).getActions();
                    return Arrays.stream(actions).filter(Objects::nonNull).anyMatch(a -> a.equalsIgnoreCase(action));
                }).collect(Collectors.toList());
        return this;
    }

    public TileObjectQuery atLocation(WorldPoint location) {
        tileObjects = tileObjects.stream().filter(tileObject -> tileObject.getWorldLocation().equals(location)).collect(Collectors.toList());
        return this;
    }

    public TileObjectQuery withinDistance(int distance) {
        tileObjects = tileObjects.stream().filter(tileObject -> tileObject.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation()) <= distance).collect(Collectors.toList());
        return this;
    }

    public TileObjectQuery atLocation(int x, int y, int plane) {
        WorldPoint p = new WorldPoint(x, y, plane);
        tileObjects =
                tileObjects.stream().filter(tileObject -> tileObject.getWorldLocation().equals(p)).collect(Collectors.toList());
        return this;
    }

    public TileObjectQuery filter(Predicate<? super TileObject> predicate) {
        tileObjects = tileObjects.stream().filter(predicate).collect(Collectors.toList());
        return this;
    }

    public TileObjectQuery nameContains(String name) {
        tileObjects =
                tileObjects.stream().filter(tileObject -> {
                    ObjectComposition comp = getObjectComposition(tileObject);
                    if (comp == null)
                        return false;
                    return comp.getName().contains(name);
                }).collect(Collectors.toList());
        return this;
    }

    public TileObjectQuery idInList(List<Integer> ids) {
        tileObjects = tileObjects.stream().filter(tileObject -> ids.contains(tileObject.getId())).collect(Collectors.toList());
        return this;
    }

    public boolean empty() {
        return tileObjects.size() == 0;
    }

    public List<TileObject> result() {
        return tileObjects;
    }

    public Optional<TileObject> first() {
        return tileObjects.stream().findFirst();
    }

    public Optional<TileObject> nearestToPlayer() {
        return tileObjects.stream().min(Comparator.comparingInt(o -> client.getLocalPlayer().getWorldLocation().distanceTo(o.getWorldLocation())));
    }

    public Optional<TileObject> nearestToPoint(WorldPoint point) {
        return tileObjects.stream().min(Comparator.comparingInt(o -> point.distanceTo(o.getWorldLocation())));
    }

    public static ObjectComposition getObjectComposition(TileObject tileObject) {
        if (client.getObjectDefinition(tileObject.getId()).getImpostorIds() == null || client.getObjectDefinition(tileObject.getId()).getImpostor() == null) {
            return client.getObjectDefinition(tileObject.getId());
        }
        return client.getObjectDefinition(tileObject.getId()).getImpostor();
    }
}
