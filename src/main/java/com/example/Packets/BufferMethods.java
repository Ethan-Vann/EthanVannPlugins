package com.example.Packets;

import com.example.PacketUtils.ObfuscatedNames;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;

public class BufferMethods {
//    @SneakyThrows
//    public static void dt(Object bufferInstance, int writtenValue) {
//        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
//        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
//        arrayField.setAccessible(true);
//        offsetField.setAccessible(true);
//        byte[] array = (byte[]) arrayField.get(bufferInstance);
//        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) writtenValue;
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (writtenValue >> 8);
//        offsetField.setInt(bufferInstance, offset);
//        arrayField.set(bufferInstance, array);
//        arrayField.setAccessible(false);
//        offsetField.setAccessible(false);
//    }
//
//    @SneakyThrows
//    public static void dd(Object bufferInstance, int writtenValue) {
//        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
//        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
//        arrayField.setAccessible(true);
//        offsetField.setAccessible(true);
//        byte[] array = (byte[]) arrayField.get(bufferInstance);
//        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (writtenValue >> 8);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (128 + writtenValue);
//        offsetField.setInt(bufferInstance, offset);
//        arrayField.set(bufferInstance, array);
//        arrayField.setAccessible(false);
//        offsetField.setAccessible(false);
//    }
//
//    @SneakyThrows
//    public static void bc(Object bufferInstance, int writtenValue) {
//        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
//        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
//        arrayField.setAccessible(true);
//        offsetField.setAccessible(true);
//        byte[] array = (byte[]) arrayField.get(bufferInstance);
//        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) writtenValue;
//        offsetField.setInt(bufferInstance, offset);
//        arrayField.set(bufferInstance, array);
//        arrayField.setAccessible(false);
//        offsetField.setAccessible(false);
//    }
//
//    @SneakyThrows
//    public static void bu(Object bufferInstance, int writtenValue) {
//        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
//        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
//        arrayField.setAccessible(true);
//        offsetField.setAccessible(true);
//        byte[] array = (byte[]) arrayField.get(bufferInstance);
//        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (writtenValue >> 8);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) writtenValue;
//        offsetField.setInt(bufferInstance, offset);
//        arrayField.set(bufferInstance, array);
//        arrayField.setAccessible(false);
//        offsetField.setAccessible(false);
//    }
//
//    @SneakyThrows
//    public static void dy(Object bufferInstance, int writtenValue) {
//        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
//        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
//        arrayField.setAccessible(true);
//        offsetField.setAccessible(true);
//        byte[] array = (byte[]) arrayField.get(bufferInstance);
//        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (writtenValue + 128);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (writtenValue >> 8);
//        offsetField.setInt(bufferInstance, offset);
//        arrayField.set(bufferInstance, array);
//        arrayField.setAccessible(false);
//        offsetField.setAccessible(false);
//    }
//
//    @SneakyThrows
//    public static void ez(Object bufferInstance, int writtenValue) {
//        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
//        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
//        arrayField.setAccessible(true);
//        offsetField.setAccessible(true);
//        byte[] array = (byte[]) arrayField.get(bufferInstance);
//        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (writtenValue >> 8);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) writtenValue;
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (writtenValue >> 24);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (writtenValue >> 16);
//        offsetField.setInt(bufferInstance, offset);
//        arrayField.set(bufferInstance, array);
//        arrayField.setAccessible(false);
//        offsetField.setAccessible(false);
//    }
//
//
//    @SneakyThrows
//    public static void bh(Object bufferInstance, int writtenValue) {
//        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
//        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
//        arrayField.setAccessible(true);
//        offsetField.setAccessible(true);
//        byte[] array = (byte[]) arrayField.get(bufferInstance);
//        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (writtenValue >> 24);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (writtenValue >> 16);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (writtenValue >> 8);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) writtenValue;
//        offsetField.setInt(bufferInstance, offset);
//        arrayField.set(bufferInstance, array);
//        arrayField.setAccessible(false);
//        offsetField.setAccessible(false);
//    }
//
//    @SneakyThrows
//    public static void dl(Object bufferInstance, int writtenValue) {
//        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
//        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
//        arrayField.setAccessible(true);
//        offsetField.setAccessible(true);
//        byte[] array = (byte[]) arrayField.get(bufferInstance);
//        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (128 + writtenValue);
//        offsetField.setInt(bufferInstance, offset);
//        arrayField.set(bufferInstance, array);
//        arrayField.setAccessible(false);
//        offsetField.setAccessible(false);
//    }
//
//
//    @SneakyThrows
//    public static void en(Object bufferInstance, int writtenValue) {
//        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
//        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
//        arrayField.setAccessible(true);
//        offsetField.setAccessible(true);
//        byte[] array = (byte[]) arrayField.get(bufferInstance);
//        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (writtenValue >> 16);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (writtenValue >> 24);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) writtenValue;
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (writtenValue >> 8);
//        offsetField.setInt(bufferInstance, offset);
//        arrayField.set(bufferInstance, array);
//        arrayField.setAccessible(false);
//        offsetField.setAccessible(false);
//    }
//
//    @SneakyThrows
//    public static void don(Object bufferInstance, int writtenValue) {
//        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
//        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
//        arrayField.setAccessible(true);
//        offsetField.setAccessible(true);
//        byte[] array = (byte[]) arrayField.get(bufferInstance);
//        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (128 - writtenValue);
//        offsetField.setInt(bufferInstance, offset);
//        arrayField.set(bufferInstance, array);
//        arrayField.setAccessible(false);
//        offsetField.setAccessible(false);
//    }
//
//
//    @SneakyThrows
//    public static void dp(Object bufferInstance, int writtenValue) {
//        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
//        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
//        arrayField.setAccessible(true);
//        offsetField.setAccessible(true);
//        byte[] array = (byte[]) arrayField.get(bufferInstance);
//        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (0 - writtenValue);
//        offsetField.setInt(bufferInstance, offset);
//        arrayField.set(bufferInstance, array);
//        arrayField.setAccessible(false);
//        offsetField.setAccessible(false);
//    }
//
//    @SneakyThrows
//    public static void er(Object bufferInstance, int writtenValue) {
//        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
//        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
//        arrayField.setAccessible(true);
//        offsetField.setAccessible(true);
//        byte[] array = (byte[]) arrayField.get(bufferInstance);
//        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) writtenValue;
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (writtenValue >> 8);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (writtenValue >> 16);
//        array[(offset += -1456241929) * -2114593081 - 1] = (byte) (writtenValue >> 24);
//        offsetField.setInt(bufferInstance, offset);
//        arrayField.set(bufferInstance, array);
//        arrayField.setAccessible(false);
//        offsetField.setAccessible(false);
//    }

