package com.example.Packets;

import com.example.PacketUtils.ObfuscatedNames;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class BufferMethods {
    //al = array
    //at = offset
    @SneakyThrows
    public static void dt(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)writtenValue;
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(writtenValue >> 8);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void dd(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(writtenValue >> 8);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(128 + writtenValue);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void bc(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)writtenValue;
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
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(writtenValue >> 8);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)writtenValue;
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
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(writtenValue + 128);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(writtenValue >> 8);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void ez(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(writtenValue >> 8);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)writtenValue;
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(writtenValue >> 24);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(writtenValue >> 16);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }


    @SneakyThrows
    public static void bh(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(writtenValue >> 24);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(writtenValue >> 16);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(writtenValue >> 8);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)writtenValue;
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void dl(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(128 + writtenValue);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }


    @SneakyThrows
    public static void en(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(writtenValue >> 16);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(writtenValue >> 24);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)writtenValue;
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(writtenValue >> 8);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void don(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(128 - writtenValue);
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
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(0 - writtenValue);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void er(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)writtenValue;
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(writtenValue >> 8);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(writtenValue >> 16);
        array[(offset += -1456241929) * -2114593081 - 1] = (byte)(writtenValue >> 24);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

}