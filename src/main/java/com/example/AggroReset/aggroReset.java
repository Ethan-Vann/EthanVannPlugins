package com.example.AggroReset;

import com.example.Packets.MousePackets;
import com.example.Packets.MovementPackets;
import com.example.pathmarker.Pathfinder;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.Keybind;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.npcunaggroarea.NpcAggroAreaPlugin;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;
import java.awt.event.KeyEvent;
import java.awt.geom.GeneralPath;
import java.time.Instant;
import java.util.Objects;

@Slf4j
@PluginDescriptor(name = "Aggro Reset",
    tags = {"pajau"}
)
@PluginDependency(NpcAggroAreaPlugin.class)
public class  aggroReset extends Plugin {

    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    @Inject
    private KeyManager keyManager;

    @Inject
    private NpcAggroAreaPlugin npcAggroAreaPlugin;

    private GeneralPath AreaSafe;

    private Pathfinder pathfinder;
    private boolean reseteando=false;
    private int timeout=-1;
    private int contador=0;
    private int llave;

    @Override
    protected void startUp() throws Exception {
        keyManager.registerKeyListener(caminador);
        reseteando=false;
        enAccion=false;
        tilePelea=null;
        choosen=null;
    }

    @Override
    protected void shutDown() throws Exception {
        reseteando=false;
        enAccion=false;
        tilePelea=null;
        choosen=null;
    }

    private static WorldPoint choosen = null;
    public static WorldPoint tilePelea = null;
    private boolean enAccion = false;
    private final KeyListener caminador = new HotkeyListener( ()->new Keybind(KeyEvent.VK_F6,0)){
        @Override
        public void hotkeyPressed() {
            clientThread.invoke( () -> {
                enAccion=!enAccion;
                tilePelea=client.getLocalPlayer().getWorldLocation();
                log.info("Se prendio la wea");
                if (!enAccion) {
                    tilePelea=null;
                }

                /*MousePackets.queueClickPacket();
                MovementPackets.queueMovement(choosen);*/
            });
        }
    };

    public boolean InsideSafe(){
        return AreaSafe.contains(client.getLocalPlayer().getLocalLocation().getX(),client.getLocalPlayer().getLocalLocation().getY());
    }

    public boolean InsideSafe(WorldPoint pt){
        return AreaSafe.contains(Objects.requireNonNull(LocalPoint.fromWorld(client, pt)).getX(),
                Objects.requireNonNull(LocalPoint.fromWorld(client, pt)).getY());
    }

    @Subscribe
    void onGameTick(GameTick event){
        if(!enAccion) return;
        if (timeout>0) {
            timeout--;
            return;
        }
        if (AreaSafe == null) {
            return;
        }
        AreaSafe = npcAggroAreaPlugin.getLinesToDisplay()[client.getPlane()];



        CollisionData[] collisionData=client.getCollisionMaps();
        assert collisionData != null;
        CollisionData collActual = collisionData[client.getPlane()];
        Player jugador = client.getLocalPlayer();
        WorldArea playerArea = client.getLocalPlayer().getWorldArea();
        WorldPoint playerPoint = client.getLocalPlayer().getWorldLocation();
        int plano = client.getPlane();
        int ScenePlayerX = client.getLocalPlayer().getLocalLocation().getSceneX();
        int ScenePlayerY = client.getLocalPlayer().getLocalLocation().getSceneY();

        int baseX = client.getBaseX();
        int baseY = client.getBaseY();

        if(playerPoint.equals(tilePelea)
                && npcAggroAreaPlugin.getEndTime().isBefore(Instant.now())
                && !InCombat(jugador)
                && !reseteando)
        {
            reseteando=true;
            timeout=5;
            return;
        }

        if (Instant.now().isAfter(npcAggroAreaPlugin.getEndTime()) && reseteando && choosen==null) {
            for (int i = 1; i < 22; i++) {
                if (isWalkable(collActual,ScenePlayerX+i,ScenePlayerY+i) && !InsideSafe(playerPoint.dx(i).dy(i)) ) {
                    choosen = new WorldPoint(baseX+ScenePlayerX+i,baseY+ScenePlayerY+i,client.getPlane());
                    break;
                } else if (isWalkable(collActual,ScenePlayerX-i,ScenePlayerY+i) && !InsideSafe(playerPoint.dx(-i).dy(i)) ) {
                    choosen = new WorldPoint(baseX+ScenePlayerX-i,baseY+ScenePlayerY+i,client.getPlane());
                    break;
                } else if (isWalkable(collActual,ScenePlayerX-i,ScenePlayerY-i) && !InsideSafe(playerPoint.dx(-i).dy(-i))) {
                    choosen = new WorldPoint(baseX+ScenePlayerX-i,baseY+ScenePlayerY-i,client.getPlane());
                    break;
                } else if (isWalkable(collActual,ScenePlayerX+i,ScenePlayerY-i) && !InsideSafe(playerPoint.dx(i).dy(-i))) {
                    choosen = new WorldPoint(baseX+ScenePlayerX+i,baseY+ScenePlayerY-i,client.getPlane());
                    break;
                }
            }
            if (choosen==null) {
                log.info("No se encontro un safetile");
                enAccion=false;
                return;
            }
            log.info("Moviendo hacia un Tile reseteador");
            MousePackets.queueClickPacket();
            MovementPackets.queueMovement(choosen);
            if (choosen != null) {
                log.info("Tile escogido: {}",choosen);
            }
        }
        if (playerPoint.equals(choosen)) {  //llego al tile reseteador
            if (llave == 0) {
                log.info("En el tile reseteador");
                timeout = 4;
                llave = 1;
            } else if (llave==1) {
                llave = 2;
                timeout = 3;
                MousePackets.queueClickPacket();
                MovementPackets.queueMovement(tilePelea);
                log.info("Moviendo hacia el tilePelea");
            }
        }
        if(llave==2){
            if (playerPoint.equals(tilePelea)) {
                log.info("se llego");
                llave=0;
                timeout=2;
                reseteando=false;
                choosen=null;
            }
        }




        /*if (client.getGameState() == GameState.LOGGED_IN && AreaSafe != null) {
            log.info("Yo dentro del SafeArea: {}", InsideSafe());
        }*/
    }

    public boolean InCombat(Player yo){
        return yo.isInteracting() || yo.getAnimation()!=-1 || client.getNpcs().stream().anyMatch(mono-> {
            if (mono.getInteracting()!=null) {
                return mono.getInteracting().equals(yo);
            }
            return false;
        });
    }

    public boolean isWalkable(CollisionData colData,int x,int y){
        return (colData.getFlags()[x][y] & (CollisionDataFlag.BLOCK_MOVEMENT_OBJECT + CollisionDataFlag.BLOCK_MOVEMENT_FLOOR +
                CollisionDataFlag.BLOCK_MOVEMENT_FLOOR_DECORATION)) == 0;
    }

    @Subscribe
    void onGameStateChanged(GameStateChanged event){
        if(event.getGameState() == GameState.LOGGED_IN){
            AreaSafe = npcAggroAreaPlugin.getLinesToDisplay()[client.getPlane()];
        }
    }

}
