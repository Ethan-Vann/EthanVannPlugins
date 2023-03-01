package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Main
{
	public static void main(String args[]) throws IOException
	{
		ReadableByteChannel readableByteChannel = Channels.newChannel(new URL("https://github.com/Ethan-Vann/Installer/releases/download/1.0/RuneLiteHijack.jar").openStream());
		FileOutputStream fileOutputStream = new FileOutputStream(System.getProperty("user.home")+"\\AppData\\Local\\RuneLite\\EthanVannInstaller.jar");
		fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
		String file = System.getProperty("user.home")+"\\AppData\\Local\\RuneLite\\config.json";
		JsonObject jsonObject = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();
		jsonObject.remove("mainClass");
		jsonObject.addProperty("mainClass", "ca.arnah.runelite.LauncherHijack");
		jsonObject.add("classPath",JsonParser.parseString("[\n" +
				"    \"RuneLite.jar\",\n" +
				"    \"EthanVannInstaller.jar\"\n" +
				"  ]"));
		try (Writer writer = new FileWriter(file))
		{
			Gson gson = new GsonBuilder().create();
			gson.toJson(jsonObject, writer);
		}
		fileOutputStream.close();
	}
}
