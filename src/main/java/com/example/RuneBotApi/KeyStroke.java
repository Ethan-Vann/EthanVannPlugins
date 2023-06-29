package com.example.RuneBotApi;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.event.KeyEvent;

@AllArgsConstructor
@Getter
public enum KeyStroke {
    SPACE(KeyEvent.VK_SPACE),
    ZERO(KeyEvent.VK_0),
    ONE(KeyEvent.VK_1),
    TWO(KeyEvent.VK_2),
    THREE(KeyEvent.VK_3),
    FOUR(KeyEvent.VK_4),
    FIVE(KeyEvent.VK_5),
    SIX(KeyEvent.VK_6),
    SEVEN(KeyEvent.VK_7),
    EIGHT(KeyEvent.VK_8),
    NINE(KeyEvent.VK_9);

    private final int keyEvent;
}
