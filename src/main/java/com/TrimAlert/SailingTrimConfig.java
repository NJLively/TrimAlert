package com.TrimAlert;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(SailingTrimConfig.GROUP)
public interface SailingTrimConfig extends Config
{
    String GROUP = "sailingtrim";

    @ConfigItem(
        keyName = "enableVisual",
        name = "Visual Alerts",
        description = "Show the on-screen trim warning?"
    )
    default boolean enableVisual()
    {
        return true;
    }

    @ConfigItem(
        keyName = "enableAudio",
        name = "Audio Alerts",
        description = "Play audio alerts?"
    )
    default boolean enableAudio()
    {
        return true;
    }

}
