package com.example.RuneBotApi;

import com.example.EthanApiPlugin.EthanApiPlugin;
import lombok.NoArgsConstructor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.game.WorldService;
import net.runelite.client.util.WorldUtil;
import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldResult;
import net.runelite.http.api.worlds.WorldType;

import java.util.EnumSet;
import java.util.List;

@NoArgsConstructor
public class WorldHopper {
    private Client client = RBApi.getClient();
    private WorldService worldService = RBApi.getWorldService();

    private net.runelite.api.World quickHopTargetWorld = null;
    private int displaySwitcherAttempts = 0;

    public void setupHop()
    {
        WorldResult worldResult = worldService.getWorlds();

        if (worldResult == null || client.getGameState() != GameState.LOGGED_IN)
        {
            return;
        }

        World currentWorld = worldResult.findWorld(client.getWorld());

        if (currentWorld == null)
        {
            return;
        }

        EnumSet<WorldType> currentWorldTypes = currentWorld.getTypes().clone();

        currentWorldTypes.remove(WorldType.PVP);
        currentWorldTypes.remove(WorldType.HIGH_RISK);
        currentWorldTypes.remove(WorldType.BOUNTY);
        currentWorldTypes.remove(WorldType.SKILL_TOTAL);
        currentWorldTypes.remove(WorldType.LAST_MAN_STANDING);

        List<World> worlds = worldResult.getWorlds();

        int worldIdx = worlds.indexOf(currentWorld);
        int totalLevel = client.getTotalLevel();

        World world;
        do
        {
            worldIdx++;

            if (worldIdx >= worlds.size())
            {
                worldIdx = 0;
            }

            world = worlds.get(worldIdx);

            EnumSet<WorldType> types = world.getTypes().clone();

            types.remove(WorldType.BOUNTY);
            // Treat LMS world like casual world
            types.remove(WorldType.LAST_MAN_STANDING);

            // Avoid switching to near-max population worlds, as it will refuse to allow the hop if the world is full
            if (world.getPlayers() >= 1800)
            {
                continue;
            }

            if (world.getPlayers() < 0)
            {
                // offline world
                continue;
            }

            // Break out if we've found a good world to hop to
            if (currentWorldTypes.equals(types))
            {
                break;
            }
        }
        while (world != currentWorld);

        if (world == currentWorld)
        {
            String chatMessage = new ChatMessageBuilder()
                    .append(ChatColorType.NORMAL)
                    .append("Couldn't find a world to quick-hop to.")
                    .build();

            RBApi.runOnClientThread(() -> {
                EthanApiPlugin.sendClientMessage("Couldn't find a world to hop to.");
            });
        }
        else
        {
            World newWorld = worldResult.findWorld(world.getId());
            if (newWorld != null) {
                RBApi.runOnClientThread(() -> hop(client, newWorld));
            }
        }
    }

    private void hop(Client client, World world)
    {
        assert client.isClientThread();

        final net.runelite.api.World rsWorld = client.createWorld();
        rsWorld.setActivity(world.getActivity());
        rsWorld.setAddress(world.getAddress());
        rsWorld.setId(world.getId());
        rsWorld.setPlayerCount(world.getPlayers());
        rsWorld.setLocation(world.getLocation());
        rsWorld.setTypes(WorldUtil.toWorldTypes(world.getTypes()));

        if (client.getGameState() == GameState.LOGIN_SCREEN)
        {
            // on the login screen we can just change the world by ourselves
            client.changeWorld(rsWorld);
            return;
        }


        quickHopTargetWorld = rsWorld;
        displaySwitcherAttempts = 0;
    }

    public void hopWorlds()
    {
        System.out.println("trying to hop");
        if (quickHopTargetWorld == null)
        {
            return;
        }

        if (client.getWidget(WidgetInfo.WORLD_SWITCHER_LIST) == null)
        {
            client.openWorldHopper();

            if (++displaySwitcherAttempts >= 10)
            {
                EthanApiPlugin.sendClientMessage("Couldn't hop!");

                displaySwitcherAttempts = 0;
                quickHopTargetWorld = null;
            }
        }
        else
        {
            client.hopToWorld(quickHopTargetWorld);
            displaySwitcherAttempts = 0;
            quickHopTargetWorld = null;
        }

    }
}
