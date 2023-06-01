package com.example.Packets;

import com.example.PacketUtils.ObfuscatedNames;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class BufferMethods {
    //al = array
    //at = offset
    @SneakyThrows
    public static void ef(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)writtenValue;
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 16);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 24);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void be(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)writtenValue;
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void dx(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue + 128);
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
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(128 + writtenValue);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 8);
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
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)writtenValue;
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 24);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 16);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void bw(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 24);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 16);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)writtenValue;
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
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)writtenValue;
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void dj(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)writtenValue;
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 8);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }


    @SneakyThrows
    public static void dz(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(0 - writtenValue);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void dk(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue + 128);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }


    @SneakyThrows
    public static void em(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 16);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 24);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)writtenValue;
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 8);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void doMethod(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(128 - writtenValue);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

}
