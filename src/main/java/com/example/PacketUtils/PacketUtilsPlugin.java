package com.example.PacketUtils;

import com.google.archivepatcher.applier.FileByFileV1DeltaApplier;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.RuneLite;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginInstantiationException;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.rs.ClientLoader;
import org.benf.cfr.reader.Main;

import javax.inject.Inject;
import javax.swing.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.jar.JarFile;
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
    public static Method addNodeMethod;
    public static boolean usingClientAddNode = false;
    public static final int CLIENT_REV = 224;
    private static String loadedConfigName = "";
    @Inject
    private PluginManager pluginManager;

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
        //setupNeverlog();
        int feature = Runtime.version().feature();
        if (feature != 11) {
            for (int i = 0; i < 10; i++) {
                log.error("ETHAN VANN PLUGINS LOADED ON JAVA != 11 THIS IS NOT SUPPORTED");
                log.error("DEVELOPERS SHOULD IGNORE BUG REPORTS CONTAINING THIS LINE UNTIL THIS ISSUE IS RESOLVED");
            }
        } else {
            log.info("Ethan Vann Plugins loaded on Java 11");
        }
        setupRuneliteUpdateHandling(RuneLiteProperties.getVersion());
        cleanup();
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
    public static void setupNeverlog() {
        staticClient.setIdleTimeout(42069);
        for (Field declaredField : staticClient.getClass().getDeclaredFields()) {
            if (declaredField.getType() == int.class && Modifier.isStatic(declaredField.getModifiers())) {
                declaredField.setAccessible(true);
                int value = declaredField.getInt(null);
                if (value != 42069) {
                    declaredField.setAccessible(false);
                    continue;
                }
                System.out.println("found idle ticks field: " + declaredField.getName());
                declaredField.setInt(null, Integer.MAX_VALUE);
                declaredField.setAccessible(false);
            }
        }
    }

    @SneakyThrows
    public void cleanup() {
        if (!loadedConfigName.equals(makeString())) {
            for (int i = 0; i < 10; i++) {
                log.error("ETHAN VANN PLUGINS LOADED WITH INCORRECT CONFIG DATA THIS IS NOT SUPPORTED");
                log.error("DEVELOPERS SHOULD IGNORE BUG REPORTS CONTAINING THIS LINE UNTIL THIS ISSUE IS RESOLVED");
            }
        } else {
            log.info("config loaded from correct path");
        }
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
        if (Files.exists(codeSource.resolve(version + "-" + client.getRevision() + ".txt"))) {
            Path f = codeSource.resolve(version + "-" + client.getRevision() + ".txt");
            List<String> lines = Files.readAllLines(f);
            loadedConfigName = f.getFileName().toString();
            System.out.println("config name: " + loadedConfigName);
            if (lines.size() < 2) {
                return;
            }
            usingClientAddNode = Boolean.parseBoolean(lines.get(0));
            if (usingClientAddNode) {
                log.info("loaded addNode config from file");
                log.info("usingClientAddNode: " + usingClientAddNode);
                log.info("addNodeMethod: " + "N/A");
                return;
            }
            String[] split = lines.get(1).split("\\.");
            Class<?> addNodeClassName = client.getClass().getClassLoader().loadClass(split[0]);
            for (Method declaredMethod : addNodeClassName.getDeclaredMethods()) {
                if (declaredMethod.getName().equals(split[1]) && declaredMethod.getParameterCount() > 0 && declaredMethod.getParameterTypes()[0].getSimpleName().equals(ObfuscatedNames.packetWriterClassName)) {
                    addNodeMethod = declaredMethod;
                }
            }
            log.info("loaded addNode config from file");
            log.info("usingClientAddNode: " + usingClientAddNode);
            log.info("addNodeMethod: " + addNodeMethod);
            return;
        }
        String doActionClassName = "";
        String doActionMethodName = "";
        Field classes = ClassLoader.class.getDeclaredField("classes");
        classes.setAccessible(true);
        ClassLoader classLoader = client.getClass().getClassLoader();
        Vector<Class<?>> classesVector = (Vector<Class<?>>) classes.get(classLoader);
        Class<?>[] params = new Class[]{int.class, int.class, int.class, int.class, int.class, int.class, String.class, String.class, int.class, int.class};
        for (int i = 0; i < classesVector.size(); i++) {
            try {
                if (classesVector.get(i).getSuperclass()!=null&&classesVector.get(i).getSuperclass().getName().contains("SSLSocketFactory")) {
                    continue;
                }
                try {
                    for (int i1 = 0; i1 < classesVector.get(i).getDeclaredMethods().length; i1++) {
                        if (!Arrays.equals(Arrays.copyOfRange(classesVector.get(i).getDeclaredMethods()[i1].getParameterTypes(), 0, 10), params)) {
                            continue;
                        }
                        doActionClassName = classesVector.get(i).getSimpleName();
                        doActionMethodName = classesVector.get(i).getDeclaredMethods()[i1].getName();
                    }
                } catch (NoClassDefFoundError | VerifyError ignored) {

                }
            } catch (Exception e) {
                log.info("exception");
            }
        }
        System.out.print("finished");
        final String doActionFinalClassName = doActionClassName;
        final String doActionFinalMethodName = doActionMethodName;
        System.out.println(doActionFinalClassName);
        System.out.println(doActionFinalMethodName);
        classes.setAccessible(false);
        URL rlConfigURL = new URL("https://static.runelite.net/jav_config.ws");
        if (!codeSource.toFile().isDirectory()) {
            Files.createDirectory(codeSource);
        }
        Path vanillaOutputPath = codeSource.resolve("vanilla.jar");
        Path patchedOutputPath = codeSource.resolve("patched.jar");
        Path doActionOutputPath = codeSource.resolve("doAction.class");
        Path decompilationOutputPath = codeSource.resolve("decompiled.txt");
        System.out.println("Downloading vanilla client");
        downloadVanillaJar(vanillaOutputPath, rlConfigURL);
        File vanilla = vanillaOutputPath.toFile();
        if (vanilla.exists()) {
            log.info("Vanilla jar exists");
        } else {
            log.info("Vanilla jar does not exist");
        }
        String[] versionSplits = version.split("\\.");
        int length = versionSplits.length;
        if (version.contains("snapshot")) {
            log.info("replacing snapshot version");
            version = version.replace("-SNAPSHOT", "");
        }
        if ((length > 0 && Integer.parseInt(versionSplits[0]) > 1 || (length > 1) && (Integer.parseInt(versionSplits[1]) > 10) )|| (length > 2 && Integer.parseInt(versionSplits[2]) > 34)) {
            String url = "https://repo.runelite.net/net/runelite/injected-client/" + version + "/injected-client-" + version + ".jar";
            URL injectedURL = new URL(url);
            log.info("Downloading injected client from " + injectedURL);
            try (InputStream clientStream = injectedURL.openStream()) {
                Files.copy(clientStream, patchedOutputPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } else {
            OutputStream patchedOutputStream = Files.newOutputStream(patchedOutputPath);
            InputStream patch = ClientLoader.class.getResourceAsStream("/client.patch");
            new FileByFileV1DeltaApplier().applyDelta(vanilla, patch, patchedOutputStream);
            patch.close();
            patchedOutputStream.flush();
            patchedOutputStream.close();
        }
        try (JarFile patchedJar = new JarFile(patchedOutputPath.toFile())) {
            patchedJar.entries().asIterator().forEachRemaining(jarEntry -> {
                System.out.println("jar entry: " + jarEntry.getName());
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
        for (String methodCall : methodCalls) {
            System.out.println(methodCall);
        }
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
                log.info("found addNode method call example " + line.trim());
                String stringOutput = usingClientAddNode +
                        "\n" +
                        mostUsedMethod;
                Path config = Files.write(Files.createFile(codeSource.resolve(version + "-" + client.getRevision() + ".txt")), stringOutput.getBytes(StandardCharsets.UTF_8));
                loadedConfigName = config.getFileName().toString();
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
                log.info("Downloading vanilla client from " + clientURL);
                try (InputStream clientStream = clientURL.openStream()) {
                    Files.copy(clientStream, vanillaOutputPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
        configReader.close();
    }

    @Override
    public void shutDown() {
        log.info("Shutdown");
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

    public String makeString() {
        return RuneLiteProperties.getVersion() + "-" + client.getRevision() + ".txt";
    }
}