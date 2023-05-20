package com.example.EthanApiPlugin.Collections.query;

import com.example.EthanApiPlugin.EthanApiPlugin;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.RuneLite;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NPCQuery {
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    private List<NPC> npcs;

    public NPCQuery(List<NPC> npcs) {
        this.npcs = new ArrayList(npcs);
    }

    public NPCQuery filter(Predicate<? super NPC> predicate) {
        npcs = npcs.stream().filter(predicate).collect(Collectors.toList());
        return this;
    }

    public NPCQuery atLocation(WorldPoint wp){
        npcs = npcs.stream().filter(npc -> npc.getWorldLocation().equals(wp)).collect(Collectors.toList());
        return this;
    }
    public NPCQuery withAction(String action) {
        npcs = npcs.stream().filter(npc -> {
            NPCComposition npcComposition = getNPCComposition(npc);
            if (npcComposition == null)
                return false;
            String[] actions = getNPCComposition(npc).getActions();
            return Arrays.stream(actions).filter(Objects::nonNull).anyMatch(a -> a.equalsIgnoreCase(action));
        }).collect(Collectors.toList());
        return this;
    }

    public NPCQuery withId(int id) {
        npcs = npcs.stream().filter(npc -> npc.getId() == id).collect(Collectors.toList());
        return this;
    }

    public NPCQuery withName(String name) {
        npcs = npcs.stream().filter(npcs -> npcs.getName() != null && npcs.getName().equals(name)).collect(Collectors.toList());
        return this;
    }

    public NPCQuery nameContains(String name) {
        npcs = npcs.stream().filter(npcs -> npcs.getName() != null && npcs.getName().contains(name)).collect(Collectors.toList());
        return this;
    }

    public NPCQuery interactingWithLocal() {
        List<NPC> filteredNPCs = new ArrayList<>();
        for (NPC npc : npcs) {
            if (!npc.isInteracting())
                continue;
            if (npc.getInteracting() == client.getLocalPlayer())
                filteredNPCs.add(npc);
        }
        npcs = filteredNPCs;
        return this;
    }

    public NPCQuery interactingWith(Actor actor) {
        List<NPC> filteredNPCs = new ArrayList<>();
        for (NPC npc : npcs) {
            if (!npc.isInteracting())
                continue;
            if (npc.getInteracting() == actor)
                filteredNPCs.add(npc);
        }
        npcs = filteredNPCs;
        return this;
    }

    public NPCQuery idInList(List<Integer> ids) {
        npcs = npcs.stream().filter(npcs -> ids.contains(npcs.getId())).collect(Collectors.toList());
        return this;
    }

    public NPCQuery indexIs(int index) {
        npcs = npcs.stream().filter(npcs -> npcs.getIndex() == index).collect(Collectors.toList());
        return this;
    }

    public boolean empty() {
        return npcs.size() == 0;
    }

    public NPCQuery filterUnique() {
        npcs =
                npcs.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(NPC::getId))), ArrayList::new));
        return this;
    }

    public List<NPC> result() {
        return npcs;
    }

    public NPCQuery walkable() {
        npcs = npcs.stream().filter(npc -> EthanApiPlugin.canPathToTile(npc.getWorldLocation()).isReachable()).collect(Collectors.toList());
        return this;
    }

    public NPCQuery interacting() {
        npcs = npcs.stream().filter(Actor::isInteracting).collect(Collectors.toList());
        return this;
    }

    public NPCQuery notInteracting() {
        npcs = npcs.stream().filter(npc -> !npc.isInteracting()).collect(Collectors.toList());
        return this;
    }

    public NPCQuery meleeable() {
        List<WorldPoint> meleeTiles = new ArrayList<>();
        meleeTiles.add(client.getLocalPlayer().getWorldLocation().dx(1));
        meleeTiles.add(client.getLocalPlayer().getWorldLocation().dx(-1));
        meleeTiles.add(client.getLocalPlayer().getWorldLocation().dy(1));
        meleeTiles.add(client.getLocalPlayer().getWorldLocation().dy(-1));
        npcs =
                npcs.stream().filter(npc -> !Collections.disjoint(meleeTiles, npc.getWorldArea().toWorldPointList()) && npc.getWorldArea().distanceTo(client.getLocalPlayer().getWorldLocation()) == 1).collect(Collectors.toList());
        return this;
    }

    public NPCQuery alive() {
        npcs = npcs.stream().filter(npc -> !npc.isDead() && npc.getHealthRatio() != 0).collect(Collectors.toList());
        return this;
    }

    public Optional<NPC> nearestToPlayer() {
        if (npcs.size() == 0) {
            return Optional.ofNullable(null);
        }
        return npcs.stream().min(Comparator.comparingInt(npc -> npc.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation())));
    }

    public Optional<NPC> nearestToPoint(WorldPoint point) {
        if (npcs.size() == 0) {
            return Optional.ofNullable(null);
        }
        return npcs.stream().min(Comparator.comparingInt(npc -> npc.getWorldLocation().distanceTo(point)));
    }

    public Optional<NPC> first() {
        if (npcs.size() == 0) {
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(npcs.get(0));
    }

    public static NPCComposition getNPCComposition(NPC npc) {
        if (npc.getComposition().getConfigs() == null || npc.getTransformedComposition() == null) {
            return npc.getComposition();
        }
        return npc.getTransformedComposition();
    }
}
