package com.example.EthanApiPlugin.Collections.query;

import com.example.EthanApiPlugin.EthanApiPlugin;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.RuneLite;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlayerQuery {
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    private List<Player> players;

    public PlayerQuery(List<Player> players) {
        this.players = new ArrayList<>(players);
    }

    public PlayerQuery withName(String name) {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getName().equals(name))
                filteredPlayers.add(player);
        }
        players = filteredPlayers;
        return this;
    }

    public PlayerQuery withinWorldArea(WorldArea area) {
        players = players.stream().filter(player -> area.contains(player.getWorldLocation())).collect(Collectors.toList());
        return this;
    }

    public PlayerQuery filter(Predicate<? super Player> predicate) {
        players = players.stream().filter(predicate).collect(Collectors.toList());
        return this;
    }

    public PlayerQuery interactingWithLocal() {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (!player.isInteracting())
                continue;
            if (player.getInteracting() == client.getLocalPlayer())
                filteredPlayers.add(player);
        }
        players = filteredPlayers;
        return this;
    }

    public PlayerQuery interactingWith(Actor actor) {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (!player.isInteracting())
                continue;
            if (player.getInteracting() == actor)
                filteredPlayers.add(player);
        }
        players = filteredPlayers;
        return this;
    }

    public PlayerQuery nameContains(String name) {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getName().contains(name))
                filteredPlayers.add(player);
        }
        players = filteredPlayers;
        return this;
    }

    public PlayerQuery notLocalPlayer() {
        players.remove(client.getLocalPlayer());
        return this;
    }

    public PlayerQuery withinLevel(int low, int high) {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getCombatLevel() >= low && player.getCombatLevel() <= high)
                filteredPlayers.add(player);
        }
        players = filteredPlayers;
        return this;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public Optional<Player> first() {
        if (players.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(players.get(0));
    }

    public List<Player> result() {
        return players;
    }

    public Optional<Player> nearestByPath() {
        return players.stream().min(Comparator.comparingInt(o -> {
            EthanApiPlugin.PathResult path = EthanApiPlugin.canPathToTile(o.getWorldLocation());
            if (!path.isReachable()) {
                return Integer.MAX_VALUE;
            }
            return path.getDistance();
        }));
    }
}
