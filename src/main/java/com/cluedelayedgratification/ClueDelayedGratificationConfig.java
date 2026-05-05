package com.cluedelayedgratification;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup(ClueDelayedGratificationConfig.GROUP)
public interface ClueDelayedGratificationConfig extends Config
{
	String GROUP = "clue-delayed-gratification";

	@ConfigSection(
		name = "Thresholds",
		description = "Minimum casket counts required before opening is allowed",
		position = 0
	)
	String thresholdSection = "thresholds";

	@Range(min = 0)
	@ConfigItem(
		keyName = "beginnerThreshold",
		name = "Beginner threshold",
		description = "Minimum number of beginner caskets required before opening is allowed. 0 = disabled.",
		section = thresholdSection,
		position = 0
	)
	default int beginnerThreshold()
	{
		return 10;
	}

	@Range(min = 0)
	@ConfigItem(
		keyName = "easyThreshold",
		name = "Easy threshold",
		description = "Minimum number of easy caskets required before opening is allowed. 0 = disabled.",
		section = thresholdSection,
		position = 1
	)
	default int easyThreshold()
	{
		return 10;
	}

	@Range(min = 0)
	@ConfigItem(
		keyName = "mediumThreshold",
		name = "Medium threshold",
		description = "Minimum number of medium caskets required before opening is allowed. 0 = disabled.",
		section = thresholdSection,
		position = 2
	)
	default int mediumThreshold()
	{
		return 10;
	}

	@Range(min = 0)
	@ConfigItem(
		keyName = "hardThreshold",
		name = "Hard threshold",
		description = "Minimum number of hard caskets required before opening is allowed. 0 = disabled.",
		section = thresholdSection,
		position = 3
	)
	default int hardThreshold()
	{
		return 10;
	}

	@Range(min = 0)
	@ConfigItem(
		keyName = "eliteThreshold",
		name = "Elite threshold",
		description = "Minimum number of elite caskets required before opening is allowed. 0 = disabled.",
		section = thresholdSection,
		position = 4
	)
	default int eliteThreshold()
	{
		return 5;
	}

	@Range(min = 0)
	@ConfigItem(
		keyName = "masterThreshold",
		name = "Master threshold",
		description = "Minimum number of master caskets required before opening is allowed. 0 = disabled.",
		section = thresholdSection,
		position = 5
	)
	default int masterThreshold()
	{
		return 5;
	}
}
