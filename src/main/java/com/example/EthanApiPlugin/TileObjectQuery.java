package com.example.EthanApiPlugin;

import net.runelite.api.Client;
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
                tileObjects.stream().filter(tileObject -> client.getObjectDefinition(tileObject.getId()).getName().equals(name)).collect(Collectors.toList());
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
                    String[] actions = null;

                    if (client.getObjectDefinition(tileObject.getId()).getImpostorIds() != null && client.getObjectDefinition(tileObject.getId()).getImpostor() != null) {
                        actions = client.getObjectDefinition(tileObject.getId()).getImpostor().getActions();
                    } else {
                        actions = client.getObjectDefinition(tileObject.getId()).getActions();
                    }
                    if (actions == null) {
                        return false;
                    }
                    return Arrays.stream(actions).filter(Objects::nonNull).anyMatch(a -> a.equalsIgnoreCase(action));
                }).collect(Collectors.toList());
        return this;
    }

    public TileObjectQuery atLocation(WorldPoint location) {
        tileObjects = tileObjects.stream().filter(tileObject -> tileObject.getWorldLocation().equals(location)).collect(Collectors.toList());
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
                tileObjects.stream().filter(tileObject -> client.getObjectDefinition(tileObject.getId()).getName().contains(name)).collect(Collectors.toList());
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
}
