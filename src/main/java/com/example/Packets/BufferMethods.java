package com.example.Packets;

import com.example.ObfuscatedNames;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class BufferMethods
{
	@SneakyThrows
	public static void dd(Object var0, int var1)
	{
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 + 128);
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}

	@SneakyThrows
	public static void bt(Object var0, int var1)
	{
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) var1;
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}

	@SneakyThrows
	public static void dv(Object var0, int var1)
	{
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (0 - var1);
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}

	@SneakyThrows
	public static void bd(Object var0, int var1)
	{
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 >> 24);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 >> 16);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 >> 8);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) var1;
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}

	@SneakyThrows
	public static void be(Object var0, int var1)
	{
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 >> 8);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) var1;
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}

	@SneakyThrows
	public static void dh(Object var0, int var1)
	{
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (128 - var1);
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}

	@SneakyThrows
	public static void dz(Object var0, int var1)
	{
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 >> 8);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 + 128);
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}

	@SneakyThrows
	public static void dl(Object var0, int var1)
	{
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (128 + var1);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 >> 8);
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}

	@SneakyThrows
	public static void dm(Object var0, int var1)
	{
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) var1;
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 >> 8);
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}

	@SneakyThrows
	public static void ep(Object var0, int var1)
	{
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) var1;
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 >> 8);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 >> 16);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 >> 24);
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}

	@SneakyThrows
	public static void eb(Object var0, int var1)
	{
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 >> 8);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) var1;
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 >> 24);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 >> 16);
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}

	@SneakyThrows
	public static void er(Object var0, int var1)
	{
		Field arrayField = var0.getClass().getField(ObfuscatedNames.bufferArrayField);
		Field offsetField = var0.getClass().getField(ObfuscatedNames.bufferOffsetField);
		arrayField.setAccessible(true);
		offsetField.setAccessible(true);
		byte array[] = (byte[]) arrayField.get(var0);
		int offset = offsetField.getInt(var0);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 >> 16);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 >> 24);
		array[(offset += 1245058623) * 146201023 - 1] = (byte) var1;
		array[(offset += 1245058623) * 146201023 - 1] = (byte) (var1 >> 8);
		offsetField.setInt(var0, offset);
		arrayField.set(var0, array);
		arrayField.setAccessible(false);
		offsetField.setAccessible(false);
	}
}
