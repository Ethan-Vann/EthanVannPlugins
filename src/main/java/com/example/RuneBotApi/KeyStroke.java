package com.example.RuneBotApi;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.event.KeyEvent;

@AllArgsConstructor
@Getter
public enum KeyStroke {
    SPACE(KeyEvent.VK_SPACE),
    ENTER(KeyEvent.VK_ENTER);

    private final int keyEvent;
}
