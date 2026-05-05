package com.cluedelayedgratification;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

class ClueDelayedGratificationOverlay extends Overlay
{
	private final ClueDelayedGratificationPlugin plugin;
	private final TooltipManager tooltipManager;

	@Inject
	ClueDelayedGratificationOverlay(ClueDelayedGratificationPlugin plugin, TooltipManager tooltipManager)
	{
		this.plugin = plugin;
		this.tooltipManager = tooltipManager;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		String tooltip = plugin.getHoveredTooltip();
		if (tooltip != null)
		{
			tooltipManager.add(new Tooltip(tooltip));
		}
		return null;
	}
}
