package com.example.RuneBotApi;

import java.util.concurrent.ThreadLocalRandom;

public class RBRandom {

    /**
     * @param lowerBound
     * @param upperBound
     * @return lowerBound <= x < upperBound
     */
    public static int randRange(int lowerBound, int upperBound)
    {
        return ThreadLocalRandom.current().nextInt(lowerBound, upperBound);
    }
}
