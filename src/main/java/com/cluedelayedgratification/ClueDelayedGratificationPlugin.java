package com.cluedelayedgratification;

import com.google.inject.Provides;
import java.util.EnumMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.Menu;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.PostMenuSort;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Clue Delayed Gratification",
	description = "Block opening caskets until a minimum stack size has been accumulated",
	tags = {"clue", "casket", "scroll", "reward"}
)
public class ClueDelayedGratificationPlugin extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private ClueDelayedGratificationConfig config;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private ClueDelayedGratificationOverlay overlay;

	private final Map<ClueTier, TierState> states = new EnumMap<>(ClueTier.class);
	private ClueTier hoveredLockedTier = null;
	private boolean bankObserved = false;

	@Override
	protected void startUp()
	{
		resetState();
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		resetState();
	}

	private void resetState()
	{
		for (ClueTier tier : ClueTier.values())
		{
			states.computeIfAbsent(tier, t -> new TierState()).reset();
		}
		hoveredLockedTier = null;
		bankObserved = false;
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		boolean isBank = event.getContainerId() == InventoryID.BANK;
		boolean isInventory = event.getContainerId() == InventoryID.INV;
		if (!isBank && !isInventory)
		{
			return;
		}

		if (isBank)
		{
			bankObserved = true;
		}

		Item[] items = event.getItemContainer().getItems();
		for (ClueTier tier : ClueTier.values())
		{
			TierState state = states.get(tier);
			int newCount = countItems(items, tier.itemId);
			if (isBank)
			{
				state.bankCount = newCount;
			}
			else
			{
				state.inventoryCount = newCount;
			}
			evaluateState(tier);
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals(ClueDelayedGratificationConfig.GROUP))
		{
			return;
		}
		if (!event.getKey().endsWith("Threshold"))
		{
			return;
		}
		for (ClueTier tier : ClueTier.values())
		{
			states.get(tier).maxSeen = 0;
			evaluateState(tier);
		}
	}

	@Subscribe
	public void onPostMenuSort(PostMenuSort ignored)
	{
		hoveredLockedTier = null;
		Menu menu = client.getMenu();
		MenuEntry[] entries = menu.getMenuEntries();

		for (MenuEntry entry : entries)
		{
			ClueTier tier = ClueTier.forItemId(entry.getItemId());
			if (tier != null && isOpenBlocked(tier))
			{
				hoveredLockedTier = tier;
				break;
			}
		}

		for (MenuEntry entry : entries)
		{
			ClueTier tier = ClueTier.forItemId(entry.getItemId());
			if (tier != null && entry.getOption().equals("Open") && isOpenBlocked(tier))
			{
				menu.removeMenuEntry(entry);
			}
		}
	}

	String getHoveredTooltip()
	{
		if (hoveredLockedTier == null)
		{
			return null;
		}
		TierState state = states.get(hoveredLockedTier);
		return "Open disabled - " + state.maxSeen + "/" + getThreshold(hoveredLockedTier);
	}

	private void evaluateState(ClueTier tier)
	{
		TierState state = states.get(tier);
		int peak = Math.max(state.bankCount, state.inventoryCount);
		int threshold = getThreshold(tier);

		if (peak > state.maxSeen)
		{
			state.maxSeen = peak;
		}

		if (threshold == 0)
		{
			return;
		}

		if (!state.openAllowed)
		{
			if (peak >= threshold)
			{
				state.openAllowed = true;
				state.maxAtUnlock = peak;
			}
		}
		else
		{
			if (bankObserved && state.bankCount == 0 && state.inventoryCount == 0)
			{
				state.openAllowed = false;
				state.maxAtUnlock = 0;
				state.maxSeen = 0;
			}
			else if (threshold > state.maxAtUnlock)
			{
				state.openAllowed = false;
				state.maxAtUnlock = 0;
			}
		}
	}

	private boolean isOpenBlocked(ClueTier tier)
	{
		int threshold = getThreshold(tier);
		if (threshold == 0)
		{
			return false;
		}
		return !states.get(tier).openAllowed;
	}

	private int getThreshold(ClueTier tier)
	{
		switch (tier)
		{
			case BEGINNER: return config.beginnerThreshold();
			case EASY:     return config.easyThreshold();
			case MEDIUM:   return config.mediumThreshold();
			case HARD:     return config.hardThreshold();
			case ELITE:    return config.eliteThreshold();
			case MASTER:   return config.masterThreshold();
			default:       return 0;
		}
	}

	private int countItems(Item[] items, int itemId)
	{
		int count = 0;
		for (Item item : items)
		{
			if (item.getId() == itemId)
			{
				count += item.getQuantity();
			}
		}
		return count;
	}

	@Provides
	ClueDelayedGratificationConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ClueDelayedGratificationConfig.class);
	}
}
