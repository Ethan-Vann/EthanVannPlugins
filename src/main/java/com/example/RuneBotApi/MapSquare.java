package com.example.RuneBotApi;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum MapSquare
{
    WINTERTODT(6462),
    BLASTFURNACE(7757),
    ARDOUGNE(10547),
    DRAYNOR(12338),
    POLLNIVNEACH(13358),
    ZULRAH(9007),

    UNNAMED(-1);

    private final int id;

    private static final Map<Integer, MapSquare> MAP_SQUARES = new HashMap<>();

    static
    {
        for (MapSquare mapSquare : values())
        {
            if (mapSquare.id != -1)
            {
                MAP_SQUARES.put(mapSquare.id, mapSquare);
            }
        }
    }

    public static MapSquare fromId(int id)
    {
        return MAP_SQUARES.getOrDefault(id, UNNAMED);
    }
}
