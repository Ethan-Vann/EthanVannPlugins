package com.example.AgroReset;

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
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.GeneralPath;
import java.time.Instant;
import java.util.Objects;

@Slf4j
@PluginDescriptor(name = "<html><font color=\"#ff6961\">Agro Reset</font></html>",
    tags = {"pajau"}
)
@PluginDependency(NpcAggroAreaPlugin.class)
public class agroReset extends Plugin {

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
    private final Color pint=Color.magenta;
    private int estado=0;

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
        estado=0;
    }

    private static WorldPoint choosen = null;
    public static WorldPoint tilePelea = null;
    private boolean enAccion = false;
    private final KeyListener caminador = new HotkeyListener( ()->new Keybind(KeyEvent.VK_F6,0)){
        @Override
        public void hotkeyPressed() {
            clientThread.invoke( () -> {

                enAccion=!enAccion;
                if (!enAccion) {
                    tilePelea = null;
                    estado=0;
                    log.info("Se apago la wea");
                    client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag("Apagado", pint), "");
                } else {
                    tilePelea=client.getLocalPlayer().getWorldLocation();
                    estado=10;
                    log.info("Se prendio la wea");
                    client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag("Prendido", pint), "");
                }
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
        AreaSafe = npcAggroAreaPlugin.getLinesToDisplay()[client.getPlane()];
        if (AreaSafe == null) {
            return;
        }


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

        if (estado==10) {
            if (!InCombat(jugador)
                    && npcAggroAreaPlugin.getEndTime().isBefore(Instant.now())
                    && playerPoint.equals(tilePelea)) {
                log.info("Se acabo el tiempo de aggro, se procede a resetear");
                estado=20;
                timeout=5;
            }
        } else if (estado == 20) {
            if (Instant.now().isAfter(npcAggroAreaPlugin.getEndTime()) ) {
                choosen=null;
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
                if (choosen == null) {
                    log.info("No se encontro un safetile");
                    enAccion = false;
                    estado = 0;
                    return;
                } else {
                    log.info("Moviendo hacia un Tile reseteador");
                    MousePackets.queueClickPacket();
                    MovementPackets.queueMovement(choosen);
                    estado=30;
                    if (choosen != null) {
                        log.info("Tile escogido: {}",choosen);
                    }
                    return;
                }
            }
        } else if (estado==30) {
            if (playerPoint.equals(choosen)) {  //llego al tile reseteador
                log.info("En el tile reseteador");
                timeout = 4;
                llave = 1;
                estado=40;
            }
        } else if (estado==40) {
            if (playerPoint.equals(tilePelea)) {
                estado=10;
                choosen=null;
                log.info("se llego");
                return;
            }
            timeout = 3;
            MousePackets.queueClickPacket();
            MovementPackets.queueMovement(tilePelea);
            log.info("Moviendo hacia el tilePelea");

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
