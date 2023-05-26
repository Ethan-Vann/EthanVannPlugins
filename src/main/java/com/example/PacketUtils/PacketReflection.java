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
//                    addNode = PACKETWRITER.get(null).getClass().getDeclaredMethod(ObfuscatedNames.addNodeMethodName, packetBufferNode.getClass(), int.class);
//                    addNode.setAccessible(true);
//                    addNode.invoke(PACKETWRITER.get(null), packetBufferNode, Integer.parseInt(ObfuscatedNames.addNodeGarbageValue));
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

    @SneakyThrows
    public static void addNode(Object eqVar0, Object lgVar1) {
        Field eqVar0_anField = eqVar0.getClass().getDeclaredField("an");
        eqVar0_anField.setAccessible(true);
        Object eqVar0_an = eqVar0_anField.get(eqVar0);
        Method eqVar0_an_an = eqVar0_an.getClass().getDeclaredMethod("an", lgVar1.getClass().getSuperclass());
        eqVar0_an_an.setAccessible(true);
        eqVar0_an_an.invoke(eqVar0_an, lgVar1);
        Field lgVar1_acField = lgVar1.getClass().getDeclaredField("ac");
        lgVar1_acField.setAccessible(true);
        Field lgVar1_awField = lgVar1.getClass().getDeclaredField("aw");
        lgVar1_awField.setAccessible(true);
        Object lgVar1_awObject = lgVar1_awField.get(lgVar1);
        Field lgVar1_aw_atField = lgVar1_awObject.getClass().getField("at");
        lgVar1_aw_atField.setAccessible(true);
        lgVar1_acField.set(lgVar1, -1120134497 * lgVar1_aw_atField.getInt(lgVar1_awObject));
        lgVar1_aw_atField.set(lgVar1_awObject, 0);
        Field eqVar0_awField = eqVar0.getClass().getDeclaredField("aw");
        eqVar0_awField.setAccessible(true);
        eqVar0_awField.set(eqVar0, (int) eqVar0_awField.get(eqVar0) + -56666229 * lgVar1_acField.getInt(lgVar1));
        eqVar0_anField.setAccessible(false);
        eqVar0_an_an.setAccessible(false);
        lgVar1_acField.setAccessible(false);
        lgVar1_awField.setAccessible(false);
        lgVar1_aw_atField.setAccessible(false);
        eqVar0_awField.setAccessible(false);
    }

