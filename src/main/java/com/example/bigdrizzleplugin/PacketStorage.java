package com.example.bigdrizzleplugin;

import com.example.PacketType;
import com.example.Packets.*;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;

public class PacketStorage {
    @Getter @Setter Object interactionTarget;
    @Getter @Setter Object interactionSource;
    @Getter @Setter String action;
    @Getter @Setter PacketType packetType;

    public PacketStorage (Object interactionTarget, String action, PacketType packetType){
        this.interactionTarget = interactionTarget;
        this.action = action;
        this.packetType = packetType;
    }

    public PacketStorage (Object interactionTarget, String action, PacketType packetType, Object interactionSource){
        this.interactionTarget = interactionTarget;
        this.action = action;
        this.packetType = packetType;
        this.interactionSource = interactionSource;
    }

    public void send(){
        switch(packetType){
            case RESUME_PAUSEBUTTON:
                //deal with this later
                break;
            case IF_BUTTON:
                if (interactionTarget instanceof Widget){
                    WidgetPackets.queueWidgetAction((Widget) interactionTarget, action);
                } else{
                    System.out.println("Error: Object is not Widget");
                }
                break;
            case OPNPC:
                if (interactionTarget instanceof NPC) {
                    NPCPackets.queueNPCAction((NPC) interactionTarget, action);
                }else{
                    System.out.println("Error: Object is not NPC");
                }
                break;
            case OPPLAYER:
                if (interactionTarget instanceof Player){
                    PlayerPackets.queuePlayerAction((Player) interactionTarget, action);
                } else {
                    System.out.println("Interaction target and menu type disagreed");
                }
                break;
            case OPOBJ:
                //Deal with tile item non-sense later
                break;
            case OPLOC:
                if (interactionTarget instanceof TileObject){
                    ObjectPackets.queueObjectAction((TileObject) interactionTarget, false, action);
                } else {
                    System.out.println("Interaction target and menu type disagreed");
                }
                break;
            case MOVE_GAMECLICK:
                if (interactionTarget instanceof WorldPoint){
                    MovementPackets.queueMovement((WorldPoint) interactionTarget);
                } else{
                    System.out.println("Interaction target and menu type disagreed");
                }
                break;
            case IF_BUTTONT:
                if (interactionTarget instanceof Widget && interactionSource instanceof Widget) {
                    WidgetPackets.queueWidgetOnWidget((Widget) interactionSource, (Widget) interactionTarget);
                } else{
                    System.out.println("Interaction target and menu type disagreed");
                }
                break;
            case OPNPCT:
                if (interactionTarget instanceof NPC && interactionSource instanceof Widget){
                    NPCPackets.queueWidgetOnNPC((NPC)interactionTarget, (Widget)interactionSource);
                }else{
                    System.out.println("Interaction target and menu type disagreed");
                }
                break;
            case OPPLAYERT:
                if (interactionTarget instanceof Player && interactionSource instanceof Widget){
                    PlayerPackets.queueWidgetOnPlayer((Player) interactionTarget, (Widget) interactionSource);
                }else{
                    System.out.println("Interaction target and menu type disagreed");
                }
                break;
            case OPOBJT:
                //Deal with tile item non-sense later
                break;
            case OPLOCT:
                if (interactionTarget instanceof TileObject && interactionSource instanceof Widget){
                    ObjectPackets.queueWidgetOnTileObject((Widget) interactionSource, (TileObject)interactionTarget);
                }else{
                    System.out.println("Interaction target and menu type disagreed");
                }
                break;
        }
    }
}
