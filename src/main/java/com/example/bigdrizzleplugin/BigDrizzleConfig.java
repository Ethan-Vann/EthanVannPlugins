/*
 * Copyright (c) 2018, Seth <Sethtroll3@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.example.bigdrizzleplugin;

import lombok.AllArgsConstructor;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("bigdrizzle")
public interface BigDrizzleConfig extends Config
{
    @AllArgsConstructor
    enum ActivityType
    {
        FISHING("Karambwan Fishing"),
        TICKCOOKING("1T Cooking"),
        NORMALCOOKING("Normal Cooking"),
        ZMI("ZMI RC"),
        CHAOSALTAR("Chaos Altar"),
        LAVARC("Lava RC"),
        OTHER("Other");

        private final String value;

        @Override
        public String toString()
        {
            return value;
        }
    }

    @ConfigItem(
            keyName = "activity",
            name = "Activity",
            description = "Activity",
            position = 1
    )
    default ActivityType activityType()
    {
        return ActivityType.FISHING;
    }

    @ConfigItem(
            position = 2,
            keyName = "debugLog",
            name = "Debug Log",
            description = "Debug Log"
    )
    default boolean debugLog()
    {
        return false;
    }
    @ConfigItem(
            position = 3,
            keyName = "queueLog",
            name = "Queue Log",
            description = "Queue Log"
    )
    default boolean queueLog()
    {
        return false;
    }
    @ConfigItem(
            position = 4,
            keyName = "consumeClicks",
            name = "Consume",
            description = "consume"
    )
    default boolean consumeClicks()
    {
        return false;
    }
}
