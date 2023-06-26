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

    public static void sendChatDialogue(ChatDialogueOptions options)
    {
        Client client = getClient();
        KeyEvent keyPress = new KeyEvent(client.getCanvas(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), InputEvent.BUTTON1_DOWN_MASK, options.getKeyEvent());
        KeyEvent keyRelease = new KeyEvent(client.getCanvas(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, options.getKeyEvent());
        client.getCanvas().dispatchEvent(keyPress);
        client.getCanvas().dispatchEvent(keyRelease);
    }


}
