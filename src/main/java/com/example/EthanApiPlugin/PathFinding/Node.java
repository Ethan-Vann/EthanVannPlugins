package com.example.EthanApiPlugin.PathFinding;

import net.runelite.api.coords.WorldPoint;

public class Node {
    WorldPoint data;
    Node previous;

    Node(WorldPoint data) {
        this.data = data;
    }
    Node(){
        this.data = null;
        this.previous = null;
    }
    Node(WorldPoint data, Node previous) {
        this.data = data;
        this.previous = previous;
    }

    public WorldPoint getData() {
        return data;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setNode(WorldPoint data, Node previous){
        this.data = data;
        this.previous = previous;
    }
}
