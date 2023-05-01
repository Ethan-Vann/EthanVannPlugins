package com.example.Gorilas;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.PacketUtilsPlugin;
import com.example.Packets.MousePackets;
import com.example.Packets.MovementPackets;
import com.example.Packets.WidgetPackets;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.api.Projectile;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.widgets.Widget;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@PluginDependency(PacketUtilsPlugin.class)
@PluginDependency(EthanApiPlugin.class)
@PluginDescriptor(name = "Gorilitas",
tags = {"pajau"})
public class GorilasPlugin extends Plugin {

    @Inject
    private Client client;

    public static Widget prayer;
    public static int animPasada;
    public static int moveToX=0;
    public static int moveToY=0;

    public static Projectile[] piedras = new Projectile[5];

    public static int[] tickLeft = {0,0,0,0,0};
    public static int c=0;


    @Subscribe
    public void onAnimationChanged(AnimationChanged event){
        if(event.getActor().getInteracting() == null){
            return;
        }
        if(event.getActor().getName() == null){return;}
        if(!event.getActor().getName().contains("Demonic")){return;}
        if(Objects.equals(event.getActor().getInteracting().getName(), client.getLocalPlayer().getName())){

            if(event.getActor().getAnimation() == 7227 && client.getVarbitValue(Prayer.PROTECT_FROM_MISSILES.getVarbit()) != 1){ //Ranged
                prayer = client.getWidget(35454998);
                MousePackets.queueClickPacket();
                WidgetPackets.queueWidgetActionPacket(1, prayer.getId(), -1,-1);
            } else if (event.getActor().getAnimation() == 7226 && client.getVarbitValue(Prayer.PROTECT_FROM_MELEE.getVarbit()) != 1) { //melee
                prayer = client.getWidget(35454999);
                MousePackets.queueClickPacket();
                WidgetPackets.queueWidgetActionPacket(1, prayer.getId(), -1,-1);
            } else if (event.getActor().getAnimation() == 7225 && client.getVarbitValue(Prayer.PROTECT_FROM_MAGIC.getVarbit()) != 1) { //magia
                prayer = client.getWidget(35454997);
                MousePackets.queueClickPacket();
                WidgetPackets.queueWidgetActionPacket(1, prayer.getId(), -1,-1);
            } else if (event.getActor().getAnimation() ==7228) {    //bolder

                return;
                //avalibletile();
                //MovementPackets.queueMovement();
            } else{
                return;
            }

            animPasada=event.getActor().getAnimation();
        }
    }

    @Subscribe
    void onProjectileMoved(ProjectileMoved event){
        if(!(event.getProjectile().getId() == 856)){
            return;
        }

        piedras[c]=event.getProjectile();
        tickLeft[c]=5;
        c++;
        c %= 5;

        /*
        LocalPoint lp = event.getProjectile().getTarget();
        WorldPoint WP = WorldPoint.fromLocal(client,lp);

        if(WP.equals(client.getLocalPlayer().getWorldLocation())){

        }

         */
    }

    @Subscribe
    void onInteractingChanged(InteractingChanged event){
        if (event.getTarget() == null || event.getSource() ==null) return;

        if (Objects.equals(event.getTarget().getName(), client.getLocalPlayer().getName()) && Objects.requireNonNull(event.getSource().getName()).contains("Demonic") ) {

            gorillas.add(event.getSource());


        }
    }
    public List<Actor> gorillas = new ArrayList<>();

    @Subscribe
    void onGameTick(GameTick event){

        if(Arrays.stream(tickLeft).anyMatch(a -> a>0 )){
            for(int i=0;i< tickLeft.length;i++){
                if(tickLeft[i]>0){
                    tickLeft[i]--;
                    if(tickLeft[i]==3) {
                        if (client.getLocalPlayer().getWorldLocation().equals(WorldPoint.fromLocal(client,piedras[i].getTarget()))){
                            //moverse
                            WorldArea playerArea = client.getLocalPlayer().getWorldArea();
                            int playerX = playerArea.getX();
                            int playerY = playerArea.getY();

                            WorldPoint tilePeligroso=null;
                            for(int m=0;m< tickLeft.length;m++){
                                if(tickLeft[m]==3){
                                    tilePeligroso = WorldPoint.fromLocal(client,piedras[m].getTarget());
                                }
                            }

                            WorldPoint playerPoint = playerArea.toWorldPoint();
                            //Actor localPlayer = client.getLocalPlayer().getInteracting();

                            log.info("player  WP: {}",playerPoint);
                            //gorillas.forEach(x -> x.getWorldArea().toWorldPointList().forEach(y->log.info("Gorilla TileWP: {}",y)));

                            log.info("N Gorilas: {}",gorillas.size());

                            if(playerArea.canTravelInDirection(client,0,1) && !playerPoint.dy(1).equals(tilePeligroso)
                                    && gorillas.stream().noneMatch( x -> x.getWorldArea().contains(playerPoint.dy(1))) ){
                                //MovementPackets.queueMovement(playerX,playerY+1,false);
                                moveToX = playerX;
                                moveToY = playerY+1;
                            } else if (playerArea.canTravelInDirection(client,1,0) && !playerPoint.dx(1).equals(tilePeligroso)
                                    && gorillas.stream().noneMatch( x -> x.getWorldArea().contains(playerPoint.dx(1))) ){
                                //MovementPackets.queueMovement(playerX+1,playerY,false);
                                moveToX = playerX+1;
                                moveToY = playerY;
                            }else if (playerArea.canTravelInDirection(client,0,-1) && !playerPoint.dy(-1).equals(tilePeligroso)
                                    && gorillas.stream().noneMatch( x -> x.getWorldArea().contains(playerPoint.dy(-1))) ){
                                //MovementPackets.queueMovement(playerX+1,playerY,false);
                                moveToX = playerX;
                                moveToY = playerY-1;
                            }else if (playerArea.canTravelInDirection(client,-1,0) && !playerPoint.dx(-1).equals(tilePeligroso)
                                    && gorillas.stream().noneMatch( x -> x.getWorldArea().contains(playerPoint.dx(-1))) ) {
                                //MovementPackets.queueMovement(playerX+1,playerY,false);
                                moveToX = playerX-1;
                                moveToY = playerY;
                            }else {
                                log.info("cagaste wei");
                            }
                        }
                    }
                }
            }
        }

        if(moveToY>0 && moveToX>0){
            MousePackets.queueClickPacket();
            MovementPackets.queueMovement(moveToX,moveToY,false);
            log.info("Se mueve hacia: {}    {}", (Object) Optional.of(moveToX), (Object) moveToY);
            moveToX=0;
            moveToY=0;
        }

        if (!gorillas.isEmpty()) {
            if (gorillas.stream().anyMatch(x -> !x.isInteracting())) {
                gorillas=gorillas.stream().filter(goril -> !goril.isInteracting()).collect(Collectors.toList());
            }
        }


    }


}
