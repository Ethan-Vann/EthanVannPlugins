package com.example.Packets;

import com.example.PacketUtils.ObfuscatedNames;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class BufferMethods {
    //al = array
    //at = offset
    @SneakyThrows
    public static void du(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (128 - writtenValue);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void bu(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) writtenValue;
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void dh(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (writtenValue + 128);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void bf(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (writtenValue >> 8);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) writtenValue;
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void dy(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (writtenValue >> 8);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (writtenValue + 128);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void el(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (writtenValue >> 16);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (writtenValue >> 24);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) writtenValue;
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (writtenValue >> 8);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }


    @SneakyThrows
    public static void dn(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) writtenValue;
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (writtenValue >> 8);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void dp(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (-writtenValue);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }


    @SneakyThrows
    public static void eb(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (writtenValue >> 8);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) writtenValue;
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (writtenValue >> 24);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (writtenValue >> 16);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void ds(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (writtenValue + 128);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (writtenValue >> 8);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }


    @SneakyThrows
    public static void ba(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (writtenValue >> 24);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (writtenValue >> 16);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) (writtenValue >> 8);
        array[(offset += -1516355947) * -1633313603 - 1] = (byte) writtenValue;
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

}
