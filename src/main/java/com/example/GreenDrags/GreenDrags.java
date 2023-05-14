package com.example.GreenDrags;

import com.example.EthanApiPlugin.*;
import com.example.InteractionApi.InteractionHelper;
import com.example.InteractionApi.InventoryInteraction;
import com.example.Packets.*;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.widgets.Widget;
import net.runelite.client.RuneLite;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.Keybind;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@PluginDescriptor(
        name = "Dragonzovish")
public class GreenDrags extends Plugin {
    @Inject
    private Client client;

    @Inject
    private KeyManager keyManager;

    @Inject
    private ClientThread clientThread;

    private int estado = 0;
    private int timeout = 0;
    private int contador = 0;
    private static boolean prendido = false;
    private TileItem huesos = null;
    private Tile huesosTile = null;
    private List<Tile> itemList = new ArrayList<>();

    void resetear() {
        estado = 0;
        timeout = 0;
        contador = 0;
        huesos = null;
        huesosTile = null;
        prendido = false;
    }

    @Override
    protected void startUp() throws Exception {
        keyManager.registerKeyListener(botonPrendido);
        resetear();
    }

    @Override
    protected void shutDown() throws Exception {
        keyManager.unregisterKeyListener(botonPrendido);
        resetear();
    }

    private final KeyListener botonPrendido = new HotkeyListener(() -> new Keybind(KeyEvent.VK_F9, 0)) {
        @Override
        public void hotkeyPressed() {

            clientThread.invoke(() -> {
                prendido = !prendido;
                if (prendido) {
                    estado = 10;
                    client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag("Encendido", Color.green), "");
                } else {
                    estado = 0;
                    client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", ColorUtil.wrapWithColorTag("Apagado", Color.RED), "");
                    resetear();
                }

            });
        }
    };

    @Subscribe
    void onItemSpawned(ItemSpawned event) {
        if (event.getItem() == null) {
            return;
        }
        if (event.getItem().getId() == 536) {
            itemList.add(event.getTile());
            log.info("Se agrego un Item, Items Size: {}", itemList.size());
            timeout = 3;
        }
    }

    @Subscribe
    void onGameTick(GameTick event) {
        if (!prendido) return;
        if (timeout > 0) {
            timeout--;
            return;
        }

        log.info("estado = {}", estado);
        if (estado == 10) { //matar dragones
            if (Inventory.getEmptySlots() == 0 || Inventory.search().withAction("Eat").empty()) {
                if (client.getLocalPlayer().getWorldLocation().getY() >= 3680) {
                    log.info("Estamos sobre 20 wild");
                    timeout = 5;
                    MousePackets.queueClickPacket();
                    MovementPackets.queueMovement(client.getLocalPlayer().getWorldLocation().getX(), 3678, false);
                    return;
                }
                log.info("Teleportando");
                MousePackets.queueClickPacket();
                Equipment.search().nameContains("Ring").first().ifPresent(xd -> WidgetPackets.queueWidgetAction(xd, "Ferox Enclave"));
                timeout = 5;
                estado = 20;
            }
            if (client.getBoostedSkillLevel(Skill.HITPOINTS) <= 60) {
                log.info("Comiendo");
                Optional<Widget> comida = Inventory.search().withAction("Eat").first();
                comida.ifPresent(com -> InventoryInteraction.useItem(comida.get(), "Eat"));
                timeout = 4;
                return;
            }
            if (!itemList.isEmpty()) {
                log.info("Looteando");
                //log.info("Id:{}    WL:{}",huesos.getId(),huesosTile.getWorldLocation());
                MousePackets.queueClickPacket();
                TileItemPackets.queueTileItemAction(3, 536, itemList.get(itemList.size() - 1).getWorldLocation().getX(), itemList.get(itemList.size() - 1).getWorldLocation().getY(), false);
                itemList.remove(itemList.size() - 1);
                timeout = 5;
                return;
                //todo Lootear
            }
            if (InCombat(client.getLocalPlayer())) {
                contador = 0;
                //todo Comer y weas
                return;
            } else {
                contador++;
                if (contador > 4) {
                    log.info("Al atake!");
                    Optional<NPC> dragonAtakable = NPCs.search().notInteracting().nameContains("Green dragon").nearestToPlayer();
                    MousePackets.queueClickPacket();
                    dragonAtakable.ifPresent(npc -> NPCPackets.queueNPCAction(npc, "Attack"));
                    contador = 0;
                }
            }
        } else if (estado == 20) {
            //todo abrir banco y depositar
            if (!client.getWidget(786442).isHidden()) {  //El bank esta abierto
                log.info("Depositando bones");
                BankInventory.search().nameContains("Dragon").first().ifPresent(Dbone -> {
                    WidgetPackets.queueWidgetAction(Dbone, "Deposit-All");
                });
            } else {
                if (client.getLocalPlayer() == null) return;
                if (client.getLocalPlayer().getIdlePoseAnimation() == client.getLocalPlayer().getPoseAnimation()) { //estoy idle
                    TileObjects.search().withAction("Use").nearestToPlayer().ifPresent(tileObject -> ObjectPackets.queueObjectAction(tileObject, false, "Use"));
                }
            }

            timeout = 5;
//26711  Id Bank chest
            estado = 30;
        } else if (estado == 30) {
            log.info("Volviendo a los dragones suckionadores");
            //todo volver a los Drags
            if (client.getLocalPlayer().getWorldLocation().equals(new WorldPoint(3155, 3635, 0))
                    && isIdle(client.getLocalPlayer())) {
                log.info("estamos en el puente");
                MovementPackets.queueMovement(new WorldPoint(3193, 3644, 0));
                timeout = 15;
            }
            if (estoyEn(new WorldPoint(3193,3644,0))) { //punto 1
                MovementPackets.queueMovement(3230,3650,false);
                timeout = 14;
            } else if ( estoyEn(new WorldPoint(3230,3650,0)) ) {
                MovementPackets.queueMovement(new WorldPoint(3243,3656,0));
                timeout = 14;
            }
            TileObjects.search().withId(39653).first().ifPresent(barrera -> {
                ObjectPackets.queueObjectAction(barrera, false, "Pass-Through");
            });
        }
    }

    public boolean InCombat(Player yo) {
        return yo.isInteracting() || yo.getAnimation() != -1 || client.getNpcs().stream().anyMatch(mono -> {
            if (mono.getInteracting() != null) {
                return mono.getInteracting().equals(yo);
            }
            return false;
        });
    }

    public boolean isIdle(Player gamer) {
        return gamer.getIdlePoseAnimation() == gamer.getPoseAnimation();
    }

    public boolean estoyEn(WorldPoint tile){
        return client.getLocalPlayer().getWorldLocation().equals(tile);
    }
}
