package com.TrimAlert;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class SailingTrimPluginTest
{
    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(SailingTrimPlugin.class);
        RuneLite.main(args);
    }
}
