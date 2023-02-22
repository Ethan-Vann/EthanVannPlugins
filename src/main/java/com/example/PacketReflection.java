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
	Client client;
	@Inject
	public static Class classWithgetPacketBufferNode = null;
	public static Method getPacketBufferNode = null;
	public static Class ClientPacket = null;
	public static Class isaacClass = null;
	public static Class PacketBufferNode = null;
	public static Field PACKETWRITER = null;
	public static Object isaac = null;
	public static Field mouseHandlerLastPressedTime = null;
	public static Field clientMouseLastLastPressedTimeMillis = null;

	@SneakyThrows
	public boolean LoadPackets()
	{
		try
		{
			classWithgetPacketBufferNode = client.getClass().getClassLoader().loadClass(ObfuscatedNames.classContainingGetPacketBufferNodeName);
			ClientPacket = client.getClass().getClassLoader().loadClass(ObfuscatedNames.clientPacketClassName);
			PACKETWRITER = client.getClass().getDeclaredField(ObfuscatedNames.packetWriterFieldName);
			PacketBufferNode = client.getClass().getClassLoader().loadClass(ObfuscatedNames.packetBufferNodeClassName);
			PACKETWRITER.setAccessible(true);
			Field isaac2 = PACKETWRITER.get(null).getClass().getDeclaredField(ObfuscatedNames.isaacCipherFieldName);
			isaac2.setAccessible(true);
			isaac = isaac2.get(PACKETWRITER.get(null));
			isaacClass = isaac.getClass();
			getPacketBufferNode = Arrays.stream(classWithgetPacketBufferNode.getDeclaredMethods()).filter(m -> m.getReturnType().equals(PacketBufferNode)).collect(Collectors.toList()).get(0);
			getPacketBufferNode.setAccessible(true);
			mouseHandlerLastPressedTime = client.getClass().getClassLoader().loadClass(ObfuscatedNames.MouseHandler_lastPressedTimeMillisClass).getDeclaredField(ObfuscatedNames.MouseHandler_lastPressedTimeMillisField);
			clientMouseLastLastPressedTimeMillis = client.getClass().getDeclaredField(ObfuscatedNames.clientMouseLastLastPressedTimeMillis);
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
	public void writeObject(String obfname, Object buffer, Object input)
	{
		switch (obfname)
		{
			//valid cases are: "ct","df","dv","au","cg","cz","ah","ak","co","cb"
			case "ct":
				BufferMethods.ct(buffer, (Integer) input);
				break;
			case "df":
				BufferMethods.df(buffer, (Integer) input);
				break;
			case "dv":
				BufferMethods.dv(buffer, (Integer) input);
				break;
			case "au":
				BufferMethods.au(buffer, (Integer) input);
				break;
			case "cg":
				BufferMethods.cg(buffer, (Integer) input);
				break;
			case "cz":
				BufferMethods.cz(buffer, (Integer) input);
				break;
			case "ah":
				BufferMethods.ah(buffer, (Integer) input);
				break;
			case "ak":
				BufferMethods.ak(buffer, (Integer) input);
				break;
			case "co":
				BufferMethods.co(buffer, (Integer) input);
				break;
			case "cb":
				BufferMethods.cb(buffer, (Integer) input);
				break;
		}
	}

	@SneakyThrows
	public void sendPacket(PacketDef def, Object... objects)
	{
		Object packetBufferNode = null;
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
//			long addNodeGarbage = Math.abs(Long.parseLong(ObfuscatedNames.addNodeGarbageValue));
//			if (addNodeGarbage < 256)
//			{
//				Method addNode = PACKETWRITER.get(null).getClass().getMethod(ObfuscatedNames.addNodeMethodName,
//						PacketBufferNode, byte.class);
//				addNode.setAccessible(true);
//				addNode.invoke(PACKETWRITER.get(null), packetBufferNode,
//						Byte.parseByte(ObfuscatedNames.addNodeGarbageValue));
//			}
//			else if (addNodeGarbage < 32768)
//			{
//				Method addNode = PACKETWRITER.get(null).getClass().getMethod(ObfuscatedNames.addNodeMethodName,
//						PacketBufferNode, short.class);
//				addNode.setAccessible(true);
//				addNode.invoke(PACKETWRITER.get(null), packetBufferNode,
//						Short.parseShort(ObfuscatedNames.addNodeGarbageValue));
//			}
//			else if (addNodeGarbage < Integer.MAX_VALUE)
//			{
//				Method addNode = PACKETWRITER.get(null).getClass().getMethod(ObfuscatedNames.addNodeMethodName,
//						PacketBufferNode, int.class);
//				addNode.setAccessible(true);
//				addNode.invoke(PACKETWRITER.get(null), packetBufferNode,
//						Integer.parseInt(ObfuscatedNames.addNodeGarbageValue));
//			}
			Method addNode = PACKETWRITER.get(null).getClass().getDeclaredMethod("je",
					PACKETWRITER.get(null).getClass(), packetBufferNode.getClass(),byte.class);
			addNode.setAccessible(true);
			addNode.invoke(null, PACKETWRITER.get(null), packetBufferNode,
					Byte.parseByte(ObfuscatedNames.addNodeGarbageValue));
		}
	}

	@SneakyThrows
	Field fetchPacketField(String name)
	{
		return ClientPacket.getDeclaredField(name);
	}
}