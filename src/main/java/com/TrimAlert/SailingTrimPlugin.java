package com.TrimAlert;

import com.google.inject.Provides;
import net.runelite.api.Client;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;
import javax.inject.Inject;

@PluginDescriptor(
        name = "TrimAlert",
        description = "An unavoidable reminder to trim your sails, you lazy scallywag!",
        tags = {"sailing", "boat", "alert"}
)
public class SailingTrimPlugin extends Plugin
{
    private static final int TRIM_VARBIT = 15100;
    private static final int TRIM_TRIGGER_VALUE = 1;
    private static final String CHAT_TRIGGER = "you feel a gust of wind";
    private static final String CHAT_CLEAR = "you trim the sails";
    private static final String CHAT_CREW_CLEAR = "gain some experience by watching your crew work";
    private static final String CHAT_CREW_CLEAR_TYPO = "gain some experience by wathcing your crew work";
    private static final String CHAT_WIND_DIES = "the wind dies down and your sails with it";
    private static final int FLASH_TICKS = 4;
    private static final int REMINDER_DELAY_TICKS = 10;
    private static final int ALERT_SOUND_ID = 3926; // Default alert ping

    @Inject
    private Client client;

    @Inject
    private SailingTrimConfig config;

    @Inject
    private Notifier notifier;

    @Inject
    private SailingTrimOverlay overlay;

    @Inject
    private OverlayManager overlayManager;

    private boolean trimAlertActive;
    private boolean chatAlertActive;

    private int flashTick;
    private int lastAlertTick;

    @Provides
    SailingTrimConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(SailingTrimConfig.class);
    }

    @Override
    protected void startUp()
    {
        overlayManager.add(overlay);
        trimAlertActive = false;
        chatAlertActive = false;
        flashTick = 0;
        lastAlertTick = -999;
        overlay.updateState(false, 1.0f);
    }

    @Override
    protected void shutDown()
    {
        overlayManager.remove(overlay);
        trimAlertActive = false;
        chatAlertActive = false;
        overlay.updateState(false, 1.0f);
    }

    @Subscribe
    public void onGameTick(GameTick tick)
    {
        final boolean varbitNeedsTrim = client.getVarbitValue(TRIM_VARBIT) == TRIM_TRIGGER_VALUE;
        final boolean shouldAlert = varbitNeedsTrim || chatAlertActive;

        if (shouldAlert)
        {
            activateAlert();
        }
        else
        {
            deactivateAlert();
        }

        if (trimAlertActive)
        {
            flashTick++;
        }
        else
        {
            flashTick = 0;
        }

        overlay.updateState(isTrimAlertActive(), getFlashAlpha());
    }

    @Subscribe
    public void onChatMessage(ChatMessage chatMessage)
    {
        if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE && chatMessage.getType() != ChatMessageType.SPAM)
        {
            return;
        }
        final String plain = Text.removeTags(chatMessage.getMessage()).toLowerCase();
        if (plain.contains(CHAT_CLEAR)
                || plain.contains(CHAT_CREW_CLEAR)
                || plain.contains(CHAT_CREW_CLEAR_TYPO)
                || plain.contains(CHAT_WIND_DIES))
        {
            chatAlertActive = false;
            deactivateAlert();
            return;
        }

        if (plain.contains(CHAT_TRIGGER))
        {
            chatAlertActive = true;
            activateAlert();
        }
    }

    void activateAlert()
    {
        trimAlertActive = true;
        if (config.enableAudio())
        {
            final int tickCount = client.getTickCount();
            if (tickCount - lastAlertTick >= REMINDER_DELAY_TICKS)
            {
                client.playSoundEffect(ALERT_SOUND_ID);
                notifier.notify("Trim your sails now!");
                lastAlertTick = tickCount;
            }
        }
    }

    void deactivateAlert()
    {
        trimAlertActive = false;
        flashTick = 0;
    }

    private boolean isTrimAlertActive()
    {
        return trimAlertActive && config.enableVisual();
    }

    private float getFlashAlpha()
    {
        final int cycle = flashTick % FLASH_TICKS;
        return cycle < FLASH_TICKS / 2 ? 0.95f : 0.55f;
    }
}