    public static void setOffset(Object bufferInstance, int offset) {
        try {
            Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
            offsetField.setAccessible(true);
            offsetField.setInt(bufferInstance, offset);
            offsetField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static int getOffset(Object bufferInstance) {
        try {
            Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
            offsetField.setAccessible(true);
            int offset = offsetField.getInt(bufferInstance);
            offsetField.setAccessible(false);
            return offset;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void setArray(Object bufferInstance, byte[] array) {
        try {
            Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
            arrayField.setAccessible(true);
            arrayField.set(bufferInstance, array);
            arrayField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static byte[] getArray(Object bufferInstance) {
        try {
            Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
            arrayField.setAccessible(true);
            byte[] array = (byte[]) arrayField.get(bufferInstance);
            arrayField.setAccessible(false);
            return array;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void makeBufferCall(String method, Object bufferInstance, int writtenValue) {
        try {
            Pair<byte[], Integer> buffer = (Pair<byte[], Integer>) BufferMethods.class.getDeclaredMethod(method, byte[].class, int.class, int.class).invoke(null, getArray(bufferInstance), getOffset(bufferInstance), writtenValue);
            setArray(bufferInstance, buffer.getLeft());
            setOffset(bufferInstance, buffer.getRight());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Pair<byte[], Integer> de(byte[] array, int offset, int writtenValue) {
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (128 + writtenValue);
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (writtenValue >> 8);
        return Pair.of(array, offset);
    }

    public static Pair<byte[], Integer> bt(byte[] array, int offset, int writtenValue) {
        array[(offset += -993585503) * 1904364897 - 1] = (byte) writtenValue;
        return Pair.of(array, offset);
    }

    public static Pair<byte[], Integer> bu(byte[] array, int offset, int writtenValue){
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (writtenValue >> 8);
        array[(offset += -993585503) * 1904364897 - 1] = (byte) writtenValue;
        return Pair.of(array, offset);
    }

    public static Pair<byte[], Integer> dz(byte[] array, int offset, int writtenValue) {
        array[(offset += -993585503) * 1904364897 - 1] = (byte) writtenValue;
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (writtenValue >> 8);
        return Pair.of(array, offset);
    }

    public static Pair<byte[], Integer> bh(byte[] array, int offset, int writtenValue) {
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (writtenValue >> 24);
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (writtenValue >> 16);
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (writtenValue >> 8);
        array[(offset += -993585503) * 1904364897 - 1] = (byte) writtenValue;
        return Pair.of(array, offset);
    }

    public static Pair<byte[], Integer> el(byte[] array, int offset, int writtenValue) {
        array[(offset += -993585503) * 1904364897 - 1] = (byte) writtenValue;
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (writtenValue >> 8);
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (writtenValue >> 16);
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (writtenValue >> 24);
        return Pair.of(array, offset);
    }

    public static Pair<byte[], Integer> dl(byte[] array, int offset, int writtenValue) {
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (writtenValue >> 8);
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (128 + writtenValue);
        return Pair.of(array, offset);
    }

    public static Pair<byte[], Integer> dm(byte[] array, int offset, int writtenValue) {
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (0 - writtenValue);
        return Pair.of(array, offset);
    }

    public static Pair<byte[], Integer> ea(byte[] array, int offset, int writtenValue) {
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (writtenValue >> 16);
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (writtenValue >> 24);
        array[(offset += -993585503) * 1904364897 - 1] = (byte) writtenValue;
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (writtenValue >> 8);
        return Pair.of(array, offset);
    }

    public static Pair<byte[], Integer> es(byte[] array, int offset, int writtenValue) {
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (writtenValue >> 8);
        array[(offset += -993585503) * 1904364897 - 1] = (byte) writtenValue;
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (writtenValue >> 24);
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (writtenValue >> 16);
        return Pair.of(array, offset);
    }

    public static Pair<byte[], Integer> dr(byte[] array, int offset, int writtenValue) {
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (128 - writtenValue);
        return Pair.of(array, offset);
    }

    public static Pair<byte[], Integer> db(byte[] array, int offset, int writtenValue) {
        array[(offset += -993585503) * 1904364897 - 1] = (byte) (128 + writtenValue);
        return Pair.of(array, offset);
    }

}