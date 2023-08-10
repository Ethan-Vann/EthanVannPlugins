package com.example.PacketUtils;

import com.google.archivepatcher.applier.FileByFileV1DeltaApplier;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ScriptEvent;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.widgets.Widget;
import net.runelite.client.RuneLite;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginInstantiationException;
import net.runelite.client.plugins.PluginManager;
import org.benf.cfr.reader.Main;

import javax.inject.Inject;
import javax.swing.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

@Slf4j
@Singleton
@PluginDescriptor(
        name = "Packet Utils",
        description = "Packet Utils for Plugins",
        enabledByDefault = true,
        tags = {"ethan"}
)
public class PacketUtilsPlugin extends Plugin {
    @Inject
    PacketUtilsConfig config;
    @Inject
    Client client;
    static Client staticClient;
    @Inject
    PacketReflection packetReflection;
    @Inject
    ClientThread thread;
    public static Method addNodeMethod;
    public static boolean usingClientAddNode = false;
    public static final int CLIENT_REV = 215;
    private static boolean loaded = false;
    @Inject
    private PluginManager pluginManager;

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            loaded = packetReflection.LoadPackets();
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    @Provides
    public PacketUtilsConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(PacketUtilsConfig.class);
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked e) {
        if (config.debug()) {
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "Packet Utils", e.toString(), null);
            System.out.println(e);
        }
    }


    @Override
    @SneakyThrows
    public void startUp() {
        Thread updateThread = new Thread(() ->
        {
            setupRuneliteUpdateHandling(RuneLiteProperties.getVersion());
            cleanup();
        });
        updateThread.start();
        staticClient = client;
        if (client.getRevision() != CLIENT_REV) {
            SwingUtilities.invokeLater(() ->
            {
                JOptionPane.showMessageDialog(null, "PacketUtils not updated for this rev please " +
                        "wait for " +
                        "plugin update");
                try {
                    pluginManager.setPluginEnabled(this, false);
                    pluginManager.stopPlugin(this);
                } catch (PluginInstantiationException ignored) {
                }
            });
            return;
        }
        thread.invoke(() ->
        {
            if (client.getGameState() != null && client.getGameState() == GameState.LOGGED_IN) {
                loaded = packetReflection.LoadPackets();
            }
        });
        SwingUtilities.invokeLater(() ->
        {
            for (Plugin plugin : pluginManager.getPlugins()) {
                if (plugin.getName().equals("EthanApiPlugin")) {
                    if (pluginManager.isPluginEnabled(plugin)) {
                        continue;
                    }
                    try {
                        pluginManager.setPluginEnabled(plugin, true);
                        pluginManager.startPlugin(plugin);
                    } catch (PluginInstantiationException e) {
                        //e.printStackTrace();
                    }
                }
            }
        });
    }

    @SneakyThrows
    public void cleanup() {
        Path codeSource = RuneLite.RUNELITE_DIR.toPath().resolve("PacketUtils");
        List<Path> toDelete = new ArrayList<>();
        toDelete.add(codeSource.resolve("vanilla.jar"));
        toDelete.add(codeSource.resolve("patched.jar"));
        toDelete.add(codeSource.resolve("doAction.class"));
        toDelete.add(codeSource.resolve("decompiled.txt"));
        for (Path path : toDelete) {
            Files.deleteIfExists(path);
        }
    }

    @SneakyThrows
    public void setupRuneliteUpdateHandling(String version) {
        Path codeSource = RuneLite.RUNELITE_DIR.toPath().resolve("PacketUtils");
        if (Files.exists(codeSource.resolve(version + ".txt"))) {
            List<String> lines = Files.readAllLines(codeSource.resolve(version + ".txt"));
            if (lines.size() < 2) {
                return;
            }
            usingClientAddNode = Boolean.parseBoolean(lines.get(0));
            if (usingClientAddNode) {
                System.out.println("loaded addNode config from file");
                System.out.println("usingClientAddNode: " + usingClientAddNode);
                System.out.println("addNodeMethod: " + "N/A");
                return;
            }
            String[] split = lines.get(1).split("\\.");
            Class<?> addNodeClassName = client.getClass().getClassLoader().loadClass(split[0]);
            for (Method declaredMethod : addNodeClassName.getDeclaredMethods()) {
                if (declaredMethod.getName().equals(split[1]) && declaredMethod.getParameterCount() > 0 && declaredMethod.getParameterTypes()[0].getSimpleName().equals(ObfuscatedNames.packetWriterClassName)) {
                    addNodeMethod = declaredMethod;
                }
            }
            System.out.println("loaded addNode config from file");
            System.out.println("usingClientAddNode: " + usingClientAddNode);
            System.out.println("addNodeMethod: " + addNodeMethod);
            return;
        }
        String doActionClassName = "";
        String doActionMethodName = "";
        Field classes = ClassLoader.class.getDeclaredField("classes");
        classes.setAccessible(true);
        ClassLoader classLoader = client.getClass().getClassLoader();
        Vector<Class<?>> classesVector = (Vector<Class<?>>) classes.get(classLoader);
        Class<?>[] params = new Class[]{int.class, int.class, int.class, int.class, int.class, String.class, String.class, int.class, int.class};
        for (Class<?> aClass : classesVector) {
            for (Method declaredMethod : aClass.getDeclaredMethods()) {
                if (declaredMethod.getParameterCount() != 10) {
                    continue;
                }
                if (declaredMethod.getReturnType() != void.class) {
                    continue;
                }
                if (!Arrays.equals(Arrays.copyOfRange(declaredMethod.getParameterTypes(), 0, 9), params)) {
                    continue;
                }
                doActionClassName = aClass.getSimpleName();
                doActionMethodName = declaredMethod.getName();
            }
        }
        final String doActionFinalClassName = doActionClassName;
        final String doActionFinalMethodName = doActionMethodName;
        classes.setAccessible(false);
        URL clientPatchURL = new URL("https://repo.runelite.net/net/runelite/client-patch/%VERSION%/client-patch-%VERSION%.jar".replaceAll("%VERSION%", version));
        URL rlConfigURL = new URL("https://static.runelite.net/jav_config.ws");
        if (!codeSource.toFile().isDirectory()) {
            Files.createDirectory(codeSource);
        }
        Path vanillaOutputPath = codeSource.resolve("vanilla.jar");
        Path patchedOutputPath = codeSource.resolve("patched.jar");
        Path doActionOutputPath = codeSource.resolve("doAction.class");
        Path decompilationOutputPath = codeSource.resolve("decompiled.txt");
        downloadVanillaJar(vanillaOutputPath, rlConfigURL);
        File vanilla = vanillaOutputPath.toFile();
        if (vanilla.exists()) {
            System.out.println("Vanilla jar exists");
        } else {
            System.out.println("Vanilla jar does not exist");
        }
        OutputStream patchedOutputStream = Files.newOutputStream(patchedOutputPath);
        new FileByFileV1DeltaApplier().applyDelta(vanilla, patchInputStream(clientPatchURL), patchedOutputStream);
        patchedOutputStream.flush();
        patchedOutputStream.close();
        try (JarFile patchedJar = new JarFile(patchedOutputPath.toFile())) {
            patchedJar.entries().asIterator().forEachRemaining(jarEntry -> {
                if (jarEntry.getName().equals(doActionFinalClassName + ".class")) {
                    try (InputStream inputStream = patchedJar.getInputStream(jarEntry)) {
                        Files.copy(inputStream, doActionOutputPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        OutputStream decompilationOutputStream = Files.newOutputStream(decompilationOutputPath);
        PrintStream s = new PrintStream(decompilationOutputStream);
        System.setOut(s);
        Main.main(new String[]{doActionOutputPath.toAbsolutePath().toString(), "--methodname", doActionFinalMethodName});
        s.flush();
        s.close();
        decompilationOutputStream.close();
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        File output = decompilationOutputPath.toFile();
        BufferedReader reader = new BufferedReader(new FileReader(output));
        List<String> lines = reader.lines().collect(Collectors.toList());
        String previousLine = null;
        ArrayList<String> methodCalls = new ArrayList<>();
        for (String line : lines) {
            if (line.length() < 300) {
                if (line.contains("}")) {
                    if (previousLine != null && previousLine.contains("(")) {
                        methodCalls.add(previousLine.split("\\(")[0].trim());
                    }
                }
                previousLine = line;
            }
        }
        reader.close();
        String mostUsedMethod = methodCalls.stream()
                .collect(Collectors.groupingBy(str -> str, Collectors.counting()))
                .entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .findFirst().get().getKey();
        if (mostUsedMethod.contains("client")) {
            usingClientAddNode = true;
        } else {
            String[] split = mostUsedMethod.split("\\.");
            Class<?> addNodeClassName = client.getClass().getClassLoader().loadClass(split[0]);
            for (Method declaredMethod : addNodeClassName.getDeclaredMethods()) {
                if (declaredMethod.getName().equals(split[1]) && declaredMethod.getParameterTypes()[0].getSimpleName().equals(ObfuscatedNames.packetWriterClassName)) {
                    addNodeMethod = declaredMethod;
                }
            }
        }
        for (String line : lines) {
            if (line.contains(mostUsedMethod)) {
                System.out.println("found addNode method call example " + line.trim());
                StringBuilder stringOutput = new StringBuilder();
                stringOutput.append(usingClientAddNode);
                stringOutput.append("\n");
                stringOutput.append(mostUsedMethod);
                Files.write(Files.createFile(codeSource.resolve(version + ".txt")), stringOutput.toString().getBytes(StandardCharsets.UTF_8));
                break;
            }
        }
    }

    public static void downloadVanillaJar(Path vanillaOutputPath, URL rlConfigURL) throws IOException {
        BufferedReader configReader = new BufferedReader(new InputStreamReader(rlConfigURL.openConnection().getInputStream()));
        while (configReader.ready()) {
            String line = configReader.readLine();
            if (line == null) {
                continue;
            }
            if (line.contains("runelite.gamepack")) {
                URL clientURL = new URL(line.split("=")[1]);
                System.out.println("Downloading vanilla client from " + clientURL);
                try (InputStream clientStream = clientURL.openStream()) {
                    Files.copy(clientStream, vanillaOutputPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
        configReader.close();
    }

    public static InputStream patchInputStream(URL clientPatchURL) throws IOException, URISyntaxException {
        System.out.println("Downloading client patch from " + clientPatchURL);
        JarInputStream patchedStream = new JarInputStream(clientPatchURL.openConnection().getInputStream());
        while (true) {
            JarEntry entry = patchedStream.getNextJarEntry();
            if (entry == null) {
                break;
            }
            if (entry.isDirectory()) {
                continue;
            }
            if (entry.getName().equals("client.patch")) {
                ByteArrayInputStream returnStream = new ByteArrayInputStream(patchedStream.readNBytes((int) entry.getSize()));
                patchedStream.close();
                return returnStream;
            }
        }
        patchedStream.close();
        return null;
    }

    @Subscribe
    public void onScriptPreFired(ScriptPreFired e) {
        if (config.debug()) {
            if (e.getScriptId() == 1405) {
                System.out.print("resume pause maybe?");
                ScriptEvent scriptEvent = e.getScriptEvent();
                Widget w = scriptEvent.getSource();
                System.out.println(scriptEvent.getOp() + ":" + w.getId() + ":" + w.getIndex());
            }
        }
    }

    @Override
    public void shutDown() {
        log.info("Shutdown");
        loaded = false;
    }

    @Inject
    private void init() {
        if (config.alwaysOn() && client.getRevision() == CLIENT_REV) {
            SwingUtilities.invokeLater(() ->
            {
                try {
                    RuneLite.getInjector().getInstance(PluginManager.class).setPluginEnabled(this, true);
                    RuneLite.getInjector().getInstance(PluginManager.class).startPlugin(this);
                } catch (PluginInstantiationException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}