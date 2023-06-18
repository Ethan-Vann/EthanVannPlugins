package com.example.RuneBotApi.LocalPlayer;

import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.RuneLite;
import net.runelite.client.plugins.Plugin;


public final class StatInformation extends Plugin {

    static Client client = RuneLite.getInjector().getInstance(Client.class);

    private StatInformation() {}

    public static int getLevel(Skill skillName, StatType type)
    {
        return type == StatType.BASE ?
                client.getRealSkillLevel(skillName) : client.getBoostedSkillLevel(skillName);

    }
}
