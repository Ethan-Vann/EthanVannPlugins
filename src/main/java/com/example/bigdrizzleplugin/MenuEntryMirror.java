package com.example.bigdrizzleplugin;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.MenuAction;
import net.runelite.api.Skill;

import java.util.concurrent.Callable;


public class MenuEntryMirror {

    @Getter @Setter private String option;
    @Getter @Setter private String target;
    @Getter @Setter private int identifier;
    @Getter @Setter private MenuAction menuAction;
    @Getter @Setter private int param0;
    @Getter @Setter private int param1;
    @Getter @Setter private boolean leftClick;
    @Getter @Setter private  boolean deprioritized;
    @Getter @Setter private int itemID;
    @Getter @Setter private int postActionTickDelay;
    @Getter @Setter private Skill blockUntilXpDrop;
    @Getter @Setter private Callable<Boolean> blockUntil = null;

    public MenuEntryMirror(String option, String target, int identifier, MenuAction menuAction, int param0, int param1, boolean leftClick, boolean deprioritized, int itemID, int postActionTickDelay){
        this.option = option;
        this.target = target;
        this.identifier = identifier;
        this.menuAction = menuAction;
        this.param0 = param0;
        this.param1 = param1;
        this.leftClick = leftClick;
        this.deprioritized = deprioritized;
        this.itemID = itemID;
        this.postActionTickDelay = postActionTickDelay;
    }

    public MenuEntryMirror(String option, String target, int identifier, MenuAction menuAction, int param0, int param1, int itemID){
        this(option, target, identifier, menuAction, param0, param1, false, false, itemID, 0);
    }

    public MenuEntryMirror(String option, int identifier, MenuAction menuAction, int param0, int param1, int itemID){
        this(option, "", identifier, menuAction, param0, param1, false, false, itemID, 0 );
    }

    public MenuEntryMirror(String option, int identifier, MenuAction menuAction, int param0, int param1, int itemID, int postActionTickDelay){
        this(option, "", identifier, menuAction, param0, param1, false, false, itemID, postActionTickDelay);
    }

    public MenuEntryMirror(){
        this.option = "";
        this.target = "";
        this.identifier = 0;
        this.menuAction = MenuAction.CC_OP;
        this.param0 = 0;
        this.param1 = 0;
        this.leftClick = false;
        this.deprioritized = false;
        this.itemID = 0;
    }

    public String toString(){
        return "MenuEntry: {Option=" + option +
                " Target=" + target +
                " Identifier=" + identifier +
                " MenuAction=" + menuAction.toString() +
                " param0=" + param0 +
                " param1=" + param1 +
                " ItemID=" + itemID +
                " TickDelay=" + postActionTickDelay +
                " BlockSkill=" + blockUntilXpDrop + "}";
    }
}
