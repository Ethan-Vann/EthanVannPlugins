package com.example.LavaRunecrafter;

import com.example.PacketUtilsPlugin;
import com.example.Packets.MousePackets;
import com.example.Packets.NPCPackets;
import com.example.Packets.ObjectPackets;
import com.example.Packets.WidgetPackets;
import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.SneakyThrows;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.GameObject;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.Varbits;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.StatChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
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
public class LavaRunecrafterPlugin extends Plugin
{
	public int timeout = 0;
	public int bindingCharges = -1;
	@Inject
	Client client;
	@Inject
	PluginManager pluginManager;
	@Inject
	ItemManager itemManager;
	@Inject
	WidgetPackets widgetPackets;
	@Inject
	MousePackets mousePackets;
	@Inject
	NPCPackets npcPackets;
	HashMap<Widget, int[]> pouches = new HashMap<>();
	@Inject
	ObjectPackets objectPackets;
	@Inject
	LavaRunecrafterPluginConfig config;


	@Provides
	public LavaRunecrafterPluginConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(LavaRunecrafterPluginConfig.class);
	}

	@Override
	@SneakyThrows
	public void startUp()
	{
		timeout = 0;
	}

	@Override
	public void shutDown()
	{

	}


	@Subscribe
	protected void onStatChanged(StatChanged event) {
		if (event.getSkill() == Skill.RUNECRAFT)
		{
			if(bindingCharges==-1||bindingCharges==0){
				return;
			}
			bindingCharges--;
		}
	}
	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (timeout > 0)
		{
			timeout--;
			return;
		}
		Player player = client.getLocalPlayer();
		TileObject bankChest = findObject("bank");
		Item binding = null;
		if (bankChest != null)
		{

			if (client.getWidget(WidgetInfo.BANK_CONTAINER) == null)
			{
				try
				{
					binding = client.getItemContainer(InventoryID.EQUIPMENT).getItem(EquipmentInventorySlot.AMULET.getSlotIdx());
				}
				catch (NullPointerException ex)
				{
					//todo
				}
				if (binding != null && binding.getId() == ItemID.BINDING_NECKLACE)
				{
					if (bindingCharges == -1)
					{
						System.out.println("checking binding");
						mousePackets.queueClickPacket();
						widgetPackets.queueWidgetActionPacket(2, 25362449, -1, -1);
						return;
					}
					if(bindingCharges==1){
						System.out.println("breaking binding");
						mousePackets.queueClickPacket();
						widgetPackets.queueWidgetActionPacket(1, 25362449, -1, -1);
						int space = getFirstFreeSlot(WidgetInfo.INVENTORY);
						mousePackets.queueClickPacket();
						widgetPackets.queueWidgetActionPacket(7,9764864,ItemID.BINDING_NECKLACE,space);
						mousePackets.queueClickPacket();
						widgetPackets.queueResumePause(38273024, 1);
						bindingCharges =16;
						return;
					}
				}
				if (isMoving())
				{
					return;
				}
				System.out.println("using bank chest");
				mousePackets.queueClickPacket();
				objectPackets.queueObjectAction(bankChest, false, "Use");
				timeout = 1;
				return;
			}
			System.out.println("doing item operations");
			try
			{
				binding = client.getItemContainer(InventoryID.EQUIPMENT).getItem(EquipmentInventorySlot.AMULET.getSlotIdx());
			}
			catch (NullPointerException ex)
			{
				//todo
			}
			if(binding==null){
				System.out.println("new binding");
				int freeSlot = getFirstFreeSlot(WidgetInfo.INVENTORY);
				mousePackets.queueClickPacket();
				widgetPackets.queueWidgetAction(getItem(ItemID.BINDING_NECKLACE,
						WidgetInfo.BANK_ITEM_CONTAINER), "Withdraw-1");
				mousePackets.queueClickPacket();
				widgetPackets.queueWidgetActionPacket(9, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getPackedId(), ItemID.BINDING_NECKLACE, freeSlot);
				bindingCharges = bindingCharges==-1?bindingCharges:16;
			}
			handleStamina();
			handleDueling();
			handlePouches();
			System.out.println("teleporting");
			mousePackets.queueClickPacket();
			widgetPackets.queueWidgetActionPacket(2, 25362456, -1, -1);
			timeout = 4;
			return;
		}
		TileObject ruins = findObject(34817);
		if (ruins != null)
		{
			if (isMoving())
			{
				System.out.println("moving");
				return;
			}
			System.out.println("entering ruins");
			timeout = 1;
			mousePackets.queueClickPacket();
			objectPackets.queueObjectAction(1, 34817, 3312, 3254, false);
			return;
		}
		TileObject altar = findObject("Altar");
		Widget earthRunes = getItem(ItemID.EARTH_RUNE, WidgetInfo.INVENTORY);
		if (altar != null && earthRunes != null)
		{
			if (isMoving()&&client.getLocalPlayer().getAnimation()!=791)
			{
				return;
			}
			System.out.println(client.getVarbitValue(Varbits.MAGIC_IMBUE));
			if (getEssenceSlots(WidgetInfo.INVENTORY) > 0&&client.getVarbitValue(Varbits.MAGIC_IMBUE)==0)
			{
				System.out.println("using spell");
				mousePackets.queueClickPacket();
				widgetPackets.queueWidgetActionPacket(1, 14286973, -1, -1);
				System.out.println("initial craft");
				mousePackets.queueClickPacket();
				objectPackets.queueWidgetOnTileObject(earthRunes, altar);
				//objectPackets.queueObjectAction(altar, false, "Craft-rune");
				return;
			}
			int essenceInPouches = essenceInPouches();
			if (essenceInPouches > 0)
			{
				System.out.println(client.getTickCount() + ": withdrawing essence");
				handleWithdraw();
				mousePackets.queueClickPacket();
				objectPackets.queueWidgetOnTileObject(earthRunes, altar);
				//objectPackets.queueObjectAction(altar, false, "Craft-rune");
				return;
			}
			mousePackets.queueClickPacket();
			widgetPackets.queueWidgetActionPacket(3, 25362456, -1, -1);
			timeout = 4;
			System.out.println("Teleporting to bank");
		}
	}

	public void handleDueling()
	{
		Widget dueling = getItemFromList(new int[]{ItemID.RING_OF_DUELING1, ItemID.RING_OF_DUELING2,
				ItemID.RING_OF_DUELING3, ItemID.RING_OF_DUELING4, ItemID.RING_OF_DUELING5, ItemID.RING_OF_DUELING6, ItemID.RING_OF_DUELING7, ItemID.RING_OF_DUELING8}, WidgetInfo.BANK_ITEM_CONTAINER);
		int equippedDueling = checkIfWearing(new int[]{ItemID.RING_OF_DUELING1, ItemID.RING_OF_DUELING2,
				ItemID.RING_OF_DUELING3, ItemID.RING_OF_DUELING4, ItemID.RING_OF_DUELING5, ItemID.RING_OF_DUELING6,
				ItemID.RING_OF_DUELING7, ItemID.RING_OF_DUELING8});
		if (config.TeleMethod() == TeleportMethods.RING_OF_DUELING)
		{
			dueling = getItemFromList(new int[]{ItemID.RING_OF_DUELING2, ItemID.RING_OF_DUELING4,
					ItemID.RING_OF_DUELING6, ItemID.RING_OF_DUELING8}, WidgetInfo.BANK_ITEM_CONTAINER);
			if (equippedDueling != -1)
			{
				if (List.of(ItemID.RING_OF_DUELING2, ItemID.RING_OF_DUELING4,
						ItemID.RING_OF_DUELING6, ItemID.RING_OF_DUELING8).contains(equippedDueling))
				{
					return;
				}
				mousePackets.queueClickPacket();
				widgetPackets.queueWidgetActionPacket(2, 786517, -1, -1);
				equippedDueling = -1;
			}
		}
		if (equippedDueling != -1)
		{
			return;
		}
		int freeSlot = getFirstFreeSlot(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
		mousePackets.queueClickPacket();
		widgetPackets.queueWidgetAction(dueling, "Withdraw-1");
		mousePackets.queueClickPacket();
		widgetPackets.queueWidgetActionPacket(9, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getPackedId(), dueling.getItemId(), freeSlot);
	}

	public void handleWithdraw()
	{
		int freeSlots = getEmptySlots(WidgetInfo.INVENTORY);
		for (Widget pouch : pouches.keySet())
		{
			if (pouches.get(pouch)[0] > 0)
			{
				int taken = Math.min(pouches.get(pouch)[0], freeSlots);
				pouches.put(pouch, new int[]{pouches.get(pouch)[0] - taken, pouches.get(pouch)[1]});
				freeSlots -= taken;
				System.out.println("withdrawing: " + taken);
				Widget realPouch = getItem(pouch.getItemId(), WidgetInfo.INVENTORY);
				mousePackets.queueClickPacket();
				widgetPackets.queueWidgetAction(realPouch, "Empty");
			}
			if (freeSlots == 0)
			{
				break;
			}
		}
	}

	public int essenceInPouches()
	{
		int sum = 0;
		for (Widget pouch : pouches.keySet())
		{
			//System.out.println("pouch: " + pouch.getItemId() + "      needs: " + (pouches.get(pouch)[1] - pouches
			// .get(pouch)[0]));
			sum += pouches.get(pouch)[0];
		}
		return sum;
	}

	public int checkIfWearing(int[] ids)
	{
		if (client.getItemContainer(InventoryID.EQUIPMENT) != null)
		{
			Item[] equipment = client.getItemContainer(InventoryID.EQUIPMENT).getItems();
			for (Item item : equipment)
			{
				for (int id : ids)
				{
					if (id == item.getId())
					{
						return item.getId();
					}
				}
			}
		}
		return -1;
	}

	public Widget getItemFromList(int[] list, WidgetInfo container)
	{
		for (int i : list)
		{
			Widget item = getItem(i, container);
			if (item != null)
			{
				return item;
			}
		}
		return null;
	}

	public int getFirstFreeSlot(WidgetInfo container)
	{
		Widget[] items = client.getWidget(container).getDynamicChildren();
		for (int i = 0; i < items.length; i++)
		{
			if (items[i].getItemId() == 6512)
			{
				return i;
			}
		}
		return -1;
	}

	public void handleStamina()
	{
		int freeSlot = getFirstFreeSlot(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
		Widget stamina = getItem(ItemID.STAMINA_POTION1, WidgetInfo.BANK_ITEM_CONTAINER);
		if (client.getEnergy() > 8000 || client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE) == 1)
		{
			System.out.println("didnt need stamina");
			return;
		}
		if (stamina == null || freeSlot == -1)
		{
			try
			{
				pluginManager.stopPlugin(this);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return;
		}
		System.out.println("drinking stam");
		mousePackets.queueClickPacket();
		widgetPackets.queueWidgetAction(stamina, "Withdraw-1");
		mousePackets.queueClickPacket();
		widgetPackets.queueWidgetActionPacket(9, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getPackedId(), ItemID.STAMINA_POTION1, freeSlot);
		if(config.VialSmasher()){
			return;
		}
		mousePackets.queueClickPacket();
		widgetPackets.queueWidgetActionPacket(2, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getPackedId(),
				ItemID.VIAL, freeSlot);
	}

	public void handlePouches()
	{
		pouches = getPouches();
		Widget essence = getItem(ItemID.PURE_ESSENCE, WidgetInfo.BANK_ITEM_CONTAINER);
		essence = essence == null ? getItem(ItemID.RUNE_ESSENCE, WidgetInfo.BANK_ITEM_CONTAINER) : essence;
		int essenceSlots = getEmptySlots(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER) + getEssenceSlots(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
		while (essenceNeeded() > 0)
		{
			//System.out.println("withdrawing all");
			mousePackets.queueClickPacket();
			widgetPackets.queueWidgetAction(essence, "Withdraw-All");
			int essenceLeft = essenceSlots;
			for (Widget pouch : pouches.keySet())
			{
				int[] values = pouches.get(pouch);
				if (values[0] >= values[1])
				{
					continue;
				}
				int transfered = Math.min(values[1] - values[0], essenceLeft);
				essenceLeft -= transfered;
				values[0] += transfered;
				//System.out.println("filling pouch: " + pouch.getItemId() + "      with: " + transfered + "      " +"essence left: " + essenceLeft);
				mousePackets.queueClickPacket();
				widgetPackets.queueWidgetAction(pouch, "Fill");
				pouches.put(pouch, values);
				if (essenceLeft == 0)
				{
					break;
				}
			}
		}
		Widget lavaRunes = getItem(ItemID.LAVA_RUNE, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
		if (lavaRunes != null)
		{
			System.out.println("depositing lava runes");
			mousePackets.queueClickPacket();
			widgetPackets.queueWidgetAction(lavaRunes, "Deposit-All");
		}
		mousePackets.queueClickPacket();
		widgetPackets.queueWidgetAction(essence, "Withdraw-All");
	}

	public int essenceNeeded()
	{
		int essenceNeeded = 0;
		for (Widget pouch : pouches.keySet())
		{
			//System.out.println("pouch: " + pouch.getItemId() + "      needs: " + (pouches.get(pouch)[1] - pouches
			// .get(pouch)[0]));
			int[] values = pouches.get(pouch);
			essenceNeeded += values[1] - values[0];
		}
		return essenceNeeded;
	}

	public HashMap<Widget, int[]> getPouches()
	{
		pouches.clear();
		pouches.put(getItem(ItemID.SMALL_POUCH, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER), new int[]{0, 3});
		pouches.put(getItem(ItemID.MEDIUM_POUCH, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER), new int[]{0, 6});
		pouches.put(getItem(ItemID.LARGE_POUCH, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER), new int[]{0, 9});
		pouches.put(getItem(ItemID.GIANT_POUCH, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER), new int[]{0, 12});
		pouches.put(getItem(ItemID.COLOSSAL_POUCH, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER), new int[]{0, 40});
		pouches.remove(null);
		return pouches;
	}

	public int getEssenceSlots(WidgetInfo widgetInfo)
	{
		List<Widget> inventoryItems = Arrays.asList(client.getWidget(widgetInfo.getId()).getDynamicChildren());
		return (int) inventoryItems.stream().filter(item -> item.getItemId() == ItemID.PURE_ESSENCE || item.getItemId()
				== ItemID.RUNE_ESSENCE).count();
	}

	public int getEmptySlots(WidgetInfo widgetInfo)
	{
		List<Widget> inventoryItems = Arrays.asList(client.getWidget(widgetInfo.getId()).getDynamicChildren());
		return (int) inventoryItems.stream().filter(item -> item.getItemId() == 6512).count();
	}


	public boolean isMoving(){
		return client.getLocalPlayer().getPoseAnimation()
				!= client.getLocalPlayer().getIdlePoseAnimation();
	}
//	@SneakyThrows
//	boolean isMoving(Player player)
//	{
//		Field pathLength = player.getClass().getSuperclass().getDeclaredField(ObfuscatedNames.ACTOR_PATH_LENGTH_FIELD);
//		pathLength.setAccessible(true);
//		int pathLengthVal = pathLength.getInt(player);
//		pathLength.setAccessible(false);
//		return pathLengthVal > 0;
//	}

	TileObject findObject(String objectName)
	{
		for (Tile[][] tile : client.getScene().getTiles())
		{
			for (Tile[] tiles : tile)
			{
				for (Tile tile1 : tiles)
				{
					if (tile1 != null && tile1.getGameObjects() != null && tile1.getGameObjects().length != 0)
					{
						GameObject returnVal =
								Arrays.stream(tile1.getGameObjects()).filter(gameObject -> gameObject != null && client.getObjectDefinition(gameObject.getId()).getName().toLowerCase().contains(objectName.toLowerCase())).findFirst().orElse(null);
						if (returnVal != null)
						{
							return returnVal;
						}
					}
				}
			}
		}
		return null;
	}

	TileObject findObject(int id)
	{
		for (Tile[][] tile : client.getScene().getTiles())
		{
			for (Tile[] tiles : tile)
			{
				for (Tile tile1 : tiles)
				{
					if (tile1 != null && tile1.getGameObjects() != null && tile1.getGameObjects().length != 0)
					{
						GameObject returnVal =
								Arrays.stream(tile1.getGameObjects()).filter(gameObject -> gameObject != null && gameObject.getId() == id).findFirst().orElse(null);
						if (returnVal != null)
						{
							return returnVal;
						}
					}
				}
			}
		}
		return null;
	}

	public Widget getItem(int id, WidgetInfo container)
	{
		Widget[] items = client.getWidget(container).getDynamicChildren();
		for (int i = 0; i < items.length; i++)
		{
			if (items[i].getItemId() == id)
			{
				return items[i];
			}
		}
		return null;
	}

	@Subscribe
	public void onChatMessage(ChatMessage e)
	{
		if (e == null || e.getMessage() == null || e.getType() != ChatMessageType.GAMEMESSAGE)
		{
			return;
		}
		if(e.getMessage().contains("disintegrated"))
		{
			bindingCharges = 16;
		}
		if(e.getMessage().equals("one charge left before your Binding necklace disintegrates.")){
			bindingCharges = 1;
			return;
		}
		if (e.getMessage().contains("left before your Binding necklace disintegrates."))
		{
			bindingCharges = Integer.parseInt(e.getMessage().split("You have ")[1].replace(" charges left before your" +
					" Binding necklace" +
					" disintegrates.", ""));
		}
	}

}
