package com.example.Packets;

import com.example.PacketUtils.ObfuscatedNames;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class BufferMethods {
    //al = array
    //at = offset
    @SneakyThrows
    public static void br(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)writtenValue;
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void de(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(128 + writtenValue);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(writtenValue >> 8);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void df(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(128 + writtenValue);
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
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)writtenValue;
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void dw(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(128 - writtenValue);
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
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)writtenValue;
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(writtenValue >> 8);
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
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)writtenValue;
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(writtenValue >> 16);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(writtenValue >> 24);
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
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(0 - writtenValue);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }


    @SneakyThrows
    public static void bz(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(writtenValue >> 24);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(writtenValue >> 16);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)writtenValue;
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
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(writtenValue >> 16);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(writtenValue >> 24);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)writtenValue;
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(writtenValue >> 8);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }


    @SneakyThrows
    public static void dq(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(128 + writtenValue);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void et(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)writtenValue;
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(writtenValue >> 24);
        array[(offset += 1671616581) * 1646688909 - 1] = (byte)(writtenValue >> 16);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

}
