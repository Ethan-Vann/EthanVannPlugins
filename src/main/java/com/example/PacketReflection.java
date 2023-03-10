package com.example;

import com.example.Packets.BufferMethods;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class PacketReflection
{
	@Inject
	Client clientInstance;
	public static Class classWithgetPacketBufferNode = null;
	public static Method getPacketBufferNode = null;
	public static Class ClientPacket = null;
	public static Class isaacClass = null;
	public static Class PacketBufferNode = null;
	public static Field PACKETWRITER = null;
	public static Object isaac = null;
	public static Field mouseHandlerLastPressedTime = null;
	public static Field clientMouseLastLastPressedTimeMillis = null;
	public static Client client = null;
	@SneakyThrows
	public boolean LoadPackets()
	{
		try
		{
			classWithgetPacketBufferNode = clientInstance.getClass().getClassLoader().loadClass(ObfuscatedNames.classContainingGetPacketBufferNodeName);
			ClientPacket = clientInstance.getClass().getClassLoader().loadClass(ObfuscatedNames.clientPacketClassName);
			PACKETWRITER = clientInstance.getClass().getDeclaredField(ObfuscatedNames.packetWriterFieldName);
			PacketBufferNode = clientInstance.getClass().getClassLoader().loadClass(ObfuscatedNames.packetBufferNodeClassName);
			PACKETWRITER.setAccessible(true);
			Field isaac2 = PACKETWRITER.get(null).getClass().getDeclaredField(ObfuscatedNames.isaacCipherFieldName);
			isaac2.setAccessible(true);
			isaac = isaac2.get(PACKETWRITER.get(null));
			isaac2.setAccessible(false);
			PACKETWRITER.setAccessible(false);
			isaacClass = isaac.getClass();
			getPacketBufferNode = Arrays.stream(classWithgetPacketBufferNode.getDeclaredMethods()).filter(m -> m.getReturnType().equals(PacketBufferNode)).collect(Collectors.toList()).get(0);
			mouseHandlerLastPressedTime = clientInstance.getClass().getClassLoader().loadClass(ObfuscatedNames.MouseHandler_lastPressedTimeMillisClass).getDeclaredField(ObfuscatedNames.MouseHandler_lastPressedTimeMillisField);
			clientMouseLastLastPressedTimeMillis = clientInstance.getClass().getDeclaredField(ObfuscatedNames.clientMouseLastLastPressedTimeMillis);
			client = clientInstance;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.warn("Failed to load Packets Into Client");
			return false;
		}
		log.warn("Successfully loaded Packets Into Client");
		return true;
	}

	@SneakyThrows
	public static void writeObject(String obfname, Object buffer, Object input)
	{
		switch (obfname)
		{
			case "dd":
				BufferMethods.dd(buffer, (Integer) input);
				break;
			case "bt":
				BufferMethods.bt(buffer, (Integer) input);
				break;
			case "dv":
				BufferMethods.dv(buffer, (Integer) input);
				break;
			case "bd":
				BufferMethods.bd(buffer, (Integer) input);
				break;
			case "be":
				BufferMethods.be(buffer, (Integer) input);
				break;
			case "dh":
				BufferMethods.dh(buffer, (Integer) input);
				break;
			case "dz":
				BufferMethods.dz(buffer, (Integer) input);
				break;
			case "dl":
				BufferMethods.dl(buffer, (Integer) input);
				break;
			case "dm":
				BufferMethods.dm(buffer, (Integer) input);
				break;
			case "ep":
				BufferMethods.ep(buffer, (Integer) input);
				break;
			case "eb":
				BufferMethods.eb(buffer, (Integer) input);
				break;
			case "er":
				BufferMethods.er(buffer, (Integer) input);
				break;
		}
	}

	@SneakyThrows
	public static void sendPacket(PacketDef def, Object... objects)
	{
		Object packetBufferNode = null;
		getPacketBufferNode.setAccessible(true);
		long garbageValue = Math.abs(Long.parseLong(ObfuscatedNames.getPacketBufferNodeGarbageValue));
		if (garbageValue < 256)
		{
			packetBufferNode = getPacketBufferNode.invoke(null, fetchPacketField(def.name).get(ClientPacket),
					isaac, Byte.parseByte(ObfuscatedNames.getPacketBufferNodeGarbageValue));
		}
		else if (garbageValue < 32768)
		{
			packetBufferNode = getPacketBufferNode.invoke(null, fetchPacketField(def.name).get(ClientPacket),
					isaac, Short.parseShort(ObfuscatedNames.getPacketBufferNodeGarbageValue));
		}
		else if (garbageValue < Integer.MAX_VALUE)
		{
			packetBufferNode = getPacketBufferNode.invoke(null, fetchPacketField(def.name).get(ClientPacket),
					isaac, Integer.parseInt(ObfuscatedNames.getPacketBufferNodeGarbageValue));
		}
		Object buffer = packetBufferNode.getClass().getDeclaredField(ObfuscatedNames.packetBufferFieldName).get(packetBufferNode);
		getPacketBufferNode.setAccessible(false);
		List<String> params = null;
		if (def.type == PacketType.RESUME_PAUSEBUTTON)
		{
			params = List.of("var0", "var1");
		}
		if (def.type == PacketType.IF_BUTTON)
		{
			params = List.of("widgetId", "slot", "itemId");
		}
		if (def.type == PacketType.OPLOC)
		{
			params = List.of("objectId", "worldPointX", "worldPointY", "ctrlDown");
		}
		if (def.type == PacketType.OPNPC)
		{
			params = List.of("npcIndex", "ctrlDown");
		}
		if (def.type == PacketType.OPPLAYER)
		{
			params = List.of("playerIndex", "ctrlDown");
		}
		if (def.type == PacketType.OPOBJ)
		{
			params = List.of("objectId", "worldPointX", "worldPointY", "ctrlDown");
		}
		if (def.type == PacketType.OPOBJT)
		{
			params = List.of("objectId", "worldPointX", "worldPointY", "slot", "itemId", "widgetId",
					"ctrlDown");
		}
		if (def.type == PacketType.EVENT_MOUSE_CLICK)
		{
			params = List.of("mouseInfo", "mouseX", "mouseY");
		}
		if (def.type == PacketType.MOVE_GAMECLICK)
		{
			params = List.of("worldPointX", "worldPointY", "ctrlDown", "5");
		}
		if (def.type == PacketType.IF_BUTTONT)
		{
			params = List.of("sourceWidgetId", "sourceSlot", "sourceItemId", "destinationWidgetId",
					"destinationSlot", "destinationItemId");
		}
		if (def.type == PacketType.OPLOCT)
		{
			params = List.of("objectId", "worldPointX", "worldPointY", "slot", "itemId", "widgetId",
					"ctrlDown");
		}
		if (def.type == PacketType.OPPLAYERT)
		{
			params = List.of("playerIndex", "itemId", "slot", "widgetId", "ctrlDown");
		}
		if (def.type == PacketType.OPNPCT)
		{
			params = List.of("npcIndex", "itemId", "slot", "widgetId", "ctrlDown");
		}
		if (params != null)
		{
			for (Map.Entry<String, String> stringEntry : def.fields.entrySet())
			{
				if (params.contains(stringEntry.getKey()))
				{

					writeObject(stringEntry.getValue(), buffer, objects[params.indexOf(stringEntry.getKey())]);
				}
			}
			PACKETWRITER.setAccessible(true);
			Method addNode = PACKETWRITER.get(null).getClass().getDeclaredMethod(ObfuscatedNames.addNodeMethodName,packetBufferNode.getClass(),int.class);
			addNode.setAccessible(true);
			addNode.invoke(PACKETWRITER.get(null), packetBufferNode,
					Integer.parseInt(ObfuscatedNames.addNodeGarbageValue));
			addNode.setAccessible(false);
			PACKETWRITER.setAccessible(false);
		}
	}

	@SneakyThrows
	static Field fetchPacketField(String name)
	{
		return ClientPacket.getDeclaredField(name);
	}
}