package com.example.RuneBotApi;

import com.example.EthanApiPlugin.Collections.Widgets;
import com.example.Packets.WidgetPackets;
import lombok.SneakyThrows;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.RuneLite;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.WorldService;
import net.runelite.client.util.Text;
import net.runelite.client.util.WildcardMatcher;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.stream.Collectors;

public class RBApi {


    public static Client getClient()
    {
        return RuneLite.getInjector().getProvider(Client.class).get();
    }

    public static ItemManager getItemManager()
    {
        return RuneLite.getInjector().getProvider(ItemManager.class).get();
    }

    public static ClientThread getClientThread()
    {
        return RuneLite.getInjector().getProvider(ClientThread.class).get();
    }

    public static WorldService getWorldService()
    {
        return RuneLite.getInjector().getProvider(WorldService.class).get();
    }

    public static void runOnClientThread(Runnable r)
    {
        getClientThread().invoke(r);
    }

    @SneakyThrows
    public static void sendKeystroke(KeyStroke options) {
        Client client = getClient();
        KeyEvent keyPress = new KeyEvent(client.getCanvas(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), InputEvent.BUTTON1_DOWN_MASK, options.getKeyEvent());
        KeyEvent keyRelease = new KeyEvent(client.getCanvas(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, options.getKeyEvent());
        client.getCanvas().dispatchEvent(keyPress);
        client.getCanvas().dispatchEvent(keyRelease);
    }

//    /**
//     * Untested WIP: do not try to use yet
//     */
//    public static boolean enterBankPin(String pin) /*throws AWTException*/ {
//
//        for (Widget i : Widgets.search().withText("1").result())
//        {
//            if (i.getParentId() >= 13959184 && i.getParentId() <= 13959202 && i.getParentId() % 2 == 0)
//            {
//                WidgetPackets.queueWidgetActionPacket(0, 13959184, -1, -1);
//                System.out.println(i.getText());
//                for (var j : i.getActions()) System.out.println(j);
//            }
//
//        }
//        for (Widget i : Widgets.search().withText("2").result())
//        {
////            if (i.getParentId() >= 13959184 && i.getParentId() <= 13959202 && i.getParentId() % 2 == 0)
////                WidgetPackets.queueWidgetActionPacket(0, i.getId(), -1, -1);
//        }
//        return true;
//        if (pin.length() != 4) return false;
//        Robot robot = new Robot();
//        robot.keyPress(KeyStroke.ZERO.getKeyEvent());
//        robot.keyRelease(KeyStroke.ZERO.getKeyEvent());
//        if (getClient() != null) return true;
//
//        for (char i : pin.toCharArray())
//        {
//            if (i < '0' || i > '9') return false;
//
//            switch (i)
//            {
//                case '0':
//                    sendKeystroke(KeyStroke.ZERO);
//                    System.out.println("in here");
//         break; case '1': sendKeystroke(KeyStroke.ONE);
//         break; case '2': sendKeystroke(KeyStroke.TWO);
//         break; case '3': sendKeystroke(KeyStroke.THREE);
//         break; case '4': sendKeystroke(KeyStroke.FOUR);
//         break; case '5': sendKeystroke(KeyStroke.FIVE);
//         break; case '6': sendKeystroke(KeyStroke.SIX);
//         break; case '7': sendKeystroke(KeyStroke.SEVEN);
//         break; case '8': sendKeystroke(KeyStroke.EIGHT);
//         break; case '9': sendKeystroke(KeyStroke.NINE);
//            }
//        }
//
//        return true;
//    }


    /**
     * provide the config.itemList or config.npcList or w/e and
     * use the resulting value for the configCSVParser. This helps
     * improve time complexity if the user spells the item name
     * correctly since having to search through every entity for every
     * config entry runs in quadratic time which is no good
     * @return configString -> HashSet<String>.forEach(str -> str.toLower().trim())
     */
    public static HashSet<String> configCSVToHashSet(String inputText)
    {
        return Text.fromCSV(inputText.toLowerCase()).stream()
                .map(String::trim)
                .collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * The nature of this being static (and potentially used concurrently)
     * requires the instantiation of the HashSet to be done from within the calling method in
     * order to see any performance benefit. See RBApi.configCSVToHashSet()
     * @return true if item is in the config list
     */
    public static boolean configMatcher(HashSet<String> providedConfigEntries, String entityName)
    {
        if (providedConfigEntries.contains(entityName.toLowerCase())) return true;

        return providedConfigEntries.stream()
                .anyMatch(pattern ->
                        WildcardMatcher.matches(pattern, entityName.toLowerCase())
                );
    }

}
