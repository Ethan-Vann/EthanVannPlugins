package com.example.LavaRunecrafter;

import com.example.EthanApiPlugin.Collections.Widgets;
import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.PacketUtils.PacketUtilsPlugin;
import com.example.PacketUtils.WidgetInfoExtended;
import com.example.Packets.MousePackets;
import com.example.Packets.ObjectPackets;
import com.example.Packets.WidgetPackets;
import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.SneakyThrows;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@PluginDescriptor(
        name = "Lava Runecrafter",
        description = "",
        enabledByDefault = false,
        tags = {"ethan"}
)
@PluginDependency(PacketUtilsPlugin.class)
@PluginDependency(EthanApiPlugin.class)
public class LavaRunecrafterPlugin extends Plugin {
    public int timeout = 0;
    public int bindingCharges = -1;
    @Inject
    Client client;
    @Inject
    PluginManager pluginManager;
    Boolean hadbook = null;
    HashMap<Widget, int[]> pouches = new HashMap<>();
    @Inject
    LavaRunecrafterPluginConfig config;
    @Inject
    EthanApiPlugin api;


    @Provides
    public LavaRunecrafterPluginConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(LavaRunecrafterPluginConfig.class);
    }

    @Override
    @SneakyThrows
    public void startUp() {
        timeout = 0;
        pouches = new HashMap<>();
    }

    @Override
    public void shutDown() {
        timeout = 0;
        pouches = new HashMap<>();
    }


    @Subscribe
    protected void onStatChanged(StatChanged event) {
        if (event.getSkill() == Skill.RUNECRAFT) {
            if (bindingCharges == -1 || bindingCharges == 0) {
                return;
            }
            bindingCharges--;
        }
    }

    @Subscribe
    @SneakyThrows
    public void onGameTick(GameTick event) {
        if (hadbook != null) {
            if (!Widgets.search().withTextContains("What do you want?").hiddenState(false).empty() || !Widgets.search().withTextContains("Can you repair").hiddenState(false).empty()) {
                MousePackets.queueClickPacket();
                WidgetPackets.queueResumePause(15138821, -1);
                MousePackets.queueClickPacket();
                WidgetPackets.queueResumePause(14352385, hadbook ? 1 : 2);
                MousePackets.queueClickPacket();
                WidgetPackets.queueResumePause(14221317, -1);
                MousePackets.queueClickPacket();
                EthanApiPlugin.invoke(-1, -1, 26, -1, -1, "", "", -1, -1);
                timeout = 0;
                return;
            }
        }
        if (timeout > 0) {
            timeout--;
            return;
        }
        if (client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        if (pouchesDegraded() && EthanApiPlugin.isMoving()) {
            //System.out.println("contacting old fuck");
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetActionPacket(2, WidgetInfoExtended.SPELL_NPC_CONTACT.getPackedId(),
                    -1, -1);
            timeout = 15;
            return;
        }
        TileObject bankChest = api.findObject("bank");
        Item binding = null;
        if (bankChest != null) {
            if (client.getWidget(WidgetInfo.BANK_CONTAINER) == null) {
                try {
                    binding = client.getItemContainer(InventoryID.EQUIPMENT).getItem(EquipmentInventorySlot.AMULET.getSlotIdx());
                } catch (NullPointerException ex) {
                    //todo
                }
                if (binding != null && binding.getId() == ItemID.BINDING_NECKLACE) {
                    if (bindingCharges == -1) {
                        //System.out.println("checking binding");
                        MousePackets.queueClickPacket();
                        WidgetPackets.queueWidgetActionPacket(2, 25362449, -1, -1);
                        return;
                    }
                    if (bindingCharges == 1) {
                        //System.out.println("breaking binding");
                        MousePackets.queueClickPacket();
                        WidgetPackets.queueWidgetActionPacket(1, 25362449, -1, -1);
                        int space = api.getFirstFreeSlot(WidgetInfo.INVENTORY);
                        MousePackets.queueClickPacket();
                        WidgetPackets.queueWidgetActionPacket(7, 9764864, ItemID.BINDING_NECKLACE, space);
                        MousePackets.queueClickPacket();
                        WidgetPackets.queueResumePause(38273024, 1);
                        bindingCharges = 16;
                        return;
                    }
                }
                if (EthanApiPlugin.isMoving()) {
                    return;
                }
                //System.out.println("using bank chest");
                MousePackets.queueClickPacket();
                ObjectPackets.queueObjectAction(bankChest, false, "Use");
                timeout = 1;
                return;
            }
            //System.out.println("doing item operations");
            try {
                binding = client.getItemContainer(InventoryID.EQUIPMENT).getItem(EquipmentInventorySlot.AMULET.getSlotIdx());
            } catch (NullPointerException ex) {
                //todo
            }
            if (binding == null) {
                //System.out.println("new binding");
                int freeSlot = api.getFirstFreeSlot(WidgetInfo.INVENTORY);
                Widget bindingNeck = EthanApiPlugin.getItem(ItemID.BINDING_NECKLACE, WidgetInfo.BANK_ITEM_CONTAINER);
                if (bindingNeck == null) {
                    EthanApiPlugin.stopPlugin(this);
                    client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Out of binding necklaces",
                            "LavaRunecrafterPlugin");
                }
                MousePackets.queueClickPacket();
                WidgetPackets.queueWidgetAction(bindingNeck, "Withdraw-1");
                MousePackets.queueClickPacket();
                WidgetPackets.queueWidgetActionPacket(9, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getPackedId(), ItemID.BINDING_NECKLACE, freeSlot);
                bindingCharges = bindingCharges == -1 ? bindingCharges : 16;
            }
            if (EthanApiPlugin.getItem(ItemID.EARTH_RUNE, WidgetInfo.INVENTORY) == null) {
                EthanApiPlugin.stopPlugin(this);
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Out of earth runes", "LavaRunecrafterPlugin");
            }
            handleStamina();
            handleDueling();
            handlePouches();
            if (!pluginManager.isPluginEnabled(this)) {
                return;
            }
            //System.out.println("teleporting");
            switch (config.TeleMethod()) {
                case RING_OF_ELEMENTS:
                    Widget ring = EthanApiPlugin.getItem(ItemID.RING_OF_THE_ELEMENTS_26818, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
                    if (ring == null) {
                        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "ring of the elements tele enabled but" +
                                        " ring not found",
                                "LavaRunecrafterPlugin");
                        EthanApiPlugin.stopPlugin(this);
                        return;
                    }
                    //System.out.println("ring teleport");
                    MousePackets.queueClickPacket();
                    WidgetPackets.queueWidgetActionPacket(6, 9764864, ItemID.RING_OF_THE_ELEMENTS_26818, ring.getIndex());
                    break;
                case RING_OF_DUELING:
                    MousePackets.queueClickPacket();
                    WidgetPackets.queueWidgetActionPacket(2, 25362456, -1, -1);
            }
            timeout = 5;
            return;
        }
        TileObject ruins = api.findObject(34817);
        if (ruins != null) {
            if (EthanApiPlugin.isMoving()) {
                return;
            }
            //System.out.println("entering ruins");
            timeout = 1;
            MousePackets.queueClickPacket();
            ObjectPackets.queueObjectAction(1, 34817, 3312, 3254, false);
            return;
        }
        TileObject altar = api.findObject("Altar");
        Widget earthRunes = EthanApiPlugin.getItem(ItemID.EARTH_RUNE, WidgetInfo.INVENTORY);
        if (altar != null && earthRunes != null) {
            if (EthanApiPlugin.isMoving() && client.getLocalPlayer().getAnimation() != 791) {
                return;
            }
            //System.out.println(client.getVarbitValue(Varbits.MAGIC_IMBUE));
            if (getEssenceSlots(WidgetInfo.INVENTORY) > 0 && client.getVarbitValue(Varbits.MAGIC_IMBUE) == 0) {
                //System.out.println("using spell");
                MousePackets.queueClickPacket();
                WidgetPackets.queueWidgetActionPacket(1, 14286973, -1, -1);
                //System.out.println("initial craft");
                MousePackets.queueClickPacket();
                ObjectPackets.queueWidgetOnTileObject(earthRunes, altar);
                //objectPackets.queueObjectAction(altar, false, "Craft-rune");
                return;
            }
            int essenceInPouches = essenceInPouches();
            if (essenceInPouches > 0) {
                if (api.getFirstFreeSlot(WidgetInfo.INVENTORY) != -1) {
                    //System.out.println(client.getTickCount() + ": withdrawing essence");
                    handleWithdraw();
                    MousePackets.queueClickPacket();
                    ObjectPackets.queueWidgetOnTileObject(earthRunes, altar);
                    //objectPackets.queueObjectAction(altar, false, "Craft-rune");
                    return;
                } else {
                    //System.out.println("weird shit");
                }
            }
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetActionPacket(3, 25362456, -1, -1);
            timeout = 3;
            //System.out.println("Teleporting to bank");
        }
    }

    public boolean pouchesDegraded() {
        return api.getItemFromList(new int[]{ItemID.MEDIUM_POUCH_5511, ItemID.LARGE_POUCH_5513, ItemID.GIANT_POUCH_5515,
                ItemID.COLOSSAL_POUCH_26786}, WidgetInfo.INVENTORY) != null;
    }

    public void handleDueling() {
        Widget dueling = api.getItemFromList(new int[]{ItemID.RING_OF_DUELING1, ItemID.RING_OF_DUELING2,
                ItemID.RING_OF_DUELING3, ItemID.RING_OF_DUELING4, ItemID.RING_OF_DUELING5, ItemID.RING_OF_DUELING6, ItemID.RING_OF_DUELING7, ItemID.RING_OF_DUELING8}, WidgetInfo.BANK_ITEM_CONTAINER);
        int equippedDueling = api.checkIfWearing(new int[]{ItemID.RING_OF_DUELING1, ItemID.RING_OF_DUELING2,
                ItemID.RING_OF_DUELING3, ItemID.RING_OF_DUELING4, ItemID.RING_OF_DUELING5, ItemID.RING_OF_DUELING6,
                ItemID.RING_OF_DUELING7, ItemID.RING_OF_DUELING8});
        if (config.TeleMethod() == TeleportMethods.RING_OF_DUELING) {
            dueling = api.getItemFromList(new int[]{ItemID.RING_OF_DUELING2, ItemID.RING_OF_DUELING4,
                    ItemID.RING_OF_DUELING6, ItemID.RING_OF_DUELING8}, WidgetInfo.BANK_ITEM_CONTAINER);
            if (equippedDueling != -1) {
                if (List.of(ItemID.RING_OF_DUELING2, ItemID.RING_OF_DUELING4,
                        ItemID.RING_OF_DUELING6, ItemID.RING_OF_DUELING8).contains(equippedDueling)) {
                    return;
                }
                MousePackets.queueClickPacket();
                WidgetPackets.queueWidgetActionPacket(2, 786517, -1, -1);
                equippedDueling = -1;
            }
        }
        if (equippedDueling != -1) {
            return;
        }
        int freeSlot = api.getFirstFreeSlot(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
        if (dueling == null) {
            EthanApiPlugin.stopPlugin(this);
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Out of dueling rings", "LavaRunecrafterPlugin");
        }
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetAction(dueling, "Withdraw-1");
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(9, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getPackedId(), dueling.getItemId(), freeSlot);
    }

    public void handleWithdraw() {
        int freeSlots = api.getEmptySlots(WidgetInfo.INVENTORY);
        for (Widget pouch : pouches.keySet()) {
            if (pouches.get(pouch)[0] > 0) {
                int taken = Math.min(pouches.get(pouch)[0], freeSlots);
                pouches.put(pouch, new int[]{pouches.get(pouch)[0] - taken, pouches.get(pouch)[1]});
                freeSlots -= taken;
                //System.out.println("withdrawing: " + taken);
                Widget realPouch = EthanApiPlugin.getItem(pouch.getItemId(), WidgetInfo.INVENTORY);
                if (realPouch == null) {
                    pouches.put(pouch, new int[]{0, pouches.get(pouch)[1]});
                    continue;
                }
                MousePackets.queueClickPacket();
                WidgetPackets.queueWidgetAction(realPouch, "Empty");
            }
            if (freeSlots == 0) {
                break;
            }
        }
    }

    public int essenceInPouches() {
        int sum = 0;
        for (Widget pouch : pouches.keySet()) {
            ////System.out.println("pouch: " + pouch.api.getItemId() + "      needs: " + (pouches.get(pouch)[1] - pouches
            // .get(pouch)[0]));
            sum += pouches.get(pouch)[0];
        }
        return sum;
    }


    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned e) {
        if (e.getGameObject().getId() == 34817) {
            //System.out.println("setting timeout 0");
            timeout = 0;
        }
    }

    public void handleStamina() {
        int freeSlot = api.getFirstFreeSlot(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
        Widget stamina = EthanApiPlugin.getItem(ItemID.STAMINA_POTION1, WidgetInfo.BANK_ITEM_CONTAINER);
        if (client.getEnergy() > 8000 || client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE) == 1) {
            //System.out.println("didnt need stamina");
            return;
        }
        if (stamina == null || freeSlot == -1) {
            EthanApiPlugin.stopPlugin(this);
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Out of stamina potions", "LavaRunecrafterPlugin");
        }
        //System.out.println("drinking stam");
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetAction(stamina, "Withdraw-1");
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(9, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getPackedId(), ItemID.STAMINA_POTION1, freeSlot);
        if (config.VialSmasher()) {
            return;
        }
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(2, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getPackedId(),
                ItemID.VIAL, freeSlot);
    }

    public void handlePouches() {
        pouches = getPouches();
        Widget essence = EthanApiPlugin.getItem(ItemID.PURE_ESSENCE, WidgetInfo.BANK_ITEM_CONTAINER);
        essence = essence == null ? EthanApiPlugin.getItem(ItemID.RUNE_ESSENCE, WidgetInfo.BANK_ITEM_CONTAINER) : essence;
        if (essence == null) {
            EthanApiPlugin.stopPlugin(this);
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Out of essence", "LavaRunecrafterPlugin");
        }
        int essenceSlots =
                api.getEmptySlots(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER) + getEssenceSlots(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
        while (essenceNeeded() > 0) {
            ////System.out.println("withdrawing all");
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(essence, "Withdraw-All");
            int essenceLeft = essenceSlots;
            for (Widget pouch : pouches.keySet()) {
                int[] values = pouches.get(pouch);
                if (values[0] >= values[1]) {
                    continue;
                }
                int transfered = Math.min(values[1] - values[0], essenceLeft);
                essenceLeft -= transfered;
                values[0] += transfered;
                ////System.out.println("filling pouch: " + pouch.api.getItemId() + "      with: " + transfered + "      " +"essence left: " + essenceLeft);
                if (EthanApiPlugin.getItem(getAlternative(pouch), WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER) != null) {
                    MousePackets.queueClickPacket();
                    WidgetPackets.queueWidgetAction(EthanApiPlugin.getItem(getAlternative(pouch), WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER), "Fill");
                } else {
                    MousePackets.queueClickPacket();
                    WidgetPackets.queueWidgetAction(pouch, "Fill");
                }
                pouches.put(pouch, values);
                if (essenceLeft == 0) {
                    break;
                }
            }
        }
        Widget lavaRunes = EthanApiPlugin.getItem(ItemID.LAVA_RUNE, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
        if (lavaRunes != null) {
            //System.out.println("depositing lava runes");
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(lavaRunes, "Deposit-All");
        }
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetAction(essence, "Withdraw-All");
    }

    public int getAlternative(Widget pouch) {
        int alternative = -1;
        switch (pouch.getItemId()) {
            case ItemID.MEDIUM_POUCH:
                alternative = ItemID.MEDIUM_POUCH_5511;
                break;
            case ItemID.LARGE_POUCH:
                alternative = ItemID.LARGE_POUCH_5513;
                break;
            case ItemID.GIANT_POUCH:
                alternative = ItemID.GIANT_POUCH_5515;
                break;
            case ItemID.COLOSSAL_POUCH:
                alternative = ItemID.COLOSSAL_POUCH_26786;
                break;
        }
        return alternative;
    }

    public int essenceNeeded() {
        int essenceNeeded = 0;
        for (Widget pouch : pouches.keySet()) {
            ////System.out.println("pouch: " + pouch.api.getItemId() + "      needs: " + (pouches.get(pouch)[1] - pouches
            // .get(pouch)[0]));
            int[] values = pouches.get(pouch);
            essenceNeeded += values[1] - values[0];
        }
        return essenceNeeded;
    }

    public HashMap<Widget, int[]> getPouches() {
        pouches.clear();
        pouches.put(EthanApiPlugin.getItem(ItemID.SMALL_POUCH, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER), new int[]{0, 3});
        pouches.put(EthanApiPlugin.getItem(ItemID.MEDIUM_POUCH, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER), new int[]{0, 6});
        pouches.put(EthanApiPlugin.getItem(ItemID.LARGE_POUCH, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER), new int[]{0, 9});
        pouches.put(EthanApiPlugin.getItem(ItemID.GIANT_POUCH, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER), new int[]{0, 12});
        pouches.put(EthanApiPlugin.getItem(ItemID.COLOSSAL_POUCH, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER), new int[]{0, 40});
        pouches.remove(null);
        return pouches;
    }

    public int getEssenceSlots(WidgetInfo widgetInfo) {
        List<Widget> inventoryItems = Arrays.asList(client.getWidget(widgetInfo.getId()).getDynamicChildren());
        return (int) inventoryItems.stream().filter(item -> item.getItemId() == ItemID.PURE_ESSENCE || item.getItemId()
                == ItemID.RUNE_ESSENCE).count();
    }


    @Subscribe
    public void onChatMessage(ChatMessage e) {
        if (e == null || e.getMessage() == null || e.getType() != ChatMessageType.GAMEMESSAGE) {
            return;
        }
        if (e.getMessage().contains("disintegrated")) {
            bindingCharges = 16;
        }
        if (e.getMessage().equals("one charge left before your Binding necklace disintegrates.")) {
            bindingCharges = 1;
            return;
        }
        if (e.getMessage().contains("left before your Binding necklace disintegrates.")) {
            bindingCharges = Integer.parseInt(e.getMessage().split("You have ")[1].replace(" charges left before your" +
                    " Binding necklace" +
                    " disintegrates.", ""));
        }
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged e) {
        if (e.getContainerId() == InventoryID.BANK.getId()) {
            if (e.getItemContainer() != null) {
                hadbook = e.getItemContainer().contains(ItemID.ABYSSAL_BOOK);
            }
        }
    }
}
