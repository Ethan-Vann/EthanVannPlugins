package com.example.bigdrizzleplugin;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Callable;

public class Action {
    @Getter @Setter PacketStorage packet;
    @Getter @Setter String desc;
    @Getter @Setter private Callable<Boolean> blockUntil;

    public Action(String desc, PacketStorage packet){
        this.desc = desc;
        this.packet = packet;
    }
}
