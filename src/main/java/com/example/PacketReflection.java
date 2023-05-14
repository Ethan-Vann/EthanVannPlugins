package com.example;

import com.example.Packets.BufferMethods;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class PacketReflection {
    public static Class classWithgetPacketBufferNode = null;
    public static Method getPacketBufferNode = null;
    public static Class ClientPacket = null;
    public static Class isaacClass = null;
    public static Class PacketBufferNode = null;
    public static Field PACKETWRITER = null;
    public static Object isaac = null;
    public static Field mouseHandlerLastPressedTime = null;
    public static Field clientMouseLastLastPressedTimeMillis = null;


    @Inject
    Client clientInstance;
    public static Client client = null;

    @SneakyThrows
    public boolean LoadPackets() {
        try {
            client = clientInstance;


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
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Failed to load Packets Into Client");
            return false;
        }
        return true;
    }

    @SneakyThrows
    public static void writeObject(String obfname, Object buffer, Object input) {
        switch (obfname) {
            case "du":
                BufferMethods.du(buffer, (Integer) input);
                break;
            case "bu":
                BufferMethods.bu(buffer, (Integer) input);
                break;
            case "dh":
                BufferMethods.dh(buffer, (Integer) input);
                break;
            case "bf":
                BufferMethods.bf(buffer, (Integer) input);
                break;
            case "dy":
                BufferMethods.dy(buffer, (Integer) input);
                break;
            case "el":
                BufferMethods.el(buffer, (Integer) input);
                break;
            case "dn":
                BufferMethods.dn(buffer, (Integer) input);
                break;
            case "dp":
                BufferMethods.dp(buffer, (Integer) input);
                break;
            case "eb":
                BufferMethods.eb(buffer, (Integer) input);
                break;
            case "ds":
                BufferMethods.ds(buffer, (Integer) input);
                break;
            case "ba":
                BufferMethods.ba(buffer, (Integer) input);
                break;
        }
    }

    @SneakyThrows
    public static void sendPacket(PacketDef def, Object... objects) {
        Object packetBufferNode = null;
        getPacketBufferNode.setAccessible(true);
        long garbageValue = Math.abs(Long.parseLong(ObfuscatedNames.getPacketBufferNodeGarbageValue));
        if (garbageValue < 256) {
            packetBufferNode = getPacketBufferNode.invoke(null, fetchPacketField(def.name).get(ClientPacket),
                    isaac, Byte.parseByte(ObfuscatedNames.getPacketBufferNodeGarbageValue));
        } else if (garbageValue < 32768) {
            packetBufferNode = getPacketBufferNode.invoke(null, fetchPacketField(def.name).get(ClientPacket),
                    isaac, Short.parseShort(ObfuscatedNames.getPacketBufferNodeGarbageValue));
        } else if (garbageValue < Integer.MAX_VALUE) {
            packetBufferNode = getPacketBufferNode.invoke(null, fetchPacketField(def.name).get(ClientPacket),
                    isaac, Integer.parseInt(ObfuscatedNames.getPacketBufferNodeGarbageValue));
        }
        Object buffer = packetBufferNode.getClass().getDeclaredField(ObfuscatedNames.packetBufferFieldName).get(packetBufferNode);
        getPacketBufferNode.setAccessible(false);
        List<String> params = null;
        if (def.type == PacketType.RESUME_PAUSEBUTTON) {
            params = List.of("var0", "var1");
        }
        if (def.type == PacketType.IF_BUTTON) {
            params = List.of("widgetId", "slot", "itemId");
        }
        if (def.type == PacketType.OPLOC) {
            params = List.of("objectId", "worldPointX", "worldPointY", "ctrlDown");
        }
        if (def.type == PacketType.OPNPC) {
            params = List.of("npcIndex", "ctrlDown");
        }
        if (def.type == PacketType.OPPLAYER) {
            params = List.of("playerIndex", "ctrlDown");
        }
        if (def.type == PacketType.OPOBJ) {
            params = List.of("objectId", "worldPointX", "worldPointY", "ctrlDown");
        }
        if (def.type == PacketType.OPOBJT) {
            params = List.of("objectId", "worldPointX", "worldPointY", "slot", "itemId", "widgetId",
                    "ctrlDown");
        }
        if (def.type == PacketType.EVENT_MOUSE_CLICK) {
            params = List.of("mouseInfo", "mouseX", "mouseY");
        }
        if (def.type == PacketType.MOVE_GAMECLICK) {
            params = List.of("worldPointX", "worldPointY", "ctrlDown", "5");
        }
        if (def.type == PacketType.IF_BUTTONT) {
            params = List.of("sourceWidgetId", "sourceSlot", "sourceItemId", "destinationWidgetId",
                    "destinationSlot", "destinationItemId");
        }
        if (def.type == PacketType.OPLOCT) {
            params = List.of("objectId", "worldPointX", "worldPointY", "slot", "itemId", "widgetId",
                    "ctrlDown");
        }
        if (def.type == PacketType.OPPLAYERT) {
            params = List.of("playerIndex", "itemId", "slot", "widgetId", "ctrlDown");
        }
        if (def.type == PacketType.OPNPCT) {
            params = List.of("npcIndex", "itemId", "slot", "widgetId", "ctrlDown");
        }
        if (params != null) {
            for (Map.Entry<String, String> stringEntry : def.fields.entrySet()) {
                if (params.contains(stringEntry.getKey())) {

                    writeObject(stringEntry.getValue(), buffer, objects[params.indexOf(stringEntry.getKey())]);
                }
            }
            PACKETWRITER.setAccessible(true);
            Method addNode = PACKETWRITER.get(null).getClass().getDeclaredMethod(ObfuscatedNames.addNodeMethodName, packetBufferNode.getClass(), int.class);
            addNode.setAccessible(true);
            addNode.invoke(PACKETWRITER.get(null), packetBufferNode, Integer.parseInt(ObfuscatedNames.addNodeGarbageValue));
            addNode.setAccessible(false);
            PACKETWRITER.setAccessible(false);
        }
    }
	//	static Object getPacketBufferNode(Object clientPacket, Object isaacCipher){
	//		//ClientPacket var0
	//		//IsaacCipher var1
	//		Object var3 = method3844();
	//		Field clientPacketField = ObfuscatedNames.packetBufferNodeClientPacketField;
	//		Field isaacCipherField = ObfuscatedNames.packetBufferNodeIsaacCipherField;
	//		Field length = ObfuscatedNames.clientPacketLengthField;
	//		var3.af = var0;
	//		var3.an = -474143459 * var0.dd;
	//		if (-1 == var3.an * -1245059367) {
	//			if (var2 >= 1132) {
	//				throw new IllegalStateException();
	//			}
    //
	//			var3.aw = new sq(260);
	//		} else if (var3.an * -1245059367 == -2) {
	//			var3.aw = new sq(10000);
	//		} else if (var3.an * -1245059367 <= 18) {
	//			var3.aw = new sq(20);
	//		} else if (-1245059367 * var3.an <= 98) {
	//			if (var2 >= 1132) {
	//				throw new IllegalStateException();
	//			}
    //
	//			var3.aw = new sq(100);
	//		} else {
	//			var3.aw = new sq(260);
	//		}
    //
	//		var3.aw.an(var1, -1962868632);
	//		var3.aw.aw(1546470819 * var3.af.dq, (byte)-107);
	//		var3.ac = 0;
	//		return var3;
	//	}
	//	@SneakyThrows
	//	static Object method3844() {
	//		Field nodeCount = PacketBufferNode.getDeclaredField(ObfuscatedNames.packetBufferNodeCountFieldName);
	//		Field nodeArray = null;
	//		for (Field declaredField : PacketBufferNode.getDeclaredFields())
	//		{
	//			if(declaredField.getType().isArray()&&declaredField.getType().getComponentType()==PacketBufferNode)
	//			{
	//				nodeArray = declaredField;
	//			}
	//		}
	//		if(nodeArray==null){
	//			throw new RuntimeException("Could not find nodeArray");
	//		}
	//		nodeCount.setAccessible(true);
	//		nodeArray.setAccessible(true);
	//		if (nodeCount.getInt(null) * ObfuscatedNames.nodeCountMultiplier == 0) {
	//			nodeArray.setAccessible(false);
	//			nodeCount.setAccessible(false);
	//			return PacketBufferNode.newInstance();
	//		} else {
	//			int nodeCountValueMinusOne = (nodeCount.getInt(null) * ObfuscatedNames.nodeCountMultiplier)-1;
	//			int realNodeCodeValueAfterMultiplier = nodeCountValueMinusOne*modInverse(ObfuscatedNames.nodeCountMultiplier);
	//			nodeCount.setInt(null,realNodeCodeValueAfterMultiplier);
	//			Object[] nodeArrayValue = (Object[]) nodeArray.get(null);
	//			nodeArray.setAccessible(false);
	//			nodeCount.setAccessible(false);
	//			return nodeArrayValue[realNodeCodeValueAfterMultiplier];
	//		}
	//	}

    @SneakyThrows
    static Field fetchPacketField(String name) {
        return ClientPacket.getDeclaredField(name);
    }


    private static BigInteger modInverse(BigInteger val) {
        BigInteger shift = BigInteger.ONE.shiftLeft(32);
        return val.modInverse(shift);
    }

    private static int modInverse(int val) {
        return modInverse(BigInteger.valueOf(val)).intValue();
    }
}