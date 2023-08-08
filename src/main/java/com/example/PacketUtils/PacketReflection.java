package com.example.PacketUtils;

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
//            //Devious fix for logout issue
//            Field dc = clientInstance.getClass().getDeclaredField("dc");
//            dc.setAccessible(true);
//            dc.set(null,Integer.MAX_VALUE);
//            dc.setAccessible(false);
//            //Devious fix for logout issue
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
            log.warn("Failed to load com.plugins.Packets Into Client");
            return false;
        }
        return true;
    }

    @SneakyThrows
    public static void writeObject(String obfname, Object buffer, Object input) {
        switch (obfname) {
            case "dt": //ef
                BufferMethods.dt(buffer, (Integer) input);
                break;
            case "df":
                BufferMethods.df(buffer, (Integer) input);
                break;
            case "dv":
                BufferMethods.dv(buffer, (Integer) input);
                break;
            case "bu":
                BufferMethods.bu(buffer, (Integer) input);
                break;
            case "eh":
                BufferMethods.eh(buffer, (Integer) input);
                break;
            case "dh":
                BufferMethods.dh(buffer, (Integer) input);
                break;
            case "ej":
                BufferMethods.ej(buffer, (Integer) input);
                break;
            case "bx":
                BufferMethods.bx(buffer, (Integer) input);
                break;
            case "dl":
                BufferMethods.dl(buffer, (Integer) input);
                break;
            case "dm":
                BufferMethods.dm(buffer, (Integer) input);
                break;
            case "bk":
                BufferMethods.bk(buffer, (Integer) input);
                break;
            case "eb":
                BufferMethods.doMethod(buffer, (Integer) input);
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
//            Method addNode = null;
//            try {
//                for (Method declaredMethod : PACKETWRITER.get(null).getClass().getDeclaredMethods()) {
//                    int modifiers = declaredMethod.getModifiers();
//                    if (!Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers) || !declaredMethod.getReturnType().equals(void.class) || declaredMethod.getParameterCount() != 2 ||
//                            !declaredMethod.getParameterTypes()[0].equals(PACKETWRITER.get(null).getClass()) || !declaredMethod.getParameterTypes()[1].equals(packetBufferNode.getClass())) {
//                        continue;
//                    }
//                    addNode = declaredMethod;
//                    addNode.setAccessible(true);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            try {
//                if (addNode == null) {
//                    addNode = PACKETWRITER.get(null).getClass().getDeclaredMethod(com.plugins.ObfuscatedNames.addNodeMethodName, packetBufferNode.getClass(), int.class);
//                    addNode.setAccessible(true);
//                    addNode.invoke(PACKETWRITER.get(null), packetBufferNode, Integer.parseInt(com.plugins.ObfuscatedNames.addNodeGarbageValue));
//                } else {
//                    addNode.invoke(null, PACKETWRITER.get(null), packetBufferNode);
//                }
                addNode(PACKETWRITER.get(null), packetBufferNode);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            addNode.setAccessible(false);
            PACKETWRITER.setAccessible(false);
        }
    }

    public static void addNode(Object packetWriter, Object packetBufferNode) {
        if (PacketUtilsPlugin.usingClientAddNode) {
            try {
                Method addNode = packetWriter.getClass().getDeclaredMethod(ObfuscatedNames.addNodeMethodName, packetBufferNode.getClass(), int.class);
                addNode.setAccessible(true);
                addNode.invoke(packetWriter, packetBufferNode, Integer.parseInt(ObfuscatedNames.addNodeGarbageValue));
                addNode.setAccessible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Method addNode = PacketUtilsPlugin.addNodeMethod;
                addNode.setAccessible(true);
                if (addNode.getParameterCount() == 2) {
                    addNode.invoke(null, packetWriter, packetBufferNode);
                } else {
                    addNode.invoke(null, packetWriter, packetBufferNode, Integer.parseInt(ObfuscatedNames.addNodeGarbageValue));
                }
                addNode.setAccessible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//     public static void addNode(Object eqVar0, Object lmVar1) {
//        try {
//            Field ay = eqVar0.getClass().getDeclaredField("ay");
//            ay.setAccessible(true);
//            Class or = client.getClass().getClassLoader().loadClass("or");
//            Method eo = or.getDeclaredMethod("eo", ay.get(eqVar0).getClass(), lmVar1.getClass().getSuperclass());
//            eo.setAccessible(true);
//            eo.invoke(null, ay.get(eqVar0), lmVar1);
//
//            Field var1ay = lmVar1.getClass().getDeclaredField("ay");
//            Field am = lmVar1.getClass().getDeclaredField("am");
//            Field arField = lmVar1.getClass().getDeclaredField("ar");
//            arField.setAccessible(true);
//            Object arObject = arField.get(lmVar1);
//            Field avField = arObject.getClass().getField("av");
//            am.setAccessible(true);
//            avField.setAccessible(true);
//            int amValue = -1643463139 * avField.getInt(arObject);
//            var1ay.setInt(lmVar1, amValue);
//            avField.setInt(arObject, 0);
//
//            Field var0ar = eqVar0.getClass().getDeclaredField("ar");
//            var0ar.setAccessible(true);
//            var1ay.setAccessible(true);
//            Field ap = eqVar0.getClass().getDeclaredField("ap");
//            ap.setAccessible(true);
//            int var0arValue = var0ar.getInt(eqVar0);
//            int x = 1559877663 * var1ay.getInt(lmVar1);
//            int totalAzValue = var0arValue + x;
//            var0ar.setInt(eqVar0, totalAzValue);
//            ap.setAccessible(false);
//            var0ar.setAccessible(false);
//            var1ay.setAccessible(false);
//            am.setAccessible(false);
//            avField.setAccessible(false);
//            arField.setAccessible(false);
//            ay.setAccessible(false);
//            eo.setAccessible(false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
