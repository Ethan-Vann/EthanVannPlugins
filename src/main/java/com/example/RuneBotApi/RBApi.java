package com.example.RuneBotApi;

import net.runelite.api.Client;
import net.runelite.client.RuneLite;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.WorldService;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

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

    public static void sendKeystroke(KeyStroke options)
    {
        Client client = getClient();
        KeyEvent keyPress = new KeyEvent(client.getCanvas(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), InputEvent.BUTTON1_DOWN_MASK, options.getKeyEvent());
        KeyEvent keyRelease = new KeyEvent(client.getCanvas(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, options.getKeyEvent());
        client.getCanvas().dispatchEvent(keyPress);
        client.getCanvas().dispatchEvent(keyRelease);
    }

    /**
     * Untested WIP: do not try to use yet
     */
    public static boolean enterBankPin(String pin)
    {
        if (pin.length() != 4) return false;

        for (char i : pin.toCharArray())
        {
            if (i < '0' || i > '9') return false;

            switch (i)
            {
                case '0': sendKeystroke(KeyStroke.ZERO);
         break; case '1': sendKeystroke(KeyStroke.ONE);
         break; case '2': sendKeystroke(KeyStroke.TWO);
         break; case '3': sendKeystroke(KeyStroke.THREE);
         break; case '4': sendKeystroke(KeyStroke.FOUR);
         break; case '5': sendKeystroke(KeyStroke.FIVE);
         break; case '6': sendKeystroke(KeyStroke.SIX);
         break; case '7': sendKeystroke(KeyStroke.SEVEN);
         break; case '8': sendKeystroke(KeyStroke.EIGHT);
         break; case '9': sendKeystroke(KeyStroke.NINE);
            }
        }

        return true;
    }


}
