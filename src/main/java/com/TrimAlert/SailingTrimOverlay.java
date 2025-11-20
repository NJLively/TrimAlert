package com.TrimAlert;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.FontManager;

public class SailingTrimOverlay extends Overlay
{
    private static final Color BORDER_COLOR = new Color(255, 96, 96, 245);
    private static final Color FILL_COLOR = new Color(32, 0, 0, 190);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color SUBTEXT_COLOR = new Color(255, 160, 160);
    private static final BasicStroke BORDER_STROKE = new BasicStroke(2.0f);
    private static final Font TITLE_FONT = FontManager.getRunescapeBoldFont();
    private static final Font SUBTEXT_FONT = FontManager.getRunescapeSmallFont();

    private final Client client;

    private boolean trimAlertActive;
    private float flashAlpha;

    @Inject
    SailingTrimOverlay(Client client)
    {
        this.client = client;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        setPriority(PRIORITY_HIGH);
    }

    void updateState(boolean active, float flashAlpha)
    {
        trimAlertActive = active;
        this.flashAlpha = flashAlpha;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!trimAlertActive)
        {
            return null;
        }

        final Rectangle bounds = client.getCanvas() != null ? client.getCanvas().getBounds() : null;
        if (bounds == null)
        {
            return null;
        }
        final int width = Math.max(220, (int) (bounds.getWidth() * 0.30));
        final int height = 90;
        final int x = (int) bounds.getCenterX() - (width / 2);
        final int y = (int) bounds.getCenterY() - (height / 2) + 40;

        final RoundRectangle2D box = new RoundRectangle2D.Double(x, y, width, height, 18, 18);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setComposite(AlphaComposite.SrcOver.derive(flashAlpha));

        graphics.setColor(FILL_COLOR);
        graphics.fill(box);

        graphics.setStroke(BORDER_STROKE);
        graphics.setColor(BORDER_COLOR);
        graphics.draw(box);

        graphics.setColor(TEXT_COLOR);
        graphics.setFont(TITLE_FONT);
        final String headline = "TRIM YOUR SAILS";
        final FontMetrics titleMetrics = graphics.getFontMetrics();
        final int titleX = x + (width - titleMetrics.stringWidth(headline)) / 2;
        final int titleY = y + 38;
        graphics.drawString(headline, titleX, titleY);

        graphics.setFont(SUBTEXT_FONT);
        graphics.setColor(SUBTEXT_COLOR);
        final String subtext = "Adjust the rig before you slow down!";
        final FontMetrics subMetrics = graphics.getFontMetrics();
        final int subX = x + (width - subMetrics.stringWidth(subtext)) / 2;
        final int subY = y + 64;
        graphics.drawString(subtext, subX, subY);

        return new Dimension(width, height);
    }
}