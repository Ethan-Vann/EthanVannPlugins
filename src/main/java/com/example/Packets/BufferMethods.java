package com.example.Packets;

import com.example.ObfuscatedNames;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class BufferMethods
{
	@SneakyThrows
	public static void ct(Object var0, int var1) {
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(var1 + 128);
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}

//	public static void bl(Object var0, int var1) {
//		if ((var1 & -128) != 0) {
//			if ((var1 & -16384) != 0) {
//				if ((var1 & -2097152) != 0) {
//					if ((var1 & -268435456) != 0) {
//						ak(var0,var1 >>> 28 | 128);
//					}
//
//					ak(var0,var1 >>> 21 | 128);
//				}
//
//				ak(var0,var1 >>> 14 | 128);
//			}
//			ak(var0,var1 >>> 7 | 128);
//		}
//		ak(var0,var1 & 127);
//	}
	@SneakyThrows
	public static void df(Object var0, int var1) {
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(var1 >> 8);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)var1;
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(var1 >> 24);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(var1 >> 16);
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}
	@SneakyThrows
	public static void dv(Object var0, int var1) {
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(var1 >> 16);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(var1 >> 24);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)var1;
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(var1 >> 8);
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}
	@SneakyThrows
	public static void au(Object var0, int var1) {
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(var1 >> 8);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)var1;
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}
	@SneakyThrows
	public static void cg(Object var0, int var1) {
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(var1 >> 8);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(var1 + 128);
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}
	@SneakyThrows
	public static void cj(Object var0, int var1) {
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(var1 + 128);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(var1 >> 8);
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}
	@SneakyThrows
	public static void cz(Object var0, int var1) {
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(128 - var1);
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}
	@SneakyThrows
	public static void ah(Object var0, int var1) {
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(var1 >> 24);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(var1 >> 16);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(var1 >> 8);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)var1;
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}
	@SneakyThrows
	public static void ak(Object var0, int var1) {
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)var1;
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}
	@SneakyThrows
	public static void co(Object var0, int var1) {
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)var1;
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(var1 >> 8);
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}
	@SneakyThrows
	public static void cb(Object var0, int var1) {
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1441360663) * -612074329 - 1] = (byte)(0 - var1);
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}
}