//    @SneakyThrows
//    static Object getPacketBufferNode(Object clientPacket, Object isaacCipher) {
//        //ClientPacket var0
//        //IsaacCipher var1
//        Object packetBufferNode = method3844();
//        Field clientPacketField = PacketBufferNode.getDeclaredField(ObfuscatedNames.packetBufferNodeClientPacketField);
//        Field clientPacketClassLengthField = ClientPacket.getDeclaredField(ObfuscatedNames.clientPacketClassLengthField);
//        Field isaacCipherField = PacketBufferNode.getDeclaredField(ObfuscatedNames.packetBufferNodeIsaacCipherField);
//        Field length = PacketBufferNode.getDeclaredField(ObfuscatedNames.clientPacketLengthField);
//        Field packetBuffer = PacketBufferNode.getDeclaredField(ObfuscatedNames.packetBufferFieldName);
//        Field clientPacketId = ClientPacket.getDeclaredField(ObfuscatedNames.clientPacketIdField);
//        Field index = PacketBufferNode.getDeclaredField(ObfuscatedNames.packetBufferNodeIndexField);
//        clientPacketField.set(packetBufferNode, clientPacket);
//        int temp = length.getInt(clientPacket) * Integer.parseInt(ObfuscatedNames.clientPacketLengthFieldMultiplier);
//        temp = temp * modInverse(Integer.parseInt(ObfuscatedNames.clientPacketClassLengthFieldMultiplier));
//        clientPacketClassLengthField.set(packetBufferNode, temp);
//        if (-1 == clientPacketClassLengthField.getInt(packetBufferNode) * Integer.parseInt(ObfuscatedNames.clientPacketLengthFieldMultiplier)) {
//            Object tempBuffer = client.getClass().getClassLoader().loadClass(ObfuscatedNames.packetBufferClass).getConstructor(int.class).newInstance(260);
//            packetBuffer.set(packetBufferNode, tempBuffer);
//        } else if (clientPacketClassLengthField.getInt(packetBufferNode) * Integer.parseInt(ObfuscatedNames.clientPacketLengthFieldMultiplier) == -2) {
//            Object tempBuffer = client.getClass().getClassLoader().loadClass(ObfuscatedNames.packetBufferClass).getConstructor(int.class).newInstance(10000);
//            packetBuffer.set(packetBufferNode, tempBuffer);
//        } else if (clientPacketClassLengthField.getInt(packetBufferNode) * Integer.parseInt(ObfuscatedNames.clientPacketLengthFieldMultiplier) <= 18) {
//            Object tempBuffer = client.getClass().getClassLoader().loadClass(ObfuscatedNames.packetBufferClass).getConstructor(int.class).newInstance(20);
//            packetBuffer.set(packetBufferNode, tempBuffer);
//        } else if (clientPacketClassLengthField.getInt(packetBufferNode) * Integer.parseInt(ObfuscatedNames.clientPacketLengthFieldMultiplier) <= 98) {
//            Object tempBuffer = client.getClass().getClassLoader().loadClass(ObfuscatedNames.packetBufferClass).getConstructor(int.class).newInstance(100);
//            packetBuffer.set(packetBufferNode, tempBuffer);
//        } else {
//            Object tempBuffer = client.getClass().getClassLoader().loadClass(ObfuscatedNames.packetBufferClass).getConstructor(int.class).newInstance(260);
//            packetBuffer.set(packetBufferNode, tempBuffer);
//        }
//        Object buffer = packetBufferNode.getClass().getDeclaredField(ObfuscatedNames.packetBufferFieldName).get(packetBufferNode);
//        isaacCipherField.set(packetBufferNode, isaacCipher);
//        BufferMethods.writeByteIsaac(buffer, clientPacketId.get(clientPacket));
//        index.setInt(packetBufferNode, 0);
//        return packetBuffer;
//    }
//
//    @SneakyThrows
//    static void addNode(Object packetBufferNode) {
//        Object packetWriter = PACKETWRITER.get(null);
//        Field index = packetBufferNode.getClass().getDeclaredField(ObfuscatedNames.packetBufferNodeIndexField);
//        Field packetBuffer = packetBufferNode.getClass().getDeclaredField(ObfuscatedNames.packetBufferFieldName);
//        Field packetBufferNodes = packetWriter.getClass().getDeclaredField(ObfuscatedNames.packetBufferNodesField);
//        Field offset = packetBuffer.get(packetBufferNode).getClass().getDeclaredField(ObfuscatedNames.packetBufferOffsetField);
//        Field bufferSize = packetWriter.getClass().getDeclaredField(ObfuscatedNames.packetWriterBufferSizeField);
//        addFirst(packetBufferNodes.get(packetWriter), packetBufferNode);
//        index.setInt(packetBufferNode, offset.getInt(packetBuffer.get(packetBufferNode)));
//        offset.setInt(packetBuffer.get(packetBufferNode), 0);
//        bufferSize.setInt(packetBufferNode,(bufferSize.getInt(packetBufferNode)+index.getInt(packetBufferNode)));
//    }
//
//    @SneakyThrows
//    static void addFirst(Object packetBufferNodesQueue, Object packetBufferNode) {
//        if (var1.next != null) {
//            remove(var1);
//        }
//        var1.next = this.sentinel.next;
//        var1.previous = this.sentinel;
//        var1.next.previous = var1;
//        var1.previous.next = var1;
//    }
//
//    public static void remove(Object var1Node) {
//        Field next = var1Node.getClass().getDeclaredField(ObfuscatedNames.packetBufferNodeNextField);
//        Field previous = var1Node.getClass().getDeclaredField(ObfuscatedNames.packetBufferNodePreviousField);
//        Object nextObject= next.get(var1Node);
//        Object previousObject = previous.get(var1Node);
//        if (nextObject != null) {
//            previous.set(nextObject, previous.get(var1Node));
//            next.set(previousObject, nextObject);
//            next.set(var1Node, null);
//            previous.set(var1Node, null);
//        }
//    }
//
//    @SneakyThrows
//    static Object method3844() {
//        Field nodeCount = PacketBufferNode.getDeclaredField(ObfuscatedNames.packetBufferNodeCountFieldName);
//        Field nodeArray = null;
//        for (Field declaredField : PacketBufferNode.getDeclaredFields()) {
//            if (declaredField.getType().isArray() && declaredField.getType().getComponentType() == PacketBufferNode) {
//                nodeArray = declaredField;
//            }
//        }
//        if (nodeArray == null) {
//            throw new RuntimeException("Could not find nodeArray");
//        }
//        nodeCount.setAccessible(true);
//        nodeArray.setAccessible(true);
//        if (nodeCount.getInt(null) * ObfuscatedNames.nodeCountMultiplier == 0) {
//            nodeArray.setAccessible(false);
//            nodeCount.setAccessible(false);
//            return PacketBufferNode.newInstance();
//        } else {
//            int nodeCountValueMinusOne = (nodeCount.getInt(null) * ObfuscatedNames.nodeCountMultiplier) - 1;
//            int realNodeCodeValueAfterMultiplier = nodeCountValueMinusOne * modInverse(ObfuscatedNames.nodeCountMultiplier);
//            nodeCount.setInt(null, realNodeCodeValueAfterMultiplier);
//            Object[] nodeArrayValue = (Object[]) nodeArray.get(null);
//            nodeArray.setAccessible(false);
//            nodeCount.setAccessible(false);
//            return nodeArrayValue[realNodeCodeValueAfterMultiplier];
//        }
//    }

//    public static void doDecompilation() {
//        Runnable myRunnable =
//                new Runnable() {
//                    public void run() {
//                        File f = RuneLite.CACHE_DIR.getAbsoluteFile().toPath().resolve("patched.cache").toFile();
//                        JarFile myJar = null;
//                        try {
//                            myJar = new JarFile(f);
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                        DecompilerSettings settings = new DecompilerSettings();
//                        settings.setTypeLoader(new JarTypeLoader(myJar));
//                        StringWriter output = new StringWriter();
//                        Decompiler.decompile("do", new PlainTextOutput(output), settings);
//                        String[] lines = output.toString().replaceAll("@Named\\(.*\\)", "").split("\n");
//                        List<String> methodLines = new ArrayList<>();
//                        int count = 0;
//                        boolean foundFirst = false;
//                        for (String line : lines) {
//                            if (foundFirst && count == 0) {
//                                break;
//                            }
//                            if (line.contains("static void " + ObfuscatedNames.RESUME_PAUSE_METHOD_NAME)) {
//                                foundFirst = true;
//                            }
//                            if (foundFirst) {
//                                if (line.contains("}")) {
//                                    count--;
//                                    continue;
//                                }
//                                if (line.contains("{")) {
//                                    count++;
//                                    continue;
//                                }
//                                if (line.contains("throw")) {
//                                    continue;
//                                }
//                                methodLines.add(line);
//                            }
//                        }
//                        for (String methodLine : methodLines) {
//                            System.out.println(methodLine);
//                        }
//                    }
//                };
//        Thread thread = new Thread(myRunnable);
//        thread.start();
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