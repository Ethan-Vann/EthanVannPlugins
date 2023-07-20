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
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)writtenValue;
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 8);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 16);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 24);
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue + 128);
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
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)writtenValue;
        array[(offset += 646629181) * -164706283 - 1] = (byte)(0 - writtenValue);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void dv(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue + 128);
        array[(offset += 646629181) * -164706283 - 1] = (byte)writtenValue;
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue >> 8);
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
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(128 + writtenValue);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 646629181) * -164706283 - 1] = (byte)writtenValue;
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void eh(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 8);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)writtenValue;
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 24);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 16);
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue >> 16);
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue >> 24);
        array[(offset += 646629181) * -164706283 - 1] = (byte)writtenValue;
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue >> 8);
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
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 24);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 16);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 8);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)writtenValue;
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 646629181) * -164706283 - 1] = (byte)(128 + writtenValue);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }


    @SneakyThrows
    public static void ej(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 8);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)writtenValue;
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 646629181) * -164706283 - 1] = (byte)writtenValue;
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue >> 24);
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue >> 16);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void bx(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)writtenValue;
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue >> 24);
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue >> 16);
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 646629181) * -164706283 - 1] = (byte)writtenValue;
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
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(0 - writtenValue);
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue + 128);
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue >> 8);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

    @SneakyThrows
    public static void dm(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 8);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue + 128);
        array[(offset += 646629181) * -164706283 - 1] = (byte)(128 - writtenValue);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }


    @SneakyThrows
    public static void bk(Object bufferInstance, int writtenValue) {
        Field arrayField = bufferInstance.getClass().getField(ObfuscatedNames.bufferArrayField);
        Field offsetField = bufferInstance.getClass().getField(ObfuscatedNames.bufferOffsetField);
        arrayField.setAccessible(true);
        offsetField.setAccessible(true);
        byte[] array = (byte[]) arrayField.get(bufferInstance);
        int offset = offsetField.getInt(bufferInstance);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 16);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 24);
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)writtenValue;
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 646629181) * -164706283 - 1] = (byte)writtenValue;
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
//        array[(offset += 1775636691) * -1705195685 - 1] = (byte)(128 - writtenValue);
        array[(offset += 646629181) * -164706283 - 1] = (byte)writtenValue;
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue >> 8);
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue >> 16);
        array[(offset += 646629181) * -164706283 - 1] = (byte)(writtenValue >> 24);
        offsetField.setInt(bufferInstance, offset);
        arrayField.set(bufferInstance, array);
        arrayField.setAccessible(false);
        offsetField.setAccessible(false);
    }

}
